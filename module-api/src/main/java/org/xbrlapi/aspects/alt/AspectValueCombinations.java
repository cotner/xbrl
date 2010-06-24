package org.xbrlapi.aspects.alt;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

import org.xbrlapi.utilities.XBRLException;

/**
 * Defines the methods that are available to interact with 
 * lists of aspects and their sometimes heirarchically 
 * ordered values.
 * This object provides a range of functionality that assists
 * with activities such as creating multilevel table row or
 * column headings based upon combinations of aspect values.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */

public interface AspectValueCombinations extends Serializable {

    /**
     * @return the axis of the model that this combinations object
     * relates to.
     */
    public String getAxis();    
    
    /**
     * @return the ordered list of the underlying aspects.
     */
    public List<Aspect> getAspects();

    /**
     * @param aspectId The ID of the aspect of interest.
     * @return true if the aspects in this combination include
     * the one specified and false otherwise.
     * @throws XBRLException if the aspect ID is null
     */
    public boolean hasAspect(URI aspectId) throws XBRLException;

    /**
     * @param aspectId The ID of the aspect of interest.
     * @return the list of aspect values for the aspect of interest or
     * the empty list of no aspect values have been set for the aspect of interest.
     * @throws XBRLException if the combinations do not include
     * the specified Aspect.
     */
    public List<AspectValue> getAspectValues(URI aspectId) throws XBRLException;
    
    /**
     * Removes the aspect values for the specified aspect.
     * @param aspectId The ID of the aspect of interest.
     * @throws XBRLException if the combinations do not include
     * the specified Aspect.
     */
    public void clearAspectValues(URI aspectId) throws XBRLException;
    
    

    /**
     * @param aspectId The ID of the aspect of interest.
     * @return the number of values for the specified aspect
     * @throws XBRLException if the combinations do not include
     * the specified Aspect.
     */
    public long getAspectValueCount(URI aspectId) throws XBRLException;

    /**
     * @param aspectId The ID of the aspect of interest.
     * @return the number of unique combinations of values for the aspects
     * that precede the aspect of interest in the list of aspects associated
     * with this combination.  This value is useful in determining the number of
     * times that you need to iterate over the values for the aspect of interest
     * when building up a set of table headings.  The value is one if this aspect
     * is the first in the list of aspects.
     * @throws XBRLException if the combinations do not include
     * the specified Aspect.
     */
    public long getAncestorCount(URI aspectId) throws XBRLException;

    /**
     * @param aspectId The ID of the aspect of interest.
     * @return the number of unique combinations of values for the aspects
     * that follow the aspect of interest in the list of aspects associated
     * with this combination.  This value is useful in determining the column or row
     * span that you need for each value of the aspect of interest 
     * when building up a set of table headings. The value is 1 if
     * there are no following aspects.
     * @throws XBRLException if the combinations do not include
     * the specified Aspect.
     */
    public long getDescendantCount(URI aspectId) throws XBRLException;
    
    /**
     * @param aspects The list of aspects to use.
     * @throws XBRLException if the aspects parameter is null or the aspect is not in the
     * specified axis of the aspect model.  
     */
    public void setAspectValues(URI aspectId, List<AspectValue> values) throws XBRLException;

}
