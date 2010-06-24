package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;
import java.util.List;

import javax.xml.namespace.QName;

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
import org.xbrlapi.xdt.Dimension;
import org.xbrlapi.xdt.XDTConstants;
import org.xbrlapi.xdt.values.DimensionValueAccessor;

/**
 * <h2>Explicit dimension aspect details</h2>
 * 
 * <p>
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ExplicitDimensionAspect extends AspectImpl implements Aspect {

    /**
     * 
     */
    private static final long serialVersionUID = -4922858269680499821L;

    private static final Logger logger = Logger.getLogger(ExplicitDimensionAspect.class);

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
     * @param domain The domain for this aspect.
     * @param id The URI that is the ID for this explicit dimension aspect.
     * @throws XBRLException if the ID parameter is null.
     */
    public ExplicitDimensionAspect(Domain domain, URI dimensionNamespace, String dimensionLocalname) throws XBRLException {
        super(domain);
        try {
            if (dimensionNamespace == null) throw new XBRLException("The explicit dimension namespace must not be null.");
            if (dimensionLocalname == null) throw new XBRLException("The explicit dimension local name must not be null.");
            this.dimensionNamespace = dimensionNamespace;
            this.dimensionLocalname = dimensionLocalname;
            ExplicitDimensionDomain d = (ExplicitDimensionDomain) domain;
            if (! (d.getDimensionNamespace().equals(dimensionNamespace) && d.getDimensionLocalname().equals(dimensionLocalname))) throw new XBRLException("The domain is not for this aspect.");
        } catch (ClassCastException e) {
            throw new XBRLException("The given domain is not derived from the ExplicitDimensionDomain");
        }
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    public ExplicitDimensionAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isNil()) return getMissingValue();
        if (fact.isTuple()) return getMissingValue();
        
        Context context = ((Item) fact).getContext();
        QName memberQName = this.getMemberQNameFromOCC(context.getEntity().getSegment());
        if (memberQName == null) {
            memberQName = this.getMemberQNameFromOCC(context.getScenario());
        }
        if (memberQName == null) return this.getMissingValue();

        return new ExplicitDimensionAspectValue(getId(), URI.create(memberQName.getNamespaceURI()), memberQName.getLocalPart());
    }
    /**
     * @see DimensionValueAccessor#getDomainMemberFromOpenContextComponent(OpenContextComponent, Dimension)
     */
    private QName getMemberQNameFromOCC(OpenContextComponent occ) throws XBRLException {
        if (occ == null) return null;
        List<Element> children = occ.getChildElements();
        for (Element child: children) {
            if (child.getNamespaceURI().equals(XDTConstants.XBRLDINamespace.toString())) {
                String dimensionQName = child.getAttribute("dimension");
                if (! dimensionQName.equals("")) {
                    URI candidateNamespace = occ.getNamespaceFromQName(dimensionQName,child);
                    String candidateLocalname = occ.getLocalnameFromQName(dimensionQName);
                    if (candidateNamespace.equals(dimensionNamespace) && candidateLocalname.equals(dimensionLocalname)) {                                
                        String memberQName = child.getTextContent().trim();
                        URI memberNamespace = occ.getNamespaceFromQName(memberQName,child);
                        String memberLocalname = occ.getLocalnameFromQName(memberQName);
                        return new QName(memberNamespace.toString(), memberLocalname);
                    }
                }
            }
        }
        return null;
    }    
    
    /**
     * @see Aspect#getMissingValue()
     */
    public ExplicitDimensionAspectValue getMissingValue() {
        try {
            return new ExplicitDimensionAspectValue(getId());
        } catch (XBRLException e) {
            // Cannot occur because ID is never null after this is instantiated.
            return null;
        }
    }


}
