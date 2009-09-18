package org.xbrlapi.data.bdbxml.examples.utilities;

import java.util.List;

import org.xbrlapi.ComplexTypeDeclaration;
import org.xbrlapi.Concept;
import org.xbrlapi.Item;
import org.xbrlapi.Measure;
import org.xbrlapi.NumericItem;
import org.xbrlapi.Unit;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Lists all facts where the data type is derives from the monetary item type 
 * but the unit is not reported using the right ISO currency code.
 * Lists all facts where the label includes the word 'number' or the word 'count' but
 * the unit is not xbrli:pure.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>There are no additional commandline arguments for this utility.</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ReportUnitMisuses extends BaseUtilityExample {
    
    public ReportUnitMisuses(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {

                ComplexTypeDeclaration monetaryItemType = store.<ComplexTypeDeclaration>getGlobalDeclaration(Constants.XBRL21Namespace,"monetaryItemType");
                monetaryItemType.serialize();
                System.exit(1);
                
                // Get the list of all units in the available instances.
                List<Unit> units = store.<Unit>getXMLResources("Unit");
                for (Unit unit: units) {
                    List<Measure> measures = unit.getResolvedNumeratorMeasures();
                    List<NumericItem> items = unit.<NumericItem>getReferencingItems();
                    for (NumericItem item: items) {
                        Concept concept = item.getConcept();
                        if (concept.getTypeDeclaration().equals(monetaryItemType)) {
                            testMeasures(concept, item, unit, measures);
                        }
                    }
                }

            } catch (Exception e) {
                badUsage(e.getMessage());
            }
        } else {
            badUsage(message);
        }
        
        tearDown();
    }
    
    private void testMeasures(Concept concept, Item item, Unit unit, List<Measure> measures) throws XBRLException {
        
        if (measures.size() > 1) {
            unit.serialize();
            concept.serialize();
            report("There are too many numerator measures for fact " + item.getIndex() + " " + concept.getLocalname());
        }
        Measure measure = measures.get(0);
        if (! measure.getNamespace().equals(Constants.ISO4217)) report(measure.getNamespace() + " The numerator measure for fact " + item.getIndex() + " is incorrectly in namespace " + measure.getNamespace());
    }
    
    private void report(String message) {
        System.out.println(message);
    }
    
    


    
    /**
     * @param args The array of commandline arguments.
     */
    public static void main(String[] args) {
        @SuppressWarnings("unused")
        ReportUnitMisuses utility = new ReportUnitMisuses(args);
    }



   
    

    
}
