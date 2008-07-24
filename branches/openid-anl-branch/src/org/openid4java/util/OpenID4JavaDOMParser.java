/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.cyberneko.html.HTMLTagBalancingListener;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLHtmlElement;
import org.xml.sax.InputSource;

/**
 * @author Sutra Zhou
 * 
 */
public class OpenID4JavaDOMParser extends DOMParser implements HTMLTagBalancingListener
{
    /**
     * Create an InputSource form a String.
     * 
     * @param s
     *            the String
     * @return an InputSource
     * @throws NullPointerException
     *             if s is null.
     */
    public static InputSource createInputSource(String s)
    {
        try
        {
            return new InputSource(
                    new ByteArrayInputStream(s.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String toXmlString(Document doc)
    {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer;
        try
        {
            transformer = factory.newTransformer();
        }
        catch (TransformerConfigurationException e)
        {
            throw new RuntimeException(e);
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        DOMSource source = new DOMSource(doc);
        StringWriter xmlString = new StringWriter();
        StreamResult streamResult = new StreamResult(xmlString);
        try
        {
            transformer.transform(source, streamResult);
        }
        catch (TransformerException e)
        {
            throw new RuntimeException(e);
        }
        return xmlString.toString();
    }

    private boolean ignoredHeadStartElement;

    public boolean isIgnoredHeadStartElement()
    {
        return ignoredHeadStartElement;
    }

    public void ignoredEndElement(QName element, Augmentations augs)
    {
        // Do nothing.
    }

    public void ignoredStartElement(QName element, XMLAttributes attrs, Augmentations augs)
    {
        if (element.rawname.equals("HEAD")
                && this.fCurrentNode instanceof HTMLHtmlElement)
        {
            this.ignoredHeadStartElement = true;
        }
    }
}