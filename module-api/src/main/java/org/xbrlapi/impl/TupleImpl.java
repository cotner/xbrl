package org.xbrlapi.impl;

import org.xbrlapi.Fact;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Tuple;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class TupleImpl extends FactImpl implements Tuple {

	/**
	 * Get the list of child facts
	 * @return list of child fact fragments for the tuple
	 * @throws XBRLException
	 * @see org.xbrlapi.Tuple#getChildFacts()
	 */
	public FragmentList<Fact> getChildFacts() throws XBRLException {
    	return getStore().query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + this.getFragmentIndex() + "' and (@type='org.xbrlapi.impl.SimpleNumericItemImpl' or @type='org.xbrlapi.impl.FractionItemImpl' or @type='org.xbrlapi.impl.NonNumericItemImpl'  or @type='org.xbrlapi.impl.TupleImpl')]");
	}

	/**
	 * Get a list of facts matching the specified fact name.
	 * @param namespace The namespace of the desired facts
	 * @param name The local name of the desired facts
	 * @return the specified facts.
	 * @throws XBRLException
	 * @see org.xbrlapi.Tuple#getChildFacts(String, String)
	 */
	public FragmentList<Fact> getChildFacts(String namespace, String name) throws XBRLException {
	    // TODO Improve query efficiency.
		String query = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + this.getFragmentIndex() + "' and namespace-uri("+ Constants.XBRLAPIPrefix+ ":" + "data/*)='"+namespace+"' and local-name("+ Constants.XBRLAPIPrefix+ ":" + "data/*)='"+name+"']";
		return getStore().query(query);
	}
	
	/**
	 * Get a list of facts matching the specified fact name and context id.
	 * @param namespace The namespace of the desired facts
	 * @param name The local name of the desired facts
	 * @param contextRef The value of the context reference for the required facts.
	 * @return the specified facts.
	 * @throws XBRLException
	 * @see org.xbrlapi.Tuple#getChildFacts(String, String, String)
	 */
	public FragmentList<Fact> getChildFacts(String namespace, String name, String contextRef) throws XBRLException {
		String query = "/"+ Constants.XBRLAPIPrefix + ":" + "fragment[@parentIndex='" + this.getFragmentIndex() + "' and " + Constants.XBRLAPIPrefix + ":" +  "data/*[namespace-uri()='" + namespace + "' and local-name()='" + name + "'  and @contextRef='" + contextRef + "']]";
		return getStore().query(query);
	}
	
	/**
	 * Add a child fact, conforming to the sequencing requirements 
	 * of the tuple syntax definition, if any is specified.
	 * TODO resolve the compliance with XML Schema ordering rules.
	 * @throws XBRLException
	 * @see org.xbrlapi.Tuple#addChild(Fact)
	 */
	public void addChild(Fact child) throws XBRLException {
		throw new XBRLException("Data update methods are not yet implemented.");
	}
	
	/**
	 * Remove a child fact
	 * TODO Implement validation against the XML Schema syntax rules.
	 * @throws XBRLException if the removal invalidates the syntax rules for the tuple.
	 * @see org.xbrlapi.Tuple#removeChild(Fact)
	 */
	public void removeChild(Fact child) throws XBRLException {
		throw new XBRLException("Data update methods are not yet implemented.");
	}	
}
