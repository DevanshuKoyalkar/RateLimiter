package ratelimitter.models;

import java.util.HashMap;
import java.util.Map;

public class RateLimitter {
    String name;
    Map<String, Service> serviceMap;

    public RateLimitter(String name) {
        this.name = name;
        this.serviceMap = new HashMap<String, Service>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Service> getServices() {
        return serviceMap;
    }

    public void setServices(Map<String, Service> services) {
        this.serviceMap = services;
    }

    public void addService( Service service ){
        serviceMap.put(service.getName(), service);
    }

    public Service getService( String serviceName){
        return serviceMap.get(serviceName);
    }

    public void addAPI( String servicename, API api){
        this.serviceMap.get(servicename).addAPI(api);
    }
}
