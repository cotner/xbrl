package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.xbrlapi.Concept;
import org.xbrlapi.Context;
import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.OpenContextComponent;
import org.xbrlapi.Scenario;
import org.xbrlapi.Segment;
import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.Domain;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.ExplicitDimension;
import org.xbrlapi.xdt.XDTConstants;

/**
 * <h2>Explicit dimension aspect details</h2>
 * 
 * <p>
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ExplicitDimensionAspect extends DimensionAspect implements Aspect {

    /**
     * 
     */
    private static final long serialVersionUID = 1229327071811330083L;
    
    private static final Logger logger = Logger.getLogger(ExplicitDimensionAspect.class);
    
    /**
     * @param domain The domain for this aspect.
     * @param id The URI that is the ID for this explicit dimension aspect.
     * @throws XBRLException if the ID parameter is null.
     */
    public ExplicitDimensionAspect(Domain domain, URI dimensionNamespace, String dimensionLocalname) throws XBRLException {
        super(domain, dimensionNamespace, dimensionLocalname);
        try {
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
        return getValue(((Item) fact).getContext());
    }
    
    /**
     * @see Aspect#getValue(Context)
     */
    public ExplicitDimensionAspectValue getValue(Context context) throws XBRLException {

        // Try segment
        Segment segment = context.getEntity().getSegment();
        ExplicitDimensionAspectValue result = this.getValue(segment);
        if (! result.isMissing()) return result;
        
        // Give scenario a whirl
        Scenario scenario = context.getScenario();
        result = this.getValue(scenario);
        if (! result.isMissing()) return result;

        // Go for a default with fall back to a missing value
        return getDefaultValue(context.getStore());
        
    }     

    ExplicitDimensionAspectValue defaultValue = null;
    
    /**
     * This method only determines the default value from the store on the first time.
     * Thereafter it is cached, even if the data store changes.
     * @param store The data store to use to get the default value.
     * @return the default value for this dimension or the missing value
     * if there is no default.
     * @throws XBRLException
     */
    public ExplicitDimensionAspectValue getDefaultValue(Store store) throws XBRLException {
        if (defaultValue == null) {
            ExplicitDimension dimension = store.<ExplicitDimension>getSchemaContent(this.getDimensionNamespace(), this.getDimensionLocalname());
            try {
                Concept defaultMember = dimension.getDefaultDomainMember();
                defaultValue = new ExplicitDimensionAspectValue(getId(), defaultMember.getTargetNamespace(), defaultMember.getName());
            } catch (XBRLException e) { // There is no default so return a missing value
                defaultValue = getMissingValue();
            }
        }
        return defaultValue;
    }

    public ExplicitDimensionAspectValue getValue(OpenContextComponent occ) throws XBRLException {
        if (occ == null) return getMissingValue();
        List<Element> children = occ.getChildElements();
        for (Element child: children) {
            if (child.getNamespaceURI().equals(XDTConstants.XBRLDINamespace.toString())) {
                String dimensionQName = child.getAttribute("dimension");
                if (! dimensionQName.equals("")) {
                    URI candidateNamespace = occ.getNamespaceFromQName(dimensionQName,child);
                    String candidateLocalname = occ.getLocalnameFromQName(dimensionQName);
                    if (candidateNamespace.equals(getDimensionNamespace()) && candidateLocalname.equals(getDimensionLocalname())) {                                
                        return getValue(occ, child);
                    }
                }
            }
        }
        return this.getMissingValue();
    }    

    public ExplicitDimensionAspectValue getValue(OpenContextComponent occ, Element child) throws XBRLException {
        String memberQName = child.getTextContent().trim();
        URI memberNamespace = occ.getNamespaceFromQName(memberQName,child);
        String memberLocalname = occ.getLocalnameFromQName(memberQName);
        return new ExplicitDimensionAspectValue(this.getId(), memberNamespace, memberLocalname);
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
