/*******************************************************************************
 * Copyright (c) 2010 Wayne Stidolph.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Affero Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/agpl-3.0.html
 *
 * Contributors:
 *     Wayne Stidolph - initial API and implementation
 ******************************************************************************/
package com.sse.abtester.external;

import java.io.Serializable;
import java.util.Properties;

import com.sse.abtester.IHasTargetFreq;

// TODO: Auto-generated Javadoc
/**
 * The Interface IVariant.
 *
 * @param <T> the generic type
 */
public interface IVariant<T> extends IHasTargetFreq, Serializable {

    /**
     * Gets the name.
     *
     * @return the name
     */
    String getName();

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    void setName(String name);

    /**
     * Gets the key.
     *
     * @return the key
     */
    int getKey();

    /**
     * Sets the key.
     *
     * @param key the new key
     */
    void setKey(int key);

    /**
     * Gets the requested executions.
     *
     * @return the requested executions
     */
    int getRequestedExecutions();

    /**
     * Sets the requested executions.
     *
     * @param requestedExecutionCount the new requested executions
     */
    void setRequestedExecutions(int requestedExecutionCount);

    /* (non-Javadoc)
     * @see com.sse.abtester.IHasTargetFreq#getTargetFreq()
     */
    @Override
    double getTargetFreq();

    /* (non-Javadoc)
     * @see com.sse.abtester.IHasTargetFreq#setTargetFreq(double)
     */
    @Override
    void setTargetFreq(double targetFreq);

    /**
     * Checks if the Variant is "dispatchable." If false, the
     * variant is not considered for new-user requests; however,
     * a non-Dispatchable variant is still applied to users
     * presenting its key.
     *
     * @return true, if is dispatchable for new uses.
     */
    boolean isDispatchable();

    /**
     * Sets the dispatchable state (true mean available for new users).
     *
     * @param active the new dispatchable
     */
    void setDispatchable(boolean active);

    /**
     * Gets the dispatched count.
     *
     * @return the dispatched count
     */
    int getDispatchedCount();

    /**
     * Sets the dispatched count.
     *
     * @param dispatchedCount the new dispatched count
     */
    void setDispatchedCount(int dispatchedCount);

    /**
     * Gets the responded count.
     *
     * @return the responded count
     */
    int getRespondedCount();

    /**
     * Sets the responded count.
     *
     * @param respondedCount the new responded count
     */
    void setRespondedCount(int respondedCount);

    /**
     * Inc dispatched counter.
     *
     * @return the int
     */
    int incDispatchedCounter();

    /**
     * Inc responded counter.
     *
     * @return the int
     */
    int incRespondedCounter();

    /**
     * Gets the component selector.
     *
     * @return the component selector
     */
    IVariationComponentSelector getComponentSelector();

    /**
     * Sets the component selector.
     *
     * @param selector the new component selector
     */
    void setComponentSelector(IVariationComponentSelector selector);

    /**
     * Gets the variation strategy.
     *
     * @return the variation strategy
     */
    IVariationStrategy getVariationStrategy();

    /**
     * Sets the variation strategy.
     *
     * @param strategy the new variation strategy
     */
    void setVariationStrategy(IVariationStrategy strategy);

    /**
     * Gets the variant props.
     *
     * @return the variant props
     */
    Properties getVariantProps();

    /**
     * Sets the variant props.
     *
     * @param variantProps the new variant props
     */
    void setVariantProps(Properties variantProps);

    /**
     * Copy.
     *
     * @return shallow copy of the IVariant.
     */
    IVariant<T> copy(); // same idea as 'clone'
}
