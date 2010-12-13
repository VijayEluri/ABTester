package com.sse.abtester;

import java.util.AbstractMap;

import javax.servlet.http.HttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Interface IVariationAssigner.
 *
 * @param <T> the generic type
 */
public interface IVariationAssigner<T> {

    /**
     * Enroll request.
     *
     * @param request the request
     * @return the i variant
     */
    IVariant<VariantBean> enrollRequest(HttpServletRequest request);

    /**
     * set the collection used by this Assigner; on setting,
     * the Assigner has an opportunity to do any expensive
     * (cacheable) calculations. If the variants collection
     * has not been set, or is null, all enrollRequest
     * invocations return null.
     * @param variants the IVariant objects to which requests will
     * be assigned.
     */
    void setIVariantCollection (AbstractMap<String,IVariant<VariantBean>> variants);
}
