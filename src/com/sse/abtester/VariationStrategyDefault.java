package com.sse.abtester;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VariationStrategyDefault implements IVariationStrategy {

    @Override
    public Strategy getStrategy() {
        // TODO Auto-generated method stub
        return Strategy.DEFAULT;
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
            HttpServletResponse hResp)
           throws IOException, ServletException {
        chain.doFilter(hReq, hResp);

    }

}
