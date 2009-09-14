package org.xbrlapi.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.Entity;
import org.xbrlapi.EntityResource;
import org.xbrlapi.LabelResource;
import org.xbrlapi.Segment;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class EntityImpl extends ContextComponentImpl implements Entity {

    /**
     * @see org.xbrlapi.Entity#getIdentifierScheme()
     */
    public String getIdentifierScheme() throws XBRLException {
    	NodeList identifiers = getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace.toString(),"identifier");
    	if (identifiers.getLength() == 0) throw new XBRLException("An entity identifier is missing from the entity.");
    	if (identifiers.getLength() > 1) throw new XBRLException("There are too many entity identifiers in the entity.");
    	Element identifier = (Element) identifiers.item(0);
    	if (! identifier.hasAttribute("scheme")) throw new XBRLException("The entity identifier scheme is not specified.");
    	return identifier.getAttribute("scheme");
    }

    /**
     * @see org.xbrlapi.Entity#getIdentifierValue()
     */
    public String getIdentifierValue() throws XBRLException {
    	NodeList identifiers = getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace.toString(),"identifier");
    	if (identifiers.getLength() == 0) throw new XBRLException("An entity identifier is missing from the entity.");
    	if (identifiers.getLength() > 1) throw new XBRLException("There are too many entity identifiers in the entity.");
    	Element identifier = (Element) identifiers.item(0);
    	if (! identifier.hasAttribute("scheme")) throw new XBRLException("The entity identifier scheme is not specified.");
    	return identifier.getTextContent().trim();
    }
    

    
    /**
     * @see org.xbrlapi.Entity#getSegment()
     */
    public Segment getSegment() throws XBRLException {
    	List<Segment> candidates = this.<Segment>getChildren("org.xbrlapi.impl.SegmentImpl");
        if (candidates.size()==1) return candidates.get(0);
        if (candidates.size()==0) return null;
        throw new XBRLException("Entity identifier " + this.getIndex() + " contains more than one segment.");
    }
    
    /**
     * @see org.xbrlapi.Entity#getEntityResources()
     */
    public List<EntityResource> getEntityResources() throws XBRLException {

        String value = this.getIdentifierValue().trim();
        try {
            Integer iValue = new Integer(value);
            value = iValue.toString();
        } catch (Exception e) {
            ; // The conversion failed so use the original value
        }
        
        String query = "#roots#[@type='org.xbrlapi.impl.EntityResourceImpl' and */*/@scheme='"+ this.getIdentifierScheme() +"' and */*/@value='" + value + "']";
        return getStore().<EntityResource>queryForXMLResources(query);
    }
    
    
    /**
     * @see org.xbrlapi.Entity#getEntityLabels()
     */
    public List<LabelResource> getEntityLabels() throws XBRLException {
        List<EntityResource> resources = this.getEntityResources();
        List<LabelResource> labels = new Vector<LabelResource>();
        for (EntityResource resource: resources) {
            labels.addAll(resource.getLabels());
        }
        return labels;
    }
    
    /**
     * @see org.xbrlapi.Entity#getAllEntityLabels()
     */
    public List<LabelResource> getAllEntityLabels() throws XBRLException {
        List<EntityResource> resources = this.getEntityResources();
        List<LabelResource> labels = new Vector<LabelResource>();
        for (EntityResource resource: resources) {
            Set<EntityResource> equivalents = resource.getEquivalents();
            for (EntityResource equivalent: equivalents) {
                labels.addAll(equivalent.getLabels());
            }
        }

        // Find unique labels
        HashMap<String,LabelResource> map = new HashMap<String,LabelResource>();
        for (LabelResource label: labels) {
            if (!map.containsKey(label.getIndex())) {
                map.put(label.getIndex(), label);
            }
        }
        
        // Convert map to a fragment list
        List<LabelResource> result = new Vector<LabelResource>();
        for (LabelResource label: map.values()) {
            result.add(label);
        }
        
        return result;
    }

    

}
