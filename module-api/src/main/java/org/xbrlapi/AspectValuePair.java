package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * This XML Resource captures:
 * 
 * <ul>
 *  <li>The index of the XML fragment that defines the aspect value recorded in the resource</li>
 *  <li>the aspect ID and</li> 
 *  <li>the aspect value ID</li>
 * </ul>
 * 
 * <p>
 * The XML fragment can be a fact, a context or a unit.
 * </p>
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface AspectValuePair extends NonFragmentXML {

    /**
     * @return the XML fragment index
     * @throws XBRLException
     */
    public String getFragmentIndex() throws XBRLException;

    /**
     * @return the XML fragment type
     * @throws XBRLException
     */
    public String getFragmentType() throws XBRLException;

    /**
     * @return the XML fragment defining this set of aspect values
     * @throws XBRLException
     */
    public Fragment getFragment() throws XBRLException;

    /**
     * @return the value of the aspect ID.
     * @throws XBRLException
     */
    public URI getAspectId() throws XBRLException;

    /**
     * @return the value of the aspect, for the XML fragment.
     * @throws XBRLException
     */
    public String getAspectValueId() throws XBRLException;
    
}
