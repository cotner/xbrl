package org.xbrlapi.sax.identifiers.tests;

import java.net.URI;
import java.util.List;

import org.xbrlapi.Concept;
import org.xbrlapi.data.dom.tests.BaseTestCase;

/**
 * Test the loader implementation.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class IdentifierTestCase extends BaseTestCase {
	
	private final String STARTING_POINT = "real.data.sec.usgaap.3";
	private URI uri = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		uri = getURI(this.STARTING_POINT);
	}
	
	public IdentifierTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test fragment identification
	 */
	public void testFragmentIdentification() {
		try {
			loader.stashURI(uri);
			loader.discoverNext();
			List<Concept> concepts = store.getFragments("Concept");;
			for (Concept concept: concepts) {
			    logger.info(concept.getName() + " " + concept.getTargetNamespace());
			}
			Concept concept = store.getConcept(new URI("http://www.microsoft.com/msft/xbrl/taxonomy/2005-02-28"),"CoverInformation");
			assertEquals("CoverInformation",concept.getName());
		} catch (Exception e) {
		    e.printStackTrace();
			fail("Unexpected " + e.getMessage());
		}
	}

}
