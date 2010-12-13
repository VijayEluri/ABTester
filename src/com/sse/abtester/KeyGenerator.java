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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import lombok.ToString;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyGenerator.
 */
public /* (non-Javadoc)
  * @see java.lang.Object#toString()
  */
 @ToString class KeyGenerator implements IKeyGenerator {

    /* (non-Javadoc)
     * @see com.sse.abtester.IKeyGenerator#getKey(HttpServletRequest)
     */
    @Override
    public String getKey(HttpServletRequest request) {
        // TODO implement
        return "default_cookie_tracker_"+(new Date()).toString();
    }

}
