package org.xbrlapi.aspects;

import org.xbrlapi.Scenario;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ScenarioAspectValue extends BaseAspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = -130331814239585381L;

    public ScenarioAspectValue(Aspect aspect, Scenario scenario)
            throws XBRLException {
        super(aspect, scenario);
    }

}
