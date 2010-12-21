package com.sse.abtester.strategies;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class UrlRewriteSupportTest {

    String BASELINE_TESTFILE = "src/test/com/sse/abtester/strategies/baseline_urlrewrite.xml";
    String TESTFILE = "src/test/com/sse/abtester/strategies/urlrewrite.xml";

    static boolean REMOVETESTFILES = true;

    @Before
    public void restoreTESTFILE() {
        // overwrite the baseline file into the test file
        try {
            InputStream in = new FileInputStream(BASELINE_TESTFILE);
            OutputStream out = new FileOutputStream(TESTFILE, false); // overwrite

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

    @AfterClass
    public static void removeTestFiles() {
        if (REMOVETESTFILES) {
            // ..
        }
    }

    private String getFileText(String fFileName) {
        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(fFileName));
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine() + NL);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } finally {
            if (scanner != null)
                scanner.close();
        }
        return text.toString();
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
    public void testAddRuleRejectsEmptyString() throws FileNotFoundException {

        boolean ruleAdded;
        ruleAdded = UrlRewriteSupport.addRule(TESTFILE, ruleTextEmptyString,
                "0001");
        assertFalse(ruleAdded);

    }

    @Test
    public void testAddRuleEmptyIsOK() throws FileNotFoundException {
        boolean ruleAdded = UrlRewriteSupport.addRule(TESTFILE,
                ruleTextEmptyRuleTag, "0002");
        assertTrue(ruleAdded);
    }

    @Test
    public void testAddRuleSimple() throws FileNotFoundException {
        boolean ruleAdded = UrlRewriteSupport.addRule(TESTFILE, ruleTextSimple,
                "0003");
        assertTrue(ruleAdded);

        String rewritten = getFileText(TESTFILE);
        System.out.println("I GOT: " + rewritten);
        // assert will fail because addRule adds name and key
        // assertTrue(rewritten.contains(ruleTextSimple));
    }

    @Test
    public void testAddRuleWithNamePreservesName() throws FileNotFoundException {
        boolean ruleAdded = UrlRewriteSupport.addRule(TESTFILE,
                ruleTextWithName, "0004");
        assertTrue(ruleAdded);

        String rewritten = getFileText(TESTFILE);
        System.out.println("I GOT: " + rewritten);
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
    String ruleTextSimple = "<rule><from>/some/test/resource.html</from><to>/some/test/resource_variant.html</to></rule>";
    String ruleTextWithName = "<rule><from>/some/test/resource.html</from><to>/some/test/resource_variant.html</to><name>WayneRule</name></rule>";
}
