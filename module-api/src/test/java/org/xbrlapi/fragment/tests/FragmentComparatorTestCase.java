package org.xbrlapi.fragment.tests;

import org.xbrlapi.impl.FragmentComparator;
import org.xbrlapi.impl.MockFragmentImpl;
import org.xbrlapi.utilities.BaseTestCase;

/**
 * Tests the fragment comparator implementation.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FragmentComparatorTestCase extends BaseTestCase {

	MockFragmentImpl f1;
	MockFragmentImpl f2;
	MockFragmentImpl f3;
	MockFragmentImpl f4;
	MockFragmentImpl f5;
	MockFragmentImpl f6;
	FragmentComparator comparator;
	
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		comparator = new FragmentComparator();
		
		f1 = new MockFragmentImpl("10");
		f1.setURL("http://urlA/");
		f1.setSequenceToParentElement("");
		f1.setParentIndex("1");
		f1.setPrecedingSiblings("0");
		
		f2 = new MockFragmentImpl("11");
		f2.setURL("http://urlA/");
		f2.setSequenceToParentElement("");
		f2.setParentIndex("1");
		f2.setPrecedingSiblings("0");
		
		f3 = new MockFragmentImpl("12");
		f3.setURL("http://urlA/");
		f3.setSequenceToParentElement("");
		f3.setParentIndex("1");
		f3.setPrecedingSiblings("1");

		f4 = new MockFragmentImpl("13");
		f4.setURL("http://urlA/");
		f4.setSequenceToParentElement("");
		f4.setParentIndex("2");
		f4.setPrecedingSiblings("0");
		
		f5 = new MockFragmentImpl("14");
		f5.setURL("http://urlA/");
		f5.setSequenceToParentElement("68 2");
		f5.setParentIndex("1");
		f5.setPrecedingSiblings("0");		

		f6 = new MockFragmentImpl("15");
		f6.setURL("http://urlB/");
		f6.setSequenceToParentElement("");
		f6.setParentIndex("1");
		f6.setPrecedingSiblings("0");		
		
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public FragmentComparatorTestCase(String arg0) {
		super(arg0);
	}

	/**
	 * Test the comparison of fragment with itself.
	 */
	public void testCompareFragmentWithItself() {
		assertEquals(0,this.comparator.compare(f1,f1));
	}

	/**
	 * Test the comparison of two equivalent fragments
	 */
	public void testCompareEquivalentFragments() {
		assertEquals(0,this.comparator.compare(f1,f2));
	}
	
	/**
	 * Test the comparison of two equivalent fragments
	 */
	public void testCompareFragmentsWithDifferentPrecedingSiblings() {
		assertTrue(this.comparator.compare(f1,f3) < 0);
	}

	/**
	 * Test the comparison of two equivalent fragments
	 */
	public void testCompareFragmentsWithDifferentParents() {
		assertTrue(this.comparator.compare(f1,f4) < 0);
	}

	/**
	 * Test the comparison of two equivalent fragments
	 */
	public void testCompareFragmentsWithDifferentXPathsToContainerElements() {
		assertTrue(this.comparator.compare(f1,f5) < 0);
	}
	
	/**
	 * Test the comparison of two equivalent fragments
	 */
	public void testCompareFragmentsWithDifferentURLs() {
		assertTrue(this.comparator.compare(f1,f6) < 0);
	}

	
	
}
