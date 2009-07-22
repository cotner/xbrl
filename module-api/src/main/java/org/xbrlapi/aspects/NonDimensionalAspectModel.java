package org.xbrlapi.aspects;

import java.io.IOException;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class NonDimensionalAspectModel extends BaseAspectModel implements AspectModel {

    public NonDimensionalAspectModel() throws XBRLException {
        super();
        initialize();
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
    
}
