package org.xbrlapi;

import java.net.URI;
import java.util.Set;

import org.xbrlapi.utilities.XBRLException;

/**
 * Defines the functionality for the custom unit resource:
 * a extended link resource that has a 1:1 association with 
 * a given Unit measure.  Each unit resource defines a pairing of
 * a namespace and a localname.  Together these define a measure that
 * can be used in XBRL units of measurement.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface MeasureResource extends MixedContentResource {

    /**
     * @return the measure namespace
     * @throws XBRLException
     */
    public URI getMeasureNamespace() throws XBRLException;

    /**
     * @return the measure local name
     * @throws XBRLException
     */
    public String getMeasureLocalname() throws XBRLException;
    
    /**
     * @return a list of all equivalent unit resources, including
     * this unit resource that is naturally equivalent to itself.
     * @throws XBRLException
     */
    public Set<MeasureResource> getEquivalents() throws XBRLException;
    
}
