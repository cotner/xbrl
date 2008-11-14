package org.xbrlapi.aspects;

import java.util.List;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 *
 */
public interface Aspect {

    /**
     * Aspect identifiers
     */
    static public final String CONCEPT = "concept";
    static public final String PERIOD = "period";
    static public final String ENTITY_IDENTIFIER = "entity_identifier";
    static public final String SEGMENT = "segment";
    static public final String SCENARIO = "scenario";
    static public final String UNIT = "unit";
    
    /**
     * @return the unique aspect identifier.
     */
    public String getType();

    /**
     * @return true if the aspect has no aspect values
     * and false otherwise.
     */
    public boolean isEmpty();

    /**
     * @return true if the aspect has just one aspect value
     * and false otherwise.
     */
    public boolean isSingular();
    
    /**
     * @return dimension name or null if the aspect is an orphan
     * (not assigned to a dimension in the containing aspect model).
     */
    public String getDimension();
    
    /**
     * @param dimension the name of the dimension or null
     * if the aspect is to be an orphan.
     */
    public void setDimension(String dimension);    

    /**
     * @param aspectModel the aspect model to assign this aspect to.
     * @throws XBRLException if the aspect model is null.
     */
    public void setAspectModel(AspectModel aspectModel) throws XBRLException;    
    
    /**
     * @return the aspect model with this aspect.
     */
    public AspectModel getAspectModel();

    /**
     * TODO give the user control over the sorting system for a the values
     * of an aspect.
     * @return a sorted list of the values for the aspect.
     */
    public List<AspectValue> getValues(); 
    
    /**
     * @return the number of values for this aspect.
     */
    public int getLength();
    
    /**
     * @return the transformer responsible for converting
     * a value for this aspect into a human readable string
     * representation of that value.
     */
    public AspectValueTransformer getTransformer();

    /**
     * @param transformer The aspect value transformer  
     * responsible for converting a value for this aspect 
     * into a human readable string representation of that value.
     */
    public void setTransformer(AspectValueTransformer transformer);

    /**
     * @param value The aspect value to add to the aspect.
     * @throws XBRLException if the aspect value cannot be added
     * because its unique identifier cannot be obtained from the
     * aspect value transformer.
     */
    public void addValue(AspectValue value) throws XBRLException;
    
    
}
