package com.sse.abtester.strategies;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import lombok.Setter;
import lombok.Synchronized;

/**
 * Manages the interfaces to the UrlRewriteFilter (does config file rewriting,
 * etc).
 *
 * @see http://www.tuckey.org/urlrewrite/
 * @author wstidolph
 *
 */
/**
 * @author wstidolph
 *
 */
public/*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
class UrlRewriteSupport {

    /**
     * Sets the conf file name.
     *
     * @param confFileName
     *            the new conf file name
     */
    @Setter
    private String confFileName;

    /**
     * Adds the rule to the UrlRewriter config file (confFileName); synchronized
     * so only one change to the file at a time.
     *
     * @param ruletext
     *            the ruletext
     * @param key
     *            the rule's key (must be unique in this file)
     * @return true if/only if the rule was added
     */
    @Synchronized
    public boolean addRule(final String ruletext, final String key) {
        // first, make the text into a node
        Document ruleDoc = makeDocFrom(ruletext);
        if(ruleDoc == null) {
            System.out.println("couldn't make ruleDoc from: " + ruletext);
        }
        ensureRuleDocIsNamed(ruleDoc, key);

        Document confDoc = getDocumentFromId(confFileName);
        if (confDoc == null) {
            return false;
        }

        boolean wasAdded = false;
        boolean confDocChanged = importNode(ruleDoc, confDoc);
        if (confDocChanged) {
            String result = putDoc(confDoc);
            wasAdded = (result != null) && (result != "");
        }

        return wasAdded;

    }

    public boolean importNode(final Node sourceNode, Document targetDoc) {
        boolean wasImported = false;

        Node importedNode = targetDoc.importNode(sourceNode, true);
        targetDoc.getDocumentElement().appendChild(importedNode);
        wasImported = true;
        return wasImported;
    }

    public void ensureRuleDocIsNamed(Document node, final String key) {
        // ensure the node is <name>-tagged
        NodeList names = node.getElementsByTagName("name");

        Node nameNode = null;

        if (names.getLength() == 0){
            // need a <name>
            nameNode = node.createElement("name"); // leave it empty
            node.getDocumentElement().appendChild(nameNode);
        } else {
            nameNode = names.item(0);
        }

        String content = nameNode.getTextContent();
        nameNode.setTextContent(content + "_" + key);
    }

    public Document makeDocFrom(String text){
        Document doc = null;
        try {
            InputStream is = new ByteArrayInputStream(
                    text.getBytes("UTF-8"));
            doc = makeDocFrom(new InputSource(is));
            try {
                is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                is = null;
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return doc;
    }
    /**
     * Create a Document (w3c) from an input stream.
     *
     * @param is
     *            the input stream to parse
     * @return null if fails, else the Document
     */
    public Document makeDocFrom(final InputSource is) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            doc = factory.newDocumentBuilder().parse(is);
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * Removes a named rule from urlrewriter config file.
     *
     * @param ruleNameSuffix
     *            rule to remove is identified by "__"+ruleNameSuffix
     * @return true if/only if the rule was found and removed
     */
    @Synchronized
    public boolean removeRule(String ruleNameSuffix) {
        boolean isRemoved = false;
        Document confDoc;
        confDoc = getDocumentFromId(confFileName);
        if (confDoc == null) {
            return false;
        }

        // find an element with the given name (ruleKey)
        NodeList nl = confDoc.getElementsByTagName("name");
        Node nameNode = null;
        for (int idx = 0; idx < nl.getLength(); idx++) {
            Node tempNode = nl.item(idx);
            String content = tempNode.getTextContent();
            if (content.endsWith("__" + ruleNameSuffix)) {
                nameNode = tempNode;
            }
            break;
        }

        // and verify it's <rule>, <outbound-rule>, <class-rule>
        if (nameNode != null) {
            Node ruleNode = nameNode.getParentNode();
            // make sure it's a rule
            String tag = ruleNode.getNodeName();
            boolean isRule = "rule".equals(tag) || "outbound-rule".equals(tag)
                    || "class-rule".equals(tag);
            if (!isRule) {
                return false; // bail out, this doc isn't what we expect
            }
            confDoc.removeChild(ruleNode);
            putDoc(confDoc);
            isRemoved = true;
        }
        return isRemoved;
    }

    /**
     * Gets and XML DOM Documaent based on a system identifer.
     *
     * @param sysId
     *            system ID (such as file path URL)
     * @return the Document (or null)
     */
    private Document getDocumentFromId(final String sysId) {
        Document doc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // set factory options
        factory.setIgnoringElementContentWhitespace(true);

        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(sysId));
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return doc;
    }

    /**
     * Replace the existing configuration doc.
     *
     * @param doc
     *            the Document to be streamed into the new file.
     * @return the old file's new name (has suffix _<timeOfReplacement>)
     */
    private String putDoc(final Document doc) {
        if (doc == null) {
            System.out.println("null doc, not writing out new " + confFileName);
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss.SSSZ");
        String dateString = df.format(new Date());

        // Use a Transformer for output
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

        DOMSource source = new DOMSource(doc);
        String newFileName = confFileName + "_" + dateString + "_new";
        File newFile = new File(newFileName);
        StreamResult result = new StreamResult(newFile);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

        // swap in the new File
        File oldFile = new File(confFileName);
        File newNameForOldFile = new File(confFileName + "_" + dateString);
        oldFile.renameTo(newNameForOldFile);

        File newNameForNewFile = new File(confFileName);
        newFile.renameTo(newNameForNewFile);

        return newNameForOldFile.getName();
    }
}
