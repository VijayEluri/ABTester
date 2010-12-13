package com.sse.abtester;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class VariationStrategyForward.
 */
public class VariationStrategyForward implements IVariationStrategy {

    /* (non-Javadoc)
     * @see com.sse.abtester.IVariationStrategy#getStrategy()
     */
    @Override
    public Strategy getStrategy() {
        // TODO Auto-generated method stub
        return Strategy.FORWARD;
    }

    /* (non-Javadoc)
     * @see com.sse.abtester.IVariationStrategy#getStrategyValue()
     */
    @Override
    public String getStrategyValue() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.sse.abtester.IVariationStrategy#setStrategyValue(String)
     */
    @Override
    public void setStrategyValue(String selector) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.sse.abtester.IVariationStrategy#execute(FilterChain, HttpServletRequest, HttpServletResponse)
     */
    @Override
    public void execute(FilterChain chain, HttpServletRequest hReq,
            HttpServletResponse hResp) {
        // TODO Auto-generated method stub

    }

}
