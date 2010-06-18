package org.xbrlapi.xdt;

import java.net.URI;

/**
 * Defines a range of constants (namespaces etc) that are used for the XDT module.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class XDTConstants {

    public final static String XBRLDTPrefix = "xbrldt";

    public final static String XBRLDIPrefix = "xbrldi";
    
    public final static String hypercube = "hypercubeItem";
    
    public final static String dimension = "dimensionItem";

    public static URI XBRLDTNamespace = URI.create("http://xbrl.org/2005/xbrldt");
    public static URI XBRLDINamespace = URI.create("http://xbrl.org/2006/xbrldi");
    public static URI HypercubeDimensionArcrole = URI.create("http://xbrl.org/int/dim/arcrole/hypercube-dimension");
    public static URI DimensionDomainArcrole = URI.create("http://xbrl.org/int/dim/arcrole/dimension-domain");
    public static URI DomainMemberArcrole = URI.create("http://xbrl.org/int/dim/arcrole/domain-member");
    public static URI AllArcrole = URI.create("http://xbrl.org/int/dim/arcrole/all");
    public static URI NotAllArcrole = URI.create("http://xbrl.org/int/dim/arcrole/notAll");
    public static URI DefaultDimensionArcrole = URI.create("http://xbrl.org/int/dim/arcrole/dimension-default");

}
