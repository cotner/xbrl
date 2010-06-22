package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

public class FilterImpl implements Filter {

    /**
     * 
     */
    private static final long serialVersionUID = -2594177408479622078L;
    
    /**
     * The map of selection criteria.
     */
    Map<URI,AspectValue> map = new HashMap<URI,AspectValue>();
    
    /**
     * @see Filter#addCriterion(AspectValue)
     */
    public void addCriterion(AspectValue criterion) throws XBRLException {
        if (criterion == null) throw new XBRLException("The criterion must not be null.");
        map.put(criterion.getAspectId(),criterion);
    }

    /**
     * @see Filter#getMatchingFacts(FactSet)
     */
    public Set<Fact> getMatchingFacts(FactSet candidateFacts) {
        Set<Fact> matches = new HashSet<Fact>();
        for (AspectValue criterion: map.values()) {
            if (matches.isEmpty()) {
                matches.addAll(candidateFacts.getFacts(criterion));
            } else {
                Collection<Fact> candidates = candidateFacts.getFacts(criterion);
                matches.retainAll(candidates);
            }
        }
        return matches;
    }

    /**
     * @see Filter#filtersOn(URI)
     */
    public boolean filtersOn(URI aspectId) {
        return map.containsKey(aspectId);
    }

    /**
     * @see Filter#removeAllCriteria()
     */
    public void removeAllCriteria() {
        // TODO Auto-generated method stub
        
    }

    /**
     * @see Filter#removeCriterion(URI)
     */
    public void removeCriterion(URI aspectId) {
        if (this.map.containsKey(aspectId)) map.remove(aspectId);
    }

    
    
}
