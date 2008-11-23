package org.xbrlapi.utilities;

/**
 * Defines a range of constants (namespaces etc) that are used throughout the
 * XBRLAPI implementation
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class Constants {

    /**
     * XMLNamespace is the namespace for the XML elements and attributes.
     * XMLPrefix is the prefix for XML elements and attributes.
     */
    public final  static String XMLNamespace = "http://www.w3.org/XML/1998/namespace";
    public final  static String XMLPrefix = "xml";

    /**
     * XLinkNamespace is the namespace for the XLink.
     */

    public final  static String XLinkNamespace = "http://www.w3.org/1999/xlink";
    public final  static String XLinkPrefix = "xlink";

    /**
     * XBRLNamespace is the namespace for the XBRL Instance elements
     */
    public final  static String XBRL21Namespace = "http://www.xbrl.org/2003/instance";
    public final  static String XBRL21Prefix = "xbrli";

    /**
     * XBRLLinkNamespace is the namespace for the XBRL Link elements
     */
    public final  static String XBRL21LinkNamespace = "http://www.xbrl.org/2003/linkbase";

    /**
     * The namespace of the generic link and generic arc elements
     */
    public final  static String GenericLinkNamespace = " http://xbrl.org/2008/generic";

    /**
     * The prefix for the generic link and generic arc elements
     */
    public final  static String GenericLinkPrefix = "gen";

    /**
     * The prefix for the XBRL 2.1 link elements
     */
    public final  static String XBRL21LinkPrefix = "link";

    public final  static String GenericLabelNamespace = "http://xbrl.org/2008/label";
    public final  static String GenericLabelPrefix = "genlab";

    public final  static String GenericReferenceNamespace = "http://xbrl.org/2008/reference";
    public final  static String GenericReferencePrefix = "genref";

    /**
     * XBRLAPI Namespace is the namespace for the elements and attributes
     * specific to DTSImpl composite documents
     */
    public final  static String XBRLAPINamespace = "http://xbrlapi.org/";
    public final  static String XBRLAPIPrefix = "xbrlapi";

    /**
     * XBRLAPI Namespace is the namespace for the elements and attributes
     * specific to DTSImpl composite documents
     */
    public final  static String XBRLAPIEntitiesNamespace = "http://xbrlapi.org/entities";
    public final  static String XBRLAPIEntitiesPrefix = "entity";
    public final  static String XBRLAPIEquivalentEntitiesArcrole = "http://xbrlapi.org/arcrole/equivalent-entity";

    /**
     * Namespace for the XBRLAPI defined language codes namespace.
     */
    public final  static String XBRLAPILanguagesNamespace = "http://xbrlapi.org/rfc1766/languages";
    public final  static String XBRLAPILanguagesPrefix = "lang";

    /**
     * Composite document namespace as used by the composite documents produced
     * by <a href="http://www.sourceforge.net/xbrlcomposer/">XBRLComposer</a>.
     */
    public final  static String CompNamespace = "http://xbrlcomposer.sourceforge.net/2003/compiled/";
    public final  static String CompPrefix = "comp";

    /**
     * XMLSchemaNamespace is the namespace for the elements and attributes
     * specific to XML Schema
     */
    public final  static String XMLSchemaNamespace = "http://www.w3.org/2001/XMLSchema";
    public final  static String XMLSchemaPrefix = "xsd";

    /**
     * XMLSchemaNamespace is the namespace for the elements and attributes
     * specific to XML Schema. TODO Add XMLSchemaInstance namespaces into
     * namespace resolvers and indexing systems etc.
     */
    public final  static String XMLSchemaInstanceNamespace = "http://www.w3.org/2001/XMLSchema-instance";
    public final  static String XMLSchemaInstancePrefix = "xsi";

    /**
     * Constant used to configure the properties of the SAX XML reader.
     */
    public final  static String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /**
     * Constant used to configure the properties of the SAX XML reader.
     */
    public final  static String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    /**
     * The local name of the root element for each fragment
     */
    public final  static String FragmentRootElementName = "fragment";
    public final  static String FragmentDataContainerElementName = "data";

    /**
     * XBRL 2.1 arcroles
     */
    public final  static String LabelArcRole = "http://www.xbrl.org/2003/arcrole/concept-label";
    public final  static String GenericLabelArcRole = "http://xbrl.org/arcrole/2008/element-label";
    public final  static String ReferenceArcRole = "http://www.xbrl.org/2003/arcrole/concept-reference";
    public final  static String GenericReferenceArcRole = "http://xbrl.org/arcrole/2008/element-reference";
    public final  static String CalculationArcRole = "http://www.xbrl.org/2003/arcrole/summation-item";
    public final  static String PresentationArcRole = "http://www.xbrl.org/2003/arcrole/parent-child";
    public final  static String GeneralSpecialArcRole = "http://www.xbrl.org/2003/arcrole/general-special";
    public final  static String EssenceAliasArcRole = "http://www.xbrl.org/2003/arcrole/essence-alias";
    public final  static String SimilarTuplesArcRole = "http://www.xbrl.org/2003/arcrole/similar-tuples";
    public final  static String RequiresElementArcRole = "http://www.xbrl.org/2003/arcrole/requires-element";
    public final  static String FactFootnoteArcRole = "http://www.xbrl.org/2003/arcrole/fact-footnote";

    /**
     * XBRL 2.1 label roles
     */
    public final  static String StandardLabelRole = "http://www.xbrl.org/2003/role/label";
    public final  static String TerseLabelRole = "http://www.xbrl.org/2003/role/terseLabel";
    public final  static String VerboseLabelRole = "http://www.xbrl.org/2003/role/verboseLabel";
    public final  static String PositiveLabelRole = "http://www.xbrl.org/2003/role/positiveLabel";
    public final  static String PositiveTerseLabelRole = "http://www.xbrl.org/2003/role/positiveTerseLabel";
    public final  static String PositiveVerboseLabelRole = "http://www.xbrl.org/2003/role/positiveVerboseLabel";
    public final  static String NegativeLabelRole = "http://www.xbrl.org/2003/role/negativeLabel";
    public final  static String NegativeTerseLabelRole = "http://www.xbrl.org/2003/role/negativeTerseLabel";
    public final  static String NegativeVerboseLabelRole = "http://www.xbrl.org/2003/role/negativeVerboseLabel";
    public final  static String ZeroLabelRole = "http://www.xbrl.org/2003/role/zeroLabel";
    public final  static String ZeroTerseLabelRole = "http://www.xbrl.org/2003/role/zeroTerseLabel";
    public final  static String ZeroVerboseLabelRole = "http://www.xbrl.org/2003/role/zeroVerboseLabel";
    public final  static String TotalLabelRole = "http://www.xbrl.org/2003/role/totalLabel";
    public final  static String PeriodStartLabelRole = "http://www.xbrl.org/2003/role/periodStartLabel";
    public final  static String PeriodEndLabelRole = "http://www.xbrl.org/2003/role/periodEndLabel";
    public final  static String DocumentationLabelRole = "http://www.xbrl.org/2003/role/documentationLabel";
    public final  static String DefinitionGuidanceLabelRole = "http://www.xbrl.org/2003/role/definitionGuidanceLabel";
    public final  static String DisclosureGuidanceLabelRole = "http://www.xbrl.org/2003/role/disclosureGuidanceLabel";
    public final  static String PresentationGuidanceLabelRole = "http://www.xbrl.org/2003/role/presentationGuidanceLabel";
    public final  static String MeasurementGuidanceLabelRole = "http://www.xbrl.org/2003/role/measurementGuidanceLabel";
    public final  static String CommentaryGuidanceLabelRole = "http://www.xbrl.org/2003/role/commentaryGuidanceLabel";
    public final  static String ExampleGuidanceLabelRole = "http://www.xbrl.org/2003/role/exampleGuidanceLabel";

    /**
     * Reference roles
     */
    public final  static String StandardReferenceRole = "http://www.xbrl.org/2003/role/reference";
    public final  static String DefinitionReferenceRole = "http://www.xbrl.org/2003/role/definitionRef";
    public final  static String DisclosureReferenceRole = "http://www.xbrl.org/2003/role/disclosureRef";
    public final  static String MandatoryDisclosureReferenceRole = "http://www.xbrl.org/2003/role/mandatoryDisclosureRef";
    public final  static String RecommendedDisclosureReferenceRole = "http://www.xbrl.org/2003/role/recommendedDisclosureRef";
    public final  static String UnspecifiedDisclosureReferenceRole = "http://www.xbrl.org/2003/role/unspecifiedDisclosureGuidanceLabel";
    public final  static String PresentationReferenceRole = "http://www.xbrl.org/2003/role/presentationRef";
    public final  static String MeasurementReferenceRole = "http://www.xbrl.org/2003/role/measurementRef";
    public final  static String CommentaryReferenceRole = "http://www.xbrl.org/2003/role/commentaryRef";
    public final  static String ExampleReferenceRole = "http://www.xbrl.org/2003/role/exampleRef";

    /**
     * Link roles
     */
    public final  static String StandardLinkRole = "http://www.xbrl.org/2003/role/link";
    
    public final static String ISO4217 = "http://www.xbrl.org/2003/iso4217";
}
