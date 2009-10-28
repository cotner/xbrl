package org.xbrlapi.aspects;

import java.io.Serializable;
import java.util.List;

import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface AspectValue extends Serializable {

    /**
     * @return the fragment expressing this aspect value.
     * If this aspect value represents a "missing value" then
     * the fragment returned is null.
     * @throws XBRLException if the fragment is not of the given type.
     */
    public <F extends Fragment> F getFragment() throws XBRLException;
    
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
    public String getIdentifier() throws XBRLException;
    
    /**
     * @return The label for this aspect value.
     * @throws XBRLException
     */
    public String getLabel() throws XBRLException;
    
    /**
     * This method supports aspects, such as the location aspect 
     * where values can be given a heirarchical 
     * organisation, such that an aspect value can be associated with 
     * an ordered set of children aspect values.
     * @return the ordered list of child aspect values. 
     * The list is empty if there are no children aspect values.
     * @throws XBRLException
     */
    public List<AspectValue> getChildren() throws XBRLException;    
    
    /**
     * This method supports aspects, such as the location aspect, 
     * where aspect values can have a heirarchical 
     * organisation, such that an aspect value can be associated with 
     * an ordered set of children aspect values.
     * @return the parent aspect value or null if none exists.
     * @throws XBRLException
     */
    public AspectValue getParent() throws XBRLException;
    
    /**
     * @return true if this aspect value has a parent aspect value
     * and false otherwise.
     * @throws XBRLException
     */
    public boolean hasParent() throws XBRLException;
    
    /**
     * @return the number of ancestor aspect values that
     * this aspect value has.
     * @throws XBRLException
     */
    public int getDepth() throws XBRLException;

    /**
     * @return True if this aspect value represents a missing value.
     */
    public boolean isMissing() throws XBRLException;

}
