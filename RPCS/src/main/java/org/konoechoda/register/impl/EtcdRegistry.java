package org.konoechoda.register.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import lombok.extern.slf4j.Slf4j;
import org.konoechoda.config.RegisterConfig;
import org.konoechoda.model.ServiceMetaInfo;
import org.konoechoda.register.Register;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * etcd 注册中心实现
 */
@Slf4j
public class EtcdRegistry implements Register {

    private Client client;
    private KV kvClient;
    // etcd服务根节点
    private static final String ETCD_ROOT_PATH = "/rpc/";
    // 注册节点 key 集合（用于维护续期）
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    @Override
    public void init(RegisterConfig registerConfig) {
        client = Client.builder()
                .endpoints(registerConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registerConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        heartbeat();
    }

    // 注册服务到 etcd，通过创建有期限的租约确保服务心跳机制，实现服务的自动下线功能
    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 创建 Lease 和 KV 客户端
        Lease leaseClient = client.getLeaseClient();
        // 创建30s的租约
        long leaseId = leaseClient.grant(30).get().getID();
        // 设置要存储的键值对
        String key = ETCD_ROOT_PATH + serviceMetaInfo.getServiceKey();
        ByteSequence keyByteSequence = ByteSequence.from(key, StandardCharsets.UTF_8);
        ByteSequence valueByteSequence = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);
        // 将键值对与租约关联 - 30s过期
        PutOption putOption = PutOption.builder()
                .withLeaseId(leaseId)
                .build();
        kvClient.put(keyByteSequence, valueByteSequence, putOption).get();
        // 添加节点信息到本地缓存
        localRegisterNodeKeySet.add(key);
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) throws Exception {
        String key = ETCD_ROOT_PATH + serviceMetaInfo.getServiceKey();
        kvClient.delete(ByteSequence.from(ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey(), StandardCharsets.UTF_8));
        localRegisterNodeKeySet.remove(key);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) throws Exception {
        // 前缀搜索， 结尾加上 /
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";
        try {
            // 前缀查询
            GetOption getOption = GetOption.builder()
                    .isPrefix(true)
                    .build();
            // 解析服务器信息
            List<KeyValue> keyValues = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption).get().getKvs();
            return keyValues.stream()
                    .map(keyValue -> JSONUtil.toBean(keyValue.getValue().toString(), ServiceMetaInfo.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("fail to get service list", e);
        }
    }

    @Override
    public void destroy() {
        log.info("EtcdRegistry offline");
        // 遍历本地注册节点的所有节点
        for (String nodeKey : localRegisterNodeKeySet) {
            try {
                kvClient.delete(ByteSequence.from(nodeKey, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(nodeKey + " failed to offline");
            }
        }
        if (client != null) {
            client.close();
        }
        if (kvClient != null) {
            kvClient.close();
        }
    }

    @Override
    public void heartbeat() {
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                // 遍历本地注册节点的所有key
                for (String nodeKey : localRegisterNodeKeySet) {
                    try {
                        // 获取 key 对应的 value
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(nodeKey, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        // 判断 key 是否存在，若不存在则认为节点失效，需要重启并重新注册
                        if (keyValues.size() != 1 || CollUtil.isEmpty(keyValues)) {
                            log.warn("{} has expired and the renewal fails ", nodeKey);
                            continue;
                        }
                        // 若节点未过期，重新注册（续签）
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString();
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(nodeKey + " failed to renew", e);
                    }
                }
            }
        });
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }
}
