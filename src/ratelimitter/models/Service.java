package ratelimitter.models;

import ratelimitter.models.method.Method;
import ratelimitter.models.method.MethodType;

import java.util.HashMap;
import java.util.Map;

public class Service {
    String name;
    Map<MethodType, Method> methodMap;
    Map<String, API> apiMap;

    public Service(String name) {
        this.name = name;
        this.apiMap = new HashMap<String, API>();
        this.methodMap = new HashMap<MethodType, Method>();
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

    public void addAPI(API api){
        apiMap.put(api.getName(), api);
    }

    public API getAPI(String apiName){
        return apiMap.get(apiName);
    }
    public void addMethod(Method method){
        methodMap.put(method.getMethodType(), method);
    }
}
