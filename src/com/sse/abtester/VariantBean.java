package com.sse.abtester;

import java.util.Properties;

import lombok.Data;
import lombok.NonNull;
import lombok.Synchronized;

public @Data class VariantBean implements IVariant<VariantBean> {
    @NonNull String name;
    int key;
    int requestedExecutions = 0;
    int dispatchedCount=0;
    int respondedCount=0;
    double targetFreq;
    boolean isDispatchable=false;
    IVariationComponentSelector componentSelector;
    IVariationStrategy variationStrategy;
    Properties variantProps = new Properties();

    public VariantBean(){    }
    public VariantBean(String name){
        this.name = name;
    }
    public VariantBean(String name, Properties variantProperties){
        this.name=name;
        this.variantProps = variantProperties;
    }

    public boolean isDispatchable(){return isDispatchable;}
    public void setDispatchable(boolean active){isDispatchable = active;}

    public @Synchronized int incDispatchedCounter(){
        return ++dispatchedCount;
    };
    public @Synchronized int incRespondedCounter(){
        return ++respondedCount;
    }
    @Override
    public VariantBean copy() {
        VariantBean copy = new VariantBean(this.name, this.variantProps);
        copy.setDispatchedCount(this.dispatchedCount);
        copy.setRequestedExecutions(this.requestedExecutions);
        copy.setRespondedCount(this.respondedCount);
        copy.setTargetFreq(this.targetFreq);
        copy.setKey(this.key);
        copy.setDispatchable(this.isDispatchable);
        return copy;
    };
}
