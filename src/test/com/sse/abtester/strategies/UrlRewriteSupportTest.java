package com.sse.abtester.strategies;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class UrlRewriteSupportTest {

    String TESTFILE = "src/test/com/sse/abtester/strategies/urlrewrite.xml";
    UrlRewriteSupport uut;

    private Document getDoc() {
        InputStream is;
        try {
            is = new FileInputStream(TESTFILE);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            fail("File note found: " + TESTFILE);
            return null;
        }
        Document doc = uut.makeDocFrom(new InputSource(is));
        try {
            is.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            fail("couldn't close TESTFILE because " + e.getMessage());
        }
        return doc;
    }

    @Before
    public void init(){
        uut = new UrlRewriteSupport();
        uut.setConfFileName(TESTFILE);
    }

    @Test
    public void testAddRule() throws FileNotFoundException {

        boolean ruleAdded;
       // ruleAdded = uut.addRule(ruleTextEmptyString, "0001");
       //  assertFalse(ruleAdded);
        ruleAdded = uut.addRule(ruleTextEmptyRuleTag, "0002");
        assertFalse(ruleAdded);

    }

    @Test
    public void testMakeDocFrom() {
        Document doc = getDoc();
        assertNotNull(doc);

    }

    @Test
    public void testRemoveRule() {
        // fail("Not yet implemented"); // TODO
    }

    String ruleTextEmptyString = "";
    String ruleTextEmptyRuleTag = "<rule/>";
}
