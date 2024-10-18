package org.kono.common;

import java.io.Serializable;

public class Invocation implements Serializable {

    // 接口名称
    private String interfaceName;
    // 方法名称
    private String methodName;
    // 参数类型
    private Class[] parameterTypes;
    // 参数
    private Object[] parameters;
    // 版本号
    private String version;

    public Invocation(String interfaceName, String methodName, Class[] parameterTypes, Object[] parameters, String version) {
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
        this.methodName = methodName;
        this.interfaceName = interfaceName;
        this.version = version;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
