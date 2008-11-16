package org.xbrlapi.aspects;

import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.Scenario;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ScenarioAspect extends BaseAspect implements Aspect {

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public ScenarioAspect(AspectModel aspectModel) throws XBRLException {
        super();
        setAspectModel(aspectModel);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return Aspect.SCENARIO;
    }

    private class Transformer implements AspectValueTransformer {
        public Transformer() {
            super();
        }
        public String transform(AspectValue value) throws XBRLException {
            if (! value.getFragment().isa("org.xbrlapi.impl.ScenarioImpl")) {
                throw new XBRLException("The fragment is not the correct fragment type.");
            }
            Scenario f = ((Scenario) value.getFragment());
            return f.getStore().serializeToString(f.getDataRootElement());
        }
    }    
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public ScenarioAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isTuple()) {
            return null;
        }
        Item item = (Item) fact;
        Scenario scenario = item.getContext().getScenario();
        if (scenario == null) {
            return null;
        }
        return new ScenarioAspectValue(this,scenario);
    }        
}
