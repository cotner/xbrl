package org.xbrlapi.fragment.tests;

import java.util.List;

import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.LabelResource;
import org.xbrlapi.Resource;

/**
 * Tests the implementation of the org.xbrlapi.Resource interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ResourceLanguageAttributeTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.local.xbrl.xml.lang.attribute.inheritance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ResourceLanguageAttributeTestCase(String arg0) {
		super(arg0);
	}
	
	public void testGettingInheritedLanguageCodes() {	

		try {
			List<LabelResource> resources = store.<LabelResource>getXMLResources("LabelResource");
			int foundLanguages = 0;
			assertEquals(3, resources.size());
			for (LabelResource resource: resources) {
			    resource.serialize();
                assertNotNull(" No language code for label : " + resource.getStringValue(), resource.getLanguage());
			    logger.info(resource.getStringValue() + "(" + resource.getLanguage() + ")");
			    if (resource.getLanguage().equals("en")) {
			        assertEquals("English", resource.getStringValue());
			        foundLanguages++;
			    } else if (resource.getLanguage().equals("fr")) {
                    assertEquals("French", resource.getStringValue());
                    foundLanguages++;
			    } else if (resource.getLanguage().equals("de")) {
                    assertEquals("German", resource.getStringValue());
                    foundLanguages++;
			    } else fail(resource.getLanguage() + " is unexpected.");
			}
            assertEquals(3,foundLanguages);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting resource title attribute value.
	 */
	public void testGetTitleAttribute() {	

		try {
			List<Resource> fragments = store.<Resource>queryForXMLResources("#roots#[*/*/@xlink:type='resource']");
			Resource fragment = fragments.get(0);
			assertEquals("label_CurrentAsset", fragment.getTitleAttribute());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	

}
