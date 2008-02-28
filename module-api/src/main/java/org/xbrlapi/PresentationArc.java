package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface PresentationArc extends Arc {

    /**
     * Get the preferredLabel attribute value.
     * Throws an exception if the preferred label attribute
     * value is not a standard label role URI or a custom label
     * URI defined in the DTS that includes this presentation arc.
     *
     * @throws XBRLException
     */
    public String getPreferredLabel() throws XBRLException;
    
	/**
     * Get the preferredLabel attribute roleType fragment.
     * @return the preferredLabel attribute roleType fragment or null if there is
     * no preferredLabel attribute.
     * @throws XBRLException
     */
    public RoleType getPreferredLabelRoleType() throws XBRLException;

	/**
     * Get the preferredLabel attribute role Definition.
     * @return the textual definition of the preferredLabel role.
     * TODO !!! Implement standard label role definition retrieval for preferredLabels.
     * @throws XBRLException
     */
    public String getPreferredLabelRoleDefinition() throws XBRLException;    
    
    /**
     * Set the weight attribute value on a calculation arc.
     * Throws an exception if the preferred label attribute
     * value is not a standard label role URI or a custom label
     * URI defined in the DTS that includes this presentation arc.
     *
     * @param preferredLabel The value of the preferredLabel attribute
     * @throws XBRLException
     */
    public void setpreferredLabel(String preferredLabel) throws XBRLException;

}
