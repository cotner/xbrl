package org.xbrlapi.xdt;

import org.xbrlapi.Concept;
import org.xbrlapi.utilities.XBRLException;


/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface ExplicitDimension extends Dimension {

    /**
     * @return true if the explicit dimension has a default domain member
     * and false otherwise (including if it has more than one default domain member,
     * thus undermining the default property of the indicated defaults.
     * @throws XBRLException
     */
    public boolean hasDefaultDomainMember() throws XBRLException;

    /**
     * @return the default domain member for the explicit dimension.
     * @throws XBRLException if the explicit dimension does not have
     * a default domain member or if it has more than one default domain
     * members.
     */
    public Concept getDefaultDomainMember() throws XBRLException;
    
}
