package org.xbrlapi.aspects.alt;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.xbrlapi.utilities.XBRLException;

/**
 * <p>
 * Defines the methods that are available to interact with 
 * lists of aspects and lists of their values.
 * </p>
 * 
 * <p>
 * This object provides a range of functionality that assists
 * with activities such as creating multilevel table row or
 * column headings based upon combinations of aspect values.
 * </p>
 * 
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
    public int getAspectValueCount(URI aspectId) throws XBRLException;

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
    public int getAncestorCount(URI aspectId) throws XBRLException;

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
    public int getDescendantCount(URI aspectId) throws XBRLException;
    
    /**
     * @param aspects The list of aspects to use.
     * @throws XBRLException if the aspects parameter is null or the aspect is not in the
     * specified axis of the aspect model.  
     */
    public void setAspectValues(URI aspectId, List<AspectValue> values) throws XBRLException;

    /**
     * @return the total number of different combinations of aspect values.
     * @throws XBRLException
     */
    public int getCombinationCount();
    
    /**
     * This method is useful when setting up filters for a specific combination
     * of aspect values.
     * The combinations are numbered from zero to the total number of combinations less one.
     * @param aspectId The ID of the aspect that the value is being sought for.
     * @param combination The index of the combination that an aspect value
     * is being sought for.
     * @return The value for the specified aspect in the specified combination.
     */
    public AspectValue getCombinationValue(URI aspectId, int combination) throws XBRLException;

    /**
     * This method is particularly useful in setting up fact set filtering based upon
     * a combination of aspect values.
     * @param combination The index of the combination of aspect values to retrieve
     * @return a map from aspect IDs to the values of those aspects for the specified
     * combination.
     * @throws XBRLException
     */
    public Map<URI,AspectValue> getCombinationValues(int combination) throws XBRLException;
}
