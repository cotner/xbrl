package org.xbrlapi.sax.identifiers.tests;

import java.net.URI;

import org.xbrlapi.Concept;
import org.xbrlapi.data.dom.tests.BaseTestCase;

/**
 * Test the basic operation of the SAX identifiers.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class BasicIdentifierTestCase extends BaseTestCase {
	
	private final String STARTING_POINT = "real.data.sec.usgaap.3";
	private URI uri = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		uri = getURI(this.STARTING_POINT);
	}
	
	public BasicIdentifierTestCase(String arg0) {
		super(arg0);
	}
	
	public void testFragmentIdentification() {
		try {
			loader.stashURI(uri);
			loader.discoverNext();
			Concept concept = store.getConcept(new URI("http://www.microsoft.com/msft/xbrl/taxonomy/2005-02-28"),"CoverInformation");
			assertEquals("CoverInformation",concept.getName());
		} catch (Exception e) {
		    e.printStackTrace();
			fail("Unexpected " + e.getMessage());
		}
	}

}
