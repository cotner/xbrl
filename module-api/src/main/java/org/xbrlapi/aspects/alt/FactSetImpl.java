package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

/**
 * <h2>Fact Set Implementation</h2>
 * 
 * <p>
 * The implementation requires high performance for
 * the {@link FactSet#getAspectValues(Fact)} method
 * and the {@link FactSet#getFacts(AspectValue)} method.
 * It should be noted that a fact will map to multiple aspect values
 * and that an aspect value will, in general, map to multiple facts.
 * </p>
 * 
 * <p>
 * To support this performance requirement, a FactSet is implemented
 * using three one-to-many maps . These are based on synchronised versions of the 
 * Google Collections Multimap.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class FactSetImpl implements FactSet {

    /**
     * 
     */
    private static final long serialVersionUID = -3788375960630670676L;

    private static final Logger logger = Logger
    .getLogger(FactSetImpl.class);
    
    /**
     * A map from one aspect value to multiple facts.
     */
    Multimap<AspectValue, Fact> valueMap;

    /**
     * A map from one fact to multiple aspect values.
     */
    Multimap<Fact, AspectValue> factMap;
    
    /**
     * A map from aspect IDs to the values for that aspect.
     */
    Multimap<URI, AspectValue> aspectMap;
        
    /** 
     * The aspect model that is being used.
     */
    AspectModel model;
    
    /**
     * @see #FactSet#getModel()
     */
    public AspectModel getModel() {
        return model;
    }

    /**
     * @param model The model to set.
     * @throws XBRLException if the model is null.
     */
    private void setModel(AspectModel model) throws XBRLException {
        if (model == null) throw new XBRLException("The model must not be null.");
        this.model = model;
    }

    /**
     * @param model The aspect model determining the aspects to work with.
     * @throws XBRLException
     */
    public FactSetImpl(AspectModel model) throws XBRLException {
        this.setModel(model);
        
        valueMap =  Multimaps.synchronizedSetMultimap(HashMultimap.<AspectValue, Fact>create()); 
        
        factMap = Multimaps.synchronizedSetMultimap(HashMultimap.<Fact, AspectValue>create()); 

        aspectMap = Multimaps.synchronizedSetMultimap(HashMultimap.<URI, AspectValue>create()); 

    }
    
    /**
     * @see FactSet#getAspectModel()
     */
    public AspectModel getAspectModel() {
        return this.model;
    }
    
    /**
     * @see FactSet#addFacts(Collection)
     */
    public <F extends Fact> void addFacts(Collection<F> facts) throws XBRLException {
        for (F fact: facts) {
            this.addFact(fact);
        }
    }

    /**
     * @see FactSet#addFact(Fact)
     */
    public void addFact(Fact fact) throws XBRLException {

        Map<URI,AspectValue> values = model.getAspectValues(fact);
        for (URI id: values.keySet()) {
            AspectValue value = values.get(id);
            if (! value.isMissing()) {
                if (! valueMap.containsEntry(value,fact)) valueMap.put(value,fact);
                if (! factMap.containsEntry(fact,value)) factMap.put(fact,value);
                if (! aspectMap.containsEntry(id,value)) aspectMap.put(id,value);
            }
        }
    }
    

    
    /**
     * @see FactSet#getAspectValues()
     */
    public Set<AspectValue> getAspectValues() {
        return valueMap.keySet();
    }

    /**
     * @see FactSet#getAspectValues(Fact)
     */
    public Collection<AspectValue> getAspectValues(Fact fact) throws XBRLException {
        Collection<AspectValue> values = factMap.get(fact);
        Set<URI> aspectsWithValues = new HashSet<URI>();
        for (AspectValue value: values) {
            aspectsWithValues.add(value.getAspectId());
        }
        for (Aspect aspect: model.getAspects()) {
            if (! aspectsWithValues.contains(aspect.getId()))
                values.add(aspect.getMissingValue());
        }
        return values;
    }
    
    /**
     * @see FactSet#getAspectValue(URI, Fact)
     */
    public AspectValue getAspectValue(URI aspectId, Fact fact) throws XBRLException {
        for (AspectValue value: factMap.get(fact)) {
            if (value.getAspectId().equals(aspectId)) return value;
        }
        return this.getAspectModel().getAspect(aspectId).getMissingValue();
            
    }    

    /**
     * @see FactSet#getAspectValues(AspectId)
     */
    public Collection<AspectValue> getAspectValues(URI aspectId) {
        Collection<AspectValue> values = aspectMap.get(aspectId);
        try {
            values.add(model.getAspect(aspectId).getMissingValue());
        } catch (XBRLException e) {
            ; // Generally unreachable.
        }
        return values;
    }
    


    /**
     * @see FactSet#getFacts()
     */
    public Set<Fact> getFacts() {
        return factMap.keySet();
    }

    /**
     * @see FactSet#getFacts(AspectValue)
     */
    public Collection<Fact> getFacts(AspectValue value) {
        Collection<Fact> facts = valueMap.get(value);
        return facts;
    }

    /**
     * @see FactSet#hasAspectValue(AspectValue)
     */
    public boolean hasAspectValue(AspectValue value) {
        return valueMap.containsKey(value);
    }

    /**
     * @see FactSet#hasFact(Fact)
     */
    public boolean hasFact(Fact fact) {
        return factMap.containsKey(fact);
    }

    /**
     * @see FactSet#getSize()
     */
    public long getSize() {
        return this.factMap.keySet().size();
    }

    /**
     * @see FactSet#isPopulated(URI)
     */
    public boolean isPopulated(URI aspectId) {
        return (!aspectMap.get(aspectId).isEmpty());
    }
    
    /**
     * @see FactSet#isSingular(URI)
     */
    public boolean isSingular(URI aspectId) {
        return (aspectMap.get(aspectId).size() == 1);
    }    

    /**
     * @see FactSet#getRootFacts(URI)
     */
    public List<Fact> getRootFacts(URI aspectId) throws XBRLException {
        List<Fact> rootFacts = new Vector<Fact>();
        for (AspectValue value: this.getAspectValues(aspectId)) {
            Domain domain = model.getAspect(aspectId).getDomain();
            if (domain.isRoot(value)) {
                rootFacts.addAll(this.getFacts(value));
            } 
        }
        return rootFacts;
    }
    
    
}
