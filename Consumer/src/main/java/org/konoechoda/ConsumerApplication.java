package org.konoechoda;

import org.konoechoda.config.RpcConfig;
import org.konoechoda.constant.RpcConstant;
import org.konoechoda.model.User;
import org.konoechoda.proxy.ServiceProxyFactory;
import org.konoechoda.service.UserService;
import org.konoechoda.utils.ConfigUtils;

public class ConsumerApplication {

    public static void main(String[] args) {

        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.RPC_CONFIG_PREFIX);
        System.out.println(rpcConfig);

        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("张三");
        User newUser = userService.getUser(user);
        if(newUser != null) {
            System.out.println("获取用户信息成功：" + newUser.getName());
        } else {
            System.out.println("获取用户信息失败");
        }
    }
}
