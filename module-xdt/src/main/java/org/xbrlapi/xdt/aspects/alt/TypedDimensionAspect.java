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
import org.xbrlapi.aspects.alt.AspectImpl;
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
public class TypedDimensionAspect extends AspectImpl implements Aspect {

    /**
     * 
     */
    private static final long serialVersionUID = 7346787542613079650L;

    private static final Logger logger = Logger.getLogger(TypedDimensionAspect.class);

    /**
     * The namespace of this explicit dimension.
     */
    private URI dimensionNamespace;
    
    /**
     * The local name of this explicit dimension.
     */
    private String dimensionLocalname;
    
    /**
     * @see Aspect#getId()
     */
    public URI getId() {
        return URI.create(dimensionNamespace + "#" + dimensionLocalname);
    }
    
    /**
     * @see Aspect#isExtensible()
     */
    @Override
    public boolean isExtensible() {
        return true;
    }
    
    /**
     * @param domain The domain for this aspect.
     * @param id The URI that is the ID for this explicit dimension aspect.
     * @throws XBRLException if the ID parameter is null.
     */
    public TypedDimensionAspect(Domain domain, URI dimensionNamespace, String dimensionLocalname) throws XBRLException {
        super(domain);
        try {
            if (dimensionNamespace == null) throw new XBRLException("The explicit dimension namespace must not be null.");
            if (dimensionLocalname == null) throw new XBRLException("The explicit dimension local name must not be null.");
            this.dimensionNamespace = dimensionNamespace;
            this.dimensionLocalname = dimensionLocalname;
            TypedDimensionDomain d = (TypedDimensionDomain) domain;
            if (! (d.getDimensionNamespace().equals(dimensionNamespace) && d.getDimensionLocalname().equals(dimensionLocalname))) throw new XBRLException("The domain is not for this aspect.");
        } catch (ClassCastException e) {
            throw new XBRLException("The given domain is not derived from the ExplicitDimensionDomain");
        }
    }
    
    public String getDimensionLocalname() {
        return dimensionLocalname;
    }
    
    public URI getDimensionNamespace() {
        return dimensionNamespace;
    }    
    
    /**
     * @see Aspect#getValue(Fact)
     */
    public TypedDimensionAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isNil()) return getMissingValue();
        if (fact.isTuple()) return getMissingValue();
        
        Context context = ((Item) fact).getContext();
        TypedDimensionAspectValue result = this.getValue(context.getEntity().getSegment());
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
                    if (candidateNamespace.equals(dimensionNamespace) && candidateLocalname.equals(dimensionLocalname)) {                                
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
