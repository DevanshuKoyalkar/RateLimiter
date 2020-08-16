package ratelimitter;

import ratelimitter.models.GranularityType;
import ratelimitter.models.method.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Driver {
    public static void main(String[] args) throws InterruptedException {
        RateLimitterManager manager = new RateLimitterManager();
        List<Method> methods;
        Method m1, m2, m3, m4;

        m1 = new Method(MethodType.GET, GranularityType.SECOND, 10);
        m2 = new Method(MethodType.GET, GranularityType.MINUTE, 50);

        m3 = new Method(MethodType.POST, GranularityType.SECOND, 10);
        m4 = new Method(MethodType.POST, GranularityType.MINUTE, 100);

        manager.addService("orderService", new ArrayList<Method>(Arrays.asList(m1, m3)));
        manager.addService("deliveryService", new ArrayList<Method>(Arrays.asList(m2, m4)));

        manager.addAPI("orderService", "CreateOrder", new ArrayList<Method>(Arrays.asList(m2, m4)) );
        manager.addAPI("orderService", "GetOrderById", new ArrayList<Method>(Arrays.asList(m2, m4)) );

        //only testing GET as manager map is not fully implemented to map of map
        for(int i=0; i<20; i++){
            manager.isCallable("orderService", "CreateOrder", MethodType.GET);
        }

        for(int i=0; i<60; i++){
            if(i%10 == 0) Thread.sleep(1000);
            manager.isCallable("orderService", "GetOrderById", MethodType.GET);

        }

    }
}
