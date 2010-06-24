package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class AspectValueCombinationsImpl implements AspectValueCombinations {

    /**
     * 
     */
    private static final long serialVersionUID = -904161372281068318L;

    /**
     * @param model The model containing the aspects and their arrangement into axes.
     * @param factSet The set of facts.
     * @param axis The model axis to generate a combination for.
     * @throws XBRLException
     */
    public AspectValueCombinationsImpl(AspectModel model, FactSet factSet, String axis) throws XBRLException {
        super();
        setFactSet(factSet);
        setAxis(axis);
    }

    /**
     * The set of facts determining the values for each aspect.
     */
    private FactSet factSet;

    /**
     * The axis of the model to generate combinations for.
     */
    private String axis;

    /**
     * The aspect model in use.
     */
    private AspectModel model;
    
    /**
     * @see AspectValueCombinations#getAncestorCount(URI)
     */
    public long getAncestorCount(URI aspectId) throws XBRLException {
        List<Aspect> aspects = model.getAspects(axis);
        Aspect aspect = model.getAspect(aspectId);
        int index = aspects.indexOf(aspect);
        if (index == 0) return 1L;
        Aspect parentAspect = aspects.get(index-1);
        return (getAspectValueCount(parentAspect.getId()) * getAncestorCount(parentAspect.getId())); 
    }

    /**
     * @see AspectValueCombinations#hasAspect(URI)
     */
    public boolean hasAspect(URI aspectId) throws XBRLException {
        if (aspectId == null) throw new XBRLException("The aspect ID must not be null.");
        return model.axisContainsAspect(axis,aspectId);
    }

    /**
     * @param aspects The list of aspects to use.
     * @throws XBRLException if the aspects parameter is null.
     */
    private void setAxis(String axis) throws XBRLException {
        if (axis == null) throw new XBRLException("The axis must not be null.");
        if (! model.hasAxis(axis)) throw new XBRLException("The model does not have axis " + axis);
        this.axis= axis;
    }

    /**
     * @see AspectValueCombinations#getAspectValueCount(URI)
     */
    public long getAspectValueCount(URI aspectId) throws XBRLException {
        return factSet.getAspectValues(aspectId).size();
    }

    /**
     * @see AspectValueCombinations#getAspectValues(URI)
     */
    public List<AspectValue> getAspectValues(URI aspectId)
            throws XBRLException {
        if (! this.hasAspect(aspectId)) 
            throw new XBRLException("The aspect " + aspectId + " is not in this combination.");
        Aspect aspect = model.getAspect(aspectId);
        Domain domain = aspect.getDomain();
        Collection<AspectValue> aspectValues = getFactSet().getAspectValues(aspectId);
        
        /*
         * Aspect value options are:
         * 1. Get the aspect values based on the domain alone - this is not always feasible.
         * 2. Get the aspect values based on facts alone - this always works.
         * 3. Augment the aspect values from facts with others defined by the domain.
         * 
         * Steps involved in getting the aspect values into order are:
         * 1. Get the root aspect values.
         * 2. Build out each tree structure stopping each branch where the leaves are empty.
         */
        
        //List<AspectValue> result = this.sort(domain, aspectValues);
        //return result;
        return null;
    }

    /**
     * @see AspectValueCombinations#getAspects()
     */
    public List<Aspect> getAspects() {
        return model.getAspects(axis);
    }

    /**
     * @see AspectValueCombinations#getDescendantCount(java.net.URI)
     */
    public long getDescendantCount(URI aspectId) throws XBRLException {
        List<Aspect> aspects = model.getAspects(axis);
        Aspect aspect = model.getAspect(aspectId);
        int index = aspects.indexOf(aspect);
        if (index == aspects.size()-1) return 1L;
        Aspect childAspect = aspects.get(index+1);
        return (getAspectValueCount(childAspect.getId()) * getAncestorCount(childAspect.getId()));
    }

    /**
     * @see AspectValueCombinations#getFactSet()
     */
    public FactSet getFactSet() {
        return factSet;
    }

    /**
     * @param factSet The fact set defining the values for each aspect.
     * @throws XBRLException if the fact set is null.
     */
    private void setFactSet(FactSet factSet) throws XBRLException {
        if (factSet == null) throw new XBRLException("The fact set must not be null.");
        this.factSet = factSet;
    }


    
    
    
}
