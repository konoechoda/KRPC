package org.konoechoda;

import lombok.extern.slf4j.Slf4j;
import org.konoechoda.config.RegisterConfig;
import org.konoechoda.config.RpcConfig;
import org.konoechoda.constant.RpcConstant;
import org.konoechoda.register.Register;
import org.konoechoda.register.RegisterFactory;
import org.konoechoda.utils.ConfigUtils;

/**
 * RPC应用
 */
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    /**
     * 初始化配置
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.RPC_CONFIG_PREFIX);
        } catch (Exception e) {
            log.error("Failed to load rpc config", e);
            // 配置加载失败，使用默认配置值
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 初始化配置
     *
     * @param newRpcConfig 新的配置
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("RPC config: {}", rpcConfig);
        // 注册中心初始化
        RegisterConfig registerConfig = rpcConfig.getRegisterConfig();
        Register register = RegisterFactory.getInstance(registerConfig.getRegistry());
        register.init(registerConfig);
        log.info("registry config init succeed, config = {}", registerConfig);
        // 创建并注册Shutdown Hook， jvm 退出时销毁注册中心
        Runtime.getRuntime().addShutdownHook(new Thread(register::destroy));
    }

    /**
     * 获取配置
     *
     * @return 配置
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }

}
