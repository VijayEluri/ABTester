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

import javax.servlet.http.HttpServletRequest;

/**
 * Allows for type-specific instance key generation.
 * Default is hashCode();
 */
public interface IKeyGenerator {

    /**
     * Gets the key.
     *
     * @param request the request
     * @return the key
     */
    String getKey(HttpServletRequest request);
}
