/*******************************************************************************
 * Copyright (c) 2010 Wayne Stidolph.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Affero Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/agpl-3.0.html
 *
 * Contributors:
 *     Wayne Stidolph - initial API and implementation
 ******************************************************************************/
package com.sse.abtester;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.sse.abtester.external.IVariationStrategy;
import com.sse.abtester.strategies.Default;
import com.sse.abtester.strategies.UrlRewrite;

// TODO: Auto-generated Javadoc
/**
 * The Class VariantSelectionFilterTest.
 */
public class VariantSelectionFilterTest {

    /** The VSKEY. */
    String VSKEY = "VSKEY";

    /** The TESTKEY. */
    int TESTKEY = 123;

    /** The fc. */
    FilterChain fc;

    /** The mock req. */
    MockHttpServletRequest mockReq;

    /** The mock res. */
    MockHttpServletResponse mockRes;

    /** The vsf. */
    VariantSelectionFilter vsf;

    /** The VM. */
    VariantManager VM;

    /** The testvariant. */
    VariantBean testvariant;

    /**
     * Reinit.
     */
    @Before
    public void reinit(){
        // make a Mockito mock of the FC so we can verify it gets called
        fc = mock(FilterChain.class);
        mockReq = new MockHttpServletRequest();
        mockReq.setSession(new MockHttpSession());
        mockRes = new MockHttpServletResponse();

        VM = mock(VariantManager.class);
        vsf = new VariantSelectionFilter();
        try {
            vsf.init(new MockFilterConfig());
        } catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        vsf.setVariantManager(VM);
        vsf.setVSKEY(VSKEY);
    }

    /**
     * Test null vm is safe.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    @Test
    public void testNullVMIsSafe() throws IOException, ServletException {
        vsf.setVariantManager(null);
        vsf.doFilter(mockReq, mockRes, fc);
        verify(fc).doFilter(mockReq, mockRes); // should still invoke chain
    }

    /**
     * Test enrolls req if no cookie.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    @Test
    public void testEnrollsReqIfNoCookie() throws IOException, ServletException {

        IVariationStrategy forward = new Default();
        VariantBean testvariant = mock(VariantBean.class);
        when(testvariant.getKey()).thenReturn(TESTKEY);

        when(testvariant.getKey()).thenReturn(TESTKEY);
        when(testvariant.getVariationStrategy()).thenReturn(forward);

        when(VM.enrollRequest(mockReq)).thenReturn(testvariant);
        vsf.doFilter(mockReq, mockRes, fc);

        Cookie attachedCookie = mockRes.getCookie(VSKEY);
        assertNotNull(attachedCookie);
        assertEquals(""+TESTKEY, attachedCookie.getValue());
        verify(VM).publishVariationResponse(mockRes);
    }

    /**
     * Test updates vm if cookie.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    @Test
    public void testUpdatesVMIfCookie() throws IOException,
            ServletException {
        VariantBean testvariant = mock(VariantBean.class);
        IVariationStrategy forward= new UrlRewrite();

        when(testvariant.getKey()).thenReturn(TESTKEY);
        when(testvariant.getVariationStrategy()).thenReturn(forward);

        when(VM.enrollRequest(mockReq)).thenReturn(testvariant);

        mockReq.setCookies(new Cookie(VSKEY,""+TESTKEY)); // set the test cookie

        vsf.doFilter(mockReq, mockRes, fc);

        verify(VM,times(0)).enrollRequest(mockReq);
        verify(VM).updateVariant(""+TESTKEY);
        verify(VM).publishVariationResponse(mockRes);
    }

}
