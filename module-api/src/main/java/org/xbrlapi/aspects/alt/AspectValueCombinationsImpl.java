package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Aspect value combinations implementation</h2>
 * 
 * <p>
 * This implementation requires a constructor that
 * specifies the aspect model and the specific axis that
 * the aspect value combinations will be required for.
 * Thereafter, the list of values for each aspect must be
 * set.  How the list of aspect values is obtained is determined
 * externally.  It may be from a fact set or it may be from
 * an aspect domain or it may be from a combination of the two.
 * The ordering of aspect values is also determined externally
 * to this object.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class AspectValueCombinationsImpl implements AspectValueCombinations {

    /**
     * 
     */
    private static final long serialVersionUID = -6280457826226771955L;

    private static final Logger logger = Logger.getLogger(AspectValueCombinationsImpl.class);
    
    /**
     * The aspect model in use.
     */
    private AspectModel model;
    
    /**
     * The map from Aspect IDs to the lists of aspect values .
     */
    private HashMap<URI,List<AspectValue>> aspectValues = new HashMap<URI,List<AspectValue>>();
    
    
    /**
     * The axis of the aspect model defining the aspects to get combinations of values for.
     */
    private String axis;
    
    /**
     * @param model The model containing the aspects and their arrangement into axes.
     * @param axis The model axis to generate a combination for.
     * @throws XBRLException
     */
    public AspectValueCombinationsImpl(AspectModel model, String axis) throws XBRLException {
        super();
        setModel(model);
        setAxis(axis);
    }

    /**
     * @param model The aspect model to use.
     * @throws XBRLException if the aspect model parameter is null or
     * the model does not have the axis set in this object.
     */
    private void setModel(AspectModel model) throws XBRLException {
        if (model == null) throw new XBRLException("The aspect model must not be null.");
        if (! model.hasAxis(this.getAxis())) throw new XBRLException("The model does not have axis " + this.getAxis());
        this.model = model;
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
     * @see AspectValueCombinations#getAncestorCount(URI)
     */
    public int getAncestorCount(URI aspectId) throws XBRLException {
        List<Aspect> aspects = model.getAspects(axis);
        Aspect aspect = model.getAspect(aspectId);
        int index = aspects.indexOf(aspect);
        if (index == 0) return 1;
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
     * @see AspectValueCombinations#setAspectValues(URI, List<AspectValue>)
     */
    public void setAspectValues(URI aspectId, List<AspectValue> values) throws XBRLException {
        if (! this.hasAspect(aspectId)) 
            throw new XBRLException("The aspect " + aspectId + " is not in this combination.");
        if (values == null) throw new XBRLException("The list of aspect values must not be null.");
        aspectValues.put(aspectId,values);
    }
    
    /**
     * @see AspectValueCombinations#clearAspectValues(URI)
     */
    public void clearAspectValues(URI aspectId) throws XBRLException {
        if (! this.hasAspect(aspectId)) 
            throw new XBRLException("The aspect " + aspectId + " is not in this combination.");
        aspectValues.remove(aspectId);
    }

    /**
     * @see AspectValueCombinations#getAspectValueCount(URI)
     */
    public int getAspectValueCount(URI aspectId) throws XBRLException {
        return getAspectValues(aspectId).size();
    }

    /**
     * @see AspectValueCombinations#getAspectValues(URI)
     */
    public List<AspectValue> getAspectValues(URI aspectId)
            throws XBRLException {
        
        if (! this.hasAspect(aspectId)) 
            throw new XBRLException("The aspect " + aspectId + " is not in this combination.");
        
        if (aspectValues.containsKey(aspectId))
            return aspectValues.get(aspectId);        
        
        return new Vector<AspectValue>();
    }

    /**
     * @see AspectValueCombinations#getAspects()
     */
    public List<Aspect> getAspects() {
        return model.getAspects(axis);
    }

    /**
     * @see AspectValueCombinations#getDescendantCount(URI)
     */
    public int getDescendantCount(URI aspectId) throws XBRLException {
        List<Aspect> aspects = model.getAspects(axis);
        Aspect aspect = model.getAspect(aspectId);
        int index = aspects.indexOf(aspect);
        if (index == aspects.size()-1) return 1;
        Aspect childAspect = aspects.get(index+1);
        return (getAspectValueCount(childAspect.getId()) * getAncestorCount(childAspect.getId()));
    }

    /**
     * @see AspectValueCombinations#getAxis()
     */
    public String getAxis() {
        return this.axis;
    }

    /**
     * @see AspectValueCombinations#getCombinationCount()
     */
    public int getCombinationCount() {
        int result = 1;
        for (Aspect aspect: this.getAspects()) {
            try {
                result *= this.getAspectValueCount(aspect.getId());
            } catch (XBRLException e) {
                // Cannot be thrown.
            }
        }
        return result;
    }
    
    /**
     * @see AspectValueCombinations#getCombinationValue(URI, int)
     */
    public AspectValue getCombinationValue(URI aspectId, int combination) throws XBRLException {

        int valueCount = this.getAspectValueCount(aspectId);
        int descendantCount = this.getDescendantCount(aspectId);
        double ancestorIndex = Math.floor( combination / (descendantCount * valueCount) );
        double valueIndex = Math.floor( (combination - descendantCount * valueCount * ancestorIndex) / descendantCount );
        return this.getAspectValues(aspectId).get(new Double(valueIndex).intValue());
        
    }
    
    /**
     * @see AspectValueCombinations#getCombinationValues(int)
     */
    public Map<URI,AspectValue> getCombinationValues(int combination) throws XBRLException {
        Map<URI,AspectValue> result = new HashMap<URI,AspectValue>();
        for (Aspect aspect: this.getAspects()) {
            URI id = aspect.getId();
            result.put(id,this.getCombinationValue(id,combination));
        }
        return result;
    }
    
}