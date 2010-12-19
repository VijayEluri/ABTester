package com.sse.abtester;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import lombok.Getter;
import lombok.Setter;

public class ApplicationWatch implements ServletContextListener {

    @Setter
    VariantManager vm;

    ServletContext sc;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        if(vm != null)
            vm.shutdown();

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        sc = arg0.getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.
        getRequiredWebApplicationContext(sc);

        vm = (VariantManager)wac.getBean("variantManager");
        if(vm != null)
            vm.startup();

    }

}
