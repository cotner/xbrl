package org.xbrlapi.aspects;

import org.xbrlapi.Context;
import org.xbrlapi.Entity;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class EntityIdentifierAspect extends ContextAspect implements Aspect {

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public EntityIdentifierAspect(AspectModel aspectModel) throws XBRLException {
        setAspectModel(aspectModel);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return Aspect.ENTITY_IDENTIFIER;
    }

    private class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {
        public Transformer() {
            super();
        }
        public String transform(AspectValue value) throws XBRLException {
            if (! value.getFragment().isa("org.xbrlapi.impl.EntityImpl")) {
                throw new XBRLException("The fragment is not the correct fragment type.");
            }
            if (hasTransform(value)) {
                return getTransform(value);
            }
            Entity f = ((Entity) value.getFragment());
            String result = f.getIdentifierScheme() + ": " + f.getIdentifierValue();
            setTransform(value,result);
            return result;
        }
    }
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public EntityIdentifierAspectValue getValue(Fact fact) throws XBRLException {
        try {
            return new EntityIdentifierAspectValue(this,getFragment(fact));
        } catch (XBRLException e) {
            return null;
        }
    }
    
    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        return ((Context) super.getFragmentFromStore(fact)).getEntity();
    }
    
}
