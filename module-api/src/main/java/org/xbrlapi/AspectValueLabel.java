package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * 
 * This interface is related to aspect value label XML resources
 * that have been placed into the data store to cache aspect value labels.
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */

public interface AspectValueLabel extends NonFragmentXML {

    /**
     * @return the label for the given aspect value
     * @throws XBRLException
     */
    public String getLabel() throws XBRLException;

    /**
     * @return the aspect ID
     * @throws XBRLException
     */
    public URI getAspectId() throws XBRLException;

    /**
     * @return the aspect value ID
     * @throws XBRLException
     */
    public URI getValueId() throws XBRLException;
    
    /**
     * @return the locale (language) code or null if none is defined.
     * @throws XBRLException
     */
    public String getLocale() throws XBRLException;
    
    /**
     * @return the extended link role or none if none is defined.
     * @throws XBRLException
     */
    public URI getLinkRole() throws XBRLException;

    /**
     * @return the resource role or null if none is defined.
     * @throws XBRLException
     */
    public URI getResourceRole() throws XBRLException;

}
