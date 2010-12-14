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

import com.sse.abtester.external.*;

import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;


import com.sse.abtester.external.VariationRequestBean;

// TODO: Auto-generated Javadoc
/**
 * The Class VariantManager.
 *
 * @author wstidolph
 */

public class VariantManager  {

    /* variables */
    /** The assigner. */

    /**
     * Sets the assigner.
     *
     * @param assigner
     *            the new assigner
     */
    @Setter
    /**
     * Gets the assigner.
     *
     * @return the assigner
     */
    @Getter
    private IVariationAssigner<VariantBean> assigner;

    /**
     * Sets the control percentage.
     *
     * @param controlPercentage
     *            the new control percentage
     */
    @Setter
    /**
     * Gets the control percentage. Defaults to 1.0 (no variants)
     *
     * @return the control percentage
     */
    @Getter
    private double controlPercentage = 1.0;


    /** The known variants (whether or not Dispatchable).
     *
     */
    @Getter
    private AbstractMap<String, IVariant<VariantBean>>
    knownVariants = new ConcurrentHashMap<String, IVariant<VariantBean>>();

    /**
     * Update tracking of received responses for variant.
     *
     * @param iv
     *            the variant to update
     */
    public void updateTrackingForVariant(final IVariant<VariantBean> iv) {
        iv.incRespondedCounter();
    }

    /**
     * Routes some fraction of the requests into variant assignment,.
     *
     * @param request
     *            the request
     * @return a variant bean (can be null for no variation)
     */
    public IVariant<VariantBean> enrollRequest(final HttpServletRequest request) {
        IVariant<VariantBean> assigned = null;
        if (assigner != null && Math.random() > controlPercentage) {
            assigned = assigner.enrollRequest(request);

        }
        return assigned;
    }

    /**
     * Update the identified Variant (if it exists). Can mark the variant as not
     * active to prevent future dispatches.
     *
     * @param key
     *            string to identify a particular VariantSet
     * @return the i variant
     */
    public IVariant<VariantBean> updateVariant(final String key) {
        IVariant<VariantBean> iv = knownVariants.get(key);
        if (iv != null) {
            updateTrackingForVariant(iv);
            if (iv.getRespondedCount() >= iv.getRequestedExecutions()) {
                deactivateIV(iv);
            }
        }
        return iv;
    }

    /**
     * Publish variation response.
     *
     * @param response
     *            the response
     */
    public void publishVariationResponse(final HttpServletResponse response) {
        // TODO hook up to JMS
        // by getting the key out of this response object
        // and publishing the related IVarient's updated status
        System.out.println("publishing response "); // TODO log
    }

    /**
     * Map a VRB into a VariantBean, put it into the knownVariants and notify
     * the 'assigner'.
     *
     * @param vrb
     *            the vrb
     * @return the generated variant.
     */
    public IVariant<VariantBean> addVariationRequest(
            final VariationRequestBean vrb) {

        IVariant<VariantBean> vb = new VariantBean(vrb.getRequestKey());
        vb.setRequestedExecutions(vrb.requestedExecutions);
        vb.setTargetFreq(vrb.requestedTargetFreq);
        vb.setKey(vb.hashCode());

        assigner.setIVariantCollection(knownVariants);
        return vb;
    }

    /**
     * Deactivate iv.
     *
     * @param ivb
     *            the ivb
     */
    private void deactivateIV(final IVariant<VariantBean> ivb) {
        if (ivb != null) {
            ivb.setDispatchable(false);
            System.out.println("deactivating variant: " + ivb.getName()); // TODO
                                                                            // log
        }
    }

    /**
     * Remove the VRB (find it by its requestKey) and notify the assigner.
     *
     * @param key
     *            the key
     * @return the IVariant that represented the removed request
     */
    public IVariant<VariantBean> removeVariationRequestByRequestKey(
            final String key) {
        IVariant<VariantBean> o = getIVariantByRequestKey(key);
        if (o != null) {
            deactivateIV(o);
            knownVariants.remove(o);
            assigner.setIVariantCollection(knownVariants);
        }
        return o;
    }

    /**
     * Gets the i variant by request key.
     *
     * @param key
     *            the key
     * @return the i variant by request key
     */
    public IVariant<VariantBean> getIVariantByRequestKey(final String key) {
        IVariant<VariantBean> o = knownVariants.get(key);
        return o;
    }

    /* constructor */
    /**
     * Instantiates a new variant manager.
     */
    public VariantManager() {
    }
}
