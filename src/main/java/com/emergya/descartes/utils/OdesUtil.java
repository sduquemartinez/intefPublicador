package com.emergya.descartes.utils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OdesUtil {
    private static final Log LOG = LogFactory.getLog(OdesUtil.class);

    private OdesUtil() {

    }

    /**
     * Method to get the XPath result from an XML
     * @param manifest XML source
     * @param property XPath property to search
     * @param multiple Multiple values or a single one
     * @return A String with the single value or a List of String if multiple values are requested; null if no values are found.
     */
    public static Object getManifestProperty(byte[] manifest, String property, boolean multiple) {
        Object result = null;
        if (manifest != null) {
            DocumentBuilder dBuilder;
            try {
                dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document manifestDoc = dBuilder.parse(new ByteArrayInputStream(manifest));

                if (multiple) {
                    result = getXPathMultiValue(manifestDoc, property);
                } else {
                    result = getXPathValue(manifestDoc, property);
                }
            } catch (Exception e) {
                
            }
        }

        return result;
    }
    
    /**
     * Method to get a single XPath value from an XML
     * @param doc XML source
     * @param xPathExpression XPath expression to search
     * @return Result of the evaluation of the XPath expression in the document
     * @throws XPathExpressionException if the expression is not correct
     */
    public static String getXPathValue(Document doc, String xPathExpression) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression xPathExp = xpath.compile(xPathExpression);
        if (xPathExp != null) {
            return xPathExp.evaluate(doc);
        } else {
            return null;
        }
    }

    /**
     * Method to get a multiple XPath value from an XML
     * @param doc XML source
     * @param xPathExpression XPath expression to search
     * @return Result of the evaluation of the XPath expression in the document
     * @throws XPathExpressionException if the expression is not correct
     */
    public static List<String> getXPathMultiValue(Document doc, String xPathExpression) throws XPathExpressionException {
        List<String> result = new ArrayList<String>();
        XPath xpath = XPathFactory.newInstance().newXPath();

        XPathExpression xPathExp = xpath.compile(xPathExpression);
        if (xPathExp != null) {
            NodeList ids = (NodeList) xPathExp.evaluate(doc, XPathConstants.NODESET);
            if (ids != null) {
                for (int idx = 0; idx < ids.getLength(); idx++) {
                    Node id = ids.item(idx);
                    result.add(id.getTextContent());
                }
            }
        }

        return result;
    }

   }
