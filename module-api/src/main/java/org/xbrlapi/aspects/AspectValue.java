package org.xbrlapi.aspects;

import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface AspectValue {

    /**
     * @return the fragment expressing this aspect value.
     */
    public Fragment getFragment();
    
    
    /**
     * @return the aspect that this is a value for.
     */
    public Aspect getAspect();

    /**
     * @return the string value of this aspect, generally, a string
     * that uniquely indicates the value of the aspect in a human readable
     * form.
     * @throws XBRLException if the string value cannot be obtained.
     */
    public String getId() throws XBRLException;

}
