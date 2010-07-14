package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.data.Store;
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
    private static final long serialVersionUID = -6655272544674517422L;

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
     * The axis of the aspect model to add new aspects to by default.
     * This defaults to "orphan".
     */
    private String defaultAxis = "orphan";

    /**
     * The data store used to define some of the standard aspects.
     */
    private Store store;
    
    /**
     * @param store The data store required to create domains for some aspects.
     * @throws XBRLException if the store is null.
     */
    public AspectModelImpl(Store store) throws XBRLException {
        super();
        if (store == null) throw new XBRLException("The data store must not be null.");
        this.store = store;
    }

    /**
     * @return the data store underlying this aspect model.
     */
    protected Store getStore() {
        return this.store;
    }
    
    /**
     * @see AspectModel#getNewAspectAxis()
     */
    public String getDefaultAxis() {
        return defaultAxis;
    }

    /**
     * @see AspectModel#setNewAspectAxis(String)
     */
    public void setDefaultAxis(String defaultAxis) {
        this.defaultAxis = defaultAxis;
    }
        
    
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
    public Aspect getAspect(URI aspectId) throws XBRLException {
        if (! aspects.containsKey(aspectId)) throw new XBRLException(aspectId + " is not in the aspect model.");
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
     * @param aspect The aspect to remove from an axis.
     */
    private void deleteAspect(Aspect aspect) {
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
     * @see AspectModel#addAspect(URI)
     */
    public void addAspect(Aspect aspect) {
        this.addAspect(this.getDefaultAxis(),aspect);
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
     * @see AspectModel#addAspect(Aspect, Aspect)
     */
    public void addAspect(Aspect parentAspect, Aspect aspect)
            throws XBRLException {
        this.addAspect(this.getDefaultAxis(), parentAspect, aspect);
    }    

    /**
     * @see AspectModel#getAxes()
     */
    public Set<String> getAxes() throws XBRLException {
        return this.axes.keySet();
    }

    /**
     * @see AspectModel#getAspectValues(Fact)
     */
    public Map<URI,AspectValue> getAspectValues(Fact fact) throws XBRLException {
        Map<URI,AspectValue> result = new HashMap<URI,AspectValue>();
        for (Aspect aspect: this.getAspects()) {
            result.put(aspect.getId(),aspect.getValue(fact));
        }
        return result;
    }

    /**
     * @see AspectModel#getAspectValues(Fact, Map<URI,AspectValue>)
     */
    public Map<URI,AspectValue> getAspectValues(Fact fact, Map<URI,AspectValue> existingValues) throws XBRLException {
        Map<URI,AspectValue> result = new HashMap<URI,AspectValue>();
        for (Aspect aspect: this.getAspects()) {
            URI id = aspect.getId();
            if (existingValues.containsKey(id)) result.put(id,existingValues.get(id));
            else result.put(id,aspect.getValue(fact));
        }
        return result;
    }
 
    /**
     * @see AspectModel#moveAspects(String, String)
     */
    public void moveAspects(String originalAxis, String newAxis) throws XBRLException {
        if (! this.hasAxis(originalAxis)) throw new XBRLException(originalAxis + " is not an axis of this model.");
        Collection<Aspect> aspects = this.getAspects(originalAxis);
        this.axes.removeAll(originalAxis);
        for (Aspect aspect: aspects) {
            this.addAspect(newAxis, aspect);
        }
    }
}
