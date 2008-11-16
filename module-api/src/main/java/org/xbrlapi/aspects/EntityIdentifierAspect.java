package org.xbrlapi.aspects;

import org.xbrlapi.Entity;
import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class EntityIdentifierAspect extends BaseAspect implements Aspect {

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public EntityIdentifierAspect(AspectModel aspectModel) throws XBRLException {
        super();
        setAspectModel(aspectModel);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return Aspect.ENTITY_IDENTIFIER;
    }

    private class Transformer implements AspectValueTransformer {
        public Transformer() {
            super();
        }
        public String transform(AspectValue value) throws XBRLException {
            if (! value.getFragment().isa("org.xbrlapi.impl.EntityImpl")) {
                throw new XBRLException("The fragment is not the correct fragment type.");
            }
            Entity f = ((Entity) value.getFragment());
            return f.getIdentifierScheme() + ": " + f.getIdentifierValue();
        }
    }
    
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public EntityIdentifierAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isTuple()) {
            return null;
        }
        return new EntityIdentifierAspectValue(this,((Item) fact).getContext().getEntity());
    }    
}
