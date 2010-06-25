package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
        
        Set<Aspect> aspects = model.getNewAspects(fact);

        // Update the aspect values for facts already in the fact set.
        if (! aspects.isEmpty()) {
            Collection<Fact> facts = factMap.keySet();
            for (Aspect aspect: aspects) {
                model.addAspect("orphan", aspect);
                if (aspect.getDomain().allowsMissingValues()) {
                    this.valueMap.putAll(aspect.getMissingValue(), facts);                
                } else {
                    for (Fact f: facts) {
                        addAspectValue(aspect.getValue(f),f);
                    }
                }
            }
        }
        
        // Put the new fact itself into the fact map.
        for (Aspect aspect: model.getAspects()) {
            AspectValue value = aspect.getValue(fact);
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
    public Collection<AspectValue> getAspectValues(Fact fact) {
        return factMap.get(fact);
    }

    /**
     * @see FactSet#getCombinationValues(AspectId)
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
        return valueMap.get(value);
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
     * @see AspectModel#getAspectValueCombinationsForAxis(String)
     */
    public List<List<AspectValue>> getAspectValueCombinationsForAxis(String axis) throws XBRLException {
        /*         
        List<Aspect> aspects = this.getAspects(axis);
        for (int i=aspects.size()-1; i>=0; i--) {
            List<Heading> headings = new Vector<Heading>();
            
        }
        
        // Set up the result matrix
        List<Aspect> aspects = getAxisAspects(dimension);
        List<List<AspectValue>> result = new Vector<List<AspectValue>>();
        Aspect firstAspect = aspects.get(0);
        int combinations = firstAspect.getValues().size() * firstAspect.getDescendantCount();
        for (int i=0; i<combinations; i++) {
            result.add(new Vector<AspectValue>());
        }
        for (Aspect aspect: aspects) {
            
            List<AspectValue> values = aspect.getValuesByHierarchy();
            int vCount = aspect.size();
            int dCount = aspect.getDescendantCount();
            int aCount = aspect.getAncestorCount();
            for (int a_i=0; a_i<aCount; a_i++) {
                for (int d_i=0; d_i<dCount; d_i++) {
                    for (int v_i=0; v_i<vCount; v_i++) {
                        int index = dCount*vCount*a_i + dCount*v_i + d_i;
                        result.get(index).add(values.get(v_i));
                    }
                }
            }            
            
        }
        return result;
    */
        return null;
    }
    
    /**
     * @see FactSet#getSize()
     */
    public long getSize() {
        return this.factMap.keySet().size();
    }
    
}
