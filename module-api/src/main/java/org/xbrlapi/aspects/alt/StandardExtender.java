package org.xbrlapi.aspects.alt;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Standard extender</h2>
 * 
 * <p>
 * The standard extender discovers the following aspects
 * </p>
 * 
 * <ul>
 *  <li>@see LocationAspect</li>
 *  <li>@see ConceptAspect</li>
 *  <li>@see EntityAspect</li>
 *  <li>@see PeriodAspect</li>
 *  <li>@see SegmentAspect</li>
 *  <li>@see ScenarioAspect</li>
 *  <li>@see UnitAspect</li>
 * </ul>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */

public class StandardExtender implements Extender {

    /**
     * 
     */
    private static final long serialVersionUID = 2213635696445815634L;

    public Collection<Aspect> getNewAspects(AspectModel model, Fact fact)
            throws XBRLException {
        Set<Aspect> aspects = new HashSet<Aspect>();

        if (! model.hasAspect(LocationAspect.ID)) {
            aspects.add(new LocationAspect(new LocationDomain(fact.getStore())));
        }

        if (! model.hasAspect(ConceptAspect.ID)) {
            aspects.add(new ConceptAspect(new ConceptDomain(fact.getStore())));
        }

        if (! fact.isTuple() && ! fact.isNil()) {
            
            if (fact.isNumeric()) {
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

            if (! model.hasAspect(SegmentAspect.ID)) {
                aspects.add(new SegmentAspect(new SegmentDomain()));
            }
            
            if (! model.hasAspect(ScenarioAspect.ID)) {
                aspects.add(new ScenarioAspect(new ScenarioDomain()));
            }

        }
        return aspects;
    }

}
