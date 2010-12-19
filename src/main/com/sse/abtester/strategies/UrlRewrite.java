/*******************************************************************************
 * Copyright (c) 2010 Wayne Stidolph.
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the GNU Affero General Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/agpl-3.0.html
 *

 *
 * Contributors:
 *     Wayne Stidolph - initial API and implementation
 *
 ******************************************************************************/
package com.sse.abtester.strategies;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Data;

// TODO: Auto-generated Javadoc
/**
 * The Class VariationStrategyForward. This processes through a Url Rewrite
 * Filter, which will alter, forward or redirect the request according to
 * control file the ABTester maintains to track variation requests.
 *
 * @see http://www.tuckey.org/urlrewrite/
 */
public @Data
class UrlRewrite extends Default {
    String rule; // rule text
    String confPath="/WEB-INF/urlrewrite.xml";

    @Override
    public void setProps(Properties props){
        super.setProps(props);
        rule = (String)props.get("ruletext");
    }

    /**
     * Add the rule string to the rules collection
     * if the configuration file.
     * @see com.sse.abtester.strategies.Default#install()
     */
    @Override
    public boolean install() {
        boolean installed = false;

        return installed;
    }

    @Override
    public boolean remove() {
        boolean removed = false;

        return removed;
    }
    /*
     * (non-Javadoc)
     *
     * @see com.sse.abtester.IVariationStrategy#execute(FilterChain,
     * HttpServletRequest, HttpServletResponse)
     */
    @Override
    public void execute(final FilterConfig filterConfig,
            final FilterChain chain, final HttpServletRequest hReq,
            final HttpServletResponse hResp) throws IOException,
            ServletException {

        chain.doFilter(hReq, hResp);

    }
}
