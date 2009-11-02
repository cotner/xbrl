package org.xbrlapi.aspects;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.XBRLException;

/**
 * Abstract implementation of common aspect methods.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
abstract public class BaseAspect implements Aspect {

    private final static Logger logger = Logger.getLogger(BaseAspect.class);  
    
    protected TreeMap<String,AspectValue> values;
    
    /**
     * The aspect model is non-transient but it is not considered as 
     * part of assessing equality or determining the hashCode for the aspect.
     */
    private AspectModel model;

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public BaseAspect(AspectModel aspectModel) throws XBRLException {
        super();
        if (aspectModel == null) throw new XBRLException("The supplied aspect model is null.");
        this.model = aspectModel;
        facts = new HashMap<String,Set<Fact>>(); 
        fragmentMap = new HashMap<String,Fragment>();
        values = new TreeMap<String,AspectValue>();        
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getAspectModel()
     */
    public AspectModel getAspectModel() {
        return model;
    }

    /**
     * The identifier for the axis that this aspect is on.
     */
    private String axis;
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getAxis()
     */
    public String getAxis() {
        return axis;
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#isOrphan()
     */
    public boolean isOrphan() {
        return (this.getAxis() == null);
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#size()
     */
    public int size() {
        return this.values.size();
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getIterator()
     */
    public Iterator<AspectValue> getIterator() {
        return values.values().iterator();
    }
    
    /**
     * @throws XBRLException 
     * @see org.xbrlapi.aspects.Aspect#getDescendantCount()
     */
    public int getDescendantCount() throws XBRLException {
        if (this.isOrphan()) return 1;
        List<Aspect> aspects = null;
        try {
            aspects = this.getAspectModel().getAxisAspects(this.getAxis());
        } catch (XBRLException e) {
            ;// Cannot be thrown
        }
        int count = 0;
        for (Aspect aspect: aspects) {
            if (aspect.getType().equals(this.getType())) count = 1;
            else if (! aspect.isEmpty()) count = count * aspect.size();
        }
        return count;
    }
    
    /**
     * @throws XBRLException 
     * @see org.xbrlapi.aspects.Aspect#getAncestorCount()
     */
    public int getAncestorCount() throws XBRLException {
        if (this.isOrphan()) return 1;
        List<Aspect> aspects = null;
        try {
            aspects = this.getAspectModel().getAxisAspects(this.getAxis());
        } catch (XBRLException e) {
            ;//Cannot be thrown
        }
        int count = 1;
        for (Aspect aspect: aspects) {
            if (aspect.getType().equals(this.getType())) return count;
            if (! aspect.isEmpty()) count = count * aspect.size();
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
     * @see org.xbrlapi.aspects.Aspect#getValuesByHierarchy()
     */
    public List<AspectValue> getValuesByHierarchy() throws XBRLException {
        List<AspectValue> result = new Vector<AspectValue>();
        List<AspectValue> roots = new Vector<AspectValue>(); 
        for (AspectValue value: values.values()) {
            if (! value.hasParent()) {
                roots.add(value);
            }
        }
        add(roots,result);
        return result;
    }
    
    private void add(List<AspectValue> parents, List<AspectValue> result) throws XBRLException {
        for (AspectValue parent: parents) {
            result.add(parent);
            List<AspectValue> children = parent.getChildren();
            if (children.size() > 0) add(children, result);
        }
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
        return (this.size() == 1);
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#isMissing()
     */
    public boolean isMissing() throws XBRLException {
        if (! isSingular()) return false;
        AspectValue value = getValues().get(0);
        if (value.<Fragment>getFragment() == null) return true;
        return false;
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
     * @see org.xbrlapi.aspects.Aspect#setAxis(java.lang.String)
     */
    public void setAxis(String dimension) {
        this.axis = dimension;
    }
    
    transient private AspectValueTransformer transformer;

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
    
    private Map<String,Set<Fact>> facts;
    
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
        if (value.isMissing()) return;
        
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
    

    private Map<String,Fragment> fragmentMap;
    /**
     * @see Aspect#getFragment(Fact)
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> F getFragment(Fact fact) throws XBRLException {
        try {
            String key = getKey(fact);
            if (fragmentMap.containsKey(key)) return (F) fragmentMap.get(key);
            F fragment = this.<F>getFragmentFromStore(fact);
            if (fragment == null) return null;
            fragmentMap.put(key,fragment);
            return fragment;
        } catch (ClassCastException e) {
            throw new XBRLException("The fragment is not of the specified type.",e);
        }
    }

    private AspectValue criterion;
    
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
        return this.facts.get(criterion.getIdentifier());
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
     * This basic implementation has a unique key for each fact
     * This is not efficient from a memory footprint perspective
     * and should be overridden where feasible.
     * @see Aspect#getKey(Fact)
     */
    public String getKey(Fact fact) throws XBRLException {
        return fact.getIndex();
    }
 
    /**
     * Convenience method to enable a list of elements
     * to be converted into something a bit more readable for
     * display purposes.
     * @param children The list of elements.
     * @return the label.
     */
    protected String getLabelFromElements(List<Element> children) {
        String label = "";
        for (int i=0; i<children.size(); i++) {
            if (i>0) {
                label += " ";
            }
            label += getLabelFromElement(children.get(i));
        }
        return label;
    }
    
    /**
     * Convenience method to return a label generated
     * from a single element.
     * @param child The single element.
     * @return the label.
     */
    protected String getLabelFromElement(Element child) {
        String label = child.getLocalName();
        String text = child.getTextContent();
        NamedNodeMap attrs = child.getAttributes();
        if (attrs.getLength() > 0) {
            label += "(";
            for (int i=0; i<attrs.getLength(); i++) {
                Attr attr = (Attr) attrs.item(i);
                if (i!=0) label += ","; 
                label += attr.getName() + "=" + attr.getValue();
            }
            label += ")";
        }
        if (! text.trim().equals("")) {
            label += "=" + text;
        }
        return label;
    }    

    /**
     * Handles object serialization
     * @param out The input object stream used to store the serialization of the object.
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(model);
        out.writeObject(criterion);
        out.writeObject(axis);
        out.writeObject(facts);
        out.writeObject(fragmentMap);
        out.writeObject(values);
   }
    
    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject( );
        model = (AspectModel) in.readObject();
        criterion = (AspectValue) in.readObject();
        axis = (String) in.readObject();
        facts = (Map<String,Set<Fact>>) in.readObject();
        fragmentMap = (Map<String,Fragment>) in.readObject();
        values = (TreeMap<String,AspectValue>) in.readObject();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((axis == null) ? 0 : axis.hashCode());
        result = prime * result
                + ((criterion == null) ? 0 : criterion.hashCode());
        result = prime * result + ((facts == null) ? 0 : facts.hashCode());
        result = prime * result
                + ((fragmentMap == null) ? 0 : fragmentMap.hashCode());
        result = prime * result + ((values == null) ? 0 : values.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseAspect other = (BaseAspect) obj;
        if (axis == null) {
            if (other.axis != null)
                return false;
        } else if (!axis.equals(other.axis)) {
            return false;
        }
        if (criterion == null) {
            if (other.criterion != null)
                return false;
        } else if (!criterion.equals(other.criterion)) {
            return false;
        }
        if (facts == null) {
            if (other.facts != null)
                return false;
        } else if (!facts.equals(other.facts)) {
            return false;
        }
        if (fragmentMap == null) {
            if (other.fragmentMap != null)
                return false;
        } else if (!fragmentMap.equals(other.fragmentMap))  {
            return false;
        }
        if (values == null) {
            if (other.values != null)
                return false;
        } else if (!values.equals(other.values)) {
            return false;
        }
        return true;
    }    

}
