package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * <p>
 * A labeller for the segment aspect.  This labeller ignores 
 * link role and resource role values.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class SegmentLabeller extends LabellerImpl implements Labeller {

    /**
     * 
     */
    private static final long serialVersionUID = -4659311007619482199L;

    /**
     * @param aspect The aspect to be a labeller for.
     */
    public SegmentLabeller(Aspect aspect) throws XBRLException {
        super(aspect);
        if (! aspect.getId().equals(SegmentAspect.ID)) throw new XBRLException("This labeller only works for the aspect: " + SegmentAspect.ID);
    }

    /**
     * @see Labeller#getAspectLabel(String, URI, URI)
     */
    @Override
    public String getAspectLabel(String locale, URI resourceRole, URI linkRole) {
        return "segment";
    }
}
