package org.xbrlapi.impl;

import org.xbrlapi.Context;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fact;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Instance;
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
     * Add a schemaRef to the XBRL instance
     * @param schemaRef the schemaRef to add to the XBRL instance
     * @throws XBRLException if the schemaRef is already part of the
     * XBRL instance.
     * TODO Make sure that the XBRLException for the getSchemaRefs method is kosher.
     * @see org.xbrlapi.Instance#addSchemaRef(SimpleLink)
     */
    public void addSchemaRef(SimpleLink schemaRef) throws XBRLException {
    	throw new XBRLException("Data update methods are not yet implemented.");
    }
    
    /**
     * Remove a schemaRef from the XBRL instance.
     * Throws an exception if the schema ref is not a part of the 
     * XBRL instance.
     * @param schemaRef the schemaRef to remove from the XBRL instance
     * @throws XBRLException
     * @see org.xbrlapi.Instance#removeSchemaRef(SimpleLink)
     */
    public void removeSchemaRef(SimpleLink schemaRef) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
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
     * Add a linkbaseRef to the XBRL instance
     * Throws an exception if the linkbaseRef is already part of the
     * XBRL instance. (I am not sure if this is really required)
     * @param linkbaseRef the linkbaseRef to add to the XBRL instance
     * @throws XBRLException
     * @see org.xbrlapi.Instance#addLinkbaseRef(SimpleLink)
     */
    public void addLinkbaseRef(SimpleLink linkbaseRef) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }
    
    /**
     * Remove a linkbaseRef from the XBRL instance.
     * Throws an exception if the linkbase ref is not a part of the 
     * XBRL instance.
     * @param linkbaseRef the linkbaseRef to remove from the XBRL instance
     * @throws XBRLException
     * @see org.xbrlapi.Instance#removeLinkbaseRef(SimpleLink)
     */
    public void removeLinkbaseRef(SimpleLink linkbaseRef) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
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
     * Get a specified context from the instance based on its id.
     * @param id The id of the context fragment
     * @return the context fragment
     * @throws XBRLException if the context does not exist or there is more than one.
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
     * Add a context to the XBRL instance.
     * TODO Decide if this exception is really required.
     * @param context the context to add to the XBRL instance
     * @throws XBRLException if the context is already part of the
     * XBRL instance.
     * @see org.xbrlapi.Instance#addContext(Context)
     */
    public void addContext(Context context) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }
    
    /**
     * Remove a context from the XBRL instance.
     * @param context the context to remove from the XBRL instance
     * @throws XBRLException if the context is not a part of the 
     * XBRL instance.
     * @see org.xbrlapi.Instance#removeContext(Context)
     */
    public void removeContext(Context context) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
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
     * Add a unit to the XBRL instance
     * @param unit the unit to add to the XBRL instance
     * @throws XBRLException if the unit is already part of the
     * XBRL instance.
     * TODO Determine if this exception is warranted.
     * @see org.xbrlapi.Instance#addUnit(Unit)
     */
    public void addUnit(Unit unit) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }
    
    /**
     * Remove a unit from the XBRL instance.
     * @param unit the unit to remove from the XBRL instance
     * @throws XBRLException if the unit is not a part of the 
     * XBRL instance.
     * @see org.xbrlapi.Instance#removeUnit(Unit)
     */
    public void removeUnit(Unit unit) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
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
     * Add a footnoteLink to the XBRL instance
     * @param footnoteLink the footnoteLink to add to the XBRL instance
     * @throws XBRLException if the footnoteLink is already part of the
     * XBRL instance.
     * TODO is this exception required?
     * @see org.xbrlapi.Instance#addFootnoteLink(ExtendedLink)
     */
    public void addFootnoteLink(ExtendedLink footnoteLink) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }
    
    /**
     * Remove a footnoteLink from the XBRL instance.
     * @param footnoteLink the footnoteLink to remove from the XBRL instance
     * @throws XBRLException if the footnoteLink is not a part of the 
     * XBRL instance.
     * @see org.xbrlapi.Instance#removeFootnoteLink(ExtendedLink)
     */
    public void removeFootnoteLink(ExtendedLink footnoteLink) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }
    
    /**
     * Get the list of facts that are children of the instance.
     * Facts that are within tuples are not included in this list.
     * @return the list of facts that are children of the instance.
     * @throws XBRLException
     * @see org.xbrlapi.Instance#getFacts()
     */
    public FragmentList<Fact> getFacts() throws XBRLException {
    	return getStore().<Fact>query("/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + this.getFragmentIndex() + "' and (@type='org.xbrlapi.impl.SimpleNumericItemImpl' or @type='org.xbrlapi.impl.FractionItemImpl' or @type='org.xbrlapi.impl.NonNumericItemImpl'  or @type='org.xbrlapi.impl.TupleImpl')]");
    }    

    /**
     * Add a fact to the XBRL instance
     * @param fact the fact to add to the XBRL instance
     * @throws XBRLException if the fact is already part of the
     * XBRL instance.
     * TODO Is this exception OK?
     * @see org.xbrlapi.Instance#addFact(Fact)
     */
    public void addFact(Fact fact) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    }
    
    /**
     * Remove a fact from the XBRL instance.
     * @param fact the fact to remove from the XBRL instance
     * @throws XBRLException if the fact is not a part of the 
     * XBRL instance.
     * @see org.xbrlapi.Instance#removeFact(Fact)
     */
    public void removeFact(Fact fact) throws XBRLException {
    	throw new XBRLException("Data update methods have not yet been implemented.");
    } 

}
