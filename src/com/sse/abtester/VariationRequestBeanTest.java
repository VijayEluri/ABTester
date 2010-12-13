package com.sse.abtester;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class VariationRequestBeanTest.
 */
public class VariationRequestBeanTest {

    /** The vrb. */
    VariationRequestBean vrb;

    /**
     * Sets the up bean.
     */
    @Before
    public void setUpBean(){
        vrb = new VariationRequestBean();
    }

    /**
     * Test hash code.
     */
    @Test
    public void testHashCode() {
        int hc = vrb.hashCode();
        assertNotSame(0, hc);
    }

    /**
     * Test variation request bean.
     */
    @Test
    public void testVariationRequestBean() {
        assertNotNull(vrb);
    }

    /**
     * Test set get request key.
     */
    @Test
    public void testSetGetRequestKey() {
        vrb.setRequestKey("FOO");
        assertEquals("FOO", vrb.getRequestKey());
    }

    /**
     * Test set get requested executions.
     */
    @Test
    public void testSetGetRequestedExecutions() {
        vrb.setRequestedExecutions(100);
        assertEquals(100, vrb.getRequestedExecutions());
    }

    /**
     * Test set get component selector.
     */
    @Test
    public void testSetGetComponentSelector() {
        // TODO
    }

    /**
     * Test set get variation strategy.
     */
    @Test
    public void testSetGetVariationStrategy() {
        IVariationComponentSelector componentSelector = null;
        vrb.setComponentSelector(componentSelector);
        assertEquals(null, componentSelector);

    }

    /**
     * Test set get requested target freq.
     */
    @Test
    public void testSetGetRequestedTargetFreq() {
        vrb.setRequestedTargetFreq(0.13);
        assertEquals(0.13, vrb.getRequestedTargetFreq(), 0.001);
    }

    /**
     * Test set get eligible percentage cap.
     */
    @Test
    public void testSetGetEligiblePercentageCap() {
        // TODO
    }

    /**
     * Test set component selector.
     */
    @Test
    public void testSetComponentSelector() {
        // TODO
    }

    /**
     * Test set get variation properties.
     */
    @Test
    public void testSetGetVariationProperties() {
        // TODO
    }

    /**
     * Test equals object.
     */
    @Test
    public void testEqualsObject() {
        // TODO
    }

    /**
     * Test can equal.
     */
    @Test
    public void testCanEqual() {
        // TODO
    }

    /**
     * Test to string.
     */
    @Test
    public void testToString() {
        String s = vrb.toString();
        assertNotNull(s);
    }

}
