package org.xbrlapi.aspects;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

/**
 * Abstract implementation of common aspect model methods.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
abstract public class BaseAspectModel implements AspectModel {

    private final static Logger logger = Logger.getLogger(BaseAspectModel.class);  

    /**
     * From aspect type to aspect.
     */
    private Map<String,Aspect> aspects;
    /**
     * From dimension name to list of aspects in dimension.
     */
    private Map<String,List<Aspect>> axes;
    
    private Set<Fact> facts;
    
    public BaseAspectModel() {
        super();
        aspects = new HashMap<String,Aspect>();
        axes = new HashMap<String,List<Aspect>>();
        facts = new HashSet<Fact>();
        
    }

    public int getFactCount() {
        return facts.size();
    }
    
    /**
     * @see AspectModel#getAspects()
     */
    public Collection<Aspect> getAspects() {
        return aspects.values();
    }
    
    /**
     * @see AspectModel#getAspect(String)
     */
    public Aspect getAspect(String type) throws XBRLException {
        if (hasAspect(type)) {
            return aspects.get(type);
        }
        throw new XBRLException("The aspect model does not include aspect " + type);
    }
    
    /**
     * @see AspectModel#hasAspect(String)
     */
    public boolean hasAspect(String type) {
        return aspects.containsKey(type);
    }

    /**
     * @see AspectModel#getOrphanAspects()
     */
    public Collection<Aspect> getOrphanAspects() {
        Collection<Aspect> aspects = this.aspects.values();
        for (Aspect aspect: aspects) {
            if (aspect.getAxis() != null) aspects.remove(aspect);
        }
        return aspects;
    }

    /**
     * @see AspectModel#getAxisAspects(String)
     */
    public List<Aspect> getAxisAspects(String axis) {
        if (axes.containsKey(axis)) return axes.get(axis);
        return new Vector<Aspect>();
    }

    /**
     * @throws XBRLException 
     * @see AspectModel#setAspect(Aspect)
     */
    public void setAspect(Aspect aspect) throws XBRLException {
        aspect.setAspectModel(this);
        if (aspects.containsKey(aspect.getType())) {
            Aspect old = aspects.get(aspect.getType());
            if (old.getAxis() != null) {
                List<Aspect> dimensionAspects = axes.get(old.getAxis());
                LOOP: for (Aspect dimensionAspect: dimensionAspects) {
                    if (dimensionAspect.getType().equals(old.getType())) {
                        dimensionAspects.remove(dimensionAspect);
                        break LOOP;
                    }
                }
            }
        }
        aspects.put(aspect.getType(),aspect);
        aspect.setAxis(null);
    }
    
    /**
     * @see AspectModel#arrangeAspect(String, String)
     */
    public void arrangeAspect(String aspectType, String axis) throws XBRLException {

        if (! aspects.containsKey(aspectType)) throw new XBRLException("The aspect is not part of the aspect model.");
        Aspect aspect = aspects.get(aspectType);
        
        if (! aspect.isOrphan()) {
           List<Aspect> dimensionAspects = axes.get(aspect.getAxis());
           dimensionAspects.remove(aspect);
        }
       
        List<Aspect> dimensionAspects;
        if (axes.containsKey(axis)) {
            dimensionAspects = this.axes.get(axis);
        } else {
            dimensionAspects = new Vector<Aspect>();
            axes.put(axis,dimensionAspects);
        }
        dimensionAspects.add(aspect);

        aspect.setAxis(axis);
    }
    
    /**
     * @see AspectModel#arrangeAspect(String, String, String)
     */
    public void arrangeAspect(String aspectType, String dimension, String parentType) throws XBRLException {
        if (! aspects.containsKey(aspectType)) throw new XBRLException("The aspect is not part of the aspect model.");
        if (! aspects.containsKey(parentType)) throw new XBRLException("The parent aspect is not part of the aspect model.");
        if (! this.axes.containsKey(dimension)) throw new XBRLException("The dimension is not part of the aspect model.");
        
        Aspect aspect = aspects.get(aspectType);
        if (aspect.getAxis() != null) {
            List<Aspect> dimensionAspects = axes.get(aspect.getAxis());
            dimensionAspects.remove(aspect);
        }
        
        List<Aspect> dimensionAspects = this.axes.get(dimension);
        FINDPARENT: for (Aspect dimensionAspect: dimensionAspects) {
            if (dimensionAspect.getType().equals(parentType)) {
                int index = dimensionAspects.indexOf(dimensionAspect)+1;
                dimensionAspects.add(index, aspect);
                break FINDPARENT;
            }
        }

        aspect.setAxis(dimension);
        
    }

    /**
     * @param fact The fact to add to the aspect model
     * @throws XBRLException if the fact cannot be added to the aspect model.
     */
    public void addFact(Fact fact) throws XBRLException {
        facts.add(fact);
        Collection<Aspect> aspects = this.getAspects();
        for (Aspect aspect: aspects) {
            aspect.addFact(fact);
        }
    }
    
    /**
     * @see AspectModel#addFacts(Collection<Fact>)
     */
    public <F extends Fact> void addFacts(Collection<F> facts) throws XBRLException {
        for (F fact: facts) {
            this.addFact(fact);
        }
    }
    
    /**
     * @see AspectModel#getAspectValues(Fact)
     */
    public List<AspectValue> getAspectValues(Fact fact) throws XBRLException {
        List<AspectValue> result = new Vector<AspectValue>();
        for (Aspect aspect: this.getAspects()) {
            result.add(aspect.getValue(fact));
        }
        return result;
    }
 

    public Set<Fact> getFacts(Collection<AspectValue> values) throws XBRLException {

        if (values == null) throw new XBRLException("The list of aspect values must not be null.");
        
        if (values.isEmpty()) return getAllFacts();
        
        Set<Fact> matches = null;
        for (AspectValue value: values) {
            if (matches == null) {
                matches = value.getAspect().getFacts(value);
            } else {
                Set<Fact> candidates = value.getAspect().getFacts(value);
                matches.retainAll(candidates);
            }
        }
        return matches;
    }
    
    public Set<Fact> getAllFacts() throws XBRLException {
        return facts;
    }
    

    
    /**
     * @see AspectModel#getMatchingFacts()
     */
    public Set<Fact> getMatchingFacts() throws XBRLException {
        logger.debug("Getting a new set of matching facts: ");
        Set<Fact> matches = new HashSet<Fact>();
        boolean gotSomeMatches = false;
        for (Aspect aspect: getAspects()) {
            if (aspect.hasSelectionCriterion()) {
                logger.debug("Filtering based on " + aspect.getType());
                if (! gotSomeMatches) {
                    matches.addAll(aspect.getMatchingFacts());
                    gotSomeMatches = true;
                } else {
                    Set<Fact> candidates = aspect.getMatchingFacts();
                    matches.retainAll(candidates);
                }
            }
            logger.debug("# matching facts = " + matches.size());
        }
        return matches;
    }
    
    /**
     * @see AspectModel#setCriterion(AspectValue)
     */
    public void setCriterion(AspectValue criterion) throws XBRLException {
        Aspect aspect = this.getAspect(criterion.getAspect().getType());
        aspect.setSelectionCriterion(criterion);
    }
    
    /**
     * @see AspectModel#setCriteria(Collection)
     */
    public void setCriteria(Collection<AspectValue> criteria) throws XBRLException {
        for (AspectValue criterion: criteria) {
            setCriterion(criterion);
        }
    }    

    /**
     * @see AspectModel#clearAllCriteria()
     */
    public void clearAllCriteria() {
        for (Aspect aspect: getAspects()) {
            aspect.clearSelectionCriterion();
        }
    }
    
    /**
     * @see AspectModel#getAspectValueCombinationsForAxis(String)
     */
    public List<List<AspectValue>> getAspectValueCombinationsForAxis(String dimension) throws XBRLException {
        
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
    }
    
    /**
     * @see AspectModel#getMinimalAspectValueCombinationsForAxis(String)
     */
    public List<List<AspectValue>> getMinimalAspectValueCombinationsForAxis(String axis) throws XBRLException {

        List<List<AspectValue>> result = new Vector<List<AspectValue>>();

        List<Aspect> aspects = getAxisAspects(axis);
        if (aspects.size() == 0) return result;
        
        Aspect firstAspect = aspects.get(0);
        int combinations = firstAspect.getValues().size() * firstAspect.getDescendantCount();

        logger.debug("#combinations = " + combinations);
        
        for (int i=0; i<combinations; i++) {
            result.add(new Vector<AspectValue>());
        }
        
        for (Aspect aspect: aspects) {
            List<AspectValue> values = aspect.getValuesByHierarchy();
            int vCount = aspect.size();
            int dCount = aspect.getDescendantCount();
            int aCount = aspect.getAncestorCount();
            logger.debug("vcount= " + vCount);
            logger.debug("acount= " + aCount);
            logger.debug("dcount= " + dCount);
            for (int a_i=0; a_i<aCount; a_i++) {
                for (int d_i=0; d_i<dCount; d_i++) {
                    for (int v_i=0; v_i<vCount; v_i++) {
                        int index = dCount*vCount*a_i + dCount*v_i + d_i;
                        logger.debug("index= " + index);
                        values.get(v_i);
                        result.get(index);
                        result.get(index).add(values.get(v_i));
                    }
                }
            }
        }
        
        List<List<AspectValue>> finalResult = new Vector<List<AspectValue>>();
        this.clearAllCriteria();
        for (List<AspectValue> combination: result) {
            this.setCriteria(combination);
            if (this.getMatchingFacts().size() > 0) {
                finalResult.add(combination);
            }
        }
        
        return finalResult;
    }
    
    /**
     * @see AspectModel#analyseAspectCombinationMatches(List, String)
     */
    public List<Integer> analyseAspectCombinationMatches(List<List<AspectValue>> combinations,String aspectType) throws XBRLException {
        
        int i = 0;
        while (! combinations.get(0).get(i).getAspect().getType().equals(aspectType)) {
            i++;
        }

        // Initialise the result using the higher ranked aspects for the axis
        if (i > 0) {
            List<Integer> result = new Vector<Integer>();
            List<Integer> baseResult = analyseAspectCombinationMatches(combinations,combinations.get(0).get(i-1).getAspect().getType());
            int startIndex = 0;
            for (Integer integer: baseResult) {
                int endIndex = startIndex + integer.intValue();
                AspectValue current = combinations.get(startIndex).get(i);
                int matches = 1;
                for (int j=startIndex+1; j<endIndex; j++) {
                    AspectValue previous = current;
                    current = combinations.get(j).get(i);
                    if (current.getIdentifier().equals(previous.getIdentifier())) {
                        matches++;
                    } else {
                        result.add(new Integer(matches));
                        matches = 1;
                    }
                }
                result.add(new Integer(matches));
                startIndex = endIndex;
            }
            return result;
        }
        
        AspectValue current = combinations.get(0).get(i);
        List<Integer> result = new Vector<Integer>();
        int matches = 1;
        for (int j=1; j<combinations.size(); j++) {
            AspectValue previous = current;
            current = combinations.get(j).get(i);
            if (current.getIdentifier().equals(previous.getIdentifier())) {
                matches++;
            } else {
                result.add(new Integer(matches));
                matches = 1;
            }
        }
        result.add(new Integer(matches));
        return result;

    }
 
    /**
     * @see AspectModel#deleteAspect(String)
     */
    public void deleteAspect(String type) throws XBRLException {
        if (! hasAspect(type)) return;
        
        Aspect aspect = this.getAspect(type);
        List<Aspect> dimensionAspects = this.getAxisAspects(aspect.getAxis());
        dimensionAspects.remove(aspect);
        aspects.remove(aspect.getType());

    }
 
    /**
     * @see AspectModel#clearFacts()
     */
    public void clearFacts() throws XBRLException {
        for (Aspect aspect: this.getAspects()) {
            aspect.clearFacts();
        }
        this.facts.clear();
    }
 
    
    /**
     * Handles object serialization
     * @param out The input object stream used to store the serialization of the object.
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(aspects);
        out.writeObject(axes);
        out.writeObject(facts);
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
        aspects = (Map<String,Aspect>) in.readObject();
        axes = (Map<String,List<Aspect>>) in.readObject();
        facts = (Set<Fact>) in.readObject();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((aspects == null) ? 0 : aspects.hashCode());
        result = prime * result + ((axes == null) ? 0 : axes.hashCode());
        result = prime * result + ((facts == null) ? 0 : facts.hashCode());
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
        BaseAspectModel other = (BaseAspectModel) obj;
        
        if (aspects == null) {
            if (other.aspects != null) {
                return false;
            }
        } else if (!aspects.equals(other.aspects)) {
            return false;
        }

        if (axes == null) {
            if (other.axes != null)
                return false;
        } else if (!axes.equals(other.axes))
            return false;

        if (facts == null) {
            if (other.facts != null)
                return false;
        } else if (!facts.equals(other.facts))
            return false;
    
        return true;
    }

}
