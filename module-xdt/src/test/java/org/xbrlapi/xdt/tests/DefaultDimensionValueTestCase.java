package org.xbrlapi.xdt.tests;

/**
 * Tests the identification of XDT fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class DefaultDimensionValueTestCase extends BaseTestCase {

    private final String STARTING_POINT = "test.data.xdt.default.dimension.values";
    
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
	}	
	
	public DefaultDimensionValueTestCase(String arg0) {
		super(arg0);
	}

	public void testDefaultDimensionValue() {

		try {
	        loader.discover(this.getURL(STARTING_POINT));
	        fail("Default dimension value handling has not yet been implemented.");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
