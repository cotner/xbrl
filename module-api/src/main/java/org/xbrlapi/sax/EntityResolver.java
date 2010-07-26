package org.xbrlapi.sax;

import java.io.Serializable;

/**
 * General entity resolver interface that enforces serializability.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface EntityResolver extends org.xml.sax.EntityResolver,
        org.apache.xerces.xni.parser.XMLEntityResolver, Serializable {

}
