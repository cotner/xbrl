package org.xbrlapi.xmlbase;

import java.io.Serializable;

/**
 * This interface declares the functionality expected
 * of classes that provide the XML Base functionality 
 * required by XBRL API.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */


public interface BaseURIResolver extends Serializable {

	public static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";

}
