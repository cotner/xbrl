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
     * @return the fact set defining the values for each aspect.
     */
    public FactSet getFactSet();




    /**
     * @param <V> The class of aspect value being retrieved.
     * @param aspectId The ID of the aspect of interest.
     * @return the ordered list of aspect values for the aspect of interest.
     * @throws XBRLException if the combinations do not include
     * the specified Aspect.
     */
    public <V extends AspectValue> List<V> getAspectValues(URI aspectId) throws XBRLException;

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

}
