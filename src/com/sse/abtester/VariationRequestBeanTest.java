package com.sse.abtester;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class VariationRequestBeanTest {

    VariationRequestBean vrb;
    @Before
    public void setUpBean(){
        vrb = new VariationRequestBean();
    }
    @Test
    public void testHashCode() {
        int hc = vrb.hashCode();
        assertNotSame(0, hc);
    }

    @Test
    public void testVariationRequestBean() {
        assertNotNull(vrb);
    }

    @Test
    public void testSetGetRequestKey() {
        vrb.setRequestKey("FOO");
        assertEquals("FOO", vrb.getRequestKey());
    }

    @Test
    public void testSetGetRequestedExecutions() {
        vrb.setRequestedExecutions(100);
        assertEquals(100, vrb.getRequestedExecutions());
    }

    @Test
    public void testSetGetComponentSelector() {
        // TODO
    }

    @Test
    public void testSetGetVariationStrategy() {
        IVariationComponentSelector componentSelector = null;
        vrb.setComponentSelector(componentSelector);
        assertEquals(null, componentSelector);

    }

    @Test
    public void testSetGetRequestedTargetFreq() {
        vrb.setRequestedTargetFreq(0.13);
        assertEquals(0.13, vrb.getRequestedTargetFreq(), 0.001);
    }

    @Test
    public void testSetGetEligiblePercentageCap() {
        // TODO
    }

    @Test
    public void testSetComponentSelector() {
        // TODO
    }

    @Test
    public void testSetGetVariationProperties() {
        // TODO
    }

    @Test
    public void testEqualsObject() {
        // TODO
    }

    @Test
    public void testCanEqual() {
        // TODO
    }

    @Test
    public void testToString() {
        String s = vrb.toString();
        assertNotNull(s);
    }

}
