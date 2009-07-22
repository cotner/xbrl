package org.xbrlapi.aspects;

import java.io.Serializable;

import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface AspectValue extends Serializable {

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
    
    /**
     * @return The label for this aspect value.
     * @throws XBRLException
     */
    public String getLabel() throws XBRLException;
    
    /**
     * This method supports aspects, such as the concept aspect and
     * XDT dimension aspects where aspect values can have a heirarchical 
     * organisation, such that an aspect value can be associated with 
     * an ordered set of children aspect values.
     * @return the parent aspect value or null if none exists.
     * @throws XBRLException
     */
    public AspectValue getParent() throws XBRLException;

}
