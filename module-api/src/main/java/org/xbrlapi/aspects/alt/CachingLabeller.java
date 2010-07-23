package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * <p>
 * A concept aspect labeller that uses a caching system
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class CachingLabeller extends ConceptLabeller implements Labeller {
    
    /**
     * 
     */
    private static final long serialVersionUID = -1573186723917326226L;
    
    /**
     * The aspect value label caching system.
     */
    private LabelCache cache;
    
    /**
     * @param aspect The aspect to be a labeller for.
     */
    public CachingLabeller(Aspect aspect, LabelCache cache) throws XBRLException {
        super(aspect);
        if (cache == null) throw new XBRLException("The label cache must not be null.");
        this.cache = cache;
    }

    /**
     * @see Labeller#getAspectLabel(String, URI, URI)
     */
    @Override
    public String getAspectLabel(String locale, URI resourceRole, URI linkRole) {
        return "concept";
    }
    
    /**
     * @see Labeller#getAspectValueLabel(AspectValue, String, URI, URI)
     */
    @Override
    public String getAspectValueLabel(AspectValue value, String locale,
            URI resourceRole, URI linkRole) {
        
        try {
            String label = cache.getLabel(getAspect().getId(), value.getId(), locale, resourceRole, linkRole);
            if (label != null) return label;
            return super.getAspectValueLabel(value, locale, resourceRole, linkRole);
        } catch (Throwable e) {
            return super.getAspectValueLabel(value,locale,resourceRole,linkRole);
        }
        
    }

}
