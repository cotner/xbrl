package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import java.util.List;
import org.xbrlapi.ReferencePartDeclaration;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Tests identification of reference part declarations during discovery.
 * Tests the ability to find the reference part declarations in a schema.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ReferencePartDeclarationTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.reference.links";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ReferencePartDeclarationTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test get the reference part declarations
	 */
	public void testGetReferencePartDeclarations() {	

		try {
			List<ReferencePartDeclaration> rpds = store.<ReferencePartDeclaration>gets("ReferencePartDeclaration");
			for (ReferencePartDeclaration rpd: rpds) {
				logger.info(rpd.getName() + " is a reference part");
				assertEquals(Constants.XBRL21LinkNamespace,rpd.getSubstitutionGroupNamespace());
				assertEquals("part",rpd.getSubstitutionGroupLocalname());
			}
		} catch (XBRLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
