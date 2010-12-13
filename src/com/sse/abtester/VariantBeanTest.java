package com.sse.abtester;

import static org.junit.Assert.*;

import org.junit.Test;

public class VariantBeanTest {

    @Test
    public void testHashCode() {
        IVariant<VariantBean> iv = new VariantBean();
        int hc = iv.hashCode();
        assertFalse(hc ==0);
        IVariant<VariantBean> copy = iv.copy();
        assertEquals(hc, copy.hashCode());
    }

    @Test
    public void testCopy() {
        int TESTKEY=13;
        double TFIV = 0.78;
        double TFCOPY = 0.99;
        IVariant<VariantBean> iv = new VariantBean();
        iv.setKey(TESTKEY);
        iv.setTargetFreq(TFIV);

        IVariant<VariantBean> copy = iv.copy();
        assertNotSame(iv,copy);
        assertEquals(iv, copy);
        copy.setTargetFreq(TFCOPY);
        assertEquals(iv.getTargetFreq(), TFIV, 0.001);
        assertEquals(copy.getTargetFreq(), TFCOPY, 0.001);

        // ensure the key was copied over
        assertEquals(TESTKEY, copy.getKey());
    }

}