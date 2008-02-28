package org.xbrlapi.xpointer;

import java.net.URI;

/**
 * Defines the functionality associated with a  single scheme component of an XPointer expression.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public interface PointerPart {
	
	final String DefaultPointerNamespace = "http://www.xbrlapi.org/XPointer";
	
	public void setSchemeNamespace(URI namespace);
	
	public URI getSchemeNamespace();
	
	public void setSchemeLocalName(String localName);
	
	public String getSchemeLocalName();

	public void setSchemePrefix(String prefix);
	
	public String getSchemePrefix();
	
	public void setUnescapedSchemeData(String data);

	public void setEscapedSchemeData(String data);
	
	/**
	 * @return The unescaped scheme data.
	 */
	public String getUnescapedSchemeData();
	
	/**
	 * @return the scheme data escaped so that it is ready for insertion into a 
	 * pointer part.
	 */
	public String getEscapedSchemeData();
	
}
