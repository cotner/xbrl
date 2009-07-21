package org.xbrlapi.data.resource.tests;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net) 
 */

import org.xbrlapi.data.resource.MD5SignerImpl;
import org.xbrlapi.utilities.BaseTestCase;


public class SignerSerializationTestCase extends BaseTestCase {
    
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public SignerSerializationTestCase(String arg0) {
		super(arg0);
	}

    public final void testMD5SignerSerialization() {
        try {
            Object object = new MD5SignerImpl();
            Object copy = getDeepCopy(object);
            this.assessCustomEquality(object,copy);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception. " + e.getMessage());
        }
    }	
	


	
}
