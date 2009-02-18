package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Tuple extends Fact {

	/**
	 * Get the child facts
	 * @throws XBRLException
	 */
	public FragmentList<Fact> getChildFacts() throws XBRLException;

	/**
	 * Get a list of facts matching the specified fact name.
	 * @param namespace The namespace of the desired facts
	 * @param localname The local name of the desired facts
	 * @return the specified facts.
	 * @throws XBRLException
	 */
	public FragmentList<Fact> getChildFacts(URI namespace, String localname) throws XBRLException;
	
	/**
	 * Get a list of facts matching the specified fact name and context id.
	 * @param namespace The namespace of the desired facts
	 * @param name The local name of the desired facts
	 * @param contextRef The value of the context reference for the required facts.
	 * @return the specified facts.
	 * @throws XBRLException
	 */
	public FragmentList<Fact> getChildFacts(URI namespace, String name, String contextRef) throws XBRLException;
	



	
}
