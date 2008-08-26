package org.xbrlapi.xdt;

import org.xbrlapi.Concept;
import org.xbrlapi.utilities.XBRLException;


/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface ExplicitDimension extends Dimension {

    /**
     * @return true if the explicit dimension has a default domain member
     * and false otherwise.
     * @throws XBRLException
     */
    public boolean hasDefaultDomainMember() throws XBRLException;

    /**
     * @return the default domain member for the explicit dimension.
     * @throws XBRLException if the explicit dimension does not have
     * a default domain member.
     */
    public Concept getDefaultDomainMember() throws XBRLException;
    
}
