package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.Collection;
import java.util.Set;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.tests.Timer;
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
     * @param model The aspect model determining the aspects to work with.
     * @throws XBRLException if the aspect model is null.
     */
    public FactSetImpl(AspectModel model) throws XBRLException {
        if (model == null) throw new XBRLException("The aspect model must not be null.");
        this.model = model;
        
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
    public void addFacts(Collection<Fact> facts) throws XBRLException {
        for (Fact fact: facts) {
            this.addFact(fact);
        }
    }

    /**
     * @see FactSet#addFact(Fact)
     */
    public void addFact(Fact fact) throws XBRLException {

        logger.info("adding fact to fact set: " + Timer.now());
        // Put the new fact into the various maps.
        for (Aspect aspect: model.getAspects()) {
            AspectValue value = aspect.getValue(fact);
            logger.info("added value for aspect " + aspect.getId() + " : " + Timer.now());
            if (! valueMap.containsEntry(value,fact)) valueMap.put(value,fact);
            if (! factMap.containsEntry(fact,value)) factMap.put(fact,value);
            if (! aspectMap.containsEntry(value.getAspectId(),value)) aspectMap.put(value.getAspectId(),value);
        }
    }
    
    /**
     * Adds an aspect-value - fact pairing to the fact set.
     * @param value The aspect value to add.
     * @param fact The fact to add.
     */
    protected void addAspectValue(AspectValue value, Fact fact) {
        valueMap.put(value,fact);
        factMap.put(fact,value);
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
        Collection<Aspect> aspects = getAspectModel().getAspects();
        for (AspectValue value: values) {
            aspects.remove(getAspectModel().getAspect(value.getAspectId()));
        }
        for (Aspect aspect: aspects) {
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
        return aspectMap.get(aspectId);
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
    
}
