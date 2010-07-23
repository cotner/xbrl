package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * <p>
 * A labeller for the period aspect.  This labeller ignores 
 * link role and resource role values.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class PeriodLabeller extends LabellerImpl implements Labeller {

    /**
     * 
     */
    private static final long serialVersionUID = 3865956219152605139L;

    /**
     * @param aspect The aspect to be a labeller for.
     */
    public PeriodLabeller(Aspect aspect) throws XBRLException {
        super(aspect);
        if (! aspect.getId().equals(PeriodAspect.ID)) throw new XBRLException("This labeller only works for the aspect: " + PeriodAspect.ID);
    }

    /**
     * @see Labeller#getAspectLabel(String, URI, URI)
     */
    @Override
    public String getAspectLabel(String locale, URI resourceRole, URI linkRole) {
        return "period";
    }    
    
    /**
     * @see Labeller#getAspectValueLabel(AspectValue, String, URI, URI)
     */
    public String getAspectValueLabel(AspectValue value, String locale, URI resourceRole, URI linkRole) {
        
        try {
            PeriodAspectValue v = (PeriodAspectValue) value;
            if (v.isForever()) return "forever";
            
            String result = "";
            if (v.isFiniteDuration()) {
                result = v.getStart().toXMLFormat() + " - " + v.getEnd().toXMLFormat();
            } else {
                result = v.getInstant().toXMLFormat();
            }
            return result.replaceAll("T00:00:00","");

        } catch (Throwable e) {
            return super.getAspectValueLabel(value,locale,resourceRole,linkRole);
        }
        
    }

}
