package org.xbrlapi.impl;

import org.xbrlapi.Context;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fact;
import java.util.List;
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
    public List<SimpleLink> getSchemaRefs() throws XBRLException {
    	List<SimpleLink> candidates = this.<SimpleLink>getChildren("org.xbrlapi.impl.SimpleLinkImpl");
    	int i = 0;
    	while (i<candidates.size()) {
    		SimpleLink c = candidates.get(i);
    		if (! c.getLocalname().equals("schemaRef")) candidates.remove(c); else  i++;
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
    public List<SimpleLink> getLinkbaseRefs() throws XBRLException {
    	List<SimpleLink> candidates = this.<SimpleLink>getChildren("org.xbrlapi.impl.SimpleLinkImpl");
    	int i = 0;
    	while (i<candidates.size()) {
    		SimpleLink c = candidates.get(i);
    		if (! c.getLocalname().equals("linkbaseRef")) candidates.remove(c); else  i++;
    	}
    	return candidates;
    }

    


    /**
     * Get the list of contexts contained in the instance.
     * Returns null if none are contained by the XBRL instance.
     * @throws XBRLException
     * @see org.xbrlapi.Instance#getContexts()
     */
    public List<Context> getContexts() throws XBRLException {
    	return this.<Context>getChildren("org.xbrlapi.impl.ContextImpl");
    }

    /**
     * @see org.xbrlapi.Instance#getContext(String)
     */
    public Context getContext(String id) throws XBRLException {
    	String xpath = "#roots#[@type='org.xbrlapi.impl.ContextImpl' and @parentIndex='" + getIndex() + "' and */*/@id='" + id + "']";
    	List<Context> list = getStore().<Context>queryForFragments(xpath);
    	if (list.size() == 0) throw new XBRLException("The instance does not contain a context with id: " + id);
    	if (list.size() > 1) throw new XBRLException("The instance contains more than one context with id: " + id);
    	return (list.get(0));
    }
    

    


    /**
     * Get the list of units contained in the instance.
     * Returns null if none are contained by the XBRL instance.
     * @throws XBRLException
     * @see org.xbrlapi.Instance#getUnits()
     */
    public List<Unit> getUnits() throws XBRLException {
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
    	List<Unit> list = getStore().queryForFragments("#roots#[@type='org.xbrlapi.impl.UnitImpl' and @parentIndex='" + this.getIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@id='" + id + "']");
    	if (list.size() == 0) throw new XBRLException("The instance does not contain a unit with id: " + id);
    	if (list.size() > 1) throw new XBRLException("The instance contains more than one unit with id: " + id);
    	return list.get(0);
    }
    

    

    
    /**
     * Get a list of footnote link fragments.
     * Returns null if none are contained by the XBRL instance.
     * @throws XBRLException
     * @see org.xbrlapi.Instance#getFootnoteLinks()
     */
    public List<ExtendedLink> getFootnoteLinks() throws XBRLException {
    	return this.<ExtendedLink>getChildren("org.xbrlapi.impl.ExtendedLinkImpl");
    }

    

    
    /**
     * @see org.xbrlapi.Instance#getFacts()
     */
    public List<Fact> getFacts() throws XBRLException {
    	return getStore().<Fact>queryForFragments("#roots#[@parentIndex='" + this.getIndex() + "' and (@type='org.xbrlapi.impl.SimpleNumericItemImpl' or @type='org.xbrlapi.impl.FractionItemImpl' or @type='org.xbrlapi.impl.NonNumericItemImpl'  or @type='org.xbrlapi.impl.TupleImpl')]");
    }
    
    /**
     * @see org.xbrlapi.Instance#getItems()
     */
    public List<Item> getItems() throws XBRLException {
        return getStore().<Item>queryForFragments("#roots#[@parentIndex='" + this.getIndex() + "' and (@type='org.xbrlapi.impl.SimpleNumericItemImpl' or @type='org.xbrlapi.impl.FractionItemImpl' or @type='org.xbrlapi.impl.NonNumericItemImpl')]");
    }
    
    


    
 

}
