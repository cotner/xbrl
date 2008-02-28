package org.xbrlapi.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Utility methods for constructing XML DOM objects.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class XMLDOMBuilder {
	
	private static Logger logger = Logger.getLogger(XMLDOMBuilder.class);
	
	private static DocumentBuilderFactory factory = null;	
	private static DocumentBuilder builder = null;
	private static EntityResolver testEntityResolver = null;
	
	public XMLDOMBuilder() {
		;
	}
	
	/**
	 * Initialise the document builder.
	 * @throws XBRLException if the builder cannot be initialised.
	 */
	private static void initialise() {
		try {
	        if (factory == null) {
				factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true);
				factory.setValidating(false);
			}
	        if (builder == null) {
		        builder = factory.newDocumentBuilder();
				if (testEntityResolver != null) {
					builder.setEntityResolver(testEntityResolver);
				}	        
	        }
		} catch (Exception e) {
			logger.error("The fragment builder could not be constructed.");
		}
	}
	
	/**
	 * Create an XML DOM document object that will contain the fragment content.
	 * The DOM is always namespace aware and non-validating
	 */
	public static Document newDocument() {
		initialise();
		return builder.newDocument();
	}
	
	/**
	 * @param inputStream The input stream to parse.
	 * @return An XML DOM object for the given input stream.
	 * @throws XBRLException if an IO or SAX exception occurs.
	 */
	public static Document newDocument(InputStream inputStream) throws XBRLException {
		try {
			initialise();
			return builder.parse(inputStream);
		} catch (IOException e) {
			throw new XBRLException("IO exception building an XML DOM.",e);
		} catch (SAXException e) {
			throw new XBRLException("SAX exception building an XML DOM.",e);
		}
	}

	/**
	 * @param xml is the XML in its string representation.
	 * @return The DOM document corresponding to the supplied XML.
	 * @throws XBRLException if problems occur constructing the DOM.
	 */
	public static Document newDocument(String xml) throws XBRLException {
		try {
			initialise();
			return builder.parse(new InputSource(new StringReader(xml)));			
		} catch (IOException e) {
			throw new XBRLException("An IO exception is causing problems.",e);
		} catch (SAXException e) {
			throw new XBRLException("A SAX exception is causing problems.",e);
		}
	}	 
 
}
