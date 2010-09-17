package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.xbrlapi.Context;
import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.OpenContextComponent;
import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.Domain;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.XDTConstants;

/**
 * <h2>Typed dimension aspect details</h2>
 * 
 * <p>
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class TypedDimensionAspect extends DimensionAspect implements Aspect {

    /**
     * 
     */
    private static final long serialVersionUID = 5810844872183326245L;
    private static final Logger logger = Logger.getLogger(TypedDimensionAspect.class);

    /**
     * @param domain The domain for this aspect.
     * @param id The URI that is the ID for this explicit dimension aspect.
     * @throws XBRLException if the ID parameter is null.
     */
    public TypedDimensionAspect(Domain domain, URI dimensionNamespace, String dimensionLocalname) throws XBRLException {
        super(domain, dimensionNamespace, dimensionLocalname);
        try {
            TypedDimensionDomain d = (TypedDimensionDomain) domain;
            if (! (d.getDimensionNamespace().equals(dimensionNamespace) && d.getDimensionLocalname().equals(dimensionLocalname))) throw new XBRLException("The domain is not for this aspect.");
        } catch (ClassCastException e) {
            throw new XBRLException("The given domain is not derived from the TypedDimensionDomain");
        }
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    public TypedDimensionAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isNil()) return getMissingValue();
        if (fact.isTuple()) return getMissingValue();
        return getValue(((Item) fact).getContext());
    }

    /**
     * @see Aspect#getValue(Context)
     */
    public TypedDimensionAspectValue getValue(Context context) throws XBRLException {
        TypedDimensionAspectValue result = getValue(context.getEntity().getSegment());
        if (! result.isMissing()) return result;
        return this.getValue(context.getScenario());
    }
    
    public TypedDimensionAspectValue getValue(OpenContextComponent occ) throws XBRLException {
        if (occ == null) return getMissingValue();
        List<Element> children = occ.getChildElements();
        for (Element child: children) {
            if (child.getNamespaceURI().equals(XDTConstants.XBRLDINamespace.toString())) {
                String dimensionQName = child.getAttribute("dimension");
                if (! dimensionQName.equals("")) {
                    URI candidateNamespace = occ.getNamespaceFromQName(dimensionQName,child);
                    String candidateLocalname = occ.getLocalnameFromQName(dimensionQName);
                    if (candidateNamespace.equals(getDimensionNamespace()) && candidateLocalname.equals(getDimensionLocalname())) {                                
                        return getValue(child);
                    }
                }
            }
        }
        return getMissingValue();
    }

    public TypedDimensionAspectValue getValue(Element child) throws XBRLException {
        return new TypedDimensionAspectValue(getId(), child);
    }    
    
    /**
     * @see Aspect#getMissingValue()
     */
    public TypedDimensionAspectValue getMissingValue() {
        try {
            return new TypedDimensionAspectValue(getId());
        } catch (XBRLException e) {
            // Cannot occur because ID is never null after this is instantiated.
            return null;
        }
    }

}
