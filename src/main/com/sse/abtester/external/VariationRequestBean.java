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

import com.google.common.collect.Multimap;

import lombok.Data;

// TODO: Auto-generated Javadoc
/**
 * The Class VariationRequestBean (just a DTO).
 */
public /* (non-Javadoc)
  * @see java.lang.Object#toString()
  */
 @Data class VariationRequestBean implements Serializable {

    /** The request key. */
    public String requestName;

    /** The requested executions. */
    public int requestedExecutions;

    /** The requested target freq. */
    public double requestedTargetFreq = 0.5f;

    /** The eligible percentage cap. */
    public double eligiblePercentageCap = 1.0f; // 1.0 is 100%, all-eligible

    /** The variation strategy; e.g., com.sse.abtester.external.VariationStrategyForward */
    public String variationStrategyClassName;

    /** The variation properties. */
    public Properties variationProperties;
}
