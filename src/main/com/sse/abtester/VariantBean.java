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

import com.sse.abtester.external.IVariant;
import com.sse.abtester.external.IVariationStrategy;
import com.sse.abtester.external.VariationRequestBean;

import lombok.Data;
import lombok.NonNull;
import lombok.Synchronized;

/**
 * The Class VariantBean.
 */
public @Data
class VariantBean implements IVariant<VariantBean> {

    /** The name. */
    @NonNull
    private String name;

    /** The key. */
    private int key;

    /** The requested executions. */
    private int requestedExecutions = 0;

    /** The dispatched count. */
    private int dispatchedCount = 0;

    /** The responded count. */
    private int respondedCount = 0;

    /** The target freq. */
    private double targetFreq;

    /** is this Variant available for new users? */
    private boolean isDispatchable = true;

    /** The variation strategy. */
    private IVariationStrategy variationStrategy;
    /**
     * Instantiates a new variant bean.
     */
    public VariantBean() {
    }

    public VariantBean(final VariationRequestBean vrb) {
        name = vrb.requestName;
        requestedExecutions = vrb.requestedExecutions;
        targetFreq = vrb.requestedTargetFreq;
        if(requestedExecutions <1) isDispatchable = false;
        String ss = vrb.variationStrategyClassName;
        try{
            Class stratClass = this.getClass().forName(ss);
            if(stratClass != null){
                variationStrategy = (IVariationStrategy)stratClass.newInstance();
            }
        } catch(ClassNotFoundException cnf){
            System.out.println("Constructing VariantBean, could not find class " + ss);
        }
        catch (InstantiationException ie) {
            System.out.println("Constructing VariantBean, failed to instantiate " + ss + " because " + ie.getMessage());
        } catch (ClassCastException cce) {
            System.out.println("Constructing VariantBean, Class " + ss + " cast to IVariationStrategy failed");
        } catch (IllegalAccessException iae) {
            System.out.println("Constructing VariantBean, couldn't find Class or nullary constructor for " + ss + " : " + iae.getMessage());
        }

        if(variationStrategy != null){
            variationStrategy.setProps(vrb.variationProperties);
            System.out.println("Created variationStrategy " + variationStrategy);
        }
    }

    /**
     * Instantiates a new variant bean.
     *
     * @param variantDisplayName
     *            the name
     */
    public VariantBean(final String variantDisplayName) {
        this.name = variantDisplayName;
    }

    /*
     * (non-Javadoc)
     * TODO rework this to be extension-friendly!
     *
     * @see com.sse.abtester.IVariant#copy()
     */
    @Override
    public final VariantBean copy() {
        VariantBean copy = new VariantBean(this.name);
        copy.setDispatchedCount(this.dispatchedCount);
        copy.setRequestedExecutions(this.requestedExecutions);
        copy.setRespondedCount(this.respondedCount);
        copy.setTargetFreq(this.targetFreq);
        copy.setKey(this.key);
        copy.setDispatchable(this.isDispatchable);
        if(variationStrategy != null)
            copy.setVariationStrategy((IVariationStrategy) // cast
                 this.variationStrategy.clone());
        else
            System.out.println("NULL variation strategy on " + name);
        return copy;
    }
    /*
     * (non-Javadoc)
     *
     * @see com.sse.abtester.IVariant#isDispatchable()
     */
    @Override
    public final boolean isDispatchable() {
        return this.isDispatchable;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sse.abtester.IVariant#setDispatchable(boolean)
     */
    @Override
    public final void setDispatchable(final boolean active) {
        this.isDispatchable = active;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sse.abtester.IVariant#incDispatchedCounter()
     */

    @Override
    public @Synchronized
    final int incDispatchedCounter() {
        return ++dispatchedCount;
    };

    /*
     * (non-Javadoc)
     *
     * @see com.sse.abtester.IVariant#incRespondedCounter()
     */
    @Override
    public @Synchronized
    final int incRespondedCounter() {
        return ++respondedCount;
    }


}
