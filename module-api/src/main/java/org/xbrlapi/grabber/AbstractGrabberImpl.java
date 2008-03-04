package org.xbrlapi.grabber;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public abstract class AbstractGrabberImpl implements Grabber {

	/**
	 * @param url The URL to parse.
	 * @return The XML DOM document node for the parsed XML resource
	 * @throws Exception if the parse operation fails.
	 */
	protected static Document getDocument(URL url) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(false);
	        DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(url.toString());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}	
	
}
