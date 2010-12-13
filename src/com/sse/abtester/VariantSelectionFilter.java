package com.sse.abtester;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// TODO: Auto-generated Javadoc
/**
 * Servlet Filter implementation class VariantSelectionFilter.
 */
public class VariantSelectionFilter implements Filter {

    /** The VM. */
    VariantManager VM;

    /** The VSKEY. */
    String VSKEY = "VSKEY"; // set default

    /** The kgen. */
    IKeyGenerator kgen;

    /** The filter config. */
    FilterConfig filterConfig = null;

    /**
     * Gets the filter config.
     *
     * @return the filter config
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * Attach variant selection and publishing to the request processing.
     *
     * @param request the request
     * @param response the response
     * @param chain the chain
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest hReq = (HttpServletRequest) request;
        HttpServletResponse hRes = (HttpServletResponse) response;
        IVariant<VariantBean> theVariant = null;
        IVariationStrategy variationStrategy = null;

        HttpSession session = hReq.getSession();

        if (VM != null) {
            String vsKey = getCookieValue(hReq);
            if (vsKey != "") {
                // has a key, so is enrolled, just update
                // (and get the variant control)
                theVariant = VM.updateVariant(vsKey);
            } else {
                // give opportunity to enroll, map key into a Cookie
                theVariant = VM.enrollRequest(hReq);
                if (theVariant != null)
                    vsKey = "" + theVariant.getKey();
                if (vsKey != null && vsKey != "") {
                    Cookie cookie = new Cookie(VSKEY, vsKey);
                    hRes.addCookie(cookie);
                }
            }
            if (theVariant != null) {
                Properties props = theVariant.getVariantProps();
                session.setAttribute(VSKEY, props);
                variationStrategy = theVariant.getVariationStrategy();
            }
        }
        if (variationStrategy == null) {
            variationStrategy = new VariationStrategyDefault();
            // chain.doFilter(hReq, hRes);
        }
        // attach the variation properties to the HttpSession

        variationStrategy.execute(chain, hReq, hRes);

        // we always offer up the response for pub,
        // even if it wasn't varied
        if (VM != null)
            VM.publishVariationResponse(hRes);
    }

    /**
     * Gets the cookie value.
     *
     * @param request the request
     * @return cookie value attached to VSKEY, or empty String.
     */
    private String getCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String cookieValue = "";
        if (cookies == null)
            return cookieValue;

        for (Cookie c : cookies) {
            if (c.getName() == VSKEY) {
                cookieValue = c.getValue();
                break;
            }
        }
        return cookieValue;
    }

    /**
     * Default constructor.
     */
    public VariantSelectionFilter() {
    }

    /**
     * Convenience constructor, avoid use of setVariantConstructor.
     *
     * @param vm the vm
     */
    public VariantSelectionFilter(VariantManager vm) {
        VM = vm;
    }

    /**
     * Sets the variant manager.
     *
     * @param vm the new variant manager
     */
    public void setVariantManager(VariantManager vm) {
        VM = vm;
    }

    /**
     * Sets the vSKEY.
     *
     * @param VSKEY the new vSKEY
     */
    public void setVSKEY(String VSKEY) {
        this.VSKEY = VSKEY;
    }

    /**
     * Sets the kgen.
     *
     * @param kgen the new kgen
     */
    public void setKgen(IKeyGenerator kgen) {
        this.kgen = kgen;
    }

    /**
     * Inits the.
     *
     * @param fConfig the f config
     * @throws ServletException the servlet exception
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        filterConfig = fConfig;
    }

    /**
     * Destroy.
     *
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
    }
}
