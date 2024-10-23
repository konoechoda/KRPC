package org.konoechoda.config;

import lombok.Data;
import org.konoechoda.register.RegisterKeys;

/**
 * RPC注册中心配置
 */
@Data
public class RegisterConfig {

    // 注册中心类别
    private String registry = RegisterKeys.ETCD;

    // 注册中心地址
    private String address = "http://localhost:2380";

    // 用户名
    private String username;

    // 密码
    private String password;

    // 超时时间
    private Long timeout = 5000L;

}
