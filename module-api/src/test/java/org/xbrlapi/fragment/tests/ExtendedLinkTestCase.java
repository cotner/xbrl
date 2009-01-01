package org.xbrlapi.fragment.tests;

import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Locator;
import org.xbrlapi.Resource;
import org.xbrlapi.XlinkDocumentation;
import org.xbrlapi.utilities.Constants;
/**
 * Tests the implementation of the org.xbrlapi.ExtendedLink interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ExtendedLinkTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT_A = "test.data.multi.concept.schema";
	private final String STARTING_POINT_B = "test.data.extended.link.documentation.element";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT_A));		
		loader.discover(this.getURI(STARTING_POINT_B));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ExtendedLinkTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test getting the documentation fragments in the extended link.
	 */
	public void testGetDocumentations() {
		try {
			FragmentList<Fragment> documentations = store.<Fragment>getFragments("XlinkDocumentation");
			for (int i=0; i<documentations.getLength(); i++) {
				Fragment documentation = documentations.getFragment(0);
				Fragment parent = documentation.getParent();
				if (parent.getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"type").equals("extended")) {
					ExtendedLink link = (ExtendedLink) parent;
					FragmentList<XlinkDocumentation> fragments = link.getDocumentations();
					assertEquals(1, fragments.getLength());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting the locators with a given label.
	 */
	public void testGetLocatorsByLabel() {
		try {
			FragmentList<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.getFragment(0);
			FragmentList<Locator> locators = link.getLocatorsByLabel("summationItem");
			assertEquals(1, locators.getLength());
			locators = link.getLocatorsByLabel("contributingItem");
			assertEquals(2, locators.getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test getting the arc ends with a given label.
	 */
	public void testGetArcEndsByLabel() {
		try {
			FragmentList<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.getFragment(0);
			FragmentList<ArcEnd> arcends = link.getArcEndsByLabel("summationItem");
			assertEquals(1, arcends.getLength());
			arcends = link.getArcEndsByLabel("contributingItem");
			assertEquals(2, arcends.getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
	/**
	 * Test getting the locators with a absolute Href.
	 */
	public void testGetLocatorsByHref() {
		try {
			FragmentList<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.getFragment(0);
			FragmentList<Locator> locators = link.getLocatorsByHref(configuration.getProperty("test.data.baseURI") + "Common/instance/397-ABC.xsd#B");
			assertEquals(1, locators.getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test getting the arcs in the extended link.
	 */
	public void testGetArcs() {
		try {
			FragmentList<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.getFragment(0);
			FragmentList<Arc> arcs = link.getArcs();		
			assertEquals(1, arcs.getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting the arcs with a given from label.
	 */
	public void testGetArcsByFromLabel() {
		try {
			FragmentList<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.getFragment(0);
			FragmentList<Arc> arcs = link.getArcsByFromLabel("summationItem");
			assertEquals(1, arcs.getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test getting the arcs with a given from label.
	 */
	public void testGetArcsByToLabel() {
		try {
			FragmentList<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.getFragment(0);
			FragmentList<Arc> arcs = link.getArcsByToLabel("contributingItem");
			assertEquals(1, arcs.getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
	/**
	 * Test getting the locators in the extended link.
	 */
	public void testGetLocators() {
		try {
			FragmentList<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.getFragment(0);
			FragmentList<Locator> locators = link.getLocators();		
			assertEquals(3, locators.getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
	/**
	 * Test getting the arcs in the extended link.
	 */
	public void testGetResources() {
		try {
			FragmentList<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.getFragment(1);
			FragmentList<Resource> resources = link.getResources();		
			assertEquals(1, resources.getLength());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting the arcs with a given from label.
	 */
	public void testGetResourcesByLabel() {
		try {
            FragmentList<ExtendedLink> links = store.<ExtendedLink>getFragments("ExtendedLink");
		    for (ExtendedLink link: links) {
		        FragmentList<Resource> resources = link.getResources();
		        for (Resource resource: resources) {
		            assertTrue(link.getResourcesByLabel(resource.getLabel()).getLength() > 0);
		        }
		    }
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
}
