package ratelimitter.models;

import ratelimitter.models.method.Method;
import ratelimitter.models.method.MethodType;

import java.util.HashMap;
import java.util.Map;

public class API {
    String name;
    Map<MethodType, Method> methodMap;

    public API(String name) {
        this.name = name;
        methodMap = new HashMap<MethodType, Method>();
    }

    public String getName() {
        return name;
    }

    public int getMethodLimit(MethodType methodType){
        return  methodMap.get(methodType).getLimit();
    }

    public GranularityType getGranularityType(MethodType methodType){
        return  methodMap.get(methodType).getGranularity();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMethod(Method m) {
        methodMap.put(m.getMethodType(), m);
    }
}


