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

import java.util.Properties;

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

    /** The is dispatchable. */
    private boolean isDispatchable = false;

    /** The component selector. */
    private IVariationComponentSelector componentSelector;

    /** The variation strategy. */
    private IVariationStrategy variationStrategy;

    /** The variant props. */
    private Properties variantProps = new Properties();

    /**
     * Instantiates a new variant bean.
     */
    public VariantBean() {
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

    /**
     * Instantiates a new variant bean.
     *
     * @param variantDisplayName
     *            the name
     * @param variantProperties
     *            the variant properties
     */
    public VariantBean(final String variantDisplayName,
            final Properties variantProperties) {
        this.name = variantDisplayName;
        this.variantProps = variantProperties;
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

    /*
     * (non-Javadoc)
     * TODO rework this to be extension-friendly!
     *
     * @see com.sse.abtester.IVariant#copy()
     */
    @Override
	public final VariantBean copy() {
        VariantBean copy = new VariantBean(this.name, this.variantProps);
        copy.setDispatchedCount(this.dispatchedCount);
        copy.setRequestedExecutions(this.requestedExecutions);
        copy.setRespondedCount(this.respondedCount);
        copy.setTargetFreq(this.targetFreq);
        copy.setKey(this.key);
        copy.setDispatchable(this.isDispatchable);
        return copy;
    }
}
