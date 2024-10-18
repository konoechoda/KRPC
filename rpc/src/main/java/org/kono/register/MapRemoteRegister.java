package org.kono.register;

import org.kono.common.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapRemoteRegister {

    private static Map<String, List<URL>> remoteRegisterMap = new HashMap<>();

    public static void register(String interfaceName, URL url){
        List<URL> list = remoteRegisterMap.get(interfaceName);
        if(list == null){
            list = new ArrayList<>();
        }
        list.add(url);
        remoteRegisterMap.put(interfaceName, list);
    }

    public static List<URL> get(String interfaceName){
        return remoteRegisterMap.get(interfaceName);
    }


}
