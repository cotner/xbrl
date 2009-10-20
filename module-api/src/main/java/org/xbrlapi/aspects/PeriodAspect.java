package org.xbrlapi.aspects;

import java.io.IOException;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Comparator;

import org.apache.log4j.Logger;
import org.xbrlapi.Context;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Period;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class PeriodAspect extends ContextAspect implements Aspect {

    public final static String TYPE = "period";
    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return TYPE;
    }
    

    
    private final static Logger logger = Logger.getLogger(PeriodAspect.class);    
    
    private static final ObjectStreamField[] serialPersistentFields = {};
    
    private class PeriodComparator implements Comparator<String>, Serializable {
        
        public PeriodComparator() {
            super();
        }
        
        public int compare(String left, String right) {
            
            if (equals(left,right)) return 0;
            
            if (left.equals("forever")) return -1;
            if (right.equals("forever")) return 1;
            
            if (left.length() > right.length()) return -1;
            if (left.length() < right.length()) return 1;
            
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

        private PeriodAspect getOuterType() {
            return PeriodAspect.this;
        }
    }
    
    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public PeriodAspect(AspectModel aspectModel) throws XBRLException {
        super(aspectModel);
        initialize();
    }
    
    protected void initialize() {
        this.setTransformer(new Transformer());
    }


    
    /**
     * @see Aspect#getKey(Fact)
     */
    public String getKey(Fact fact) throws XBRLException {
        Context context = (Context) super.getFragmentFromStore(fact);
        return context.getURI().toString() + context.getId();
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
            if (! value.getFragment().isa("org.xbrlapi.impl.PeriodImpl")) {
                throw new XBRLException("The aspect value must have a period fragment.");
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
            Period f = ((Period) value.getFragment());
            String id = "";
            if (f.isFiniteDurationPeriod()) {
                id += dateTime(f.getStart()) + " to " + dateTime(f.getEnd());
            } else if (f.isInstantPeriod()) {
                id += dateTime(f.getInstant());
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
            return getIdentifier(value);
        }        
        
        private String dateTime(String value) {
            int tIndex = value.indexOf('T');
            if (tIndex == -1) {
                return value;
            }
            String[] dateTime = value.split("T");
            String dateValue = dateTime[0];
            String timeValue = dateTime[1];
            return dateValue + ":" + timeValue;
            
        }
    }    

    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public AspectValue getValue(Fact fact) throws XBRLException {
        try {
            return new PeriodAspectValue(this,getFragment(fact));
        } catch (XBRLException e) {
            return null;
        }
    }

    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        return ((Context) super.getFragmentFromStore(fact)).getPeriod();
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
