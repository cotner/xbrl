package org.xbrlapi.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Utility methods for constructing XML DOM objects.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class XMLDOMBuilder {
	
	private static Logger logger = Logger.getLogger(XMLDOMBuilder.class);
	
	private DocumentBuilderFactory factory = null;	
	private DocumentBuilder builder = null;
	
	public XMLDOMBuilder() throws XBRLException {
		initialise();
	}
	
	/**
	 * Initialise the document builder.
	 * @throws XBRLException if the builder cannot be initialised.
	 */
	private void initialise() throws XBRLException {
		try {
	        if (factory == null) {
				factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true);
				factory.setValidating(false);
			}
	        if (builder == null) {
		        builder = factory.newDocumentBuilder();
	        }
		} catch (Exception e) {
			logger.error("The fragment builder could not be constructed.");
			throw new XBRLException("The DOM builder could not be initialised.",e);
		}
	}
	
	/**
	 * Create an XML DOM document object that will contain the fragment content.
	 * The DOM is always namespace aware and non-validating
	 */
	public Document newDocument() {
		return builder.newDocument();
	}
	
	/**
	 * @param inputStream The input stream to parse.
	 * @return An XML DOM object for the given input stream.
	 * @throws XBRLException if an IO or SAX exception occurs.
	 */
	public Document newDocument(InputStream inputStream) throws XBRLException {
		try {
			return builder.parse(inputStream);
		} catch (IOException e) {
			throw new XBRLException("IO exception building an XML DOM.",e);
		} catch (SAXException e) {
			throw new XBRLException("SAX exception building an XML DOM.",e);
		}
	}

    /**
     * @param uri The URI of a document to parse into a DOM object.
     * @return An XML DOM object for the given URI.
     * @throws XBRLException if the URI corresponds to a malformed URL.
     */
    public Document newDocument(URI uri) throws XBRLException {
        try {
            return newDocument(uri.toURL().openStream());
        } catch (MalformedURLException e) { 
            throw new XBRLException(uri + " is a malformed URL.");
        } catch (IOException e) { 
            throw new XBRLException(uri + " caused an IO exception.");
        }
    }	
	
	/**
	 * @param xml is the XML in its string representation.
	 * @return The DOM document corresponding to the supplied XML.
	 * @throws XBRLException if problems occur constructing the DOM.
	 */
	public Document newDocument(String xml) throws XBRLException {
		try {
			return builder.parse(new InputSource(new StringReader(xml)));			
		} catch (IOException e) {
            logger.error("The problematic XML is");
            logger.error(xml);
			throw new XBRLException("An IO exception is causing problems.",e);
		} catch (SAXException e) {
            logger.error("The problematic XML is");
		    logger.error(xml);
			throw new XBRLException("A SAX exception is causing problems.",e);
		}
	}
 
}
