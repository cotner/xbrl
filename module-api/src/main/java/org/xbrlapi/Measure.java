package org.xbrlapi;

import java.io.Serializable;
import java.net.URI;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public interface Measure extends Serializable {

    /**
     * @return the namespace for the measure value.
     */
    public URI getNamespace();
    
    /**
     * @return the local name for the measure value.
     */
    public String getLocalname();

    /**
     * @return the QName prefix for the measure value.
     */
    public String getPrefix();
    
}
