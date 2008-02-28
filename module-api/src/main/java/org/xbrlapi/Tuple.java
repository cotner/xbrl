package org.xbrlapi;

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
	 * @param name The local name of the desired facts
	 * @return the specified facts.
	 * @throws XBRLException
	 */
	public FragmentList<Fact> getChildFacts(String namespace, String name) throws XBRLException;
	
	/**
	 * Get a list of facts matching the specified fact name and context id.
	 * @param namespace The namespace of the desired facts
	 * @param name The local name of the desired facts
	 * @param contextRef The value of the context reference for the required facts.
	 * @return the specified facts.
	 * @throws XBRLException
	 */
	public FragmentList<Fact> getChildFacts(String namespace, String name, String contextRef) throws XBRLException;
	
	/**
	 * Add a child fact, conforming to the sequencing requirements 
	 * of the tuple syntax definition, if any is specified.
	 * @throws XBRLException
	 */
	public void addChild(Fact child) throws XBRLException;

	/**
	 * Remove a child fact
	 * TODO Ensure that operation results in valid XML Schema constructs.
	 * @throws XBRLException if the removal invalidates the
	 * syntax rules for the tuple.
	 */
	public void removeChild(Fact child) throws XBRLException;
	
}
