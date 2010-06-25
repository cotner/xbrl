package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.Scenario;
import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.AspectImpl;
import org.xbrlapi.aspects.alt.Domain;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Scenario remainder aspect details</h2>
 * 
 * <p>
 * The value is based on the non-XDT content of 
 * the context scenario.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ScenarioRemainderAspect extends AspectImpl implements Aspect {

    /**
     * 
     */
    private static final long serialVersionUID = -6218851432259892401L;

    private static final Logger logger = Logger.getLogger(ScenarioRemainderAspect.class);

    /**
     * The URI uniquely identifying this concept aspect.
     */
    public static URI ID = URI.create("http://xbrlapi.org/aspect/scenario-remainder/1.0");
    
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
    public ScenarioRemainderAspect(Domain domain) throws XBRLException {
        super(domain);
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    public ScenarioRemainderAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isTuple()) return getMissingValue();
        if (fact.isNil()) return getMissingValue();
        Item item = (Item) fact;
        Scenario scenario = item.getContext().getScenario();
        if (scenario == null) return getMissingValue();
        return new ScenarioRemainderAspectValue(scenario);
    }

    /**
     * @see Aspect#getMissingValue()
     */
    public ScenarioRemainderAspectValue getMissingValue() {
        return new ScenarioRemainderAspectValue();
    }

}
