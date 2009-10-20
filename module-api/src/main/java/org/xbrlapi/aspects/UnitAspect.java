package org.xbrlapi.aspects;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Item;
import org.xbrlapi.Measure;
import org.xbrlapi.NumericItem;
import org.xbrlapi.Unit;
import org.xbrlapi.impl.UnitImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class UnitAspect extends BaseAspect implements Aspect {

    public final static String TYPE = "unit";
    
    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return TYPE;
    }

    private final static Logger logger = Logger.getLogger(UnitAspect.class);    

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public UnitAspect(AspectModel aspectModel) throws XBRLException {
        super(aspectModel);
        initialize();
    }
    
    protected void initialize() {
        this.setTransformer(new Transformer());
    }



    public class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {
        
        public Transformer() {
            super();
        }
        

        /**
         * @see AspectValueTransformer#validate(AspectValue)
         */
        public void validate(AspectValue value) throws XBRLException {
            super.validate(value);
            if (value.getFragment() == null) return;
            if (! value.getFragment().isa(UnitImpl.class)) {
                throw new XBRLException("The aspect value must have a unit fragment.");
            }
        }

        /**
         * @see AspectValueTransformer#getIdentifier(AspectValue)
         */
        public String getIdentifier(AspectValue value) throws XBRLException {
            validate(value);
            if (hasMapId(value)) {
                return getMapId(value);
            }
            if (value.getFragment() == null) {
                setMapId(value,"");
                return "";
            }
            Unit f = ((Unit) value.getFragment());
            String result = "";
            List<Measure> numerators = f.getResolvedNumeratorMeasures();
            for (int i=0; i<numerators.size(); i++) {
                Measure measure = numerators.get(i);
                if (i == 0) result += measure.getNamespace() + ":" + measure.getLocalname();
                else result += "*" + measure.getNamespace() + ":" + measure.getLocalname();
            }
            if (f.hasDenominator()) {
                result += "/";
                List<Measure> denominators = f.getResolvedDenominatorMeasures();
                for (int i=0; i<denominators.size(); i++) {
                    Measure measure = denominators.get(i);
                    if (i == 0) result += measure.getNamespace() + ":" + measure.getLocalname();
                    else result += "*" + measure.getNamespace() + ":" + measure.getLocalname();
                }
            }
            setMapId(value,result);
            return result;
        }
        
        /**
         * @see AspectValueTransformer#getLabel(AspectValue)
         */
        public String getLabel(AspectValue value) throws XBRLException {
            String id = getIdentifier(value);
            if (hasMapLabel(id)) {
                return getMapLabel(id);
            }
            if (value.getFragment() == null) {
                setMapLabel("","");
                return "";
            }
            Unit f = ((Unit) value.getFragment());
            String result = "";
            List<Measure> numerators = f.getResolvedNumeratorMeasures();
            for (int i=0; i<numerators.size(); i++) {
                Measure measure = numerators.get(i);
                if (i == 0) result += measure.getLocalname();
                else result += "*" + measure.getLocalname();
            }
            if (f.hasDenominator()) {
                result += "/";
                List<Measure> denominators = f.getResolvedDenominatorMeasures();
                for (int i=0; i<denominators.size(); i++) {
                    Measure measure = denominators.get(i);
                    if (i == 0) result += measure.getLocalname();
                    else result += "*" + measure.getLocalname();
                }
            }
            logger.debug("Unit aspect value label is " + result);            
            setMapLabel(id,result);
            return result;
        } 
        
    }    
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public AspectValue getValue(Fact fact) throws XBRLException {
        try {
            return new UnitAspectValue(this,getFragment(fact));
        } catch (XBRLException e) {
            return new MissingAspectValue(this);
        }
    }
    
    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        if (fact.isTuple()) {
            throw new XBRLException("The fact must not be a tuple.");
        }
        Item item = (Item) fact;
        if (! item.isNumeric()) throw new XBRLException("The fact must be numeric.");
        return item.getUnit();
    }    
    
    /**
     * @see Aspect#getKey(Fact)
     */
    public String getKey(Fact fact) throws XBRLException {
        if (fact.isTuple()) {
            throw new XBRLException("The fact must not be a tuple.");
        }
        Item item = (Item) fact;
        if (! item.isNumeric()) throw new XBRLException("The fact must be numeric.");
        return item.getURI() + ((NumericItem) item).getUnitId();
    }
    
    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject( );
        initialize();
    }
    
    /**
     * Handles object serialization
     * @param out The input object stream used to store the serialization of the object.
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }    
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
       return true;
    }
    
}
