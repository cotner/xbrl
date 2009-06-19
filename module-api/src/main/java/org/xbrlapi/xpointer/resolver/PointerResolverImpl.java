package org.xbrlapi.xpointer.resolver;

import java.net.URI;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Fragment;
import java.util.List;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xpointer.ParseException;
import org.xbrlapi.xpointer.PointerGrammar;
import org.xbrlapi.xpointer.PointerPart;

/**
 * Implementation of the XPointer resolver.
 * 
 * TODO Eliminate PointerResolverImpl class.  
 * Only the pointer parser is actually used and that 
 * parser is only used to resolve xlink:href values.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class PointerResolverImpl implements PointerResolver {

	// Create the logger
	static Logger logger = Logger.getLogger(PointerResolver.class);
	
	private Store store;
	
	private URI uri;
	
	/**
	 * XPointer resolver constructor
	 */
	public PointerResolverImpl(Store store) {
		super();
		this.store = store;
	}
	
	/** 
	 * Set the URI of the document within which the XPointer is being resolved.
	 * @param uri The URI of the document.
	 */
	public void setDocumentURI(URI uri) {
		this.uri = uri;
	}
	
	/**
	 * Get the URI of the document within which the XPointer is being resolved.
	 * @return the URI of the document within which the XPointer is being resolved.
	 */
	public URI getDocumentURI() {
		return uri;
	}
	
	/**
	 * Set the data store to be used by the loader
	 * TODO Decide if the loader setStore method should be private or public
	 */
	private void setStore(Store store) {
		this.store = store;
	}

	/**
	 * Get the data store used by a loader
	 * TODO should the loader getStore method be public or private
	 */
	public Store getStore() {
		return store;
	}
	
	/**
	 * Resolver the XPointer expression to a fragment in the data store.
	 * @param pointer The XPointer expression
	 * @return The fragment identified by the XPointer expression or null if none
	 * is found in the data store.
	 * @throws XBRLException
	 */
	@SuppressWarnings("unchecked")
	public Fragment resolveXPointer(String pointer) throws XBRLException {

	  	java.io.StringReader stringReader = new java.io.StringReader(pointer);
		java.io.Reader reader = new java.io.BufferedReader(stringReader);
		PointerGrammar grammar = new PointerGrammar(reader);
		try {
			Vector pointerParts = grammar.Pointer();
			
			// TODO Iterate pointer parts until X pointer is resolved to a specific fragment.
			HashMap<String,String> namespaces = new HashMap<String,String>();
			for (int i=0; i<pointerParts.size(); i++) {
				PointerPart part = (PointerPart) pointerParts.get(i);
				String data = part.getUnescapedSchemeData();
				
				// Handle xmlns scheme pointer parts
				if (part.getSchemeNamespace().equals(PointerPart.DefaultPointerNamespace) && part.getSchemeLocalName().equals("xmlns")) {
					String[] declaration = data.split("\\s*=\\s*");
					if (declaration.length != 2) {
						throw new XBRLException("The xmlns scheme: " + data + " failed to parse correctly.");
					}
					String prefix = declaration[0];
					String namespace = declaration[1];
					namespaces.put(prefix,namespace);
					 
				// Handle element scheme pointer parts
				} else if (part.getSchemeNamespace().toString().equals(PointerPart.DefaultPointerNamespace) && part.getSchemeLocalName().equals("element")) {
					String xpath = "#roots#[@uri='" + getDocumentURI() + "' and " + Constants.XBRLAPIPrefix + ":" + "xptr/@value='" + data + "']";
					List<Fragment> fl = getStore().query(xpath);					
					if (fl.size() != 0) {
						if (fl.size() > 1) {
							throw new XBRLException("A locator locates more than one fragment in the DTS.");
						}
						return fl.get(0);
					}
				}
				
			}

		} catch (ParseException e) {
			throw new XBRLException("The xpointer parser failed.", e);
		}
		
		return null;
		
	}
	
}
