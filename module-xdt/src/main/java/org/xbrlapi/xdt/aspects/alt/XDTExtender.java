package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.xbrlapi.Context;
import org.xbrlapi.Fact;
import org.xbrlapi.Item;
import org.xbrlapi.OpenContextComponent;
import org.xbrlapi.Relationship;
import org.xbrlapi.Scenario;
import org.xbrlapi.Segment;
import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.AspectModel;
import org.xbrlapi.aspects.alt.ConceptAspect;
import org.xbrlapi.aspects.alt.ConceptDomain;
import org.xbrlapi.aspects.alt.EntityAspect;
import org.xbrlapi.aspects.alt.EntityDomain;
import org.xbrlapi.aspects.alt.Extender;
import org.xbrlapi.aspects.alt.LocationAspect;
import org.xbrlapi.aspects.alt.LocationDomain;
import org.xbrlapi.aspects.alt.PeriodAspect;
import org.xbrlapi.aspects.alt.PeriodDomain;
import org.xbrlapi.aspects.alt.UnitAspect;
import org.xbrlapi.aspects.alt.UnitDomain;
import org.xbrlapi.data.Store;
import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.Dimension;
import org.xbrlapi.xdt.ExplicitDimension;
import org.xbrlapi.xdt.XDTConstants;

/**
 * <h2>XDT aspect model extender</h2>
 * 
 * <p>
 * The XDT extender discovers the following aspects
 * </p>
 * 
 * <ul>
 *  <li>@see LocationAspect</li>
 *  <li>@see ConceptAspect</li>
 *  <li>@see EntityAspect</li>
 *  <li>@see PeriodAspect</li>
 *  <li>@see SegmentRemainderAspect</li>
 *  <li>@see ScenarioRemainderAspect</li>
 *  <li>@see UnitAspect</li>
 *  <li>@see ExplicitDimensionAspect</li>
 *  <li>@see TypedDimensionAspect</li>
 * </ul>
 * 
 * Explicit dimensions with default values are always found.
 * 
 * These are the full set of aspects defined under the XDT 
 * aspect model in the XBRL Variables 1.0 specification.
 * @see http://www.xbrl.org/Specification/variables/REC-2009-06-22/variables-REC-2009-06-22.html
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */

public class XDTExtender implements Extender {

    /**
     * 
     */
    private static final long serialVersionUID = 8292817281957644676L;
    
    private static final Logger logger = Logger.getLogger(XDTExtender.class);    
    
    /**
     * Set to true when the default dimensions have been added
     * to the aspect model.
     */
    private boolean added_default_dimensions = false;
    
    public Collection<Aspect> getNewAspects(AspectModel model, Fact fact)
            throws XBRLException {
        Set<Aspect> aspects = new HashSet<Aspect>();

        // Set up default explicit dimension aspects.
        if (! added_default_dimensions) {
            Store store = fact.getStore();
            Networks networks = store.getNetworks(XDTConstants.DefaultDimensionArcrole);
            for (Network network: networks) {
                List<Relationship> relationships = network.getAllRelationships();
                for (Relationship relationship: relationships) {
                    ExplicitDimension dimension = relationship.<ExplicitDimension>getSource();
                    if (! model.hasAspect(URI.create(dimension.getTargetNamespace() + "#" + dimension.getName()))) {
                        URI dimensionNamespace = dimension.getTargetNamespace();
                        String dimensionLocalname = dimension.getName();
                        ExplicitDimensionDomain domain = new ExplicitDimensionDomain(store,dimensionNamespace,dimensionLocalname);
                        aspects.add(new ExplicitDimensionAspect(domain,dimensionNamespace,dimensionLocalname));
                    }
                }
            }            
            added_default_dimensions = true;
        }
        
        if (! model.hasAspect(LocationAspect.ID)) {
            aspects.add(new LocationAspect(new LocationDomain(fact.getStore())));
        }

        if (! model.hasAspect(ConceptAspect.ID)) {
            aspects.add(new ConceptAspect(new ConceptDomain(fact.getStore())));
        }

        if (! fact.isTuple() && ! fact.isNil()) {

            Item item = (Item) fact;
            
            if (item.isNumeric()) {
                if (! model.hasAspect(UnitAspect.ID)) {
                    aspects.add(new UnitAspect(new UnitDomain()));
                }
            }

            if (! model.hasAspect(PeriodAspect.ID)) {
                aspects.add(new PeriodAspect(new PeriodDomain()));
            }
        
            if (! model.hasAspect(EntityAspect.ID)) {
                aspects.add(new EntityAspect(new EntityDomain()));
            }

            Context context = item.getContext();
            Segment segment = context.getEntity().getSegment();
            if (segment != null) aspects.addAll(addNewXDTAspects(model,  segment));

            if (! model.hasAspect(SegmentRemainderAspect.ID)) {
                aspects.add(new SegmentRemainderAspect(new SegmentRemainderDomain()));
            }
            
            Scenario scenario = context.getScenario();
            if (scenario != null) aspects.addAll(addNewXDTAspects(model,  scenario));

            if (! model.hasAspect(ScenarioRemainderAspect.ID)) {
                aspects.add(new ScenarioRemainderAspect(new ScenarioRemainderDomain()));
            }

        }
        return aspects;
    }

    /**
     * @param model The aspect model being extended.
     * @param occ The segment or scenario fragment to examine for new dimensional aspects.
     * @return the new XDT dimension aspects.
     * @throws XBRLException
     */
    private Collection<Aspect> addNewXDTAspects(AspectModel model, OpenContextComponent occ) throws XBRLException {

        Set<Aspect> aspects = new HashSet<Aspect>();
        
        Store store = occ.getStore();

        List<Element> children = occ.getChildElements();

        for (Element child: children) {
            if (child.getNamespaceURI().equals(XDTConstants.XBRLDINamespace.toString())) {
                if (child.hasAttribute("dimension")) {
                    String qname = child.getAttribute("dimension");
                    URI dimensionNamespace = occ.getNamespaceFromQName(qname,child);
                    String dimensionLocalname = occ.getLocalnameFromQName(qname);
                    URI aspectId = URI.create(dimensionNamespace + "#" + dimensionLocalname);
                    if (! model.hasAspect(aspectId)) {
                        Dimension dimension = store.<Dimension>getGlobalDeclaration(dimensionNamespace,dimensionLocalname);
                        if (dimension.isExplicitDimension()) {
                            ExplicitDimensionDomain domain = new ExplicitDimensionDomain(store,dimensionNamespace, dimensionLocalname);
                            aspects.add(new ExplicitDimensionAspect(domain, dimensionNamespace, dimensionLocalname));
                        } else {
                            TypedDimensionDomain domain = new TypedDimensionDomain(store,dimensionNamespace, dimensionLocalname);
                            aspects.add(new TypedDimensionAspect(domain, dimensionNamespace, dimensionLocalname));
                        }
                    }
                }
            }
        }
        
        return aspects;
    }    
    
}
