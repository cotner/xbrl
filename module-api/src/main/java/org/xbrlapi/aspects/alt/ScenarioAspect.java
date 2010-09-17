package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.Context;
import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.Scenario;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Scenario aspect details</h2>
 * 
 * <p>
 * Scenario aspects capture information in the context scenario.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ScenarioAspect extends AspectImpl implements Aspect {
    
    /**
     * 
     */
    private static final long serialVersionUID = 7357414426851438249L;

    private static final Logger logger = Logger.getLogger(ScenarioAspect.class);

    /**
     * The URI uniquely identifying this concept aspect.
     */
    public static URI ID = URI.create("http://xbrlapi.org/aspect/scenario/1.0");
    
    /**
     * @see Aspect#getId()
     */
    public URI getId() {
        return ID;
    }
    
    
    /**
     * @param domain The domain for this aspect.
     * @throws XBRLException
     */
    public ScenarioAspect(Domain domain) throws XBRLException {
        super(domain);
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    public ScenarioAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isTuple()) return getMissingValue();
        if (fact.isNil()) return getMissingValue();
        return getValue(((Item) fact).getContext());
    }
    
    /**
     * @see Aspect#getValue(Context)
     */
    public ScenarioAspectValue getValue(Context context) throws XBRLException {
        return getValue(context.getScenario());
    }    
    
    /**
     * @param scenario The context scenario
     * @return the scenario aspect value.
     * @throws XBRLException
     */
    public ScenarioAspectValue getValue(Scenario scenario) throws XBRLException {
        if (scenario == null) return getMissingValue();
        return new ScenarioAspectValue(scenario);
    }    
    
    /**
     * @see Aspect#getMissingValue()
     */
    public ScenarioAspectValue getMissingValue() {
        return new ScenarioAspectValue();
    }

}
