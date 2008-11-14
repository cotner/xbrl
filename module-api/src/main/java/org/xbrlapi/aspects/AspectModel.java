package org.xbrlapi.aspects;

import java.util.Collection;
import java.util.List;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 *
 */
public interface AspectModel {

    /**
     * Non-dimensional aspect model identifier
     */
    static public final String NONDIMENSIONAL = "nondimensional";

    /**
     * @return The unique identifier of the aspect model.
     */
    public String getType();
    
    /**
     * @return a Collection of the aspects in the aspect model.
     */
    public Collection<Aspect> getAspects();
    
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
     * @return the list of aspects for the dimension.
     * @throws XBRLException if the dimension is not defined for the aspect model.
     */
    public List<Aspect> getDimensionAspects(String dimension) throws XBRLException;

    /**
     * @param aspect The aspect to set in the aspect model, as an orphan.
     */
    public void setOrphanAspect(Aspect aspect);
    
    /**
     * @param aspect The aspect to add to the aspect model.
     * @param dimension the dimension to put the aspect in, in last 
     * place in the ordering.
     */
    public void setAspect(Aspect aspect, String dimension);
    
    /**
     * @param aspect The aspect to add to the aspect model.
     * @param dimension the dimension to put the aspect in.
     * @param parentType The type of parent aspect for the aspect being set.
     */
    public void setAspect(Aspect aspect, String dimension, String parentType);
    
}
