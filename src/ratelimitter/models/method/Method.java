package ratelimitter.models.method;

import ratelimitter.models.GranularityType;

public class Method {
    MethodType methodType;
    GranularityType granularityType;
    int limit;

    public Method(MethodType methodType, GranularityType granularityType, int limit) {
        this.methodType = methodType;
        this.granularityType = granularityType;
        this.limit = limit;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodType method) {
        this.methodType = method;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public GranularityType getGranularity() {
        return granularityType;
    }

    public void setGranularity(GranularityType granularity) {
        this.granularityType = granularity;
    }
}
