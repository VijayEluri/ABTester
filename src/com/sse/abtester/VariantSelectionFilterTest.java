package com.sse.abtester;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

public class VariantSelectionFilterTest {
    String VSKEY = "VSKEY";
    int TESTKEY = 123;
    FilterChain fc;
    MockHttpServletRequest mockReq;
    MockHttpServletResponse mockRes;
    VariantSelectionFilter vsf;
    VariantManager VM;
    VariantBean testvariant;

    @Before
    public void reinit(){
        // make a Mockito mock of the FC so we can verify it gets called
        fc = mock(FilterChain.class);
        mockReq = new MockHttpServletRequest();
        mockReq.setSession(new MockHttpSession());
        mockRes = new MockHttpServletResponse();

        VM = mock(VariantManager.class);
        vsf = new VariantSelectionFilter();
        vsf.setVariantManager(VM);
        vsf.setVSKEY(VSKEY);
    }

    @Test
    public void testNullVMIsSafe() throws IOException, ServletException {
        vsf.setVariantManager(null);
        vsf.doFilter(mockReq, mockRes, fc);
        verify(fc).doFilter(mockReq, mockRes); // should still invoke chain
    }

    @Test
    public void testEnrollsReqIfNoCookie() throws IOException, ServletException {

        IVariationStrategy forward= new VariationStrategyForward();
        VariantBean testvariant = mock(VariantBean.class);
        when(testvariant.getKey()).thenReturn(TESTKEY);
        when(testvariant.getVariantProps()).thenReturn(new Properties());
        when(testvariant.getKey()).thenReturn(TESTKEY);
        when(testvariant.getVariationStrategy()).thenReturn(forward);
        when(testvariant.getVariantProps()).thenReturn(new Properties());
        when(VM.enrollRequest(mockReq)).thenReturn(testvariant);
        vsf.doFilter(mockReq, mockRes, fc);

        Cookie attachedCookie = mockRes.getCookie(VSKEY);
        assertNotNull(attachedCookie);
        assertEquals(""+TESTKEY, attachedCookie.getValue());
        verify(VM).publishVariationResponse(mockRes);
    }

    @Test
    public void testUpdatesVMIfCookie() throws IOException,
            ServletException {
        VariantBean testvariant = mock(VariantBean.class);
        IVariationStrategy forward= new VariationStrategyForward();

        when(testvariant.getKey()).thenReturn(TESTKEY);
        when(testvariant.getVariationStrategy()).thenReturn(forward);
        when(testvariant.getVariantProps()).thenReturn(new Properties());
        when(VM.enrollRequest(mockReq)).thenReturn(testvariant);

        mockReq.setCookies(new Cookie(VSKEY,""+TESTKEY)); // set the test cookie

        vsf.doFilter(mockReq, mockRes, fc);

        verify(VM,times(0)).enrollRequest(mockReq);
        verify(VM).updateVariant(""+TESTKEY);
        verify(VM).publishVariationResponse(mockRes);
    }

}
