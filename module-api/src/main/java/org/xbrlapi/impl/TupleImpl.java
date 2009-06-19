package org.xbrlapi.impl;

import java.net.URI;

import org.xbrlapi.Fact;
import java.util.List;
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
	public List<Fact> getChildFacts() throws XBRLException {
    	return getStore().query("#roots#[@parentIndex='" + this.getIndex() + "' and (@type='org.xbrlapi.impl.SimpleNumericItemImpl' or @type='org.xbrlapi.impl.FractionItemImpl' or @type='org.xbrlapi.impl.NonNumericItemImpl'  or @type='org.xbrlapi.impl.TupleImpl')]");
	}

	/**
	 * Get a list of facts matching the specified fact name.
	 * @param namespace The namespace of the desired facts
	 * @param name The local name of the desired facts
	 * @return the specified facts.
	 * @throws XBRLException
	 * @see org.xbrlapi.Tuple#getChildFacts(URI, String)
	 */
	public List<Fact> getChildFacts(URI namespace, String name) throws XBRLException {
	    // TODO Improve query efficiency.
		String query = "#roots#[@parentIndex='" + this.getIndex() + "' and namespace-uri("+ Constants.XBRLAPIPrefix+ ":" + "data/*)='"+namespace+"' and local-name("+ Constants.XBRLAPIPrefix+ ":" + "data/*)='"+name+"']";
		return getStore().query(query);
	}
	
	/**
	 * Get a list of facts matching the specified fact name and context id.
	 * @param namespace The namespace of the desired facts
	 * @param name The local name of the desired facts
	 * @param contextRef The value of the context reference for the required facts.
	 * @return the specified facts.
	 * @throws XBRLException
	 * @see org.xbrlapi.Tuple#getChildFacts(URI, String, String)
	 */
	public List<Fact> getChildFacts(URI namespace, String name, String contextRef) throws XBRLException {
	    getStore().setNamespaceBinding(namespace,"xbrlapi_tupleNamespacePrefix");
		String query = "#roots#[@parentIndex='" + this.getIndex() + "' and " + Constants.XBRLAPIPrefix + ":" +  "data/xbrlapi_tupleNamespacePrefix:" +  name + "[@contextRef='" + contextRef + "']]";
		return getStore().query(query);
	}
	

	
	
}
