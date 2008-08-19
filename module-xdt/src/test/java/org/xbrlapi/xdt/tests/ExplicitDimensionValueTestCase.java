package org.xbrlapi.xdt.tests;

/**
 * Tests the identification of XDT fragments.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class ExplicitDimensionValueTestCase extends BaseTestCase {

    private final String STARTING_POINT = "test.data.xdt.explicit.dimension.values";
    
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
	}	
	
	public ExplicitDimensionValueTestCase(String arg0) {
		super(arg0);
	}

	public void testExplicitDimensionValue() {

		try {
	        loader.discover(this.getURL(STARTING_POINT));
	        fail("Explicit dimension value handling has not yet been implemented.");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
