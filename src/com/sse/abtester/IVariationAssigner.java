package com.sse.abtester;

import java.util.AbstractMap;

import javax.servlet.http.HttpServletRequest;

public interface IVariationAssigner<T> {
    IVariant<VariantBean> enrollRequest(HttpServletRequest request);

    /**
     * set the collection used by this Assigner; on setting,
     * the Assigner has an opportunity to do any expensive
     * (cacheable) calculations. If the variantws collection
     * is not set, or is null, all enrollRequest invocations
     * will return null.
     * @param variants the IVariant objects to which requests will
     * be assigned.
     */
    void setIVariantCollection (AbstractMap<String,IVariant<VariantBean>> variants);
}
