package org.xbrlapi.fragment.tests;

import java.net.URI;
import java.util.regex.Pattern;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Fragment;
import java.util.List;
import org.xbrlapi.Title;
import org.xbrlapi.Xlink;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Tests the implementation of the org.xbrlapi.Xlink interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class XlinkTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.multi.concept.schema";
	
	private String document = 
		"<linkbase xmlns=\"http://www.xbrl.org/2003/linkbase\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n" +
	    "<presentationLink xlink:type=\"extended\" xlink:role=\"http://www.xbrl.org/2003/role/link\">\n" + 
		"<loc xlink:type=\"locator\" xlink:href=\"397-ABC.xsd#A\" xlink:label=\"parent\" />\n" + 
		"<loc xlink:type=\"locator\" xlink:href=\"397-ABC.xsd#B\" xlink:label=\"child\" />\n" + 
		"<loc xlink:type=\"locator\" xlink:href=\"397-ABC.xsd#C\" xlink:label=\"child\" >\n" + 
		"<title xlink:type=\"title\" xml:lang=\"en\">Title content</title>\n" + 
		"</loc>\n" + 
		"<presentationArc xlink:title=\"stuff\" xlink:type=\"arc\" xlink:arcrole=\"http://www.xbrl.org/2003/arcrole/parent-child\" xlink:from=\"parent\" xlink:to=\"child\" />\n" +
		"</presentationLink>\n" +
		"</linkbase>\n";	
	
	private URI uri = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
        String myURI = Pattern.compile("397-ABC.xsd").matcher(getURI("test.data.multi.concept.schema").toString()).replaceAll("xlink_test.xml");
        uri = new URI(myURI);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public XlinkTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting the type of XLink fragment.
	 */
	public void testGetXLinkType() {		
		try {
			List<Xlink> fragments = store.<Xlink>getFragments("Arc");
            assertTrue(fragments.size() > 0);
			Xlink fragment = fragments.get(0);
			assertEquals("arc", fragment.getXlinkType());
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test getting the value of the XLink title attribute.
	 */
	public void testGetXLinkTitle() {		

		// Case where none exists
		try {
			List<Xlink> fragments = store.<Xlink>getFragments("Arc");
			Xlink fragment = fragments.get(0);
			assertNull(fragment.getTitleAttribute());
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
		
		// Case where one exists
		try {
			loader.discover(uri, document);
			List<Xlink> fragments = store.<Xlink>queryForFragments("#roots#/" + Constants.XBRLAPIPrefix + ":" + "data/*/@xlink:title");
			Xlink fragment = fragments.get(0);
			assertEquals("stuff", fragment.getTitleAttribute());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting xlink title elements.
	 */
	public void testGetXLinkTitleElements() {	

		// Case where none exists
		try {
			List<Xlink> fragments = store.<Xlink>getFragments("Arc");
			assertTrue(fragments.size() > 0);
			Xlink fragment = fragments.get(0);
			assertEquals(0,fragment.getTitleElements().size());
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
		
		// Case where one exists
		try {
			loader.discover(uri, document);
			List<Title> fragments = store.getFragments("Title");
			Fragment title = fragments.get(0);
			Xlink fragment = (Xlink) title.getParent();
			List<Title> titles = fragment.getTitleElements();
			assertNotNull(titles);
			assertEquals(1, titles.size());
			assertEquals("title",titles.get(0).getLocalname());
			assertEquals("Title content",(titles.get(0)).getValue());
			assertEquals("en",(titles.get(0)).getLanguage());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	

}
