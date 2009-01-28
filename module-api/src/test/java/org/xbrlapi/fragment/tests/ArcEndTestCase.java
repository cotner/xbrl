package org.xbrlapi.fragment.tests;

/**
 * Tests the implementation of the org.xbrlapi.ArcEnd interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.net.URI;
import java.util.regex.Pattern;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Locator;

public class ArcEndTestCase extends DOMLoadingTestCase {

	private final String STARTING_POINT = "test.data.multi.concept.schema";

	URI uri = null;
	String document = 
		"<linkbase xmlns=\"http://www.xbrl.org/2003/linkbase\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n" +
	    "<presentationLink xlink:type=\"extended\" xlink:role=\"http://www.xbrl.org/2003/role/link\">\n" + 
		"<loc xlink:type=\"locator\" xlink:href=\"397-ABC.xsd#A\" xlink:label=\"parent\" />\n" + 
		"<loc xlink:type=\"locator\" xlink:href=\"397-ABC.xsd#B\" xlink:label=\"child\" />\n" + 
		"<loc id=\"unique\" xlink:role=\"http://www.xbrlapi.org/xlink/role\" xlink:type=\"locator\" xlink:href=\"397-ABC.xsd#C\" xlink:label=\"child\" />\n" + 
		"<presentationArc xlink:type=\"arc\" xlink:arcrole=\"http://www.xbrl.org/2003/arcrole/parent-child\" xlink:from=\"parent\" xlink:to=\"child\" />\n" +
		"</presentationLink>\n" +
		"</linkbase>\n";
	
	protected void setUp() throws Exception {
		super.setUp();
		uri = getURI(STARTING_POINT);
		String myURI = Pattern.compile("397-ABC.xsd").matcher(uri.toString()).replaceAll("string.xml");
		loader.discover(new URI(myURI), document);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ArcEndTestCase(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test getting xlink label value.
	 */
	public void testGetLabel() {	

		// Case where one exists
		try {
			FragmentList<Locator> locators = store.<Locator>getFragments("Locator");
			assertEquals("parent", locators.get(0).getLabel());
			assertEquals("child", locators.get(1).getLabel());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}	

	/**
	 * Test getting xlink role value.
	 */
	public void testGetRole() {	

		// Case where one exists
		try {
			FragmentList<Locator> locators = store.<Locator>getFragments("Locator");
			assertNull(locators.get(0).getRole());
			assertEquals("http://www.xbrlapi.org/xlink/role", locators.get(2).getRole());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}	
	
	/**
	 * Test getting xlink role value.
	 */
	public void testGetArcEndId() {	

		// Case where one exists
		try {
			FragmentList<Locator> locators = store.<Locator>getFragments("Locator");
			assertNull(locators.get(0).getArcEndId());
			assertEquals("unique", locators.get(2).getArcEndId());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}	
	
}
