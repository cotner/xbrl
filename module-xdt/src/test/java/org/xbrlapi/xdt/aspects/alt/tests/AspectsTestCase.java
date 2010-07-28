package org.xbrlapi.xdt.aspects.alt.tests;

import java.util.List;
import java.util.Vector;

import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.AspectModel;
import org.xbrlapi.aspects.alt.AspectModelImpl;
import org.xbrlapi.aspects.alt.ConceptAspect;
import org.xbrlapi.aspects.alt.ConceptAspectValue;
import org.xbrlapi.aspects.alt.ConceptDomain;
import org.xbrlapi.aspects.alt.FactSet;
import org.xbrlapi.aspects.alt.FactSetImpl;
import org.xbrlapi.aspects.alt.Filter;
import org.xbrlapi.aspects.alt.FilterImpl;
import org.xbrlapi.aspects.alt.LocationAspect;
import org.xbrlapi.aspects.alt.LocationAspectValue;
import org.xbrlapi.aspects.alt.LocationDomain;
import org.xbrlapi.aspects.alt.PeriodAspect;
import org.xbrlapi.aspects.alt.PeriodDomain;
import org.xbrlapi.impl.ConceptImpl;
import org.xbrlapi.xdt.aspects.alt.DimensionalAspectModel;
import org.xbrlapi.xdt.tests.BaseTestCase;


/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class AspectsTestCase extends BaseTestCase {
    
	private final String FIRST_SMALL_INSTANCE = "test.data.small.instance";
    private final String SECOND_SMALL_INSTANCE = "test.data.small.instance.2";
    private final String TUPLE_INSTANCE = "test.data.local.xbrl.instance.tuples.with.units";
    private final String EXPLICIT_DIMENSIONS = "test.data.local.xdt.several.explicit.dimension.values";
    private final String EXPLICIT_DIMENSIONS_WITH_DEFAULTS = "test.data.local.xdt.several.explicit.dimension.values.with.defaults";
    private final String TYPED_DIMENSIONS = "test.data.local.xdt.typed.dimension.values";
    
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public AspectsTestCase(String arg0) {
		super(arg0);
	}

	public void testAspectModel() {
		try {

		    // Load and retrieve the facts
		    loader.discover(this.getURI(FIRST_SMALL_INSTANCE));       
	        loader.discover(this.getURI(SECOND_SMALL_INSTANCE));
            loader.discover(this.getURI(TUPLE_INSTANCE));       
            loader.discover(this.getURI(EXPLICIT_DIMENSIONS));
            loader.discover(this.getURI(EXPLICIT_DIMENSIONS_WITH_DEFAULTS));       
            loader.discover(this.getURI(TYPED_DIMENSIONS));     
            
			List<Fact> facts = store.getAllFacts();
			assertTrue(facts.size() > 0);
			
			// Set up the aspect model
            AspectModel model = new AspectModelImpl(store);
            ConceptDomain conceptDomain = new ConceptDomain(store);
            List<Concept> concepts = store.<Concept>getXMLResources(ConceptImpl.class);
            List<Concept> concreteConcepts = new Vector<Concept>();
            for (Concept concept: concepts) {
                if (! concept.isAbstract()) concreteConcepts.add(concept);
            }
            assertEquals(concreteConcepts.size(),conceptDomain.getSize());
            assertEquals(conceptDomain.getSize(), conceptDomain.getAllAspectValues().size());
            ConceptAspect conceptAspect = new ConceptAspect(conceptDomain);
            model.addAspect("row", conceptAspect);
            assertEquals(1,model.getAspects().size());
            assertTrue(model.hasAxis("row"));
            assertFalse(model.hasAxis("col"));
            assertEquals(1,model.getAspects("row").size());
            assertEquals(1,model.getAxes().size());

            // Add in the location aspect
            LocationAspect locationAspect = new LocationAspect(new LocationDomain(store));
            model.addAspect("row", locationAspect);
            assertEquals(2,model.getAspects("row").size());
            model.addAspect("col", locationAspect);
            assertEquals(1,model.getAspects("row").size());
            assertEquals(2,model.getAxes().size());
            assertTrue(model.hasAxis("col"));

            // Set up the filtration system
            Filter filter = new FilterImpl();
            Fact firstFact = facts.get(0);
            ConceptAspectValue conceptAspectValue = new ConceptAspectValue(firstFact.getNamespace(), firstFact.getLocalname());
            filter.addCriterion(conceptAspectValue);
            assertTrue(filter.filtersOn(ConceptAspect.ID));
            assertFalse(filter.filtersOn(LocationAspect.ID));
            LocationAspectValue locationAspectValue = new LocationAspectValue(firstFact.getIndex());
            filter.addCriterion(locationAspectValue);
            assertTrue(filter.filtersOn(LocationAspect.ID));
            filter.removeCriterion(LocationAspect.ID);
            assertFalse(filter.filtersOn(LocationAspect.ID));

            // Create a fact set
            model.addAspect(new PeriodAspect(new PeriodDomain(store)));
            FactSet factSet = new FactSetImpl(model);
            factSet.addFacts(facts);

            assertEquals(3, model.getAxes().size());
            assertTrue(model.hasAxis("orphan"));
            assertTrue(model.hasAspect(PeriodAspect.ID));

            for (String axis: model.getAxes()) {
                logger.info(axis);
                for (Aspect aspect: model.getAspects(axis)) {
                    logger.info(aspect.getId());
                }
            }
            
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
    public void testDimensionalAspectModelConstruction() {

        try {
            loader.discover(this.getURI(this.EXPLICIT_DIMENSIONS));
            loader.discover(this.getURI(this.TYPED_DIMENSIONS));
            
            DimensionalAspectModel model = new DimensionalAspectModel(store);
            FactSet factSet = new FactSetImpl(model);
            factSet.addFacts(store.getAllFacts());

            assertEquals(4, factSet.getSize());
            for (Aspect aspect: model.getAspects()) {
                logger.info(aspect.getId());
            }
            assertEquals(10,model.getAspects().size());
            List<Aspect> explicitAspects = model.getExplicitDimensionAspects();
            assertEquals(2, explicitAspects.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }
    
    public void testDimensionalAspectModelWithDefaults() {

        try {
            loader.discover(this.getURI(this.EXPLICIT_DIMENSIONS_WITH_DEFAULTS));
            DimensionalAspectModel model = new DimensionalAspectModel(store);
            FactSet factSet = new FactSetImpl(model);
            factSet.addFacts(store.getAllFacts());
            List<Aspect> aspects = model.getExplicitDimensionAspects();
            assertEquals(2, aspects.size());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }	
	
	
}
