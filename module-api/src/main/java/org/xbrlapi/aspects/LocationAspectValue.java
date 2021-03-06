package org.xbrlapi.aspects;

import java.util.List;
import java.util.Set;

import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Tuple;
import org.xbrlapi.impl.InstanceImpl;
import org.xbrlapi.impl.TupleImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class LocationAspectValue extends BaseAspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = 4216609408194340807L;

    public LocationAspectValue(Aspect aspect, Fact fact)
            throws XBRLException {
        super(aspect, fact);
    }
    
    /**
     * @see AspectValue#getParent()
     * @see BaseAspectValue#getParent()
     */
    @Override
    public AspectValue getParent() throws XBRLException {
        Fact fact = this.<Fact>getFragment();
        Fragment parent = fact.getParent();
        if (parent.isa(InstanceImpl.class)) {
            return null;
        }
        return this.getAspect().getValue((Fact) parent);
    }
    
    /**
     * @see AspectValue#hasParent()
     * @see BaseAspectValue#hasParent()
     */
    @Override
    public boolean hasParent() throws XBRLException {
        Fact fact = this.<Fact>getFragment();
        if (fact.getParent().isa(InstanceImpl.class)) return false;
        
        Aspect aspect = this.getAspect();
        Set<Fact> facts = aspect.getAllFacts();
        if (facts.contains(this.getFragment().getParent())) return true;
        
        return false;
    }    
    
    
    
    /**
     * @see AspectValue#getChildren()
     * @see BaseAspectValue#getChildren()
     */
    @Override
    public List<AspectValue> getChildren() throws XBRLException {
        List<AspectValue> result = super.getChildren();
        
        Fact fact = this.<Fact>getFragment();
        if (fact.isa(TupleImpl.class)) {
            List<Fact> children = ((Tuple) fact).getChildFacts();
            for (Fact child: children) {
                result.add(this.getAspect().getValue(child));
            }
        }
        
        return result;
    }    

}
