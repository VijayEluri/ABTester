package com.sse.abtester;

import lombok.Data;

// TODO: Auto-generated Javadoc
/**
 * The Class VariationRequestBean.
 */
public /* (non-Javadoc)
  * @see java.lang.Object#toString()
  */
 @Data class VariationRequestBean {

    /** The request key. */
    String requestKey;

    /** The requested executions. */
    public int requestedExecutions;

    /** The requested target freq. */
    public double requestedTargetFreq = 0.5f;

    /** The eligible percentage cap. */
    double eligiblePercentageCap = 1.0f; // 1.0 is 100%, all-eligible

    /** The component selector. */
    IVariationComponentSelector componentSelector;

    /** The variation strategy. */
    IVariationStrategy variationStrategy;

    /** The variation properties. */
    Object variationProperties;
}
