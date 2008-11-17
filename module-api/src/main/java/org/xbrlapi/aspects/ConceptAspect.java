package org.xbrlapi.aspects;

import java.util.HashMap;
import java.util.Map;

import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ConceptAspect extends BaseAspect implements Aspect {

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public ConceptAspect(AspectModel aspectModel) throws XBRLException {
        setAspectModel(aspectModel);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return Aspect.CONCEPT;
    }

    private class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {
        public Transformer() {
            super();
        }
        public String transform(AspectValue value) throws XBRLException {
            if (! value.getFragment().isa("org.xbrlapi.impl.ConceptImpl")) {
                throw new XBRLException("The fragment is not the correct fragment type.");
            }
            if (hasTransform(value)) {
                return getTransform(value);
            }
            Concept f = ((Concept) value.getFragment());
            String result = f.getTargetNamespaceURI() + ": " + f.getName();
            setTransform(value,result);
            return result;
        }
        
        private Map<AspectValue,String> map = new HashMap<AspectValue,String>();
    }

    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public ConceptAspectValue getValue(Fact fact) throws XBRLException {
        return new ConceptAspectValue(this,getFragment(fact));
    }
    
    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        return fact.getConcept();
    }
    
    /**
     * @see Aspect#getFragmentKey(Fact)
     */
    public String getFragmentKey(Fact fact) throws XBRLException {
        return fact.getNamespaceURI() + fact.getLocalname();
    }

}
