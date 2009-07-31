package org.xbrlapi;

import java.io.Serializable;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public interface Measure extends Serializable {

    /**
     * @return the namespace for the measure value.
     */
    public String getNamespace();
    
    /**
     * @return the local name for the measure value.
     */
    public String getLocalname();

    /**
     * @return the QName prefix for the measure value.
     */
    public String getPrefix();
    
}
