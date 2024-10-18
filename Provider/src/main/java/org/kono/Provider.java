package org.kono;

import org.kono.common.URL;
import org.kono.protocol.HttpServer;
import org.kono.register.LocalRegister;
import org.kono.register.MapRemoteRegister;

public class Provider {

    public static void main(String[] args) {

        LocalRegister.register(HelloService.class.getName(), "1.0" ,HelloServiceImpl.class);

        URL url = new URL("localhost", 8080);
        MapRemoteRegister.register(HelloService.class.getName(),url);


        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostname(), url.getPort());
    }
}
