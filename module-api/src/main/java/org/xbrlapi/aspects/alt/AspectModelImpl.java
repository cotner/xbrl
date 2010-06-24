package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * Implementation of common aspect model methods.
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class AspectModelImpl implements AspectModel {

    /**
     * 
     */
    private static final long serialVersionUID = -352814819536983560L;

    private static final Logger logger = Logger
            .getLogger(AspectModelImpl.class);

    /**
     * Map from axis names to lists of the aspects in that axis.
     * @serial
     */
    private ListMultimap<String, Aspect> axes = ArrayListMultimap.<String, Aspect> create();

    /**
     * Map from aspect IDs to the aspect implementations being used.
     */
    private Map<URI,Aspect> aspects = new HashMap<URI,Aspect>();
    
    /** 
     * The set of extenders used by the aspect model.
     */
    private Set<Extender> extenders = new TreeSet<Extender>();
    
    /**
     * @see AspectModel#hasAxis(String)
     */
    public boolean hasAxis(String axis) {
        return axes.containsKey(axis);
    }

    /**
     * @see AspectModel#hasAspect(URI)
     */
    public boolean hasAspect(URI aspectId) {
        return aspects.containsKey(aspectId);
    }
    
    /**
     * @see AspectModel#getAspect(URI)
     */
    public Aspect getAspect(URI aspectId) {
        return aspects.get(aspectId);
    }
    

    /**
     * @see AspectModel#getAspects()
     */
    public Collection<Aspect> getAspects() throws XBRLException {
        return aspects.values();
    }

    /**
     * @see AspectModel#getAspects(String)
     */
    public List<Aspect> getAspects(String axis) {
        if (axes.containsKey(axis))
            return axes.get(axis);
        return new Vector<Aspect>();
    }

    /**
     * @see AspectModel#deleteAspect(Aspect)
     */
    public void deleteAspect(Aspect aspect) {
        if (axes.containsValue(aspect)) {
            for (String key : axes.keySet()) {
                if (axes.get(key).contains(aspect)) {
                    axes.remove(key, aspect);
                    break;
                }
            }
            aspects.remove(aspect.getId());
        }
        
    }

    /**
     * @see AspectModel#axisContainsAspect(String, URI)
     */
    public boolean axisContainsAspect(String axis, URI aspectId) {
        if (!axes.containsKey(axis)) return false;
        return axes.get(axis).contains(aspects.get(aspectId));
    }

    /**
     * @see AspectModel#addAspect(String, URI)
     */
    public void addAspect(String axis, Aspect aspect) {
        this.deleteAspect(aspect);
        axes.put(axis, aspect);
        aspects.put(aspect.getId(),aspect);
    }

    /**
     * @see AspectModel#addAspect(String, Aspect, Aspect)
     */
    public void addAspect(String axis, Aspect parentAspect, Aspect aspect)
            throws XBRLException {
        this.deleteAspect(aspect);
        aspects.put(aspect.getId(),aspect);
        if (!axes.containsKey(axis)) {
            addAspect(axis, aspect);
            return;
        }
        if (!axisContainsAspect(axis, parentAspect.getId())) {
            addAspect(axis, aspect);
            return;
        }
        List<Aspect> aspects = axes.get(axis);
        axes.removeAll(axis);
        for (Aspect a: aspects) {
            axes.put(axis, a);
            if (a.equals(parentAspect)) axes.put(axis, aspect);
        }
    }

    /**
     * @see AspectModel#getAxes()
     */
    public Set<String> getAxes() throws XBRLException {
        return this.axes.keySet();
    }
    
    /**
     * @see AspectModel#addExtender(Extender)
     */
    public void addExtender(Extender extender) throws XBRLException {
        if (extender == null) throw new XBRLException("The aspect model extender must not be null.");
        extenders.add(extender);
    }

    /**
     * @see AspectModel#removeAllExtenders()
     */
    public void removeAllExtenders() {
        extenders.clear();
    }

    /**
     * @see AspectModel#removeExtender(Extender)
     */
    public void removeExtender(Extender extender) {
        extenders.remove(extender);
    }

    /**
     * @see AspectModel#getNewAspects(Fact)
     */
    public Set<Aspect> getNewAspects(Fact fact) throws XBRLException {
        Set<Aspect> newAspects = new HashSet<Aspect>();
        for (Extender extender: extenders) {
            newAspects.addAll(extender.getNewAspects(this,fact));
        }
        return newAspects;
    }

}
