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
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO: Auto-generated Javadoc
/**
 * The Interface IVariationStrategy.
 */
public interface IVariationStrategy extends Serializable, Cloneable {

    /**
     * The Enum Strategy.
     */
    enum Strategy {
        /**
         * Basic strategy, just call the chain.
         */
        DEFAULT ("com.sse.abtester.strategies.Default"),
        /**
         * Tell ABTester Filter to attach an ABSTYLE property to the request,;
         * use of the ABSTYLE is up to the Varier.
         */
        ATTACH_STYLE ("com.sse.abtester.strategies.Attach"),

        /**
         * Use UrlRewriteFilter rules.
         * @see http://www.tuckey.org/urlrewrite/
         */
        REWRITE ("com.sse.abtester.strategies.UrlRewrite"),

        /** cause instantiation of class identified in strategyValue string */
        CUSTOM ("");

        String implementingClassName;
        Strategy(String classname){
            implementingClassName = classname;
        }
    }

    /**
     * Gets the strategy.
     *
     * @return the strategy
     */
    Strategy getStrategy();

    void setProps(Properties props);
    Properties getProps();


    /**
     * Install this IVariant into its backing system. For
     * example, a UrlRewrite might use this call to rewrite
     * and reload the Filter definition.
     *
     * @return true, if successful
     */
    boolean install();

    /**
     * Removes the IVariant from teh backing system. For
     * example, a UrlRewrite might use this call to rewrite
     * and reload the Filter definition.
     *
     * @return true, if successful
     */
    boolean remove();

    Object clone();
    /**
     * Execute. Yes, I know a GOF Strategy really shouldn't take
     * parameters in the execute; maybe I'll rename to process()
     *
     * @param chain the chain
     * @param hReq the h req
     * @param hResp the h resp
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    void execute(FilterConfig filterConfig, FilterChain chain, HttpServletRequest hReq,
            HttpServletResponse hResp)
         throws IOException, ServletException;

}
