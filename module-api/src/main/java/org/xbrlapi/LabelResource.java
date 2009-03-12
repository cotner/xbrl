package org.xbrlapi;

import java.util.List;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */


public interface LabelResource extends MixedContentResource {

	/**
	 * Get the value of the XHTML resource as a text string,
	 * @return the value of the XHTML resource as a text string
	 * with the XHTML markup replaced by spaces and with leading, trailing
	 * and double spaces removed.
	 * @throws XBRLException
	 */
	public String getStringValue() throws XBRLException;
	
	/**
	 * @return the list of Concepts in the data store that have this label.
	 * @throws XBRLException
	 */
	public List<Concept> getConcepts() throws XBRLException;

}
