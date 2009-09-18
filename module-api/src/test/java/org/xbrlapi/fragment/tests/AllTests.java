package org.xbrlapi.fragment.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.xbrlapi.fragment.tests");
		//$JUnit-BEGIN$
        suite.addTestSuite(ArcEndTestCase.class);
        suite.addTestSuite(ArcroleTypeTestCase.class);
        suite.addTestSuite(ArcSemanticKeyTestCase.class);
		suite.addTestSuite(ArcTestCase.class);
        suite.addTestSuite(ConceptTestCase.class);
        suite.addTestSuite(ContextComponentTestCase.class);
        suite.addTestSuite(ContextTestCase.class);
        suite.addTestSuite(FragmentTestCase.class);
		suite.addTestSuite(FactDimensionContainerTestCase.class);
		suite.addTestSuite(ReferencePartTestCase.class);
        suite.addTestSuite(ReferencePartDeclarationTestCase.class);
		suite.addTestSuite(ElementDeclarationTestCase.class);
        suite.addTestSuite(EntityResourceTestCase.class);
		suite.addTestSuite(Fragment_LoaderDependentTestCase.class);
		suite.addTestSuite(ReferenceResourceTestCase.class);
		suite.addTestSuite(SchemaContentTestCase.class);
		suite.addTestSuite(LabelResourceTestCase.class);
        suite.addTestSuite(ResourceLanguageAttributeTestCase.class);
		suite.addTestSuite(ResourceTestCase.class);
		suite.addTestSuite(ExtendedLinkContentTestCase.class);
		suite.addTestSuite(PeriodTestCase.class);
		suite.addTestSuite(NumericItemTestCase.class);
		suite.addTestSuite(InstanceTestCase.class);
		suite.addTestSuite(XlinkTestCase.class);
		suite.addTestSuite(EntityTestCase.class);
		suite.addTestSuite(SimpleLinkTestCase.class);
		suite.addTestSuite(MixedContentResourceTestCase.class);
		suite.addTestSuite(UsedOnTestCase.class);
		suite.addTestSuite(SchemaTestCase.class);
		suite.addTestSuite(FactTestCase.class);
		suite.addTestSuite(FragmentComparatorTestCase.class);
		suite.addTestSuite(Fragment_LoaderIndependentTestCase.class);
		suite.addTestSuite(XlinkDocumentationTestCase.class);
		suite.addTestSuite(SegmentTestCase.class);
		suite.addTestSuite(TitleTestCase.class);
		suite.addTestSuite(ItemTestCase.class);
		suite.addTestSuite(CustomTypeTestCase.class);
		suite.addTestSuite(SimpleNumericItemTestCase.class);
		suite.addTestSuite(ScenarioTestCase.class);
		suite.addTestSuite(ExtendedLinkTestCase.class);
		suite.addTestSuite(UnitTestCase.class);
		suite.addTestSuite(OpenContextComponentTestCase.class);
        suite.addTestSuite(LanguageTestCase.class);
		suite.addTestSuite(LinkTestCase.class);
		suite.addTestSuite(LinkbaseTestCase.class);
        suite.addTestSuite(LocatorTestCase.class);
		suite.addTestSuite(TupleTestCase.class);
		suite.addTestSuite(NonNumericItemTestCase.class);
        suite.addTestSuite(TypeDeclarationTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
