package org.xbrlapi.aspects;

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;

import org.apache.log4j.Logger;
import org.xbrlapi.Context;
import org.xbrlapi.Fact;
import org.xbrlapi.Period;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class QuarterlyPeriodAspect extends ContextAspect implements Aspect {

    public final static String TYPE = "period";
    
    private final static Logger logger = Logger.getLogger(QuarterlyPeriodAspect.class);    

    private class PeriodComparator implements Comparator<String>, Serializable {
        
        public PeriodComparator() {
            super();
        }
        
        public int compare(String left, String right) {
            
            if (equals(left,right)) return 0;
            
            if (left.equals("forever")) return -1;
            if (right.equals("forever")) return 1;
            
            return left.compareTo(right);
        }
        
        public boolean equals(String left, String right) {
            return left.equals(right);
        }

        /**
         * Handles object inflation.
         * @param in The input object stream used to access the object's serialization.
         * @throws IOException
         * @throws ClassNotFoundException
         */
        private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject( );
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
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            return result;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PeriodComparator other = (PeriodComparator) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            return true;
        }

        private QuarterlyPeriodAspect getOuterType() {
            return QuarterlyPeriodAspect.this;
        }   
    }
    
    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public QuarterlyPeriodAspect(AspectModel aspectModel) throws XBRLException {
        super(aspectModel);
        initialize();
    }
    
    protected void initialize() {
        this.setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return Aspect.PERIOD;
    }
    
    

    public class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {
        public Transformer() {
            super();
        }
        
        /**
         * @see AspectValueTransformer#getIdentifier(AspectValue)
         */
        public String getIdentifier(AspectValue value) throws XBRLException {

            if (hasMapId(value)) {
                return getMapId(value);
            }
            Period f = ((Period) value.getFragment());
            String id = "";
            if (f.isFiniteDurationPeriod()) {
                id += getYear(f.getEnd()) + "-Q" + getQuarter(f.getEnd());
            } else if (f.isInstantPeriod()) {
                id += getYear(f.getInstant()) + "-Q" + getQuarter(f.getInstant());
            } else {
                id += "forever";
            }
            setMapId(value,id);
            return id;
        }
        
        /**
         * @see AspectValueTransformer#getLabel(AspectValue)
         */
        public String getLabel(AspectValue value) throws XBRLException {
            if (value.getFragment() == null) return null;
            return getIdentifier(value);
        }        
        
        private String getYear(String value) {
            return value.substring(0,4);
        }
        
        private String getQuarter(String value) {
            int month = (new Integer(value.substring(5,6))).intValue();
            if (month <= 3) return "1";
            if (month <= 6) return "2";
            if (month <= 9) return "3";
            return "4";
        }
        
        
    }    

    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public PeriodAspectValue getValue(Fact fact) throws XBRLException {
        Period period = this.<Period>getFragment(fact);
        return new PeriodAspectValue(this,period);
    }

    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    @SuppressWarnings("unchecked")
    public Period getFragmentFromStore(Fact fact) throws XBRLException {
        Context context = getContextFromStore(fact);
        if (context == null) return null;
        return context.getPeriod();
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
