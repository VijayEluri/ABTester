package com.sse.abtester.strategies;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class UrlRewriteSupportTest {

    String BASELINE_TESTFILE = "src/test/com/sse/abtester/strategies/baseline_urlrewrite.xml";
    String TESTFILE = "src/test/com/sse/abtester/strategies/urlrewrite.xml";

    @Before
    public void restoreTESTFILE() {
        // copy the baseline file into the test file
        try {
            InputStream in = new FileInputStream(BASELINE_TESTFILE);
            OutputStream out = new FileOutputStream(TESTFILE);

            byte[] buf = new byte[2048];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Document getDoc() {
        InputStream is;
        try {
            is = new FileInputStream(TESTFILE);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            fail("File note found: " + TESTFILE);
            return null;
        }
        Document doc = UrlRewriteSupport.makeDocFrom(new InputSource(is));
        try {
            is.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            fail("couldn't close TESTFILE because " + e.getMessage());
        }
        return doc;
    }

    @Test
    public void testAddRule() throws FileNotFoundException {

        boolean ruleAdded;
        ruleAdded = UrlRewriteSupport.addRule(TESTFILE, ruleTextEmptyString, "0001");
        assertFalse(ruleAdded);
        ruleAdded = UrlRewriteSupport.addRule(TESTFILE, ruleTextEmptyRuleTag, "0002");
        assertTrue(ruleAdded);

    }

    @Test
    public void testMakeDocFromString() {
       Document doc = UrlRewriteSupport.makeDocFrom(ruleTextEmptyRuleTag);
       assertNotNull(doc);
       doc = UrlRewriteSupport.makeDocFrom(ruleTextSimple);
       assertNotNull(doc);
    }

    @Test
    public void testRemoveRule() {
        // fail("Not yet implemented"); // TODO
    }

    String ruleTextEmptyString = "";
    String ruleTextEmptyRuleTag = "<rule/>";
    String ruleTextSimple = "<rule><from></from>/some/test/resource.html<to>/some/test/resource_variant.html</to></rule>";
}
