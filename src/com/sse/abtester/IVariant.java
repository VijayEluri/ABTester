package com.sse.abtester;

import java.util.Properties;

public interface IVariant<T> extends IHasTargetFreq {

    String getName();
    void setName(String name);

    int getKey();
    void setKey(int key);

    int getRequestedExecutions();
    void setRequestedExecutions(int requestedExecutionCount);

    double getTargetFreq();
    void setTargetFreq(double targetFreq);

    boolean isDispatchable();
    void setDispatchable(boolean active);

    int getDispatchedCount();
    void setDispatchedCount(int dispatchedCount);

    int getRespondedCount();
    void setRespondedCount(int respondedCount);

    int incDispatchedCounter();
    int incRespondedCounter();

    IVariationComponentSelector getComponentSelector();
    void setComponentSelector(IVariationComponentSelector selector);

    IVariationStrategy getVariationStrategy();
    void setVariationStrategy(IVariationStrategy strategy);

    Properties getVariantProps();
    void setVariantProps(Properties variantProps);

    IVariant<T> copy(); // same idea as 'clone'
}