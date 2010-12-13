package com.sse.abtester;

import java.util.AbstractMap;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

import java.lang.Math;

/**
 * Holds the known test requests (VariationRequestBeans) and allocates requests
 * between them.
 *
 * @author wstidolph
 *
 */
public class VariationAssigner<T> implements IVariationAssigner<VariantBean> {

    static final int DEFAULT_PRECISION = 3; // will cause generation of 1000 per Variant
    static final int MAX_PRECISION = 5; // room for 100,000 ... that seems a lot!
    int precision = DEFAULT_PRECISION;

    ArrayList<IVariant<VariantBean>> weightedCollection;

    @Override
    public IVariant<VariantBean> enrollRequest(HttpServletRequest request) {
        // choose one of the weighted collection at random
        // this will honor the distribution preferences on the IVariants
        return getNext(weightedCollection, rangen);
    }

    RandomData rangen = new RandomDataImpl();

    protected IVariant<VariantBean> getNext(
            ArrayList<IVariant<VariantBean>> wc, RandomData generator) {
        if (wc == null || wc.size() == 0)
            return null;
        int random = generator.nextInt(0, wc.size() - 1);
        return wc.get(random);
    }

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

    public void setIVariantCollection(
            AbstractMap<String, IVariant<VariantBean>> ivc) {
        ArrayList<IVariant<VariantBean>> newWeightedCollection =
            generateWeightedCollection(ivc, precision);
        doWcReplacement(newWeightedCollection);
    }

    private synchronized void doWcReplacement(
            ArrayList<IVariant<VariantBean>> newWc) {
        weightedCollection = newWc;
    }
}
