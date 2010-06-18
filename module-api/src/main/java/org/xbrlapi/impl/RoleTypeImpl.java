package org.xbrlapi.impl;

import java.util.List;

import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Resource;
import org.xbrlapi.RoleType;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class RoleTypeImpl extends CustomTypeImpl implements RoleType {

    /**
     * 
     */
    private static final long serialVersionUID = 2596352804658433028L;

    /**
     * @see RoleType#getUsingExtendedLinks()
     */
    public List<ExtendedLink> getUsingExtendedLinks() throws XBRLException {
        String query = "for $root in #roots#[@type='"+ExtendedLinkImpl.class.getName()+"'] where $root/xbrlapi:data/*/@xlink:role='"+ this.getCustomURI() +"' return $root";
        List<ExtendedLink> result = getStore().<ExtendedLink>queryForXMLResources(query);
        return result;
    }
    
    /**
     * @see RoleType#getUsingSimpleLinks()
     */
    public List<SimpleLink> getUsingSimpleLinks() throws XBRLException {
        String query = "for $root in #roots#[@type='"+SimpleLinkImpl.class.getName()+"'] where $root/xbrlapi:data/*/@xlink:role='"+ this.getCustomURI() +"' return $root";
        List<SimpleLink> result = getStore().<SimpleLink>queryForXMLResources(query);
        return result;
    }    

    /**
     * @see RoleType#getUsingResources()
     */
    public List<Resource> getUsingResources() throws XBRLException {
        String query = "for $root in #roots# let $data := $root/xbrlapi:data/* where ($data/@xlink:type='resource' and $data/@xlink:role='"+ this.getCustomURI() +"') return $root";
        List<Resource> result = getStore().<Resource>queryForXMLResources(query);
        return result;
    }    

}
