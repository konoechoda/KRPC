package org.kono.register;

import java.util.HashMap;
import java.util.Map;

public class LocalRegister {

    private static Map<String, Class> LocalRegisterMap =  new HashMap<>();

    public static void register(String interfaceName, String version, Class implClass){
        LocalRegisterMap.put(interfaceName + version, implClass);
    }

    public static Class get(String interfaceName, String version){
        return LocalRegisterMap.get(interfaceName + version);
    }

}
