package org.xbrlapi;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public interface Measure {

    /**
     * @return the namespace for the measure value.
     */
    public String getNamespace();
    
    /**
     * @return the local name for the measure value.
     */
    public String getLocalname();
}
