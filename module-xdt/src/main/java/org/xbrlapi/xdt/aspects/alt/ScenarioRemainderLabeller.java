package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;

import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.Labeller;
import org.xbrlapi.aspects.alt.LabellerImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * <p>
 * A labeller for the segment remainder aspect.  This labeller ignores 
 * link role and resource role values.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ScenarioRemainderLabeller extends LabellerImpl implements Labeller {

    /**
     * 
     */
    private static final long serialVersionUID = -3690456459667430825L;

    /**
     * @param aspect The aspect to be a labeller for.
     */
    public ScenarioRemainderLabeller(Aspect aspect) throws XBRLException {
        super(aspect);
        if (! aspect.getId().equals(ScenarioRemainderAspect.ID)) throw new XBRLException("This labeller only works for the aspect: " + ScenarioRemainderAspect.ID);
    }

    /**
     * @see Labeller#getAspectLabel(String, URI, URI)
     */
    @Override
    public String getAspectLabel(String locale, URI resourceRole, URI linkRole) {
        return "scenario";
    }
}
