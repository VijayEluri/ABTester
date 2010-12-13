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
