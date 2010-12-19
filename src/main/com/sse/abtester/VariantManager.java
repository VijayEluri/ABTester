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

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;


import org.springframework.web.context.ServletContextAware;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.sse.abtester.external.VariationRequestBean;

// TODO: Auto-generated Javadoc
/**
 * The Class VariantManager.
 *
 * @author wstidolph
 */

public class VariantManager implements ServletContextAware {

    @Setter
    private DelegatingFilterProxy urlRewriteFilterProxy;

    ServletContext sc;
    @Setter
    @Getter
    String rewriteFilterConfPath;

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

    /**
     * The known variants (whether or not Dispatchable).
     *
     */
    @Getter
    private AbstractMap<String, IVariant<VariantBean>> knownVariants = new ConcurrentHashMap<String, IVariant<VariantBean>>();

    @Setter
    @Getter
    private ArrayList<VariationRequestBean> preloadRequests;

    private Queue<VariationRequestBean> requestQueue = new ArrayBlockingQueue<VariationRequestBean>(
            16);

    public int drainRequestQueue() {
        int numProcessed = 0;

        for (VariationRequestBean vrb : requestQueue) {
            IVariant<VariantBean> var = addVariationRequest(vrb);
            System.out.println("processed request " + vrb.getRequestName()
                    + " producing " + var);

            numProcessed++;
        }
        return numProcessed;
    }

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

        IVariant<VariantBean> vb = new VariantBean(vrb);
        vb.setKey(vb.hashCode());
        vb.getVariationStrategy().install(); // notify baccking system
        knownVariants.put("" + vb.getKey(), vb);
        assigner.setIVariantCollection(knownVariants);

        // throw NPE for fail-early detect of config errs
        // urlRewriteFilter.reloadConf();
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
            sc.log("deactivating variant: " + ivb.getName());
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

            // throw NPE for fail-early detect of config errs

        }
        return o;
    }

    /**
     * Gets the IVariant by request key.
     *
     * @param key
     *            the key
     * @return the i variant by request key
     */
    public IVariant<VariantBean> getIVariantByRequestKey(final String key) {
        IVariant<VariantBean> o = knownVariants.get(key);
        return o;
    }

    public void startup() {

        System.out.println("VARIANT MANAGER STARTUP!");
        if (preloadRequests != null) {
            int preloaded = 0;
            for (VariationRequestBean vrb : preloadRequests) {
                requestQueue.add(vrb);
                preloaded++;
            }
            System.out.println("preloaded " + preloaded
                    + " VariationRequestBeans");
        } else {
            System.out.println("no preloadRequests");
        }
        int started = drainRequestQueue();
        System.out.println("Began with " + started + " variation requests");
    }

    public void shutdown(){
        System.out.println("VARIANT MANAGER SHUTDOWN!");
    }

    /* constructor */
    /**
     * Instantiates a new variant manager.
     */
    public VariantManager() {
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        sc = servletContext;
        String filepath = sc.getRealPath(rewriteFilterConfPath);

        File filterConfFile = new File(filepath);
        System.out.println("found " + filepath + "and it is Writeable? " + filterConfFile.canWrite());
    }
}
