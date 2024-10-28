package org.konoechoda.rpc.springboot.starter.annotation;

import org.konoechoda.constant.RpcConstant;
import org.konoechoda.fault.retry.RetryStrategyKeys;
import org.konoechoda.fault.tolerant.TolerantStrategyKeys;
import org.konoechoda.loadbalancer.LoadBalancerKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务消费者注解（用于注入服务）
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {
    //服务接口类
    Class<?> interfaceClass() default void.class;
    //版本
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;
    //负载均衡器
    String loadBalancer() default LoadBalancerKeys.ROUND_ROBIN;
    //重试策略
    String retryStrategy() default RetryStrategyKeys.NO;
    //容错策略
    String tolerantStrategy() default TolerantStrategyKeys.FAIL_FAST;
    //模拟调用
    boolean mock() default false;

}