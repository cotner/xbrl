package org.xbrlapi.aspects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.XBRLException;

/**
 * Abstract implementation of common aspect methods.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
abstract public class BaseAspect implements Aspect {

    protected static Logger logger = Logger.getLogger(BaseAspect.class);  
    
    TreeMap<String,AspectValue> values = new TreeMap<String,AspectValue>();
    
    private AspectModel model = null;

    /**
     * @see org.xbrlapi.aspects.Aspect#getAspectModel()
     */
    public AspectModel getAspectModel() {
        return model;
    }

    private String dimension = null;
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getDimension()
     */
    public String getDimension() {
        return dimension;
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#isOrphan()
     */
    public boolean isOrphan() {
        return (this.dimension == null);
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#getLength()
     */
    public int getLength() {
        return this.values.size();
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getIterator()
     */
    public Iterator<AspectValue> getIterator() {
        return values.values().iterator();
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getDescendantCount()
     */
    public int getDescendantCount() {
        if (this.isOrphan()) return 1;
        List<Aspect> aspects = null;
        try {
            aspects = this.getAspectModel().getDimensionAspects(this.getDimension());
        } catch (XBRLException e) {
            ;// Cannot be thrown
        }
        int count = 0;
        for (Aspect aspect: aspects) {
            if (aspect.getType().equals(this.getType())) count = 1;
            else if (! aspect.isEmpty()) count = count * aspect.getLength();
        }
        return count;
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getAncestorCount()
     */
    public int getAncestorCount() {
        if (this.isOrphan()) return 1;
        List<Aspect> aspects = null;
        try {
            aspects = this.getAspectModel().getDimensionAspects(this.getDimension());
        } catch (XBRLException e) {
            ;//Cannot be thrown
        }
        int count = 1;
        for (Aspect aspect: aspects) {
            if (aspect.getType().equals(this.getType())) return count;
            if (! aspect.isEmpty()) count = count * aspect.getLength();
        }
        return count;
    }    

    /**
     * @see org.xbrlapi.aspects.Aspect#getValues()
     */
    public List<AspectValue> getValues() {
        List<AspectValue> v = new Vector<AspectValue>();
        v.addAll(values.values());
        return v;
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(String)
     */
    public AspectValue getValue(String id) {
        return values.get(id);
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(String)
     */
    public boolean hasValue(String id) {
        return values.containsKey(id);
    }    
    
    /**
     * @see org.xbrlapi.aspects.Aspect#addValue(AspectValue)
     */
    public void addValue(AspectValue value) throws XBRLException {
        String key = this.getTransformer().getIdentifier(value);
        if (! values.containsKey(key)) values.put(key, value);
    }    

    /**
     * @see org.xbrlapi.aspects.Aspect#isEmpty()
     */
    public boolean isEmpty() {
        return values.isEmpty();
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#isSingular()
     */
    public boolean isSingular() {
        return (this.getLength() == 1);
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#isMissing()
     */
    public boolean isMissing() {
        return (isSingular() && (getValues().get(0).getFragment() == null));
    }
    
    /**
     * @see Aspect#hasValue(AspectValue)
     */
    public boolean hasValue(AspectValue value) throws XBRLException {
        return values.containsKey(getTransformer().getIdentifier(value));
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#setAspectModel(org.xbrlapi.aspects.AspectModel)
     */
    public void setAspectModel(AspectModel aspectModel) throws XBRLException {
        if (aspectModel == null) throw new XBRLException("The aspect model must not be null.");
        model = aspectModel;
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#setDimension(java.lang.String)
     */
    public void setDimension(String dimension) {
        this.dimension = dimension;
    }
    
    private AspectValueTransformer transformer = null;

    /**
     * @see Aspect#getTransformer()
     */
    public AspectValueTransformer getTransformer() {
        return transformer;
    }

    /**
     * @see Aspect#setTransformer(AspectValueTransformer)
     */
    public void setTransformer(AspectValueTransformer transformer) {
        this.transformer = transformer;
    }
    
    private HashMap<String,Set<Fact>> facts = new HashMap<String,Set<Fact>>();
    
    /**
     * @see Aspect#getFacts(AspectValue)
     */
    public Set<Fact> getFacts(AspectValue value) throws XBRLException {
        if (! this.hasValue(value)) throw new XBRLException("This aspect does not have the given aspect value.");
        String key = getTransformer().getIdentifier(value);
        if (! facts.containsKey(key)) return new HashSet<Fact>();
        return facts.get(key);
    }

    /**
     * @see Aspect#addFact(Fact)
     */
    public void addFact(Fact fact) throws XBRLException {
        AspectValue value = getValue(fact);
        if (value == null) return;
        this.addValue(value);
        AspectValueTransformer transformer = this.getTransformer();
        String key = transformer.getIdentifier(value);
        if (facts.containsKey(key)) {
            Set<Fact> set = facts.get(key);
            if (! set.contains(fact)) set.add(fact);
        } else {
            Set<Fact> set = new HashSet<Fact>();
            set.add(fact);
            facts.put(key,set);
        }
    }
    

    private Map<String,Fragment> fragmentMap = new HashMap<String,Fragment>();
    public Fragment getFragment(Fact fact) throws XBRLException {
        String fragmentKey = getFragmentKey(fact);
        if (fragmentMap.containsKey(fragmentKey)) {
            return fragmentMap.get(fragmentKey);
        }
        Fragment fragment = getFragmentFromStore(fact);
        fragmentMap.put(fragmentKey,fragment);
        return fragment;
    }

    private AspectValue criterion = null;
    
    /**
     * @see org.xbrlapi.aspects.Aspect#clearSelectionCriterion()
     */
    public void clearSelectionCriterion() {
        criterion = null;
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#getMatchingFacts()
     */
    public Set<Fact> getMatchingFacts() throws XBRLException {
        if (getSelectionCriterion() == null) {
            Set<Fact> result = new HashSet<Fact>();
            for (AspectValue value: values.values()) {
                result.addAll(facts.get(value));
            }
            return result;
        }
        return this.facts.get(criterion.getId());
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#hasSelectionCriterion()
     */
    public boolean hasSelectionCriterion() {
        return (getSelectionCriterion() != null);
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getSelectionCriterion()
     */
    public AspectValue getSelectionCriterion() {
        return criterion;
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#setSelectionCriterion(org.xbrlapi.aspects.AspectValue)
     */
    public void setSelectionCriterion(AspectValue criterion) {
        this.criterion = criterion;
    }
    
    /**
     * @see Aspect#clearFacts()
     */
    public void clearFacts() throws XBRLException {
        this.facts.clear();
        this.values.clear();
        this.getTransformer().clearIdentifiers();
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#getChildren(AspectValue)
     */
    public List<AspectValue> getChildren(AspectValue parent)
            throws XBRLException {
        return new Vector<AspectValue>();
    }    

}
