package org.xbrlapi.impl;

import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Locator;
import org.xbrlapi.Resource;
import org.xbrlapi.XlinkDocumentation;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ExtendedLinkImpl extends LinkImpl implements ExtendedLink {

    /**
     * Get the list of locators contained by the extended link.
     * @return the list of locator fragments or the empty list if none 
     * are found.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getLocators()
     */
    public FragmentList<Locator> getLocators() throws XBRLException {
    	FragmentList<Locator> locators = this.<Locator>getChildren("org.xbrlapi.impl.LocatorImpl");
    	return locators;
    }

    /**
     * Get the list of Arc Ends (locators or resources) with a specified label in 
     * the extended link.
     * @param label The string value of the xlink:label attribute on the arc ends.
     * @return the list of matching fragments or the empty list if none are found.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getArcEndsByLabel(String)
     */
    public <E extends ArcEnd> FragmentList<E> getArcEndsByLabel(String label) throws XBRLException {
    	String xpath = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + getFragmentIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@xlink:label='" + label + "']";
    	FragmentList<E> ends = getStore().<E>query(xpath);
    	logger.debug("Extended link " + getFragmentIndex() + " has " + ends.getLength() + " ends with label " + label);
    	return ends;
    }    
    
    /**
     * Get the list of locators with a specified label in the extended link.
     * @param label The string value of the xlink:label attribute on the locator.
     * @return the list of matching locator fragments or the empty list if none are found.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getLocatorsByLabel(String)
     */
    public FragmentList<Locator> getLocatorsByLabel(String label) throws XBRLException {
    	String xpath = "/*[@parentIndex='" + getFragmentIndex() + "' and @type='org.xbrlapi.impl.LocatorImpl' and " + Constants.XBRLAPIPrefix + ":" + "data/link:loc/@xlink:label='" + label + "']";
    	return getStore().<Locator>query(xpath);
    }

    /**
     * Get the list of locators in the extended link with a specified absolute HREF.
     * @param href The resolved value of the xlink:href attribute on the locator.
     * @return the list of matching locator fragments or the empty list if none are found.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getLocatorsByHref(String)
     */
    public FragmentList<Locator> getLocatorsByHref(String href) throws XBRLException {
    	String xpath = "/*[@parentIndex='" + getFragmentIndex() + "' and @type='org.xbrlapi.impl.LocatorImpl' and @absoluteHref='" + href + "']";
    	return getStore().<Locator>query(xpath);
    }
    




    /**
     * Get the list of arcs contained by the extended link.
     * @return the list of matching arc fragments or the empty list if none are found.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getArcs()
     */
    public FragmentList<Arc> getArcs() throws XBRLException {
    	String xpath = "/*[@parentIndex='" + getFragmentIndex() + "' and " + Constants.XBRLAPIPrefix + ":" + "data/*/@xlink:type='arc']";
    	return getStore().<Arc>query(xpath);
    }

    /**
	 * Get the list of arcs with a given xlink:from label in extended link.
	 * @param to The required value of the xlink:to attribute of the arcs.
	 * @return the list of matching arc fragments or the empty list if none are found.
	 * @throws XBRLException
	 * @see org.xbrlapi.ExtendedLink#getArcsByToLabel(String)
	 */
	public FragmentList<Arc> getArcsByToLabel(String to) throws XBRLException {
		String xpath = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + getFragmentIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@xlink:to='" + to + "']";
		return getStore().<Arc>query(xpath);
	}

	/**
     * Get the list of arcs with a given xlink:from label in extended link.
     * @param from The required value of the xlink:from attribute of the arcs.
     * @return the list of matching arc fragments or the empty list if none are found.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getArcsByFromLabel(String)
     */
    public FragmentList<Arc> getArcsByFromLabel(String from) throws XBRLException {
    	String xpath = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + getFragmentIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@xlink:from='" + from + "']";
    	FragmentList<Arc> arcs = getStore().<Arc>query(xpath);
    	return arcs;
    }





    /**
     * Get the list of resources contained by the extended link.
     * @return the list of resource fragments in the extended link.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getResources()
     * 
     */
    public FragmentList<Resource> getResources() throws XBRLException {
    	String xpath = "/*[@parentIndex='" + getFragmentIndex() + "' and " + Constants.XBRLAPIPrefix + ":" + "data/*/@xlink:type='resource']";
    	return getStore().<Resource>query(xpath);
    }
    
    /**
     * Get the list of resources with the specified label.
     * @param label The value of the label used to select resources in the extended link.
     * @return the list of resource fragments with the given xlink:label attribute value.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getResourcesByLabel(String)
     */
    public FragmentList<Resource> getResourcesByLabel(String label) throws XBRLException {
    	String xpath = "/*[@parentIndex='" + getFragmentIndex() + "' and " + Constants.XBRLAPIPrefix + ":" + "data/*[@xlink:type='resource']/@xlink:label='" + label + "']";
    	return getStore().<Resource>query(xpath);
    }
    




    /**
     * Get the list of documentation fragments contained by the extended link.
     * Returns the list of documentation fragments in the extended link.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getDocumentations()
     */
    public FragmentList<XlinkDocumentation> getDocumentations() throws XBRLException {
    	return this.<XlinkDocumentation>getChildren("org.xbrlapi.impl.XlinkDocumentationImpl");
    }
    




}
