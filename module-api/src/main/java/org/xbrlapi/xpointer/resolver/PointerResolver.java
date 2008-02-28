package org.xbrlapi.xpointer.resolver;

import java.net.URL;

import org.xbrlapi.Fragment;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 * 
 * An XPointer resolver parses an XPointer, returning the set of resources in the data store
 * that are identified by the XPointer
 */
public interface PointerResolver {
	
	
	/** 
	 * Set the URL of the document within which the XPointer is being resolved.
	 * @param url The URL of the document.
	 */
	public void setDocumentURL(URL url);
	
	/**
	 * Get the URL of the document within which the XPointer is being resolved.
	 * @return the url of the document within which the XPointer is being resolved.
	 */
	public URL getDocumentURL();
	
	/**
	 * Get the data store used by a loader
	 */
	public Store getStore();
	
	/**
	 * Resolver the XPointer expression to a fragment in the data store.
	 * @param pointer The XPointer expression.
	 * @return The fragment identified by the XPointer expression.
	 * @throws XBRLException
	 */
	public Fragment resolveXPointer(String pointer) throws XBRLException;
	
}
