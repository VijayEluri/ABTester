package com.sse.abtester;

import static org.junit.Assert.*;

import java.util.AbstractMap;
import java.util.HashMap;

import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class VariationAssignerTest.
 */
public class VariationAssignerTest {

    /**
     * Test enroll request is null if not enrolled.
     */
    @Test
    public void testEnrollRequestIsNullIfNotEnrolled() {
        VariationAssigner<VariantBean> va =
            new VariationAssigner<VariantBean>();
        MockHttpServletRequest req = new MockHttpServletRequest();
        IVariant<VariantBean> result = va.enrollRequest(req);
        assertEquals("should get back null for no enrollment", null, result);
    }

    /**
     * Test n variants.
     *
     * @param num_variants the num_variants
     */
    public void testNVariants(int num_variants){
        // make up some variants
        AbstractMap<String,IVariant<VariantBean>> coll =
            new HashMap<String,IVariant<VariantBean>>();

        for(int i = 0; i<num_variants; i++){
            String name = "Bean_"+i;
            IVariant<VariantBean> vb = new VariantBean(name);
            vb.setDispatchable(true); // else non of the bean is
                                // copied into normalized/weighted collections
            vb.setTargetFreq(1.0/num_variants);
            coll.put(name,vb);
        }
        VariationAssigner<VariantBean>  va =
            new VariationAssigner<VariantBean>();

        va.setIVariantCollection(coll);
        Frequency f = new Frequency();
        MockHttpServletRequest req = new MockHttpServletRequest();
        int TEST_LOOPS = 1000;
        double ALLOWABLE_DELTA=100.0/TEST_LOOPS; // rough check
        for (int i = 0; i < TEST_LOOPS; i++){
            IVariant<VariantBean> assigned =
                va.enrollRequest(req);
            if(assigned != null) f.addValue(assigned.getName());
        }
        for(IVariant<VariantBean> vrb: coll.values()){
            assertEquals(1.0/num_variants,
                    f.getPct(vrb.getName()), // NaN here can mean unknown name
                    ALLOWABLE_DELTA);
        }
    }

    /**
     * Test distribution tracks target freq.
     */
    @Test
    public void testDistributionTracksTargetFreq(){
        testNVariants(0);
        testNVariants(1);
        testNVariants(5);
        testNVariants(10);
        testNVariants(50);

    }
}
