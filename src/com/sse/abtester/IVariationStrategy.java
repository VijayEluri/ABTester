package com.sse.abtester;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IVariationStrategy {
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
        /**
         * Tells ABTester to forward the request (with attached properties) to a
         * name (as identified by the strategyValue)
         */
        FORWARD,
        /**
         * Tells ABTester to redirect the client browser, to a URL given in the
         * strategyValue
         */
        REDIRECT

    }

    Strategy getStrategy();

    String getStrategyValue();

    /**
     * Set a value to be used in strategy application. Actual use varies between
     * Strategies:
     * <ul>
     * <li>ATTACH_STYLE => passed as the value of the ABSTYLE key</li>
     * <li>FORWARD => used to name the servlet to which to Forward
     * </ul>
     *
     * @param selector
     */
    void setStrategyValue(String selector);

    void execute(FilterChain chain, HttpServletRequest hReq,
            HttpServletResponse hResp)
         throws IOException, ServletException;

}
