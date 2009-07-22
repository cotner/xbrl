package org.xbrlapi.relationships.tests;

/**
 * @see org.xbrlapi.impl.Networks
 * @see org.xbrlapi.impl.NetworksImpl
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.util.List;

import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Error;
import org.xbrlapi.LabelResource;
import org.xbrlapi.networks.AnalyserImpl;
import org.xbrlapi.networks.Networks;

public class PersistedRelationshipErrorsTestCase extends DOMLoadingTestCase {

	Networks networks = null;
	LabelResource label = null;
	Concept concept = null;

	protected void setUp() throws Exception {
        super.setUp();
	    loader.stashURI(getURI("test.data.local.xbrl.bad.locator.id"));
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public PersistedRelationshipErrorsTestCase(String arg0) {
		super(arg0);
	}
	
    /**
     * Test persisting of active relationships for all networks as part of data
     * discovery.
     */
    public void testErrorRecordingDuringPersistingOfActiveRelationships() { 
        try {
            store.setAnalyser(new AnalyserImpl(store));
            loader.discover();
            
            List<Error> errors = store.<Error>getXMLResources("Error");
            assertTrue(errors.size() > 0);
/*            for (Error error: errors) {
                error.serialize();
            }*/
                        
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
