package org.xbrlapi.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

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
     * @see org.xbrlapi.ExtendedLink#getLocators()
     */
    public List<Locator> getLocators() throws XBRLException {
    	List<Locator> locators = this.<Locator>getChildren("org.xbrlapi.impl.LocatorImpl");
    	return locators;
    }

    /**
     * @see org.xbrlapi.ExtendedLink#getArcEndsWithLabel(String)
     */
    public <E extends ArcEnd> List<E> getArcEndsWithLabel(String label) throws XBRLException {
    	String xpath = "#roots#[@parentIndex='" + getIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@xlink:label='" + label + "']";
    	List<E> ends = getStore().<E>queryForFragments(xpath);
    	logger.debug("Extended link " + getIndex() + " has " + ends.size() + " ends with label " + label);
    	return ends;
    }    
    
    /**
     * @see org.xbrlapi.ExtendedLink#getLocatorsWithLabel(String)
     */
    public List<Locator> getLocatorsWithLabel(String label) throws XBRLException {
    	String xpath = "#roots#[@parentIndex='" + getIndex() + "' and @type='org.xbrlapi.impl.LocatorImpl' and " + Constants.XBRLAPIPrefix + ":" + "data/link:loc/@xlink:label='" + label + "']";
    	return getStore().<Locator>queryForFragments(xpath);
    }

    /**
     * @see org.xbrlapi.ExtendedLink#getLocatorsWithHref(String)
     */
    public List<Locator> getLocatorsWithHref(String href) throws XBRLException {
    	String xpath = "#roots#[@parentIndex='" + getIndex() + "' and @type='org.xbrlapi.impl.LocatorImpl' and @absoluteHref='" + href + "']";
    	return getStore().<Locator>queryForFragments(xpath);
    }
    
    /**
     * @see org.xbrlapi.ExtendedLink#getArcs()
     */
    public List<Arc> getArcs() throws XBRLException {
    	String xpath = "#roots#[@parentIndex='" + getIndex() + "' and " + Constants.XBRLAPIPrefix + ":" + "data/*/@xlink:type='arc']";
    	return getStore().<Arc>queryForFragments(xpath);
    }

    /**
	 * @see org.xbrlapi.ExtendedLink#getArcsWithToLabel(String)
	 */
	public List<Arc> getArcsWithToLabel(String to) throws XBRLException {
		String xpath = "#roots#[@parentIndex='" + getIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@xlink:to='" + to + "']";
		return getStore().<Arc>queryForFragments(xpath);
	}

	/**
     * @see org.xbrlapi.ExtendedLink#getArcsWithFromLabel(String)
     */
    public List<Arc> getArcsWithFromLabel(String from) throws XBRLException {
    	String xpath = "#roots#[@parentIndex='" + getIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@xlink:from='" + from + "']";
    	List<Arc> arcs = getStore().<Arc>queryForFragments(xpath);
    	return arcs;
    }
    
    /**
     * @see org.xbrlapi.ExtendedLink#getArcsWithFromLabelAndArcrole(String,URI)
     */
    public List<Arc> getArcsWithFromLabelAndArcrole(String from, URI arcrole) throws XBRLException {
        String xpath = "#roots#[@parentIndex='" + getIndex() + "' and */*[@xlink:from='" + from + "' and @xlink:arcrole='" + arcrole + "']]";
        List<Arc> arcs = getStore().<Arc>queryForFragments(xpath);
        return arcs;
    }
    
    /**
     * @see org.xbrlapi.ExtendedLink#getArcsWithArcrole(URI)
     */
    public List<Arc> getArcsWithArcrole(URI arcrole) throws XBRLException {
        String xpath = "#roots#[@parentIndex='" + getIndex() + "' and */*/@xlink:arcrole='" + arcrole + "']";
        List<Arc> arcs = getStore().<Arc>queryForFragments(xpath);
        return arcs;
    }    
    
    /**
     * @see org.xbrlapi.ExtendedLink#getArcsWithToLabelAndArcrole(String,URI)
     */
    public List<Arc> getArcsWithToLabelAndArcrole(String to, URI arcrole) throws XBRLException {
        String xpath = "#roots#[@parentIndex='" + getIndex() + "' and */*[@xlink:to='" + to + "' and @xlink:arcrole='" + arcrole + "']]";
        List<Arc> arcs = getStore().<Arc>queryForFragments(xpath);
        return arcs;
    }
    
    /**
     * @see org.xbrlapi.ExtendedLink#getResources()
     * 
     */
    public List<Resource> getResources() throws XBRLException {
    	String xpath = "#roots#[@parentIndex='" + getIndex() + "' and " + Constants.XBRLAPIPrefix + ":" + "data/*/@xlink:type='resource']";
    	return getStore().<Resource>queryForFragments(xpath);
    }
    
    /**
     * @see org.xbrlapi.ExtendedLink#getResourcesByLabel(String)
     */
    public List<Resource> getResourcesWithLabel(String label) throws XBRLException {
    	String xpath = "#roots#[@parentIndex='" + getIndex() + "' and " + Constants.XBRLAPIPrefix + ":" + "data/*[@xlink:type='resource']/@xlink:label='" + label + "']";
    	return getStore().<Resource>queryForFragments(xpath);
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
    
    /**
     * @return A map, indexed by XLink type (title,locator, resource, arc) of the XLink sources and targets
     * in an extended link.  The children are represented by a map from label to indexes of the children.
     * @throws XBRLException
     */
    public Map<String,List<String>> getArcEndIndicesByLabel() throws XBRLException {
        Map<String,List<String>> result = new HashMap<String,List<String>>();
        String query = "#roots#[@parentIndex='" + getIndex() + "' and */*[@xlink:type='resource' or @xlink:type='locator']]/@index";
        Set<String> indices = getStore().queryForIndices(query);
        for (String index: indices) {
            ArcEnd arcEnd = (ArcEnd) getStore().getFragment(index);
            String label = arcEnd.getLabel();
            if (result.containsKey(label)) {
                result.get(label).add(index);
            } else {
                List<String> list = new Vector<String>();
                list.add(index);
                result.put(label,list);
            }
        }

        return result;
    }


}
