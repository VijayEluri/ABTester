package com.sse.abtester;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wstidolph
 *
 */
public class VariantManager {

    /* variables */
    @Setter @Getter IVariationAssigner<VariantBean> assigner;
    @Setter @Getter double controlPercentage = 1.0; // be default, don't touch requests

    AbstractMap<String,IVariant<VariantBean>> knownVariants
      = new ConcurrentHashMap<String,IVariant<VariantBean>>();

    public IVariant<VariantBean> getNamedVariantSet(String key) {
        return knownVariants.get(key);
    }

    public void updateTrackingForVariant(IVariant<VariantBean> vs) {
        vs.incRespondedCounter();
    }

    /**
     * Routes some fraction of the requests into variant assignment,
     * @param request
     * @return a variant bean (can be null for no variation)
     */
    public IVariant<VariantBean> enrollRequest(HttpServletRequest request) {
        IVariant<VariantBean> assigned = null;
        if (Math.random() > controlPercentage) {
            assigned = assigner.enrollRequest(request);

            IVariationStrategy vs = assigned.getVariationStrategy();
        }
        return assigned;
    }

    /**
     * Update the identified Variant (if it exists). Can mark
     * the variant as not active to prevent future dispatches.
     *
     * @param key
     *            string to identify a particular VariantSet
     */
    public IVariant<VariantBean> updateVariant(String key) {
        IVariant<VariantBean> iv = knownVariants.get(key);
        if (iv != null){
            updateTrackingForVariant(iv);
            if(iv.getRespondedCount() >= iv.getRequestedExecutions()){
                deactivateIV(iv);
            }
        }
        return iv;
    }

    public void publishVariationResponse(HttpServletResponse response) {
        // TODO hook up to JMS
        // by getting the key out of this response object
        // and publishing the related IVarient's updated status
        System.out.println("publishing response "); // TODO log
    }

    /**
     * Map a VRB into a VariantBean, put it into the knwonVariants
     * and notify the 'assigner'
     * @param vrb
     */
    public IVariant<VariantBean> addVariationRequest(VariationRequestBean vrb){

        IVariant<VariantBean> vb = new VariantBean(vrb.requestKey);
        vb.setRequestedExecutions(vrb.requestedExecutions);
        vb.setTargetFreq(vrb.requestedTargetFreq);
        vb.setKey(vb.hashCode());

        assigner.setIVariantCollection(knownVariants);
        return vb;
    }

    private void deactivateIV(IVariant<VariantBean> ivb){
        if(ivb != null){
            ivb.setDispatchable(false);
            System.out.println("deactivating variant: " + ivb.getName()); // TODO log
        }
    }

    /**
     * Remove the VRB (find it by its requestKey)
     * and notify the assigner.
     * @param vrb
     * @return the IVariant that represented the removed request
     */
    public IVariant<VariantBean> removeVariationRequestByRequestKey(String key){
        IVariant<VariantBean> o = getIVariantByRequestKey(key);
        if(o != null){
            deactivateIV(o);
            knownVariants.remove(o);
            assigner.setIVariantCollection(knownVariants);
        }
        return o;
    }

    public IVariant<VariantBean> getIVariantByRequestKey(String key){
        IVariant<VariantBean> o = knownVariants.get(key);
        return o;
    }
    /* constructor */
    public VariantManager() { }
}
