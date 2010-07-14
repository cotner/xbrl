package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;
import java.util.List;

import org.xbrlapi.LabelResource;
import org.xbrlapi.SchemaContent;
import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.Labeller;
import org.xbrlapi.aspects.alt.LabellerImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * <p>
 * A labeller for the typed dimension aspect based upon XBRL labels and generic XBRL labels.
 * </p>
 * 
 * <p>
 * This labeller does not make use of label caching systems.  Instead labels are obtained from
 * the XLink relationships from typed dimensions to XBRL 2.1 and generic labels.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class TypedDimensionLabeller extends LabellerImpl implements Labeller {

    /**
     * 
     */
    private static final long serialVersionUID = 3050503522357576606L;

    /**
     * @param aspect The aspect to be a labeller for.
     */
    public TypedDimensionLabeller(Aspect aspect) throws XBRLException {
        super(aspect);
        try {
            @SuppressWarnings("unused")
            TypedDimensionAspect a = (TypedDimensionAspect) aspect;
        } catch (Throwable e) {
            throw new XBRLException("This labeller only works for typed dimension aspects.");
        }
        
    }

    /**
     * @see Labeller#getAspectLabel(String, URI, URI)
     */
    @Override
    public String getAspectLabel(String locale, URI resourceRole, URI linkRole) {
        TypedDimensionAspect aspect = (TypedDimensionAspect) getAspect();
        try {
            SchemaContent sc = getStore().getSchemaContent(aspect.getDimensionNamespace(),aspect.getDimensionLocalname());
            List<LabelResource> labels = sc.getLabelsWithLanguageAndResourceRoleAndLinkRole(locale,resourceRole, linkRole);
            if (! labels.isEmpty()) return labels.get(0).getStringValue();
            return super.getAspectLabel(locale,resourceRole,linkRole);
        } catch (XBRLException e) {
            return super.getAspectLabel(locale,resourceRole,linkRole);
        }
    }

}
