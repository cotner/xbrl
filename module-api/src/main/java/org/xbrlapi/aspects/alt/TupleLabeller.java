package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * <p>
 * A labeller for the tuple aspect.  This labeller ignores 
 * link role and resource role values.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class TupleLabeller extends LabellerImpl implements Labeller {

    /**
     * 
     */
    private static final long serialVersionUID = 3161673708249240447L;

    /**
     * @param aspect The aspect to be a labeller for.
     */
    public TupleLabeller(Aspect aspect) throws XBRLException {
        super(aspect);
        if (! aspect.getId().equals(TupleAspect.ID)) throw new XBRLException("This labeller only works for the aspect: " + TupleAspect.ID);
    }

    /**
     * @see Labeller#getAspectLabel(String, URI, URI)
     */
    @Override
    public String getAspectLabel(String locale, URI resourceRole, URI linkRole) {
        return "fact type";
    }
    
}
