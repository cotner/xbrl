package org.xbrlapi.utilities;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

/**
 * Defines a range of constants (namespaces etc) that are used throughout the
 * XBRLAPI implementation
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */
public class Constants {

    public final static String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    public final static String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    
    public final static String XMLPrefix = "xml";
    public final static String XLinkPrefix = "xlink";
    public final static String XBRL21Prefix = "xbrli";
    public final static String GenericLinkPrefix = "gen";
    public final static String XBRL21LinkPrefix = "link";
    public final static String GenericLabelPrefix = "genlab";
    public final static String GenericReferencePrefix = "genref";
    public final static String XBRLAPIPrefix = "xbrlapi";
    public final static String CompPrefix = "comp";
    public final static String XBRLAPIEntitiesPrefix = "entity";
    public final static String XBRLAPILanguagesPrefix = "lang";
    public final static String XMLSchemaPrefix = "xsd";
    public final static String XMLSchemaInstancePrefix = "xsi";

    public final static String FragmentRootElementName = "fragment";
    public final static String FragmentDataContainerElementName = "data";

    protected static Logger logger = Logger.getLogger(Constants.class);
    
    static {
        
        try {
            XMLNamespace = new URI("http://www.w3.org/XML/1998/namespace");
            XMLNSNamespace = new URI("http://www.w3.org/2000/xmlns/");
            XLinkNamespace = new URI("http://www.w3.org/1999/xlink");
            XBRL21Namespace = new URI("http://www.xbrl.org/2003/instance");
            XBRL21LinkNamespace = new URI("http://www.xbrl.org/2003/linkbase");
            GenericLinkNamespace = new URI("http://xbrl.org/2008/generic");
            GenericLabelNamespace = new URI("http://xbrl.org/2008/label");
            GenericReferenceNamespace = new URI("http://xbrl.org/2008/reference");
            XBRLAPINamespace = new URI("http://xbrlapi.org/");
            CompNamespace = new URI("http://xbrlapi.org/composite");
            XBRLAPIEntitiesNamespace = new URI("http://xbrlapi.org/entities");
            XBRLAPIEquivalentEntitiesArcrole = new URI("http://xbrlapi.org/arcrole/equivalent-entity");
            XBRLAPILanguagesNamespace = new URI("http://xbrlapi.org/rfc1766/languages");    
            XMLSchemaNamespace = new URI("http://www.w3.org/2001/XMLSchema");
            XMLSchemaInstanceNamespace = new URI("http://www.w3.org/2001/XMLSchema-instance");
            LabelArcrole = new URI("http://www.xbrl.org/2003/arcrole/concept-label");
            GenericLabelArcrole = new URI("http://xbrl.org/arcrole/2008/element-label");
            ReferenceArcrole = new URI("http://www.xbrl.org/2003/arcrole/concept-reference");
            GenericReferenceArcrole = new URI("http://xbrl.org/arcrole/2008/element-reference");
            CalculationArcrole = new URI("http://www.xbrl.org/2003/arcrole/summation-item");
            PresentationArcrole = new URI("http://www.xbrl.org/2003/arcrole/parent-child");
            GeneralSpecialArcrole = new URI("http://www.xbrl.org/2003/arcrole/general-special");
            EssenceAliasArcrole = new URI("http://www.xbrl.org/2003/arcrole/essence-alias");
            SimilarTuplesArcrole = new URI("http://www.xbrl.org/2003/arcrole/similar-tuples");
            RequiresElementArcrole = new URI("http://www.xbrl.org/2003/arcrole/requires-element");
            FactFootnoteArcrole = new URI("http://www.xbrl.org/2003/arcrole/fact-footnote");
            StandardLabelRole = new URI("http://www.xbrl.org/2003/role/label");
            TerseLabelRole = new URI("http://www.xbrl.org/2003/role/terseLabel");
            VerboseLabelRole = new URI("http://www.xbrl.org/2003/role/verboseLabel");
            PositiveLabelRole = new URI("http://www.xbrl.org/2003/role/positiveLabel");
            PositiveTerseLabelRole = new URI("http://www.xbrl.org/2003/role/positiveTerseLabel");
            PositiveVerboseLabelRole = new URI("http://www.xbrl.org/2003/role/positiveVerboseLabel");
            NegativeLabelRole = new URI("http://www.xbrl.org/2003/role/negativeLabel");
            NegativeTerseLabelRole = new URI("http://www.xbrl.org/2003/role/negativeTerseLabel");
            NegativeVerboseLabelRole = new URI("http://www.xbrl.org/2003/role/negativeVerboseLabel");
            ZeroLabelRole = new URI("http://www.xbrl.org/2003/role/zeroLabel");
            ZeroTerseLabelRole = new URI("http://www.xbrl.org/2003/role/zeroTerseLabel");
            ZeroVerboseLabelRole = new URI("http://www.xbrl.org/2003/role/zeroVerboseLabel");
            TotalLabelRole = new URI("http://www.xbrl.org/2003/role/totalLabel");
            PeriodStartLabelRole = new URI("http://www.xbrl.org/2003/role/periodStartLabel");
            PeriodEndLabelRole = new URI("http://www.xbrl.org/2003/role/periodEndLabel");
            DocumentationLabelRole = new URI("http://www.xbrl.org/2003/role/documentationLabel");
            DefinitionGuidanceLabelRole = new URI("http://www.xbrl.org/2003/role/definitionGuidanceLabel");
            DisclosureGuidanceLabelRole = new URI("http://www.xbrl.org/2003/role/disclosureGuidanceLabel");
            PresentationGuidanceLabelRole = new URI("http://www.xbrl.org/2003/role/presentationGuidanceLabel");
            MeasurementGuidanceLabelRole = new URI("http://www.xbrl.org/2003/role/measurementGuidanceLabel");
            CommentaryGuidanceLabelRole = new URI("http://www.xbrl.org/2003/role/commentaryGuidanceLabel");
            ExampleGuidanceLabelRole = new URI("http://www.xbrl.org/2003/role/exampleGuidanceLabel");
            StandardReferenceRole = new URI("http://www.xbrl.org/2003/role/reference");
            DefinitionReferenceRole = new URI("http://www.xbrl.org/2003/role/definitionRef");
            DisclosureReferenceRole = new URI("http://www.xbrl.org/2003/role/disclosureRef");
            MandatoryDisclosureReferenceRole = new URI("http://www.xbrl.org/2003/role/mandatoryDisclosureRef");
            RecommendedDisclosureReferenceRole = new URI("http://www.xbrl.org/2003/role/recommendedDisclosureRef");
            UnspecifiedDisclosureReferenceRole = new URI("http://www.xbrl.org/2003/role/unspecifiedDisclosureGuidanceLabel");
            PresentationReferenceRole = new URI("http://www.xbrl.org/2003/role/presentationRef");
            MeasurementReferenceRole = new URI("http://www.xbrl.org/2003/role/measurementRef");
            CommentaryReferenceRole = new URI("http://www.xbrl.org/2003/role/commentaryRef");
            ExampleReferenceRole = new URI("http://www.xbrl.org/2003/role/exampleRef");
            StandardLinkRole = new URI("http://www.xbrl.org/2003/role/link");
            ISO4217 = new URI("http://www.xbrl.org/2003/iso4217");
            
            StandardGenericLabelRole = new URI("http://www.xbrl.org/2008/role/label");
            TerseGenericLabelRole = new URI("http://www.xbrl.org/2008/role/terseLabel");
            VerboseGenericLabelRole = new URI("http://www.xbrl.org/2008/role/verboseLabel");
            DocumentationGenericLabelRole = new URI("http://www.xbrl.org/2008/role/documentation");
            
            StandardGenericReferenceRole = new URI("http://www.xbrl.org/2008/role/reference");
            LinkbaseReferenceArcrole = new URI("http://www.w3.org/1999/xlink/properties/linkbase");
        } catch (URISyntaxException e) {
            logger.error("Exception thrown though.");
        }
        
    }

