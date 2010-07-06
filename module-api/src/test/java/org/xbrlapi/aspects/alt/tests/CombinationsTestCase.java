package org.xbrlapi.aspects.alt.tests;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fact;
import org.xbrlapi.aspects.alt.AspectModel;
import org.xbrlapi.aspects.alt.AspectValue;
import org.xbrlapi.aspects.alt.Combinations;
import org.xbrlapi.aspects.alt.CombinationsImpl;
import org.xbrlapi.aspects.alt.ConceptAspect;
import org.xbrlapi.aspects.alt.FactSet;
import org.xbrlapi.aspects.alt.FactSetImpl;
import org.xbrlapi.aspects.alt.LocationAspect;
import org.xbrlapi.aspects.alt.StandardAspectModel;



/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class CombinationsTestCase extends DOMLoadingTestCase {
	private final String FIRST_SMALL_INSTANCE = "test.data.small.instance";
    private final String SECOND_SMALL_INSTANCE = "test.data.small.instance.2";
    
    private final String TUPLE_INSTANCE = "test.data.local.xbrl.instance.tuples.with.units";
	
    private AspectModel model;
    private FactSet factSet;
    private final String axis = "row";
    
	protected void setUp() throws Exception {
		super.setUp();

        // Load and retrieve the facts
        loader.discover(this.getURI(FIRST_SMALL_INSTANCE));       
        loader.discover(this.getURI(SECOND_SMALL_INSTANCE));

        // Set up the aspect model
        model = new StandardAspectModel(store);

        // Create a fact set
        List<Fact> facts = store.getAllFacts();
        assertEquals(2,facts.size());
        factSet = new FactSetImpl(model);
        factSet.addFacts(facts);

        // Set up a row axis in the model
        model.addAspect(axis,model.getAspect(ConceptAspect.ID));
        model.addAspect(axis,model.getAspect(LocationAspect.ID));
        
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public CombinationsTestCase(String arg0) {
		super(arg0);
	}

    public void testAspectValueCombinations() {
        try {
            
            // Create an aspect value combinations object
            Combinations combinations = new CombinationsImpl(model,"row");

            assertEquals(2,combinations.getAspects().size());
            assertEquals(ConceptAspect.ID,combinations.getAspects().get(0).getId());
            assertEquals(LocationAspect.ID,combinations.getAspects().get(1).getId());
            
            // Populate aspect values
            List<AspectValue> conceptAspectValues = new Vector<AspectValue>(factSet.getAspectValues(ConceptAspect.ID));
            Collections.sort(conceptAspectValues,model.getAspect(ConceptAspect.ID).getDomain());
            combinations.setAspectValues(ConceptAspect.ID,conceptAspectValues);
            assertEquals(2, combinations.getAspectValueCount(ConceptAspect.ID));
            assertEquals(1, combinations.getAncestorCount(ConceptAspect.ID));
            assertEquals(1, combinations.getDescendantCount(ConceptAspect.ID));
            
            List<AspectValue> locationAspectValues = new Vector<AspectValue>(factSet.getAspectValues(LocationAspect.ID));
            Collections.sort(locationAspectValues,model.getAspect(LocationAspect.ID).getDomain());
            combinations.setAspectValues(LocationAspect.ID,locationAspectValues);
            assertEquals(2, combinations.getAspectValueCount(LocationAspect.ID));

            assertEquals(1, combinations.getAncestorCount(ConceptAspect.ID));
            assertEquals(2, combinations.getDescendantCount(ConceptAspect.ID));

            assertEquals(2, combinations.getAncestorCount(LocationAspect.ID));
            assertEquals(1, combinations.getDescendantCount(LocationAspect.ID));
            
            assertEquals(4, combinations.getCombinationCount());

            for (int i=0; i<combinations.getCombinationCount(); i++) {
                logger.info(combinations.getCombinationValue(ConceptAspect.ID,i).getId() + " " + combinations.getCombinationValue(LocationAspect.ID,i).getId());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }	
	
	
	
}
