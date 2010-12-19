package com.sse.abtester.strategies;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AttachProps extends Default {

    @Override
    public void execute(
            final FilterConfig filterConfig,
            final FilterChain chain,
            final HttpServletRequest hReq,
            final HttpServletResponse hResp)
    throws IOException, ServletException {
        chain.doFilter(hReq, hResp);

    }
}