    public static URI XMLNamespace;
    public static URI XMLNSNamespace;
    public static URI XLinkNamespace;
    public static URI XBRL21Namespace;
    public static URI XBRL21LinkNamespace;
    public static URI GenericLinkNamespace;
    public static URI GenericLabelNamespace;
    public static URI GenericReferenceNamespace;
    public static URI XBRLAPINamespace;
    public static URI CompNamespace;
    public static URI XBRLAPIEntitiesNamespace;
    public static URI XBRLAPIEquivalentEntitiesArcrole;
    public static URI XBRLAPILanguagesNamespace;    
    public static URI XMLSchemaNamespace;
    public static URI XMLSchemaInstanceNamespace;
    public static URI LabelArcrole;
    public static URI GenericLabelArcrole;
    
    public static URI StandardGenericLabelRole;
    
    public static URI TerseGenericLabelRole;
    
    public static URI VerboseGenericLabelRole;
    
    public static URI DocumentationGenericLabelRole;    
    public static URI ReferenceArcrole;
    public static URI GenericReferenceArcrole;
    public static URI CalculationArcrole;
    public static URI PresentationArcrole;
    public static URI GeneralSpecialArcrole;
    public static URI EssenceAliasArcrole;
    public static URI SimilarTuplesArcrole;
    public static URI RequiresElementArcrole;
    public static URI FactFootnoteArcrole;
    public static URI StandardLabelRole;
    public static URI TerseLabelRole;
    public static URI VerboseLabelRole;
    public static URI PositiveLabelRole;
    public static URI PositiveTerseLabelRole;
    public static URI PositiveVerboseLabelRole;
    public static URI NegativeLabelRole;
    public static URI NegativeTerseLabelRole;
    public static URI NegativeVerboseLabelRole;
    public static URI ZeroLabelRole;
    public static URI ZeroTerseLabelRole;
    public static URI ZeroVerboseLabelRole;
    public static URI TotalLabelRole;
    public static URI PeriodStartLabelRole;
    public static URI PeriodEndLabelRole;
    public static URI DocumentationLabelRole;
    public static URI DefinitionGuidanceLabelRole;
    public static URI DisclosureGuidanceLabelRole;
    public static URI PresentationGuidanceLabelRole;
    public static URI MeasurementGuidanceLabelRole;
    public static URI CommentaryGuidanceLabelRole;
    public static URI ExampleGuidanceLabelRole;
    public static URI StandardReferenceRole;
    
    public static URI StandardGenericReferenceRole;
    public static URI DefinitionReferenceRole;
    public static URI DisclosureReferenceRole;
    public static URI MandatoryDisclosureReferenceRole;
    public static URI RecommendedDisclosureReferenceRole;
    public static URI UnspecifiedDisclosureReferenceRole;
    public static URI PresentationReferenceRole;
    public static URI MeasurementReferenceRole;
    public static URI CommentaryReferenceRole;
    public static URI ExampleReferenceRole;
    public static URI StandardLinkRole;
    public static URI ISO4217;
    
    public static URI LinkbaseReferenceArcrole;
}
