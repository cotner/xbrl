package org.xbrlapi.aspects;

import org.xbrlapi.Context;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Scenario;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ScenarioAspect extends BaseContextAspect implements Aspect {

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
        public String transform(AspectValue value) throws XBRLException {
            if (! value.getFragment().isa("org.xbrlapi.impl.ScenarioImpl")) {
                throw new XBRLException("The fragment is not the correct fragment type.");
            }
            Scenario f = ((Scenario) value.getFragment());
            String result = f.getStore().serializeToString(f.getDataRootElement());
            setTransform(value,result);
            return result;
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
        Scenario scenario = ((Context) super.getFragment(fact)).getScenario();
        if (scenario == null) throw new XBRLException("The scenario fragment is not available.");
        return scenario;
    }    
}
