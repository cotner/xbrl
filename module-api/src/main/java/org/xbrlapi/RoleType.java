package org.xbrlapi;

import java.util.List;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface RoleType extends CustomType {

    /**
     * @return the list of extended links that use this role as the 
     * extended link role.
     */
    public List<ExtendedLink> getUsingExtendedLinks() throws XBRLException;
    
    /**
     * @return the list of simple links that use this role as the 
     * link role.
     */
    public List<SimpleLink> getUsingSimpleLinks() throws XBRLException;
    
    /**
     * @return the list of XLink resources that use this role as the 
     * resource role.
     */
    public List<Resource> getUsingResources() throws XBRLException;      
    
}
