package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.List;

import org.xbrlapi.Concept;
import org.xbrlapi.LabelResource;
import org.xbrlapi.utilities.XBRLException;

/**
 * <p>
 * A concept aspect labeller that uses a caching system
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ConceptCachingLabeller extends LabellerImpl implements Labeller {

    /**
     * 
     */
    private static final long serialVersionUID = -2179477289946077839L;

    /**
     * @param aspect The aspect to be a labeller for.
     */
    public ConceptCachingLabeller(Aspect aspect) throws XBRLException {
        super(aspect);
        if (! aspect.getId().equals(ConceptAspect.ID)) throw new XBRLException("This labeller only works for the aspect: " + ConceptAspect.ID);
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
            ConceptAspectValue v = (ConceptAspectValue) value;
            Concept concept = getStore().getConcept(v.getNamespace(),v.getLocalname());
            List<LabelResource> labels = concept.getLabelsWithLanguageAndResourceRoleAndLinkRole(locale,resourceRole, linkRole);
            if (labels.isEmpty()) super.getAspectValueLabel(value,locale,resourceRole,linkRole);
            return labels.get(0).getStringValue();
        } catch (Throwable e) {
            return super.getAspectValueLabel(value,locale,resourceRole,linkRole);
        }
        
    }

}
