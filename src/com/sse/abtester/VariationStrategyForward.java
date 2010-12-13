package com.sse.abtester;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VariationStrategyForward implements IVariationStrategy {

    @Override
    public Strategy getStrategy() {
        // TODO Auto-generated method stub
        return Strategy.FORWARD;
    }

    @Override
    public String getStrategyValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setStrategyValue(String selector) {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute(FilterChain chain, HttpServletRequest hReq,
            HttpServletResponse hResp) {
        // TODO Auto-generated method stub

    }

}
