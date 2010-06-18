package org.xbrlapi.aspects;

import java.io.Serializable;
import java.util.List;

import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>
 * Explanation of aspect values. 
 * </h2>
 * 
 * <p>
 * Aspect values have the following characteristics:
 * </p>
 * 
 * <ul>
 * <li>Each aspect value is associated with a specific aspect that has its own unique 
 * aspect type.  Aspect values for different aspects cannot be equal.</li>
 * <li>Semantically equivalent aspect values can be identified as such based upon the value
 * of a single property of the aspect value: its identifier.</li>
 * <li>Aspect values can have a heirarchical ordering.  
 * Thus, an aspect value can have a single
 * parent aspect value and multiple child aspect values.  
 * Also, sibling aspect values can have a strict ordering.</li>
 * <li>Aspect values always have human readable labels.  These can be in 
 * multiple languages/locales, where appropriate.</li>
 * <li>Aspect values define a mapping from the aspect identifier to the aspect value labels.</li>
 * <li>It must always be possible to construct an aspect value without resorting to obtaining one from
 * an XBRL fact.  This is required to enable filtering of XBRL data by aspect without having to 
 * start with actual XBRL data with the required aspect values.  This implies that aspect values are 
 * not defined in terms of specific XBRL fragments.</li>
 * </ul>
 * 
 * <p>
 * Note that aspect values store the information necessary to enable the determination of 
 * their appropriate human-readable labels in various locales.  In some cases this information
 * will best be a fragment index.  In others, it might be an element QName or an XPointer expression.
 * The details of the information recorded in an aspect value to enable determination of aspect value
 * labels are private to aspect value implementations.
 * </p>
 * 
 * <ul>
 *   <li>Do we need to record the aspect label in all possible languages?</li>
 *   <li>We need to record the aspect type rather than the aspect object as part of the aspect value identifier.</li>
 * </ul>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface AspectValue extends Serializable {

    /**
     * TODO this method should be eliminated because the details of the 
     * data required to construct an aspect value identifier and the aspect
     * value labels need to be hidden.
     * 
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
     * that uniquely indicates the value of the aspect, though not necessarily
     * in a human readable form.
     * @throws XBRLException if the string value cannot be obtained.
     */
    public String getIdentifier() throws XBRLException;
    
    /**
     * @return The human-readable label for this aspect value.
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
     * in the aspect model and false otherwise.
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
