package org.xbrlapi.sax.DTDHandler.framework.tests;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xbrlapi.utilities.XBRLException;
import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Content, DTD, and Error handler for the SAX parser.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class Handler extends DefaultHandler implements DTDHandler, DeclHandler {

	protected static Logger logger = Logger.getLogger(Handler.class);	

	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	private DocumentBuilder builder = null;
	
	private DOMImplementation domImplementation = null;
	
	private Document dom = null;
	
	private DocumentType docType = null;
	
	public Handler() {
		super();
		
		try {
			factory.setNamespaceAware(true);
			builder = factory.newDocumentBuilder();
			logger.debug(builder.getClass());
			domImplementation = builder.getDOMImplementation();
			// docType = domImplementation.createDocumentType("svg", "-//W3C//DTD SVG 1.0//EN","http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd");
			// dom = domImplementation.createDocument("http://www.w3.org/2000/svg", "svg", docType);
			// docType = dom.getDoctype();
			// DocumentType xmlDocType = xmlDomi.createDocumentType("xml", "", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
    
    public void startDocument() throws SAXException {
    	logger.debug("Starting the document");
    	dom = builder.newDocument();
    	docType = dom.getDoctype();
    }
    
    public void endDocument() throws SAXException {
    	logger.debug("Ending the document");
    	try {
    		serialize(dom);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void startElement(
    		String namespaceURI, 
    		String lName, 
    		String qName, 
    		Attributes attrs) throws SAXException {
    	if (attrs.getValue("anchor") != null) {
        	logger.debug("Starting element " + lName + ". ANCHOR = " + attrs.getValue("anchor"));
    	}
    }
    
    public void endElement(
    		String namespaceURI, 
			String sName, 
			String qName) throws SAXException {
    	//logger.debug("Ending element " + sName);
    }
    
    public void characters(char buf[], int offset, int len) throws SAXException {
	    //String s = new String(buf, offset, len);
    }

    public void ignorableWhitespace(char buf[], int offset, int len) throws SAXException {
	    //String s = new String(buf, offset, len).trim();
    }

    /**
     * Copy across processing instructions to the DTSImpl
     */
    public void processingInstruction(String target, String data) throws SAXException {
    }
    
    // TODO Tighten up the XML Schema validation error handling.
    public void error(SAXParseException exception) throws SAXException {
		logger.debug(exception.getMessage());
	}

	public void fatalError(SAXParseException exception) throws SAXException {
		logger.debug(exception.getMessage());
	}

	public void warning(SAXParseException exception) throws SAXException {
		logger.debug(exception.getMessage());
	}	

	public void notationDecl(
			String name, 
			String publicId, 
			String systemId
			) throws SAXException {
		;
	}

	public void unparsedEntityDecl(
			String name, 
			String publicId,
			String systemId, 
			String notationName) 
	throws SAXException {
		;
	}

	public void elementDecl(
			String name, 
			String model
			) throws SAXException {
		logger.debug("DTD Element declaration " + name + " " + model);
	}

	public void internalEntityDecl(
			String name, 
			String value
			) throws SAXException {
		logger.debug("Internal entity " + name);
	}

	public void externalEntityDecl(
			String name, 
			String publicId, 
			String systemId
			) throws SAXException {
		logger.debug("External entity " + name);
	}

	public void attributeDecl(
			String eName, 
			String aName, 
			String type,
			String valueDefault, 
			String value
			) throws SAXException {
		;
	}
    
    private void serialize(Node what) throws IOException, XBRLException {
    	OutputFormat format = null;
		if (what.getNodeType() == Node.DOCUMENT_NODE) {
	    	format = new OutputFormat((Document) what, "UTF-8", true);
		} else {
			format = new OutputFormat(what.getOwnerDocument(), "UTF-8", true);
		}
		XMLSerializer output = new XMLSerializer(System.out, format);
		output.setNamespaces(true);
		if (what.getNodeType() == Node.DOCUMENT_NODE) {
			output.serialize((Document) what);
		} else if (what.getNodeType() == Node.ELEMENT_NODE) {
			output.serialize((Element) what);
		} else {
			throw new XBRLException("The node is not a document or element node.");
		}
    }

}
