package org.xbrlapi.aspects;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

/**
 * Abstract implementation of common aspect model methods.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
abstract public class BaseAspectModel implements AspectModel {

    /**
     * From aspect type to aspect.
     */
    private Map<String,Aspect> aspects = new HashMap<String,Aspect>();
    /**
     * From dimension name to list of aspects in dimension.
     */
    private Map<String,List<Aspect>> dimensions = new HashMap<String,List<Aspect>>();
    
    /**
     * @see AspectModel#getAspects()
     */
    public Collection<Aspect> getAspects() {
        return aspects.values();
    }

    /**
     * @see AspectModel#getOrphanAspects()
     */
    public Collection<Aspect> getOrphanAspects() {
        Collection<Aspect> aspects = this.aspects.values();
        for (Aspect aspect: aspects) {
            if (aspect.getDimension() != null) aspects.remove(aspect);
        }
        return aspects;
    }

    /**
     * @see AspectModel#getDimensionAspects(String)
     */
    public List<Aspect> getDimensionAspects(String dimension) {
        return dimensions.get(dimension);
    }

    /**
     * @see AspectModel#setOrphanAspect(Aspect)
     */
    public void setOrphanAspect(Aspect aspect) {
        try {
            aspect.setAspectModel(this);
        } catch (XBRLException e) {
            ;//Not possible
        }
        if (aspects.containsKey(aspect.getType())) {
            Aspect old = aspects.get(aspect.getType());
            if (old.getDimension() != null) {
                List<Aspect> dimensionAspects = dimensions.get(old.getDimension());
                LOOP: for (Aspect dimensionAspect: dimensionAspects) {
                    if (dimensionAspect.getType().equals(old.getType())) {
                        dimensionAspects.remove(dimensionAspect);
                        break LOOP;
                    }
                }
            }
        }
        aspects.put(aspect.getType(),aspect);
        aspect.setDimension(null);
    }
    
    /**
     * @see AspectModel#setAspect(Aspect, String)
     */
    public void setAspect(Aspect aspect, String dimension) {
        try {
            aspect.setAspectModel(this);
        } catch (XBRLException e) {
            ;//Not possible
        }
        if (aspects.containsKey(aspect.getType())) {
            Aspect old = aspects.get(aspect.getType());
            if (old.getDimension() != null) {
                List<Aspect> dimensionAspects = dimensions.get(old.getDimension());
                LOOP: for (Aspect dimensionAspect: dimensionAspects) {
                    if (dimensionAspect.getType().equals(old.getType())) {
                        dimensionAspects.remove(dimensionAspect);
                        break LOOP;
                    }
                }
            }
        }
        aspect.setDimension(dimension);    
        dimensions.get(dimension).add(aspect);
        aspects.put(aspect.getType(),aspect);
    }
    
    /**
     * @see AspectModel#setAspect(Aspect, String, String)
     */
    public void setAspect(Aspect aspect, String dimension, String parentType) {
        try {
            aspect.setAspectModel(this);
        } catch (XBRLException e) {
            ;//Not possible
        }
        if (aspects.containsKey(aspect.getType())) {
            Aspect old = aspects.get(aspect.getType());
            if (old.getDimension() != null) {
                List<Aspect> dimensionAspects = dimensions.get(old.getDimension());
                LOOP: for (Aspect dimensionAspect: dimensionAspects) {
                    if (dimensionAspect.getType().equals(old.getType())) {
                        dimensionAspects.remove(dimensionAspect);
                        break LOOP;
                    }
                }
            }
        }
        aspect.setDimension(dimension);
        List<Aspect> dimensionAspects = dimensions.get(dimension);
        GETPARENT: for (Aspect dimensionAspect: dimensionAspects) {
            if (dimensionAspect.getType().equals(parentType)) {
                dimensionAspects.add(dimensionAspects.indexOf(dimensionAspect)+1,aspect);
                break GETPARENT;
            }
        }
        aspects.put(aspect.getType(),aspect);
    }

    /**
     * @see AspectModel#addFact(Fact)
     */
    public void addFact(Fact fact) throws XBRLException {
        for (Aspect aspect: getAspects()) {
            aspect.addFact(fact);
        }
    }
    
}
