package org.xbrlapi.aspects.alt;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.Measure;
import org.xbrlapi.Unit;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

public class UnitAspectValue extends AspectValueImpl implements AspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = 6028909248578218123L;

    protected final static Logger logger = Logger
    .getLogger(UnitAspectValue.class);

    /** 
     * Missing values are indicated by an empty list of numerator measures.
     * The numerator measures in the unit.
     */
    List<Measure> numerators;

    /** 
     * The denominator measures in the unit.
     */
    List<Measure> denominators;
    
    /**
     * Missing aspect value constructor.
     */
    public UnitAspectValue() {
        super();
        this.numerators = new Vector<Measure>();
        this.denominators = new Vector<Measure>();
    }

    /**
     * @param unit The unit fragment.
     */
    public UnitAspectValue(Unit unit) throws XBRLException {
        super();
        if (unit != null) {
            this.numerators = unit.getResolvedNumeratorMeasures();
            this.denominators = unit.getResolvedDenominatorMeasures();
        }
    }
    
    public List<Measure> getNumerators() {
        return numerators;
    }
    
    public List<Measure> getDenominators() {
        return denominators;
    }
    
    public boolean hasDenominators() {
        return ! denominators.isEmpty();
    }
    
    /**
     * @see AspectHandler#getAspectId()
     */
    public URI getAspectId() {
        return UnitAspect.ID;
    }
    
    /**
     * @see AspectValue#isMissing()
     */
    public boolean isMissing() {
        return getNumerators().isEmpty();
    }

    /**
     * The missing aspect value ID is the empty string.
     * @see AspectValue#getId()
     */
    public String getId() {
        
        if (numerators.isEmpty()) return "";

        String id = "";

        Collections.<Measure>sort(numerators);
        Collections.<Measure>sort(denominators);
        
        if (numerators.size() == 1 && ! denominators.isEmpty() && numerators.get(0).getNamespace().equals(Constants.ISO4217))
               id = "Currency: " + numerators.get(0).getLocalname();
        else if (numerators.size() == 1 && ! denominators.isEmpty() && numerators.get(0).getNamespace().equals(Constants.XBRL21Namespace) && numerators.get(0).getLocalname().equals("pure") )
            id = "Pure number";
        else {
            for (int i=0; i<numerators.size(); i++) {
                Measure measure = numerators.get(i);
                if (i == 0) id += measure.getNamespace() + "#" + measure.getLocalname();
                else id += " x " + measure.getNamespace() + "#" + measure.getLocalname();
            }
            if (! denominators.isEmpty()) {
                id += " / (";
                for (int i=0; i < denominators.size(); i++) {
                    Measure measure = denominators.get(i);
                    if (i == 0) id += measure.getNamespace() + "#" + measure.getLocalname();
                    else id += " x " + measure.getNamespace() + "#" + measure.getLocalname();
                }
                id += ")";
            }
        }
        
        return id;
    }
    
}
