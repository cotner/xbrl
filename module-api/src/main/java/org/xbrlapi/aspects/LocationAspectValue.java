package org.xbrlapi.aspects;

import java.util.List;

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
     * @see AspectValue#getChildren(AspectValue)
     * @see BaseAspectValue#getChildren(AspectValue)
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
