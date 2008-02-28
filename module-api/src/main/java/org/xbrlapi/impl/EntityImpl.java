package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.Entity;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Segment;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class EntityImpl extends ContextComponentImpl implements Entity {

    /**
     * Get the entity identifier scheme.
     * @return the entity identifier scheme URI 
     * @throws XBRLException if any information is missing or provided too often.
     * @see org.xbrlapi.Entity#getIdentifierScheme()
     */
    public String getIdentifierScheme() throws XBRLException {
    	NodeList identifiers = getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"identifier");
    	if (identifiers.getLength() == 0) throw new XBRLException("An entity identifier is missing from the entity.");
    	if (identifiers.getLength() > 1) throw new XBRLException("There are too many entity identifiers in the entity.");
    	Element identifier = (Element) identifiers.item(0);
    	if (! identifier.hasAttribute("scheme")) throw new XBRLException("The entity identifier scheme is not specified.");
    	return identifier.getAttribute("scheme");
    }
    
    /**
     * Set the scheme for the entity identifier.
     * @param scheme the scheme for the entity identifier
     * @throws XBRLException if the scheme is not
     * a valid URI.
     * @see org.xbrlapi.Entity#setIdentifierScheme(String)
     */
    public void setIdentifierScheme(String scheme) throws XBRLException {
		throw new XBRLException("The data update methods are not yet implemented.");
    }

    /**
     * Get the entity identifier value
     * @return the string corresponding to the entity from
     * among the full set of valid entity identifiers in the
     * nominated scheme.
     * @throws XBRLException
     * @see org.xbrlapi.Entity#getIdentifierValue()
     */
    public String getIdentifierValue() throws XBRLException {
    	NodeList identifiers = getDataRootElement().getElementsByTagNameNS(Constants.XBRL21Namespace,"identifier");
    	if (identifiers.getLength() == 0) throw new XBRLException("An entity identifier is missing from the entity.");
    	if (identifiers.getLength() > 1) throw new XBRLException("There are too many entity identifiers in the entity.");
    	Element identifier = (Element) identifiers.item(0);
    	if (! identifier.hasAttribute("scheme")) throw new XBRLException("The entity identifier scheme is not specified.");
    	return identifier.getTextContent();
    }
    
    /**
     * Set the entity identifier value
     * @param identifierValue the local value identifying the entity 
     * within the specified scheme
     * @throws XBRLException 
     * @see org.xbrlapi.Entity#setIdentifierValue(String)
     */
    public void setIdentifierValue(String identifierValue) throws XBRLException {
		throw new XBRLException("The data update methods are not yet implemented.");
	}
    
    /**
     * Get the segment of the entity
     * @return the segment information for the entity
     * or null if the entity does not include segment information.
     * @throws XBRLException
     * @see org.xbrlapi.Entity#getSegment()
     */
    public Segment getSegment() throws XBRLException {
    	FragmentList<Segment> candidates = this.<Segment>getChildren("org.xbrlapi.impl.SegmentImpl");
    	if (candidates.getLength()==0) return null;
    	return candidates.getFragment(0);
    }
    
    /**
     * Set the segment in the entity
     * @param segment the segment to be added
     * @throws XBRLException
     * @see org.xbrlapi.Entity#setSegment(Segment)
     */
    public void setSegment(Segment segment) throws XBRLException {
		throw new XBRLException("The data update methods are not yet implemented.");
    }

    /**
     * Remove the segment from the entity.
     * @throws XBRLException 
     * @see org.xbrlapi.Entity#removeSegment()
     */
    public void removeSegment() throws XBRLException {
		throw new XBRLException("The data update methods are not yet implemented.");
    }
}
