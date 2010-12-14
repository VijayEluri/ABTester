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

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO: Auto-generated Javadoc
/**
 * The Interface IVariationStrategy.
 */
public interface IVariationStrategy extends Serializable {

    /**
     * The Enum Strategy.
     */
    enum Strategy {
        /**
         * Basic strategy, just call the chain.
         */
        DEFAULT,
        /**
         * Tell ABTester Filter to attach an ABSTYLE property to the request,;
         * use of the ABSTYLE is up to the Varier.
         */
        ATTACH_STYLE,

        /** Tells ABTester to forward the request (with attached properties) to a name (as identified by the strategyValue). */
        FORWARD,

        /** Tells ABTester to redirect the client browser, to a URL given in the strategyValue. */
        REDIRECT

    }

    /**
     * Gets the strategy.
     *
     * @return the strategy
     */
    Strategy getStrategy();

    /**
     * Gets the strategy value.
     *
     * @return the strategy value
     */
    String getStrategyValue();

    /**
     * Set a value to be used in strategy application. Actual use varies between
     * Strategies:
     * <ul>
     * <li>ATTACH_STYLE => passed as the value of the ABSTYLE key</li>
     * <li>FORWARD => used to name the servlet to which to Forward
     * </ul>
     *
     * @param selector the new strategy value
     */
    void setStrategyValue(String selector);

    /**
     * Execute.
     *
     * @param chain the chain
     * @param hReq the h req
     * @param hResp the h resp
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    void execute(FilterChain chain, HttpServletRequest hReq,
            HttpServletResponse hResp)
         throws IOException, ServletException;

}
