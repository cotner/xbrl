package org.xbrlapi.aspects;

import org.xbrlapi.Entity;
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
}
