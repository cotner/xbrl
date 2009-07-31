package org.xbrlapi.data.bdbxml.examples.utilities;

import java.net.URI;
import java.util.List;

import org.xbrlapi.Measure;
import org.xbrlapi.Unit;

/**
 * Provides a summary of the units of measurement set out in an given XBRL document.
 * The QNames in each unit measure numerator and denominator are listed in full.
 * Additional commandline arguments (optional ones marked with an *)
 * <ul>
 *  <li>-document [The URI of the document]</li>
 * </ul> 
 * These are in addition to those commandline arguments documented at
 * @link BaseUtilityExample
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class AnalyseUnitMeasures extends BaseUtilityExample {
    
    public AnalyseUnitMeasures(String[] args) {
        argumentDocumentation = addArgumentDocumentation();
        parseArguments(args);
        String message = setUp();
        if (message.equals("")) {
            try {

                URI uri = new URI(arguments.get("document"));
                store.serialize(store.getRootFragmentForDocument(uri));
                List<Unit> units = store.getFragmentsFromDocument(uri,"Unit");
                for (Unit unit: units) {
                    for (Measure measure: unit.getResolvedNumeratorMeasures()) {
                        System.out.print(measure.getNamespace() + ":" + measure.getLocalname());
                    }
                    System.out.println("");
                    List<Measure> denominators = unit.getResolvedDenominatorMeasures();
                    if (denominators.size() > 0) {
                        for (Measure measure: denominators) {
                            System.out.print(measure.getNamespace() + ":" + measure.getLocalname());
                        }
                        System.out.println("");
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
    
    


    
    /**
     * @param args The array of commandline arguments.
     */
    public static void main(String[] args) {
        @SuppressWarnings("unused")
        AnalyseUnitMeasures utility = new AnalyseUnitMeasures(args);
    }



   
    

    
}
