package org.xbrlapi.aspects.alt;

import java.io.Serializable;
import java.net.URI;
import java.util.Set;

import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Filter</h2>
 * 
 * <p>
 * A filter defines a filtration of a set of facts based upon aspect value matching.
 * </p>
 * 
 * <p>
 * A filter only uses one aspect value for a given aspect.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface Filter extends Serializable {

    /**
     * @param criterion The aspect value to use as a filtration selection 
     * criterion.
     * @throws XBRLException if any of the parameters are null.
     */
    public void addCriterion(AspectValue criterion) throws XBRLException;
    

    
    /**
     * @param aspectId The aspect ID for the aspect that is to have its selection
     * criterion removed from this filter.  This method does nothing if the
     * filter is not currently applying a criterion for the specified aspect.
     */
    public void removeCriterion(URI aspectId);
    
    /**
     * Removes all selection criteria from the filter.
     */
    public void removeAllCriteria();

    /**
     * @param aspectId The aspect ID for the aspect that is to have its selection
     * criterion removed from this filter.
     * @return true if the filter has a selection criterion for the given aspect.
     */
    public boolean filtersOn(URI aspectId);

    
    /**
     * @param candidateFacts The facts to be filtered.
     * @return the subset of facts with aspect values that match the aspect values
     * that constitute the criteria in applied by the filter.
     */
    public Set<Fact> getMatchingFacts(FactSet candidateFacts);
    
}
