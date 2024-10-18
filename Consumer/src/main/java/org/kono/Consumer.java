package org.kono;


import org.kono.proxy.ProxyFactory;

public class Consumer {

    public static void main(String[] args) {

        HelloService helloService = ProxyFactory.getProxy(HelloService.class, "1.0");
        String s = helloService.sayHello("=======");
        System.out.println(s);

    }

}
