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
import org.xml.sax.SAXException;

import lombok.Setter;
import lombok.Synchronized;

// TODO: Auto-generated Javadoc
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
        boolean wasAdded = false;
        // first, make the text into a node
        Node ruleNode = null;
        try {
            InputStream is = new ByteArrayInputStream(
                    ruletext.getBytes("UTF-8"));
            Document doc = makeDocFrom(is);
            ruleNode = doc.getFirstChild();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (ruleNode == null) {
            return false;
        }

        // ensure the node is <name>-tagged
        NodeList children = ruleNode.getChildNodes();
        if (children == null) {
            System.out.println("resletext no child nodes: " + ruletext);
            return false;
        }
        Node nameNode = null;
        for (int idx = 0; idx < children.getLength(); idx++) {
            Node tempNode = children.item(idx);

            if ("name".equals(tempNode.getNodeName())) {
                nameNode = tempNode;
            }
            break;
        }

        Document confDoc = getConfFileDocument(confFileName);

        if (nameNode == null) {
            nameNode = confDoc.createElement("name");
            ruleNode.appendChild(nameNode);
        }

        String content = nameNode.getTextContent();
        nameNode.setTextContent(content + "_" + key);

        // finally, get the doc, append the node, put the doc

        if (confDoc == null || "".equals(confDoc))
            return false;

        String result = putDoc(confDoc);
        wasAdded = (result != null) && (result != "");

        return wasAdded;
    }

    /**
     * Create a Document (w3c) from an input stream.
     *
     * @param is
     *            the input stream to parse
     * @return null if fails, else the Document
     */
    public Document makeDocFrom(final InputStream is) {
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
        confDoc = getConfFileDocument(confFileName);
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
     * Gets the writeable conf file.
     *
     * @param fName
     *            the f name
     * @return the writeable conf file
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private Document getConfFileDocument(String fName) {
        Document doc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new File(fName));
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
