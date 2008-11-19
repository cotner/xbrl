package org.xbrlapi.aspects;

import org.xbrlapi.Context;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Scenario;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ScenarioAspect extends ContextAspect implements Aspect {

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public ScenarioAspect(AspectModel aspectModel) throws XBRLException {
        setAspectModel(aspectModel);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return Aspect.SCENARIO;
    }

    private class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {
        public Transformer() {
            super();
        }
        
        /**
         * @see AspectValueTransformer#validate(AspectValue)
         */
        public void validate(AspectValue value) throws XBRLException {
            super.validate(value);
            if (! value.getFragment().isa("org.xbrlapi.impl.ScenarioImpl")) {
                throw new XBRLException("The aspect value must have a scenario fragment.");
            }
        }

        /**
         * @see AspectValueTransformer#getIdentifier(AspectValue)
         */
        public String getIdentifier(AspectValue value) throws XBRLException {
            validate(value);
            if (hasMapId(value)) {
                return getMapId(value);
            }
            Scenario f = ((Scenario) value.getFragment());
            String id = f.getStore().serializeToString(f.getDataRootElement());
            setMapId(value,id);
            return id;
        }
        
        /**
         * @see AspectValueTransformer#getLabel(AspectValue)
         */
        public String getLabel(AspectValue value) throws XBRLException {
            return getIdentifier(value);
        }        

    }    
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public ScenarioAspectValue getValue(Fact fact) throws XBRLException {
        try {
            return new ScenarioAspectValue(this,getFragment(fact));
        } catch (XBRLException e) {
            return null;
        }
    }        
    
    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        Scenario scenario = ((Context) super.getFragmentFromStore(fact)).getScenario();
        if (scenario == null) throw new XBRLException("The scenario fragment is not available.");
        return scenario;
    }    
}
