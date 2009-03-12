package org.xbrlapi.impl;

import java.net.URI;
import java.util.List;

import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.ExtendedLink;
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
    public List<Locator> getLocators() throws XBRLException {
    	List<Locator> locators = this.<Locator>getChildren("org.xbrlapi.impl.LocatorImpl");
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
    public <E extends ArcEnd> List<E> getArcEndsByLabel(String label) throws XBRLException {
    	String xpath = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + getIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@xlink:label='" + label + "']";
    	List<E> ends = getStore().<E>query(xpath);
    	logger.debug("Extended link " + getIndex() + " has " + ends.size() + " ends with label " + label);
    	return ends;
    }    
    
    /**
     * Get the list of locators with a specified label in the extended link.
     * @param label The string value of the xlink:label attribute on the locator.
     * @return the list of matching locator fragments or the empty list if none are found.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getLocatorsByLabel(String)
     */
    public List<Locator> getLocatorsByLabel(String label) throws XBRLException {
    	String xpath = "/*[@parentIndex='" + getIndex() + "' and @type='org.xbrlapi.impl.LocatorImpl' and " + Constants.XBRLAPIPrefix + ":" + "data/link:loc/@xlink:label='" + label + "']";
    	return getStore().<Locator>query(xpath);
    }

    /**
     * Get the list of locators in the extended link with a specified absolute HREF.
     * @param href The resolved value of the xlink:href attribute on the locator.
     * @return the list of matching locator fragments or the empty list if none are found.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getLocatorsByHref(String)
     */
    public List<Locator> getLocatorsByHref(String href) throws XBRLException {
    	String xpath = "/*[@parentIndex='" + getIndex() + "' and @type='org.xbrlapi.impl.LocatorImpl' and @absoluteHref='" + href + "']";
    	return getStore().<Locator>query(xpath);
    }
    




    /**
     * Get the list of arcs contained by the extended link.
     * @return the list of matching arc fragments or the empty list if none are found.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getArcs()
     */
    public List<Arc> getArcs() throws XBRLException {
    	String xpath = "/*[@parentIndex='" + getIndex() + "' and " + Constants.XBRLAPIPrefix + ":" + "data/*/@xlink:type='arc']";
    	return getStore().<Arc>query(xpath);
    }

    /**
	 * Get the list of arcs with a given xlink:from label in extended link.
	 * @param to The required value of the xlink:to attribute of the arcs.
	 * @return the list of matching arc fragments or the empty list if none are found.
	 * @throws XBRLException
	 * @see org.xbrlapi.ExtendedLink#getArcsByToLabel(String)
	 */
	public List<Arc> getArcsByToLabel(String to) throws XBRLException {
		String xpath = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + getIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@xlink:to='" + to + "']";
		return getStore().<Arc>query(xpath);
	}

	/**
     * @see org.xbrlapi.ExtendedLink#getArcsByFromLabel(String)
     */
    public List<Arc> getArcsByFromLabel(String from) throws XBRLException {
    	String xpath = "/"+ Constants.XBRLAPIPrefix+ ":" + "fragment[@parentIndex='" + getIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@xlink:from='" + from + "']";
    	List<Arc> arcs = getStore().<Arc>query(xpath);
    	return arcs;
    }
    
    /**
     * @see org.xbrlapi.ExtendedLink#getArcsByFromLabelAndArcrole(String,URI)
     */
    public List<Arc> getArcsByFromLabelAndArcrole(String from, URI arcrole) throws XBRLException {
        String xpath = "/*[@parentIndex='" + getIndex() + "' and */*[@xlink:from='" + from + "' and @xlink:arcrole='" + arcrole + "']]";
        List<Arc> arcs = getStore().<Arc>query(xpath);
        return arcs;
    }
    
    /**
     * @see org.xbrlapi.ExtendedLink#getArcsByArcrole(URI)
     */
    public List<Arc> getArcsByArcrole(URI arcrole) throws XBRLException {
        String xpath = "/*[@parentIndex='" + getIndex() + "' and */*/@xlink:arcrole='" + arcrole + "']";
        List<Arc> arcs = getStore().<Arc>query(xpath);
        return arcs;
    }    
    
    /**
     * @see org.xbrlapi.ExtendedLink#getArcsByToLabelAndArcrole(String,URI)
     */
    public List<Arc> getArcsByToLabelAndArcrole(String to, URI arcrole) throws XBRLException {
        String xpath = "/*[@parentIndex='" + getIndex() + "' and */*[@xlink:to='" + to + "' and @xlink:arcrole='" + arcrole + "']]";
        List<Arc> arcs = getStore().<Arc>query(xpath);
        return arcs;
    }
    





    /**
     * Get the list of resources contained by the extended link.
     * @return the list of resource fragments in the extended link.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getResources()
     * 
     */
    public List<Resource> getResources() throws XBRLException {
    	String xpath = "/*[@parentIndex='" + getIndex() + "' and " + Constants.XBRLAPIPrefix + ":" + "data/*/@xlink:type='resource']";
    	return getStore().<Resource>query(xpath);
    }
    
    /**
     * Get the list of resources with the specified label.
     * @param label The value of the label used to select resources in the extended link.
     * @return the list of resource fragments with the given xlink:label attribute value.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getResourcesByLabel(String)
     */
    public List<Resource> getResourcesByLabel(String label) throws XBRLException {
    	String xpath = "/*[@parentIndex='" + getIndex() + "' and " + Constants.XBRLAPIPrefix + ":" + "data/*[@xlink:type='resource']/@xlink:label='" + label + "']";
    	return getStore().<Resource>query(xpath);
    }
    




    /**
     * Get the list of documentation fragments contained by the extended link.
     * Returns the list of documentation fragments in the extended link.
     * @throws XBRLException
     * @see org.xbrlapi.ExtendedLink#getDocumentations()
     */
    public List<XlinkDocumentation> getDocumentations() throws XBRLException {
    	return this.<XlinkDocumentation>getChildren("org.xbrlapi.impl.XlinkDocumentationImpl");
    }
    




}
