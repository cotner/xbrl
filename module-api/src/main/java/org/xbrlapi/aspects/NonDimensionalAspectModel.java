package org.xbrlapi.aspects;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.log4j.Logger;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class NonDimensionalAspectModel extends BaseAspectModel implements AspectModel {

    /**
     * 
     */
    private static final long serialVersionUID = 5027794569305067678L;
    private static final Logger logger = Logger.getLogger(NonDimensionalAspectModel.class);    
    
    public NonDimensionalAspectModel() throws XBRLException {
        super();
        initialize();
        this.setAspect(new LocationAspect(this));
        this.setAspect(new ConceptAspect(this));
        this.setAspect(new EntityIdentifierAspect(this));
        this.setAspect(new SegmentAspect(this));
        this.setAspect(new PeriodAspect(this));
        this.setAspect(new ScenarioAspect(this));
        this.setAspect(new UnitAspect(this));
    }

    protected void initialize() {
        ;
    }
    
    public String getType() {
        return "nondimensional";        
    }

    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
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
