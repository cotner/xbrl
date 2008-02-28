package org.xbrlapi.xpointer;

/**
 * Implementation of the PointerPart interface.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */

import java.net.URI;
import java.net.URISyntaxException;


public class PointerPartImpl implements PointerPart {

	URI namespace;
	String localName;
	String prefix;
	String escapedData;
	String unescapedData;
	/**
	 * Create an uninitialised pointer part.
	 *
	 */
	public PointerPartImpl() {
		super();
		try {
			namespace = new URI(DefaultPointerNamespace);
		} catch (URISyntaxException e) {
			// This exception can never be thrown here
		}
		localName = null;
		prefix = null;
		escapedData = null;
		unescapedData = null;
		
	}

	public void setSchemeNamespace(URI namespace) {
		this.namespace = namespace;
	}

	public URI getSchemeNamespace() {
		return namespace;
	}

	public void setSchemeLocalName(String localName) {
		this.localName = localName;
	}

	public String getSchemeLocalName() {
		return localName;
	}

	public void setSchemePrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSchemePrefix() {
		return prefix;
	}

	public void setEscapedSchemeData(String escapedData) {
		this.escapedData = escapedData;
	}

	public void setUnescapedSchemeData(String unescapedData) {
		this.unescapedData = unescapedData;
	}
	/**
	 * @return The unescaped scheme data.
	 */
	public String getUnescapedSchemeData() {
		return unescapedData;
	}
	
	/**
	 * @return the scheme data escaped so that it is ready for insertion into a 
	 * pointer part.
	 */
	public String getEscapedSchemeData() {
		return escapedData;
	}
	
}
