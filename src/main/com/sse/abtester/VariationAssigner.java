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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

import com.sse.abtester.external.IVariant;

import java.lang.Math;

// TODO: Auto-generated Javadoc
/**
 * Holds the known test requests (VariationRequestBeans) and allocates requests
 * between them.
 *
 * @param <T> the generic type
 * @author wstidolph
 */
public class VariationAssigner<T> implements IVariationAssigner<VariantBean> {

    /** The Constant DEFAULT_PRECISION. */
    static final int DEFAULT_PRECISION = 3; // will cause generation of 1000 per Variant

    /** The Constant MAX_PRECISION. */
    static final int MAX_PRECISION = 5; // room for 100,000 ... that seems a lot!

    /** The precision. */
    int precision = DEFAULT_PRECISION;

    /** The weighted collection. */
    ArrayList<IVariant<VariantBean>> weightedCollection;

    /* (non-Javadoc)
     * @see com.sse.abtester.IVariationAssigner#enrollRequest(HttpServletRequest)
     */
    @Override
    public IVariant<VariantBean> enrollRequest(HttpServletRequest request) {
        // choose one of the weighted collection at random
        // this will honor the distribution preferences on the IVariants
        return getNext(weightedCollection, rangen);
    }

    /** The rangen. */
    RandomData rangen = new RandomDataImpl();

    /**
     * Gets the next.
     *
     * @param wc the wc
     * @param generator the generator
     * @return the next
     */
    protected IVariant<VariantBean> getNext(
            ArrayList<IVariant<VariantBean>> wc, RandomData generator) {
        if (wc == null || wc.size() == 0)
            return null;
        int random = generator.nextInt(0, wc.size() - 1);
        return wc.get(random);
    }

    /**
     * Generate weighted collection.
     *
     * @param src the src
     * @param precision the precision
     * @return the array list
     */
    ArrayList<IVariant<VariantBean>> generateWeightedCollection(
            AbstractMap<String, IVariant<VariantBean>> src,
            int precision) {
        ArrayList<IVariant<VariantBean>> normalizedCollection =
            new ArrayList<IVariant<VariantBean>>();
        ArrayList<IVariant<VariantBean>> newWeightedCollection =
            new ArrayList<IVariant<VariantBean>>();
        // first we normalize the targetFrequencies
        double sumTF = 0;
        for (IVariant<VariantBean> o : src.values()) {
            sumTF += o.getTargetFreq();
        }
        for (IVariant<VariantBean> o : src.values()) {
            if (o.isDispatchable()) {
                IVariant<VariantBean> temp = o.copy();
                temp.setTargetFreq(temp.getTargetFreq() / sumTF);
                normalizedCollection.add(temp);
            }
        }

        int cutoff = (int) Math.round(Math.pow(10,
                Math.max(precision, MAX_PRECISION)));
        for (IVariant<VariantBean> targetable : normalizedCollection) {
            long replicaCount = Math.round(targetable.getTargetFreq() * cutoff);
            for (int x = 0; x < replicaCount; x++) {
                newWeightedCollection.add(targetable);
            }
        }
        return newWeightedCollection;
    }

    /* (non-Javadoc)
     * @see com.sse.abtester.IVariationAssigner#setIVariantCollection(AbstractMap)
     */
    @Override
	public void setIVariantCollection(
            AbstractMap<String, IVariant<VariantBean>> ivc) {
        ArrayList<IVariant<VariantBean>> newWeightedCollection =
            generateWeightedCollection(ivc, precision);
        doWcReplacement(newWeightedCollection);
    }

    /**
     * Do wc replacement.
     *
     * @param newWc the new wc
     */
    private synchronized void doWcReplacement(
            ArrayList<IVariant<VariantBean>> newWc) {
        weightedCollection = newWc;
    }
}
