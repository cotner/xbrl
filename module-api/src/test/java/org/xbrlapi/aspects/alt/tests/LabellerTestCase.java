package org.xbrlapi.aspects.alt.tests;

import java.util.List;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fact;
import org.xbrlapi.aspects.alt.AspectModel;
import org.xbrlapi.aspects.alt.AspectValue;
import org.xbrlapi.aspects.alt.ConceptAspect;
import org.xbrlapi.aspects.alt.ConceptLabeller;
import org.xbrlapi.aspects.alt.EntityAspect;
import org.xbrlapi.aspects.alt.EntityLabeller;
import org.xbrlapi.aspects.alt.FactSet;
import org.xbrlapi.aspects.alt.FactSetImpl;
import org.xbrlapi.aspects.alt.Labeller;
import org.xbrlapi.aspects.alt.LocationAspect;
import org.xbrlapi.aspects.alt.LocationLabeller;
import org.xbrlapi.aspects.alt.PeriodAspect;
import org.xbrlapi.aspects.alt.PeriodLabeller;
import org.xbrlapi.aspects.alt.ScenarioAspect;
import org.xbrlapi.aspects.alt.ScenarioLabeller;
import org.xbrlapi.aspects.alt.SegmentAspect;
import org.xbrlapi.aspects.alt.SegmentLabeller;
import org.xbrlapi.aspects.alt.StandardAspectModel;
import org.xbrlapi.aspects.alt.TupleAspect;
import org.xbrlapi.aspects.alt.TupleDomain;
import org.xbrlapi.aspects.alt.TupleLabeller;
import org.xbrlapi.aspects.alt.UnitAspect;
import org.xbrlapi.aspects.alt.UnitLabeller;


/**
 * Tests basic aspect labelling classes
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LabellerTestCase extends DOMLoadingTestCase {
	private final String FIRST_SMALL_INSTANCE = "test.data.small.instance";
    private final String INSTANCE_WITH_LABELLED_CONCEPTS = "test.data.local.xbrl.presentation.simple";
    private final String SECOND_SMALL_INSTANCE = "test.data.small.instance.2";
    private final String MEASURES = "real.data.xbrl.api.measures";
    private final String TUPLE_INSTANCE = "test.data.local.xbrl.instance.tuples.with.units";
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public LabellerTestCase(String arg0) {
		super(arg0);
	}

	public void testAspectLabels() {
		try {

			// Set up the aspect model
            AspectModel model = new StandardAspectModel(store);
            model.addAspect(new TupleAspect(new TupleDomain(store)));

            model.setLabeller(LocationAspect.ID,new LocationLabeller(model.getAspect(LocationAspect.ID)));
            model.setLabeller(TupleAspect.ID,new TupleLabeller(model.getAspect(TupleAspect.ID)));
            model.setLabeller(ConceptAspect.ID,new ConceptLabeller(model.getAspect(ConceptAspect.ID)));
            model.setLabeller(EntityAspect.ID,new EntityLabeller(model.getAspect(EntityAspect.ID)));
            model.setLabeller(SegmentAspect.ID,new SegmentLabeller(model.getAspect(SegmentAspect.ID)));
            model.setLabeller(PeriodAspect.ID,new PeriodLabeller(model.getAspect(PeriodAspect.ID)));
            model.setLabeller(ScenarioAspect.ID,new ScenarioLabeller(model.getAspect(ScenarioAspect.ID)));
            model.setLabeller(UnitAspect.ID,new UnitLabeller(model.getAspect(UnitAspect.ID)));

            // Load and retrieve the facts
            loader.discover(this.getURI(MEASURES));       
            loader.discover(this.getURI(FIRST_SMALL_INSTANCE));       
            loader.discover(this.getURI(SECOND_SMALL_INSTANCE));
            loader.discover(this.getURI(INSTANCE_WITH_LABELLED_CONCEPTS));

            List<Fact> facts = store.getAllFacts();
            assertEquals(3,facts.size());
            
            // Create the fact set
            FactSet factSet = new FactSetImpl(model);
            factSet.addFacts(facts);

            for (Fact fact: factSet.getFacts()) {
                for (AspectValue value: factSet.getAspectValues(fact)) {
                    Labeller labeller = model.getLabeller(value.getAspectId());
                    logger.info(labeller.getAspectLabel(null,null,null) + " " + labeller.getAspectValueLabel(value,"en",null,null));
                }
            }
            
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
	
	
	
}
