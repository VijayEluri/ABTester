package com.sse.abtester;

import lombok.Data;

public @Data class VariationRequestBean {
    String requestKey;
    public int requestedExecutions;
    public double requestedTargetFreq = 0.5f;
    double eligiblePercentageCap = 1.0f; // 1.0 is 100%, all-eligible
    IVariationComponentSelector componentSelector;
    IVariationStrategy variationStrategy;
    Object variationProperties;
}
