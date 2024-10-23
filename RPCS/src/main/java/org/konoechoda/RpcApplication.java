package org.konoechoda;

import lombok.extern.slf4j.Slf4j;
import org.konoechoda.config.RpcConfig;
import org.konoechoda.constant.RpcConstant;
import org.konoechoda.utils.ConfigUtils;

/**
 * RPC应用
 */
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig  rpcConfig;

    /**
     * 初始化配置
     */
    public static void init() {
        RpcConfig newRpcConfig = null;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.RPC_CONFIG_PREFIX);
        }catch (Exception e){
            log.error("Failed to load rpc config", e);
            rpcConfig = new RpcConfig();
        }
        if ( newRpcConfig == null){
            rpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 初始化配置
     * @param newRpcConfig 新的配置
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("RPC config: {}", rpcConfig);
    }

    /**
     * 获取配置
     * @return 配置
     */
    public static RpcConfig getRpcConfig() {
        if(rpcConfig == null){
            synchronized (RpcApplication.class){
                if(rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }

}
