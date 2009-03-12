package org.xbrlapi.aspects;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 *
 */
public interface AspectModel {




    
    /**
     * @return The unique identifier of the aspect model.
     */
    public String getType();
    
    /**
     * @return a Collection of the aspects in the aspect model.
     */
    public Collection<Aspect> getAspects();

    /**
     * @param type the aspect type.
     * @return the aspect with the specified aspect type.
     * @throws XBRLException if the aspect model does not have the
     * specified aspect.
     */
    public Aspect getAspect(String type) throws XBRLException;
    
    /**
     * @param type the aspect type.
     * @return true if the aspect model includes the aspect with 
     * the specified aspect type and false otherwise.
     */
    public boolean hasAspect(String type);
    
    /**
     * @return a collection of aspects that are not associated with a dimension, either
     * as the root aspect for the dimension or as a descendant of another aspect
     * associated with the dimension.
     */
    public Collection<Aspect> getOrphanAspects();
    
    /**
     * @param dimension The unique (for the aspect model) identifier for
     * a dimension of the aspect model.  Aspect models can have zero or more
     * dimensions, each of which has a root aspect.  Root aspects can have a series
     * of child aspects, all of which are aspects associated with the same dimension
     * of the aspect model.  These dimensions are useful for relating aspects
     * to rows and columns of a table, for example.
     * @return the linked list of aspects for the dimension.
     * @throws XBRLException if the dimension is not defined for the aspect model.
     */
    public LinkedList<Aspect> getDimensionAspects(String dimension) throws XBRLException;

    /**
     * @param aspect The aspect to set in the aspect model, as an orphan.
     */
    public void setAspect(Aspect aspect);
    
    /**
     * @param fact The fact to add to the aspect model
     * @throws XBRLException if the fact cannot be added to the aspect model.
     */
    public void addFact(Fact fact) throws XBRLException;

    
    /**
     * @param aspectType The type of aspect to arrange in the aspect model.
     * @param dimension the dimension to put the aspect in, in last 
     * place in the ordering.
     * @throws XBRLException if the aspect is not in the aspect model.
     */
    public void arrangeAspect(String aspectType, String dimension) throws XBRLException;
    
    /**
     * @param aspectType The type of aspect to arrange in the aspect model.
     * @param dimension the dimension to put the aspect in.
     * @param parentType The type of parent aspect for the aspect being set.
     * @throws XBRLException if the aspect or parent are not in the aspect model.
     */
    public void arrangeAspect(String aspectType, String dimension, String parentType) throws XBRLException;
 

    
    /**
     * @param values is a collection of AspectValues to be matched.
     * @return the set of all facts in the aspect model that 
     * match all of the aspect values to be matched.
     * @throws XBRLException
     */
    public Set<Fact> getFacts(Collection<AspectValue> values) throws XBRLException;    
    
    /**
     * @return the set of all facts in the aspect model.
     * @throws XBRLException
     */
    public Set<Fact> getAllFacts() throws XBRLException;
    
    /**
     * @return The set of facts matching the selection 
     * criteria that have been added to the aspect model.
     * @throws XBRLException
     */
    public Set<Fact> getMatchingFacts() throws XBRLException;
    
    /**
     * Adds a fact selection criterion used in selecting
     * a set of the facts in the aspect model.
     * @param criterion The aspect value that the selected
     * facts must have.
     * @see AspectModel#getMatchingFacts()
     * @throws XBRLException
     */
    public void setCriterion(AspectValue criterion) throws XBRLException;
    
    /**
     * Adds a collection of fact selection criteria.
     * @param criteria The collection of aspect values 
     * that the selected facts must have.
     * @see AspectModel#getMatchingFacts()
     * @see AspectModel#setCriterion(AspectValue)
     * @throws XBRLException
     */
    public void setCriteria(Collection<AspectValue> criteria) throws XBRLException;    

    /**
     * Clears all fact selection criteria in the aspect model.
     * @see AspectModel#setCriterion(AspectValue)
     * @see AspectModel#getMatchingFacts()
     */
    public void clearAllCriteria();

    /**
     * @param dimension The name of the dimension
     * @return a list of lists of aspect values where
     * each list in the list is a combination of one 
     * aspect value for each aspect in the dimension.
     * The aspect values in each combination 
     * are ordered in the same order as the aspects in the
     * dimension.  The lists in the list are ordered 
     * by the orderings of the values for each aspect.
     */
    public List<List<AspectValue>> getAspectValueCombinationsForDimension(String dimension);
    
    /**
     * @param dimension The name of the dimension
     * @return a list of lists of aspect values where
     * each list in the list is a combination of one 
     * aspect value for each aspect in the dimension.
     * The aspect values in each combination 
     * are ordered in the same order as the aspects in the
     * dimension.  The lists in the list are ordered 
     * by the orderings of the values for each aspect.
     * Any aspect that has just missing aspect values is omitted.
     */
    public List<List<AspectValue>> getMinimalAspectValueCombinationsForDimension(String dimension);    
    
    /**
     * @param type The type identifying the aspect to delete.
     * @throws XBRLException
     */
    public void deleteAspect(String type) throws XBRLException;
    
    /**
     * Removes all facts from the aspect model and all aspect values
     * from the aspects in the aspect models.  The aspects retain their
     * maps from aspect value IDs to aspect value labels, however.
     * @throws XBRLException
     */
    public void clearFacts() throws XBRLException;
 
 
}
