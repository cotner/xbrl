package org.xbrlapi.aspects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.XBRLException;

/**
 * Abstract implementation of common aspect methods.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
abstract public class BaseAspect implements Aspect {

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
     * @see org.xbrlapi.aspects.Aspect#getLength()
     */
    public int getLength() {
        // TODO Auto-generated method stub
        return 0;
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
     * @see org.xbrlapi.aspects.Aspect#addValue()
     */
    public void addValue(AspectValue value) throws XBRLException {
        String key = this.getTransformer().transform(value);
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
     * @see Aspect#hasValue(AspectValue)
     */
    public boolean hasValue(AspectValue value) throws XBRLException {
        return values.containsKey(getTransformer().transform(value));
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
        String key = getTransformer().transform(value);
        if (! facts.containsKey(key)) return new HashSet<Fact>();
        return facts.get(key);
    }

    /**
     * @see Aspect#addFact(Fact)
     */
    public void addFact(Fact fact) throws XBRLException {
        AspectValue value = getValue(fact);
        this.addValue(value);
        AspectValueTransformer transformer = this.getTransformer();
        String key = transformer.transform(value);
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
    
    
    
}
