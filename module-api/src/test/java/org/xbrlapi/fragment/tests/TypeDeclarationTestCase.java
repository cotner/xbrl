package org.xbrlapi.fragment.tests;

import org.xbrlapi.ComplexTypeDeclaration;
import org.xbrlapi.DOMLoadingTestCase;
import org.xbrlapi.Schema;
import org.xbrlapi.TypeDeclaration;
import org.xbrlapi.utilities.Constants;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class TypeDeclarationTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "test.data.tuple.instance";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}	
	
	public TypeDeclarationTestCase(String arg0) {
		super(arg0);
	}
    
    public void testTypeDerivationAnalysis() {
        try {
            Schema schema = store.getSchema(Constants.XBRL21Namespace);
            ComplexTypeDeclaration monetaryItemType = schema.getGlobalDeclaration("monetaryItemType");
            TypeDeclaration parentType = monetaryItemType.getParentType();
            assertNotNull(parentType);
            assertTrue(monetaryItemType.isDerivedFrom(parentType));
            assertTrue(monetaryItemType.isDerivedFrom(parentType.getTargetNamespace(),parentType.getName()));
            assertTrue(monetaryItemType.isDerivedFrom(Constants.XMLSchemaNamespace,"decimal"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("1: " + e.getMessage());
        }
    }
    
}
