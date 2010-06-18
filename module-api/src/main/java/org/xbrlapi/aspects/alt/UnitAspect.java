package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.NumericItem;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Unit aspect details</h2>
 * 
 * <p>
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class UnitAspect extends AspectImpl<UnitAspectValue> implements Aspect {
    
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
    public UnitAspect(Domain<UnitAspectValue> domain) throws XBRLException {
        super(domain);
    }
    
    /**
     * @see Aspect#getValue(Fact)
     */
    @SuppressWarnings("unchecked")
    public UnitAspectValue getValue(Fact fact) throws XBRLException {
        if (fact.isNil()) return getMissingValue();
        if (fact.isNumeric()) return new UnitAspectValue(((NumericItem) fact).getUnit());
        return new UnitAspectValue();
    }

    /**
     * @see Aspect#getDomain()
     */
    @SuppressWarnings("unchecked")
    public Domain<UnitAspectValue> getDomain() {
        return domain;
    }

    /**
     * Implies no missing values are defined.
     * @return null.
     */
    @SuppressWarnings("unchecked")
    public UnitAspectValue getMissingValue() {
        return new UnitAspectValue();
    }


}
