package org.xbrlapi.fragment.tests;

import org.xbrlapi.DOMLoadingTestCase;
import java.util.List;
import org.xbrlapi.Language;

/**
 * Tests the implementation of the org.xbrlapi.Language interface.
 * Uses the DOM-based data store to ensure rapid testing.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class LanguageTestCase extends DOMLoadingTestCase {
	private final String STARTING_POINT = "real.data.languages";
	
	protected void setUp() throws Exception {
		super.setUp();
		loader.discover(this.getURI(STARTING_POINT));		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
	}	
	
	public LanguageTestCase(String arg0) {
		super(arg0);
	}

	public void testLanguagePropertyAccessors() {

		try {
			List<Language> fragments = store.<Language>getXMLResources("Language");
			Language fragment = fragments.get(0);
			assertEquals("Afar", fragment.getName());
			assertEquals("aa", fragment.getCode());
			assertEquals("en", fragment.getLanguage());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void testGetLanguageName() {

		try {
			Language language = store.getLanguage("EN","EN");
			assertEquals("English", language.getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	

	
	
}
