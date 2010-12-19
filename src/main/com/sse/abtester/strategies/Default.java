/*******************************************************************************
 * Copyright (c) 2010 Wayne Stidolph.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Affero Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/agpl-3.0.html
 *
 * Contributors:
 *     Wayne Stidolph - initial API and implementation
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

import com.sse.abtester.external.IVariationStrategy;

// TODO: Auto-generated Javadoc
/**
 * The Class VariationStrategyDefault.
 */
@SuppressWarnings("serial")
public @Data class Default implements IVariationStrategy {

    protected Properties props;

    /*
     * (non-Javadoc)
     *
     * @see com.sse.abtester.IVariationStrategy#getStrategy()
     */
    @Override
    public Strategy getStrategy() {
        // TODO Auto-generated method stub
        return Strategy.DEFAULT;
    }

    public boolean install() { return true;}
    public boolean remove() { return true;}
    /*
     * (non-Javadoc)
     *
     * @see com.sse.abtester.IVariationStrategy#execute(FilterChain,
     * HttpServletRequest, HttpServletResponse)
     */
    @Override
    public void execute(
            final FilterConfig filterConfig,
            final FilterChain chain,
            final HttpServletRequest hReq,
            final HttpServletResponse hResp)
    throws IOException, ServletException {
        chain.doFilter(hReq, hResp);

    }

    @Override
    public Object clone() {
        try {
            Object theClone = super.clone();
            if(props != null)
                ((Default) theClone).setProps((Properties)props.clone());
            return theClone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

}
