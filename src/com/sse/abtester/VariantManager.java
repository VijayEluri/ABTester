/*******************************************************************************
 * Copyright (c) 2010 Wayne Stidolph.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Affero Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/agpl-3.0.html
 * 
 * Contributors:
 *     Wayne Stidolph - initial API and implementation
 ******************************************************************************/
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

// TODO: Auto-generated Javadoc
/**
 * The Class VariantManager.
 *
 * @author wstidolph
 */
public class VariantManager {

    /* variables */
    /** The assigner. */

    /**
     * Sets the assigner.
     *
     * @param assigner the new assigner
     */
    @Setter
 /**
  * Gets the assigner.
  *
  * @return the assigner
  */
 @Getter IVariationAssigner<VariantBean> assigner;

    /**
     * Sets the control percentage.
     *
     * @param controlPercentage the new control percentage
     */
    @Setter
 /**
  * Gets the control percentage.
  *
  * @return the control percentage
  */
 @Getter double controlPercentage = 1.0; // be default, don't touch requests

    /** The known variants. */
    AbstractMap<String,IVariant<VariantBean>> knownVariants
      = new ConcurrentHashMap<String,IVariant<VariantBean>>();

    /**
     * Gets the named variant set.
     *
     * @param key the key
     * @return the named variant set
     */
    public IVariant<VariantBean> getNamedVariantSet(String key) {
        return knownVariants.get(key);
    }

    /**
     * Update tracking for variant.
     *
     * @param vs the vs
     */
    public void updateTrackingForVariant(IVariant<VariantBean> vs) {
        vs.incRespondedCounter();
    }

    /**
     * Routes some fraction of the requests into variant assignment,.
     *
     * @param request the request
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
     * @param key string to identify a particular VariantSet
     * @return the i variant
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

    /**
     * Publish variation response.
     *
     * @param response the response
     */
    public void publishVariationResponse(HttpServletResponse response) {
        // TODO hook up to JMS
        // by getting the key out of this response object
        // and publishing the related IVarient's updated status
        System.out.println("publishing response "); // TODO log
    }

    /**
     * Map a VRB into a VariantBean, put it into the knwonVariants
     * and notify the 'assigner'.
     *
     * @param vrb the vrb
     * @return the i variant
     */
    public IVariant<VariantBean> addVariationRequest(VariationRequestBean vrb){

        IVariant<VariantBean> vb = new VariantBean(vrb.requestKey);
        vb.setRequestedExecutions(vrb.requestedExecutions);
        vb.setTargetFreq(vrb.requestedTargetFreq);
        vb.setKey(vb.hashCode());

        assigner.setIVariantCollection(knownVariants);
        return vb;
    }

    /**
     * Deactivate iv.
     *
     * @param ivb the ivb
     */
    private void deactivateIV(IVariant<VariantBean> ivb){
        if(ivb != null){
            ivb.setDispatchable(false);
            System.out.println("deactivating variant: " + ivb.getName()); // TODO log
        }
    }

    /**
     * Remove the VRB (find it by its requestKey)
     * and notify the assigner.
     *
     * @param key the key
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

    /**
     * Gets the i variant by request key.
     *
     * @param key the key
     * @return the i variant by request key
     */
    public IVariant<VariantBean> getIVariantByRequestKey(String key){
        IVariant<VariantBean> o = knownVariants.get(key);
        return o;
    }
    /* constructor */
    /**
     * Instantiates a new variant manager.
     */
    public VariantManager() { }
}
