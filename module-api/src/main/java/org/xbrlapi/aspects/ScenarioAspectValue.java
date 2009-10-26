package org.xbrlapi.aspects;

import org.xbrlapi.Scenario;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ScenarioAspectValue extends BaseAspectValue {

    public ScenarioAspectValue(Aspect aspect, Scenario scenario)
            throws XBRLException {
        super(aspect, scenario);
    }

}
