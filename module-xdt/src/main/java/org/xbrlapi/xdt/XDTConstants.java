package org.xbrlapi.xdt;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * Defines a range of constants (namespaces etc) that are used for the XDT module.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class XDTConstants {

    public final static String XBRLDTNamespace = "http://xbrl.org/2005/xbrldt";
    public final static String XBRLDTPrefix = "xbrldt";

    public final static String XBRLDINamespace = "http://xbrl.org/2006/xbrldi";
    public final static String XBRLDIPrefix = "xbrldi";
    
    public final static String hypercube = "hypercubeItem";
    
    public final static String dimension = "dimensionItem";

    public final static String defaultDimensionArcrole= "http://xbrl.org/int/dim/arcrole/dimension-default";
    public final static URI defaultDimensionArcrole() {
        try {
            return new URI(defaultDimensionArcrole);
        } catch (URISyntaxException e) { // Cannot be thrown.
            return null;
        }
    }
    
    public final static URI domainMemberArcrole() {
        try {
            return new URI(domainMemberArcrole);
        } catch (URISyntaxException e) { // Cannot be thrown.
            return null;
        }
    }  
    
    public final static URI XBRLDINamespace() {
        try {
            return new URI(XBRLDINamespace);
        } catch (URISyntaxException e) { // Cannot be thrown.
            return null;
        }
    }        
    
    public final static URI XBRLDTNamespace() {
        try {
            return new URI(XBRLDTNamespace);
        } catch (URISyntaxException e) { // Cannot be thrown.
            return null;
        }
    }            
    
    public final static String domainMemberArcrole= "http://xbrl.org/int/dim/arcrole/domain-member";
    
}
