package org.xbrlapi.fragment.tests;

/**
 * Tests the implementation of the org.xbrlapi.ElementDeclaration interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.net.URI;
import java.util.List;

import org.xbrlapi.Concept;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.ElementDeclaration;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

public class ElementDeclarationTestCase extends DOMLoadingTestCase {

	private final String STARTING_POINT = "test.data.multi.concept.schema";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	private String documentStart = 
		"<?xml version=\"1.0\"?>\n"
		+"<schema targetNamespace=\"http://www.xbrlapi.org/targetNamespace/\"\n"
		+  "xmlns:tns=\"http://www.xbrlapi.org/targetNamespace/\"\n"
		+  "xmlns=\"http://www.w3.org/2001/XMLSchema\"\n"
		+  "xmlns:xbrli=\"http://www.xbrl.org/2003/instance\"\n"
		+  "xmlns:link=\"http://www.xbrl.org/2003/linkbase\"\n"
		+  "xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n" 
		+  "elementFormDefault=\"qualified\">\n"

		+	"<import namespace=\"http://www.xbrl.org/2003/instance\"\n" 
		+          "schemaLocation=\"http://www.xbrl.org/2003/xbrl-instance-2003-12-31.xsd\"/>\n"
		+	"<element  name=\"A\"\n" 
		+            "id=\"A\"\n"
		+            "type=\"xbrli:monetaryItemType\"\n"
		+            "substitutionGroup=\"xbrli:item\"\n";
		
	private String documentEnd = "xbrli:periodType=\"instant\"/>\n</schema>\n";

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public ElementDeclarationTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test retrieval of the value for the nillable attribute.
	 */
	public void testGetNillable() {		
		try {
			String xml = documentStart + documentEnd;
			URI uri = new URI("http://www.xbrlapi.org/default.xsd");
			
			loader.discover(uri,xml);
		
			List<ElementDeclaration> fragments = store.<ElementDeclaration>query("/" + Constants.XBRLAPIPrefix + ":" + "fragment[" + Constants.XBRLAPIPrefix + ":" + "data/xsd:element/@nillable='true']");
			for (int i=0; i< fragments.size(); i++) {
				ElementDeclaration fragment = fragments.get(i);
				assertTrue(fragment.getIndex() + " is nillable", fragment.isNillable());				
			}
			fragments = store.<ElementDeclaration>query("/" + Constants.XBRLAPIPrefix + ":" + "fragment[" + Constants.XBRLAPIPrefix + ":" + "data/xsd:element/@nillable='false']");
			for (int i=0; i< fragments.size(); i++) {
				ElementDeclaration fragment = fragments.get(i);
				assertFalse(fragment.getIndex() + " is not nillable", fragment.isNillable());				
			}

			fragments = store.<ElementDeclaration>query("/" + Constants.XBRLAPIPrefix + ":" + "fragment/" + Constants.XBRLAPIPrefix + ":" + "data/xsd:element[count(@nillable)=0]");
			for (int i=0; i< fragments.size(); i++) {
				ElementDeclaration fragment = fragments.get(i);
				assertFalse(fragment.getIndex() + " is not nillable", fragment.isNillable());		
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Test retrieval of the value for the nillable attribute.
	 */
	public void testGetFormQualified() {		
		try {
			List<ElementDeclaration> fragments = store.<ElementDeclaration>gets("ElementDeclaration");
			for (int i=0; i< fragments.size(); i++) {
				ElementDeclaration fragment = fragments.get(i);
				assertTrue(fragment.getIndex() + " is element form qualified", fragment.getSchema().isElementFormQualified());				
			}
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
		
	}	
	
	
	/**
	 * Test retrieval of the value for the default attribute.
	 */
	public void testGetDefault() {		

		try {
			String xml = 
				documentStart +
				" default=\"12.4\" " + 
				documentEnd;
			URI uri = new URI("http://www.xbrlapi.org/default.xsd");
			loader.discover(uri,xml);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		
		// Missing default attribute
		try {
			List<ElementDeclaration> fragments = store.<ElementDeclaration>query("/" + Constants.XBRLAPIPrefix + ":" + "fragment/" + Constants.XBRLAPIPrefix + ":" + "data/xsd:element[count(@default)=0]");
			for (int i=0; i< fragments.size(); i++) {
				ElementDeclaration fragment = fragments.get(i);
				assertNull(fragment.getIndex() + " has not default", fragment.getDefault());				
			}
		} catch (XBRLException e) {
			fail(e.getMessage());
		}

		// Available attribute
		try {
			List<ElementDeclaration> fragments = store.<ElementDeclaration>query("/" + Constants.XBRLAPIPrefix + ":" + "fragment/" + Constants.XBRLAPIPrefix + ":" + "data/xsd:element[count(@default)=1]");
			for (int i=0; i< fragments.size(); i++) {
				ElementDeclaration fragment = fragments.get(i);
				assertNotNull(fragment.getIndex() + " has not default", fragment.getDefault());				
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Test retrieval of the value for the fixed attribute.
	 */
	public void testGetFixed() {		

		// Missing default attribute
		try {
			List<ElementDeclaration> fragments = store.<ElementDeclaration>gets("ElementDeclaration");
			ElementDeclaration fragment = fragments.get(0);
			assertNull(fragment.getFixed());
		} catch (XBRLException e) {
			fail(e.getMessage());
		}

		// Available attribute
		try {
			String xml = 
				documentStart +
				" fixed=\"12.4\" " + 
				documentEnd;
			URI uri = new URI("http://www.xbrlapi.org/default.xsd");
			loader.discover(uri,xml);

			String query = "/*[" + Constants.XBRLAPIPrefix + ":" + "data/*/@fixed='12.4']";
			List<ElementDeclaration> fragments = store.<ElementDeclaration>query(query);
			ElementDeclaration fragment = fragments.get(0);
			store.serialize(fragment.getMetadataRootElement());
			assertEquals("12.4", fragment.getFixed());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Test getting the information about the data type.
	 */
	public void testGetTypeInformation() {	
		try {
		    
            List<Concept> fragments = store.gets("Concept");
            assertTrue(fragments.size() > 0);
            for (Concept fragment: fragments) {

                String type = fragment.getDataRootElement().getAttribute("type");
                assertEquals(type, fragment.getTypeQName());

                String prefix = fragment.getPrefixFromQName(type);
                assertNotNull(prefix);
                assertEquals(Constants.XBRL21Prefix , fragment.getTypeNamespaceAlias());

                URI namespace = fragment.getNamespaceFromQName(type,fragment.getDataRootElement());
                assertEquals(namespace, fragment.getTypeNamespace());
                
                String localname = fragment.getLocalnameFromQName(type);
                assertEquals(fragment.getTypeLocalname(), localname);
                
            }

			
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test getting the information about the substitution group.
	 */
	public void testGetSubstitutionGroupInformation() {		
	    try {
	        
            List<Concept> fragments = store.gets("Concept");
            assertTrue(fragments.size() > 0);
            for (Concept fragment: fragments) {

                String sg = fragment.getDataRootElement().getAttribute("substitutionGroup");
                assertEquals(sg, fragment.getSubstitutionGroupQName());

                String prefix = fragment.getPrefixFromQName(sg);
                assertNotNull(prefix);
                assertEquals(Constants.XBRL21Prefix , fragment.getSubstitutionGroupNamespaceAlias());

                URI namespace = fragment.getNamespaceFromQName(sg,fragment.getDataRootElement());
                assertEquals(namespace, fragment.getSubstitutionGroupNamespace());
                
                String localname = fragment.getLocalnameFromQName(sg);
                assertEquals(fragment.getSubstitutionGroupLocalname(), localname);
                
            }
			
		} catch (XBRLException e) {
			fail(e.getMessage());
		}
	}
		
    public void testDerterminationOfBeingATuple() {     
        try {
            List<Concept> fragments = store.gets("Concept");
            assertTrue(fragments.size() > 0);
            for (Concept fragment: fragments) {
                logger.info("Tuple" + fragment.isTuple());
                logger.info("Item" + fragment.isItem());
                assertTrue(fragment.isTuple() || fragment.isItem());
            }
        } catch (XBRLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
		
		
	}	
	

}
