package com.sse.abtester;

import javax.servlet.Filter;
import javax.servlet.ServletException;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

public class RewriterFilterProxy extends DelegatingFilterProxy {
    WebApplicationContext _wac;

    public WebApplicationContext findWebApplicationContest(){
        return _wac;
    }

    public Filter initDelegate(WebApplicationContext wac) throws ServletException {
        _wac = wac;
        return super.initDelegate(wac);
    }

}
