package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.xbrlapi.Linkbase;
import org.xbrlapi.PresentationArc;
import org.xbrlapi.RoleType;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class PresentationArcImpl extends ArcImpl implements PresentationArc {

	/**
     * Get the preferredLabel attribute value.
     * @return the preferredLabel attribute value or null if none is provided.
     * @throws XBRLException if the preferred label attribute
     * value is not a standard label role URI or a custom label
     * URI defined in the DTS that includes this presentation arc.
     * @see org.xbrlapi.PresentationArc#getPreferredLabel()
     */
    public String getPreferredLabel() throws XBRLException {
    	Element root = getDataRootElement();
    	// TODO !!! implement checking of preferredLabel attribute values against standard label role values.
    	if (! root.hasAttribute("preferredLabel")) return null;
    	return getDataRootElement().getAttribute("preferredLabel");
    }
    
	/**
     * Get the preferredLabel attribute roleType fragment.
     * @return the preferredLabel attribute roleType fragment or null if there is
     * no preferredLabel attribute.
     * @throws XBRLException.
     * @see org.xbrlapi.PresentationArc#getPreferredLabelRoleType()
     */
    public RoleType getPreferredLabelRoleType() throws XBRLException {
    	String uri = getPreferredLabel();
    	if (uri == null) return null;
    	if (uri.equals("")) return null;
    	Linkbase linkbase = (Linkbase) this.getAncestorOrSelf("org.xbrlapi.impl.LinkbaseImpl");
    	SimpleLink roleRef = null;
    	try {
    		roleRef = linkbase.getRoleRef(uri);
    	} catch (XBRLException e) {
    		return null;
    	}
    	return (RoleType) roleRef.getTargetFragment();
    	
    }

	/**
     * Get the preferredLabel attribute role Definition.
     * @return the textual definition of the preferredLabel role.
     * TODO Implement standard label role definition retrieval for preferredLabels.
     * @throws XBRLException
     * @see org.xbrlapi.PresentationArc#getPreferredLabelRoleDefinition()
     */
    public String getPreferredLabelRoleDefinition() throws XBRLException {
    	return getPreferredLabelRoleType().getDefinition();
    }
    
    /**
     * Set the weight attribute value on a calculation arc.
     * Throws an exception if the preferred label attribute
     * value is not a standard label role URI or a custom label
     * URI defined in the DTS that includes this presentation arc.
     * @param preferredLabel The value of the preferredLabel attribute
     * @throws XBRLException
     * @see org.xbrlapi.PresentationArc#setpreferredLabel(String)
     */
    public void setpreferredLabel(String preferredLabel) throws XBRLException {
    	throw new XBRLException("Data update methods are yet to be implemented.");
    }

}
