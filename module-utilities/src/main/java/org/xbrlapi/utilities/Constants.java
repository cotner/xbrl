package org.xbrlapi.utilities;

/**
 * Defines a range of constants (namespaces etc)
 * that are used throughout the XBRLAPI implementation
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public interface Constants {

	/**
	 * The URL of the Standard XBRL 2.1 role declarations schema.
	 */
	final static String ROLES_URL = "http://www.xbrlapi.org/xbrl/xbrl-2.1-roles.xsd";		
	
	/**
	 * XMLNamespace is the namespace for the XML elements and attributes.
	 * XMLPrefix is the prefix for XML elements and attributes.
	 */
	final static String XMLNamespace = "http://www.w3.org/XML/1998/namespace";
	final static String XMLPrefix = "xml";
	
	/**
	 * XLinkNamespace is the namespace for the XLink.
	 */

	final static String XLinkNamespace = "http://www.w3.org/1999/xlink";
	final static String XLinkPrefix = "xlink";

	/**
	 * XBRLNamespace is the namespace for the XBRL Instance elements
	 */
	final static String XBRL21Namespace = "http://www.xbrl.org/2003/instance";
	final static String XBRL21Prefix = "xbrli";

	/**
	 * XBRLLinkNamespace is the namespace for the XBRL Link elements
	 */
	final static String XBRL21LinkNamespace = "http://www.xbrl.org/2003/linkbase";
	
    /**
     * The namespace of the generic link and generic arc elements
     */
    final static String GenericLinkNamespace = " http://xbrl.org/2008/generic";	

    /**
     * The prefix for the generic link and generic arc elements
     */
    final static String GenericLinkPrefix = "gen";
    
    /**
     * The prefix for the XBRL 2.1 link elements
     */
	final static String XBRL21LinkPrefix = "link";

	final static String GenericLabelNamespace = "http://xbrl.org/2007/label";
	final static String GenericLabelPrefix = "genlab";
	
	final static String GenericReferenceNamespace = "http://xbrl.org/2007/reference";
	final static String GenericReferencePrefix = "genref";
	
	/**
	 * XBRLAPI Namespace is the namespace for the elements and attributes
	 * specific to DTSImpl composite documents
	 */
	final static String XBRLAPINamespace = "http://xbrlapi.org/";
	final static String XBRLAPIPrefix = "xbrlapi";

    /**
     * XBRLAPI Namespace is the namespace for the elements and attributes
     * specific to DTSImpl composite documents
     */
    final static String XBRLAPIEntitiesNamespace = "http://xbrlapi.org/entities";
    final static String XBRLAPIEntitiesPrefix = "entity";
    final static String XBRLAPIEquivalentEntitiesArcrole = "http://xbrlapi.org/arcrole/equivalent-entity";

	/**
	 * Namespace for the XBRLAPI defined language codes namespace.
	 */
	final static String XBRLAPILanguagesNamespace = "http://xbrlapi.org/rfc1766/languages";
	final static String XBRLAPILanguagesPrefix = "lang";	

	/**
	 * Composite document namespace as used by the composite documents
	 * produced by <a href="http://www.sourceforge.net/xbrlcomposer/">XBRLComposer</a>.
	 */
	final static String CompNamespace = "http://xbrlcomposer.sourceforge.net/2003/compiled/";
	final static String CompPrefix = "comp";
	
	/**
	 * XMLSchemaNamespace is the namespace for the elements and attributes
	 * specific to XML Schema
	 */
	final static String XMLSchemaNamespace = "http://www.w3.org/2001/XMLSchema";
	final static String XMLSchemaPrefix = "xsd";

	/**
	 * XMLSchemaNamespace is the namespace for the elements and attributes
	 * specific to XML Schema.
	 * TODO Add XMLSchemaInstance namespaces into namespace resolvers and indexing systems etc.
	 */
	final static String XMLSchemaInstanceNamespace = "http://www.w3.org/2001/XMLSchema-instance";
	final static String XMLSchemaInstancePrefix = "xsi";
	
	/**
	 * Constant used to configure the properties of the SAX XML reader.
	 */
	final static String JAXP_SCHEMA_LANGUAGE =
	    "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	
	/**
	 * Constant used to configure the properties of the SAX XML reader.
	 */
	final static String W3C_XML_SCHEMA =
	    "http://www.w3.org/2001/XMLSchema";
	
	/**
	 * The local name of the root element for each fragment
	 */
	final static String FragmentRootElementName = "fragment";
	final static String FragmentDataContainerElementName = "data";

	/**
	 * XBRL 2.1 arcroles
	 */
	final static String LabelArcRole = "http://www.xbrl.org/2003/arcrole/concept-label";
    final static String GenericLabelArcRole = "http://xbrl.org/arcrole/2008/element-label";
	final static String ReferenceArcRole = "http://www.xbrl.org/2003/arcrole/concept-reference";
    final static String GenericReferenceArcRole = "http://xbrl.org/arcrole/2008/element-reference";
	final static String CalculationArcRole = "http://www.xbrl.org/2003/arcrole/summation-item";
	final static String PresentationArcRole = "http://www.xbrl.org/2003/arcrole/parent-child";
	final static String GeneralSpecialArcRole = "http://www.xbrl.org/2003/arcrole/general-special";
	final static String EssenceAliasArcRole = "http://www.xbrl.org/2003/arcrole/essence-alias";
	final static String SimilarTuplesArcRole = "http://www.xbrl.org/2003/arcrole/similar-tuples";
	final static String RequiresElementArcRole = "http://www.xbrl.org/2003/arcrole/requires-element";
	final static String FactFootnoteArcRole = "http://www.xbrl.org/2003/arcrole/fact-footnote";

	/**
	 * XBRL 2.1 label roles
	 */
	final static String StandardLabelRole = "http://www.xbrl.org/2003/role/label";
	final static String TerseLabelRole = "http://www.xbrl.org/2003/role/terseLabel";
	final static String VerboseLabelRole = "http://www.xbrl.org/2003/role/verboseLabel";
	final static String PositiveLabelRole = "http://www.xbrl.org/2003/role/positiveLabel";
	final static String PositiveTerseLabelRole = "http://www.xbrl.org/2003/role/positiveTerseLabel";
	final static String PositiveVerboseLabelRole = "http://www.xbrl.org/2003/role/positiveVerboseLabel";
	final static String NegativeLabelRole = "http://www.xbrl.org/2003/role/negativeLabel";
	final static String NegativeTerseLabelRole = "http://www.xbrl.org/2003/role/negativeTerseLabel";
	final static String NegativeVerboseLabelRole = "http://www.xbrl.org/2003/role/negativeVerboseLabel";
	final static String ZeroLabelRole = "http://www.xbrl.org/2003/role/zeroLabel";
	final static String ZeroTerseLabelRole = "http://www.xbrl.org/2003/role/zeroTerseLabel";
	final static String ZeroVerboseLabelRole = "http://www.xbrl.org/2003/role/zeroVerboseLabel";
	final static String TotalLabelRole = "http://www.xbrl.org/2003/role/totalLabel";
	final static String PeriodStartLabelRole = "http://www.xbrl.org/2003/role/periodStartLabel";
	final static String PeriodEndLabelRole = "http://www.xbrl.org/2003/role/periodEndLabel";
	final static String DocumentationLabelRole = "http://www.xbrl.org/2003/role/documentationLabel";
	final static String DefinitionGuidanceLabelRole = "http://www.xbrl.org/2003/role/definitionGuidanceLabel";
	final static String DisclosureGuidanceLabelRole = "http://www.xbrl.org/2003/role/disclosureGuidanceLabel";
	final static String PresentationGuidanceLabelRole = "http://www.xbrl.org/2003/role/presentationGuidanceLabel";
	final static String MeasurementGuidanceLabelRole = "http://www.xbrl.org/2003/role/measurementGuidanceLabel";
	final static String CommentaryGuidanceLabelRole = "http://www.xbrl.org/2003/role/commentaryGuidanceLabel";
	final static String ExampleGuidanceLabelRole = "http://www.xbrl.org/2003/role/exampleGuidanceLabel";

	/**
	 * Reference roles
	 */
	final static String StandardReferenceRole = "http://www.xbrl.org/2003/role/reference";
	final static String DefinitionReferenceRole = "http://www.xbrl.org/2003/role/definitionRef";
	final static String DisclosureReferenceRole = "http://www.xbrl.org/2003/role/disclosureRef";
	final static String MandatoryDisclosureReferenceRole = "http://www.xbrl.org/2003/role/mandatoryDisclosureRef";
	final static String RecommendedDisclosureReferenceRole = "http://www.xbrl.org/2003/role/recommendedDisclosureRef";
	final static String UnspecifiedDisclosureReferenceRole = "http://www.xbrl.org/2003/role/unspecifiedDisclosureGuidanceLabel";
	final static String PresentationReferenceRole = "http://www.xbrl.org/2003/role/presentationRef";
	final static String MeasurementReferenceRole = "http://www.xbrl.org/2003/role/measurementRef";
	final static String CommentaryReferenceRole = "http://www.xbrl.org/2003/role/commentaryRef";
	final static String ExampleReferenceRole = "http://www.xbrl.org/2003/role/exampleRef";
	
	/**
	 * Link roles
	 */
	final static String StandardLinkRole = "http://www.xbrl.org/2003/role/link";
}
