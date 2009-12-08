package org.xbrlapi.xdt;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * Defines a range of constants (namespaces etc) that are used for the XDT module.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class XDTConstants {

    public final static String XBRLDTPrefix = "xbrldt";

    public final static String XBRLDIPrefix = "xbrldi";
    
    public final static String hypercube = "hypercubeItem";
    
    public final static String dimension = "dimensionItem";

    public static URI XBRLDTNamespace;
    public static URI XBRLDINamespace;
    
    public static URI HypercubeDimensionArcrole;
    public static URI DimensionDomainArcrole;
    public static URI DomainMemberArcrole;
    public static URI AllArcrole;    
    public static URI NotAllArcrole;    
    public static URI DefaultDimensionArcrole;

    static {
        
        try {
            XBRLDTNamespace = new URI("http://xbrl.org/2005/xbrldt");
            XBRLDINamespace = new URI("http://xbrl.org/2006/xbrldi");
            HypercubeDimensionArcrole = new URI("http://xbrl.org/int/dim/arcrole/hypercube-dimension");
            DimensionDomainArcrole = new URI("http://xbrl.org/int/dim/arcrole/dimension-domain");
            DomainMemberArcrole = new URI("http://xbrl.org/int/dim/arcrole/domain-member");
            AllArcrole = new URI("http://xbrl.org/int/dim/arcrole/all");
            NotAllArcrole = new URI("http://xbrl.org/int/dim/arcrole/notAll");
            DefaultDimensionArcrole = new URI("http://xbrl.org/int/dim/arcrole/dimension-default");
            
        } catch (URISyntaxException e) {
            ;// Not possible
        }
        
    }
    
    
}
