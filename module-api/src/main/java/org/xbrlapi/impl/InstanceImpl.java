package org.xbrlapi.impl;

import org.xbrlapi.Context;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fact;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Instance;
import org.xbrlapi.Item;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.Unit;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class InstanceImpl extends FragmentImpl implements Instance {

    /**
     * Get the list of schemaRef fragments in the instance
     * Returns null if none are contained by the XBRL instance.
     * @throws XBRLException
     * @see org.xbrlapi.Instance#getSchemaRefs()
     */
    public FragmentList<SimpleLink> getSchemaRefs() throws XBRLException {
    	FragmentList<SimpleLink> candidates = this.<SimpleLink>getChildren("org.xbrlapi.impl.SimpleLinkImpl");
    	int i = 0;
    	while (i<candidates.getLength()) {
    		SimpleLink c = candidates.getFragment(i);
    		if (! c.getLocalname().equals("schemaRef")) candidates.removeFragment(c); else  i++;
    	}
    	return candidates;
    }
    

    


    /**
     * Get the list of linkbaseRef fragments in the instance
     * Returns null if none are contained by the XBRL instance.
     * TODO Eliminate any simple links that are not linkbaseRefs.
     * @throws XBRLException
     * @see org.xbrlapi.Instance#getLinkbaseRefs()
     */
    public FragmentList<SimpleLink> getLinkbaseRefs() throws XBRLException {
    	FragmentList<SimpleLink> candidates = this.<SimpleLink>getChildren("org.xbrlapi.impl.SimpleLinkImpl");
    	int i = 0;
    	while (i<candidates.getLength()) {
    		SimpleLink c = candidates.getFragment(i);
    		if (! c.getLocalname().equals("linkbaseRef")) candidates.removeFragment(c); else  i++;
    	}
    	return candidates;
    }

    


    /**
     * Get the list of contexts contained in the instance.
     * Returns null if none are contained by the XBRL instance.
     * @throws XBRLException
     * @see org.xbrlapi.Instance#getContexts()
     */
    public FragmentList<Context> getContexts() throws XBRLException {
    	return this.<Context>getChildren("org.xbrlapi.impl.ContextImpl");
    }

    /**
     * @see org.xbrlapi.Instance#getContext(String)
     */
    public Context getContext(String id) throws XBRLException {
    	String xpath = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@type='org.xbrlapi.impl.ContextImpl' and @parentIndex='" + getFragmentIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@id='" + id + "']";
    	FragmentList<Context> list = getStore().<Context>query(xpath);
    	if (list.getLength() == 0) throw new XBRLException("The instance does not contain a context with id: " + id);
    	if (list.getLength() > 1) throw new XBRLException("The instance contains more than one context with id: " + id);
    	return (list.getFragment(0));
    }
    

    


    /**
     * Get the list of units contained in the instance.
     * Returns null if none are contained by the XBRL instance.
     * @throws XBRLException
     * @see org.xbrlapi.Instance#getUnits()
     */
    public FragmentList<Unit> getUnits() throws XBRLException {
    	return this.<Unit>getChildren("org.xbrlapi.impl.UnitImpl");
    }    

    /**
     * Get a specified unit from the instance based on its id.
     * @param id The id of the unit fragment
     * @return the unit fragment
     * @throws XBRLException if the unit does not exist or is not unique.
     * @see org.xbrlapi.Instance#getUnit(String)
     */
    public Unit getUnit(String id) throws XBRLException {
    	FragmentList<Unit> list = getStore().query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@type='org.xbrlapi.impl.UnitImpl' and @parentIndex='" + this.getFragmentIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@id='" + id + "']");
    	if (list.getLength() == 0) throw new XBRLException("The instance does not contain a unit with id: " + id);
    	if (list.getLength() > 1) throw new XBRLException("The instance contains more than one unit with id: " + id);
    	return list.getFragment(0);
    }
    

    

    
    /**
     * Get a list of footnote link fragments.
     * Returns null if none are contained by the XBRL instance.
     * @throws XBRLException
     * @see org.xbrlapi.Instance#getFootnoteLinks()
     */
    public FragmentList<ExtendedLink> getFootnoteLinks() throws XBRLException {
    	return this.<ExtendedLink>getChildren("org.xbrlapi.impl.ExtendedLinkImpl");
    }

    

    
    /**
     * @see org.xbrlapi.Instance#getFacts()
     */
    public FragmentList<Fact> getFacts() throws XBRLException {
    	return getStore().<Fact>query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + this.getFragmentIndex() + "' and (@type='org.xbrlapi.impl.SimpleNumericItemImpl' or @type='org.xbrlapi.impl.FractionItemImpl' or @type='org.xbrlapi.impl.NonNumericItemImpl'  or @type='org.xbrlapi.impl.TupleImpl')]");
    }
    
    /**
     * @see org.xbrlapi.Instance#getItems()
     */
    public FragmentList<Item> getItems() throws XBRLException {
        return getStore().<Item>query("/*[@parentIndex='" + this.getFragmentIndex() + "' and (@type='org.xbrlapi.impl.SimpleNumericItemImpl' or @type='org.xbrlapi.impl.FractionItemImpl' or @type='org.xbrlapi.impl.NonNumericItemImpl')]");
    }    


    
 

}
