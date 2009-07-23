package org.xbrlapi.relationships.tests;

/**
 * @see org.xbrlapi.impl.Networks
 * @see org.xbrlapi.impl.NetworksImpl
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.util.List;

import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.LabelResource;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.utilities.Constants;

public class NetworkSerializationTestCase extends DOMLoadingTestCase {

	Networks networks = null;
	LabelResource label = null;
	Concept concept = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(getURI("test.data.xlink.titles"));
		List<LabelResource> labels = store.<LabelResource>getXMLResources("LabelResource");
		label = labels.get(0);
		List<Concept> concepts = store.<Concept>getXMLResources("Concept");
		concept = concepts.get(0);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public NetworkSerializationTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test whether the networks loading process operates.
	 */
	public void testNetworksSerialization() {	

		try {
		    networks = store.getNetworks(Constants.LabelArcrole);
		    this.assessCustomEquality(networks,this.getDeepCopy(networks));
		} catch (Exception e) {
		    e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
}
