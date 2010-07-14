package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;
import java.util.List;

import org.xbrlapi.LabelResource;
import org.xbrlapi.SchemaContent;
import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.AspectValue;
import org.xbrlapi.aspects.alt.Labeller;
import org.xbrlapi.aspects.alt.LabellerImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * <p>
 * A labeller for the explicit dimension aspect based upon XBRL labels and generic XBRL labels.
 * </p>
 * 
 * <p>
 * This labeller does not make use of label caching systems.  Instead labels are obtained from
 * the XLink relationships from explicit dimensions to XBRL 2.1 and generic labels.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ExplicitDimensionLabeller extends LabellerImpl implements Labeller {

    /**
     * 
     */
    private static final long serialVersionUID = -2179477289946077839L;

    /**
     * @param aspect The aspect to be a labeller for.
     */
    public ExplicitDimensionLabeller(Aspect aspect) throws XBRLException {
        super(aspect);
        try {
            @SuppressWarnings("unused")
            ExplicitDimensionAspect a = (ExplicitDimensionAspect) aspect;
        } catch (Throwable e) {
            throw new XBRLException("This labeller only works for explicit dimension aspects.");
        }
        
    }

    /**
     * @see Labeller#getAspectLabel(String, URI, URI)
     */
    @Override
    public String getAspectLabel(String locale, URI resourceRole, URI linkRole) {
        ExplicitDimensionAspect aspect = (ExplicitDimensionAspect) getAspect();
        try {
            SchemaContent sc = getStore().getSchemaContent(aspect.getDimensionNamespace(),aspect.getDimensionLocalname());
            List<LabelResource> labels = sc.getLabelsWithLanguageAndResourceRoleAndLinkRole(locale,resourceRole, linkRole);
            if (! labels.isEmpty()) return labels.get(0).getStringValue();
            return super.getAspectLabel(locale,resourceRole,linkRole);
        } catch (XBRLException e) {
            return super.getAspectLabel(locale,resourceRole,linkRole);
        }
    }
    
    /**
     * @see Labeller#getAspectValueLabel(AspectValue, String, URI, URI)
     */
    @Override
    public String getAspectValueLabel(AspectValue value, String locale,
            URI resourceRole, URI linkRole) {
        
        try {
            ExplicitDimensionAspectValue v = (ExplicitDimensionAspectValue) value;
            SchemaContent sc = getStore().getSchemaContent(v.getNamespace(),v.getLocalname());
            List<LabelResource> labels = sc.getLabelsWithLanguageAndResourceRoleAndLinkRole(locale,resourceRole, linkRole);
            if (labels.isEmpty()) super.getAspectValueLabel(value,locale,resourceRole,linkRole);
            return labels.get(0).getStringValue();
        } catch (Throwable e) {
            return super.getAspectValueLabel(value,locale,resourceRole,linkRole);
        }
        
    }

}
