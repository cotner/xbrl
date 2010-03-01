package org.xbrlapi.fragment.tests;

import java.net.URI;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.LabelResource;
import org.xbrlapi.Schema;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.impl.ConceptImpl;
import org.xbrlapi.impl.LabelResourceImpl;
import org.xbrlapi.impl.SchemaImpl;
import org.xbrlapi.impl.SimpleLinkImpl;
import org.xbrlapi.utilities.Constants;

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
			List<LabelResource> resources = store.<LabelResource>getXMLResources(LabelResourceImpl.class);
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
	
    public void testGettingInheritedLanguageCodesInSchemas() {

        try {
            List<Concept> concepts = store.<Concept>getXMLResources(ConceptImpl.class);
            assertEquals(1, concepts.size());

            for (Concept concept: concepts) {
                assertEquals("root", concept.getName());
                assertNotNull(concept.getLanguage());
                assertEquals("en", concept.getLanguage());
            }

            List<SimpleLink> links = store.<SimpleLink>getXMLResources(SimpleLinkImpl.class);
            for (SimpleLink link: links) {
                if (link.getDataRootElement().getLocalName().equals("linkbaseRef")) {
                    assertEquals("fr", link.getLanguage());
                }
            }
            
            List<Schema> schemas = store.<Schema>getXMLResources(SchemaImpl.class);
            for (Schema schema: schemas) {
                if (schema.getTargetNamespace().equals(new URI("http://xbrlapi.org/test/xbrl/005"))) {
                    Element root = schema.getDataRootElement();
                    NodeList annotations = root.getElementsByTagNameNS(Constants.XMLSchemaNamespace.toString(),"annotation");
                    assertEquals(1, annotations.getLength());
                    for (int i=0; i< annotations.getLength(); i++) {
                        Element annotation = (Element) annotations.item(i); 
                        NodeList appinfos = annotation.getElementsByTagNameNS(Constants.XMLSchemaNamespace.toString(),"appinfo");
                        assertEquals(1, appinfos.getLength());
                        for (int j=0; j < appinfos.getLength(); j++) {
                            Element appinfo = (Element) appinfos.item(j); 
                            assertEquals("fr", schema.getLanguage(appinfo));
                        }
                    }
                    
                }
            }
            
        
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
	
	
	

}
