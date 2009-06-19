package org.xbrlapi.fragment.tests;

import java.util.List;

import org.xbrlapi.Arc;
import org.xbrlapi.ArcEnd;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
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
			List<Fragment> documentations = store.<Fragment>getFragments("XlinkDocumentation");
			for (int i=0; i<documentations.size(); i++) {
				Fragment documentation = documentations.get(0);
				Fragment parent = documentation.getParent();
				if (parent.getDataRootElement().getAttributeNS(Constants.XLinkNamespace,"type").equals("extended")) {
					ExtendedLink link = (ExtendedLink) parent;
					List<XlinkDocumentation> fragments = link.getDocumentations();
					assertEquals(1, fragments.size());
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
			List<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.get(0);
			List<Locator> locators = link.getLocatorsWithLabel("summationItem");
			assertEquals(1, locators.size());
			locators = link.getLocatorsWithLabel("contributingItem");
			assertEquals(2, locators.size());
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
			List<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.get(0);
			List<ArcEnd> arcends = link.getArcEndsWithLabel("summationItem");
			assertEquals(1, arcends.size());
			arcends = link.getArcEndsWithLabel("contributingItem");
			assertEquals(2, arcends.size());
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
			List<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.get(0);
			List<Locator> locators = link.getLocatorsWithHref(configuration.getProperty("test.data.baseURI") + "Common/instance/397-ABC.xsd#B");
			assertEquals(1, locators.size());
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
			List<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.get(0);
			List<Arc> arcs = link.getArcs();		
			assertEquals(1, arcs.size());
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
			List<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.get(0);
			List<Arc> arcs = link.getArcsWithFromLabel("summationItem");
			assertEquals(1, arcs.size());
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
			List<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.get(0);
			List<Arc> arcs = link.getArcsWithToLabel("contributingItem");
			assertEquals(1, arcs.size());
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
			List<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.get(0);
			List<Locator> locators = link.getLocators();		
			assertEquals(3, locators.size());
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
			List<ExtendedLink> fragments = store.<ExtendedLink>getFragments("ExtendedLink");
			ExtendedLink link = fragments.get(1);
			List<Resource> resources = link.getResources();		
			assertEquals(1, resources.size());
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
            List<ExtendedLink> links = store.<ExtendedLink>getFragments("ExtendedLink");
		    for (ExtendedLink link: links) {
		        List<Resource> resources = link.getResources();
		        for (Resource resource: resources) {
		            assertTrue(link.getResourcesWithLabel(resource.getLabel()).size() > 0);
		        }
		    }
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
}
