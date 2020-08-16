package ratelimitter;

import ratelimitter.models.API;
import ratelimitter.models.GranularityType;
import ratelimitter.models.RateLimitter;
import ratelimitter.models.Service;
import ratelimitter.models.method.Method;
import ratelimitter.models.method.MethodType;

import java.util.*;

import static ratelimitter.models.GranularityType.SECOND;

public class RateLimitterManager {
    RateLimitter rateLimitter;
    Map<String, LinkedList<Long>> callTimestampsMap;// make it Map<String, Map< MethodType, Long>>
    Map<String, Integer> secondsLimitMap, minutesLimitMap;

    public RateLimitterManager() {
        rateLimitter = new RateLimitter("test1");
        callTimestampsMap = new HashMap<String, LinkedList<Long>>();
        secondsLimitMap = new HashMap<String, Integer>();
        minutesLimitMap = new HashMap<String, Integer>();
    }

    public void addService(String name, List<Method> methods) {
        Service service = new Service(name);
        for(Method method: methods){
            service.addMethod(method);
        }
        rateLimitter.addService(service);
    }

    public void addAPI(String servicename, String name, List<Method> methods) {
        API api = new API(name);
        for(Method m: methods){
            api.addMethod(m);
        }
        rateLimitter.addAPI(servicename, api);
    }

    public void setLimits(String serviceName, String apiName, MethodType methodType){
        Service service = rateLimitter.getService(serviceName);
        API api = service.getAPI(apiName);
        String callTimestampsKey = serviceName+apiName+methodType.toString();
        if(!secondsLimitMap.containsKey(callTimestampsKey)){
            int serviceLimit = service.getMethodLimit(methodType);
            GranularityType serviceGranularity = service.getGranularityType(methodType);

            int apiLimit = api.getMethodLimit(methodType);
            GranularityType apiGranularity = api.getGranularityType(methodType);

            int secondsLimit = Integer.MAX_VALUE, minutesLimit = Integer.MAX_VALUE;

            if(serviceGranularity.equals(SECOND) && apiGranularity.equals(SECOND)){
                secondsLimit = Math.min(serviceLimit, apiLimit);
            } else if(serviceGranularity.equals(SECOND)){
                secondsLimit = serviceLimit;
                minutesLimit = apiLimit;
            } else if(apiGranularity.equals(SECOND)){
                secondsLimit = apiLimit;
                minutesLimit = serviceLimit;
            } else{
                minutesLimit = Math.min(apiLimit, serviceLimit);
            }

            secondsLimitMap.put(callTimestampsKey, secondsLimit);
            minutesLimitMap.put(callTimestampsKey, minutesLimit);
        }
    }

    public boolean isCallable(String serviceName, String apiName, MethodType methodType){
        Service service = rateLimitter.getService(serviceName);
        API api = service.getAPI(apiName);
        String callTimestampsKey = serviceName+apiName+methodType.toString();
        Long currTimestamp = System.currentTimeMillis();

        setLimits(serviceName, apiName, methodType);
        int secondsLimit = secondsLimitMap.get(callTimestampsKey);
        int minutesLimit = minutesLimitMap.get(callTimestampsKey);

        if(callTimestampsMap.containsKey(callTimestampsKey)){
            LinkedList<Long> timestamps = callTimestampsMap.get(callTimestampsKey);

            timestamps.push(currTimestamp);
            for(Long timestamp: timestamps){
                if(currTimestamp - timestamp > 60 * 1000) timestamps.remove(timestamp);
                else break;
            }

            int minutesCount = timestamps.size();
            int secondsCount = 0;

            for(int i=timestamps.size()-1; i>=0; i--){
                if(currTimestamp - timestamps.get(i) > 1000) break;
                else secondsCount++;
            }

            if(secondsCount > secondsLimit || minutesCount > minutesLimit){
                //System.out.println(String.format("%d %d %d %d", secondsCount, minutesCount, secondsLimit, minutesLimit));
                System.out.println(String.format("%s: %s %s cannot be served", serviceName, apiName, methodType.toString()) );
                return false;
            }

            return true;
        }
        else {
            callTimestampsMap.put(callTimestampsKey, new LinkedList<Long>());
            callTimestampsMap.get(callTimestampsKey).add(currTimestamp);
        }
        return true;
    }
}
