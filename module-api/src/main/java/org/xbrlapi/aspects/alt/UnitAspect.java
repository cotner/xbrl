package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.NumericItem;
import org.xbrlapi.Unit;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Unit aspect details</h2>
 * 
 * <p>
 * Captures XBRL instance unit value information.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class UnitAspect extends AspectImpl implements Aspect {
    
    /**
     * 
     */
    private static final long serialVersionUID = 6762933296532647581L;

    private static final Logger logger = Logger.getLogger(UnitAspect.class);

    /**
     * The URI uniquely identifying this concept aspect.
     */
    public static URI ID = URI.create("http://xbrlapi.org/aspect/unit/1.0");
    
    /**
     * @see Aspect#getId()
     */
    public URI getId() {
        return ID;
    }
    
    /**
     * @param domain The domain for this aspect.
     * @throws XBRLException
     */
    public UnitAspect(Domain domain) throws XBRLException {
        super(domain);
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    public UnitAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isNil()) return getMissingValue();
        if (fact.isNumeric()) return getValue(((NumericItem) fact).getUnit());
        return getMissingValue();
    }

    /**
     * @param unit The unit to capture the unit aspect value from
     * @return the unit aspect value.
     * @throws XBRLException
     */
    public UnitAspectValue getValue(Unit unit) throws XBRLException {
        return new UnitAspectValue(unit);
    }
    
    /**
     * @see Aspect#getMissingValue()
     */
    public UnitAspectValue getMissingValue() {
        return new UnitAspectValue();
    }

}
