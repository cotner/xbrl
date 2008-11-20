package org.xbrlapi.aspects;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
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
    static public final String ENTITY_IDENTIFIER = "entity";
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
     * @param value The value to test for.
     * @return true if the aspect has the given value
     * @throws XBRLException
     */
    public boolean hasValue(AspectValue value) throws XBRLException;
    

    /**
     * @return true if the aspect has just one aspect value
     * and false otherwise.
     */
    public boolean isSingular();
    
    /**
     * @return true if the aspect has just one aspect value
     * that is the missing aspect value and false otherwise.
     */
    public boolean isMissing();    
    
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
     * @return true if the aspect is in a dimension of the 
     * containing aspect model and false otherwise.
     */
    public boolean isOrphan();
    
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
    public <A extends AspectValue> List<A> getValues();
   
    
    /**
     * @return the number of values for this aspect.
     */
    public int getLength();
    
    /**
     * @return an iterator over the ordered values for this aspect.
     */
    public Iterator<AspectValue> getIterator();
    
    /**
     * @return the number of combinations of descendant aspect
     * values (for those descendant aspects in the same dimension
     * of the aspect model).
     */
    public int getDescendantCount();
    
    /**
     * @return the number of combinations of ancestor aspect
     * values (for those ancestor aspects in the same dimension
     * of the aspect model).
     */
    public int getAncestorCount();    
    
    
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
    public <A extends AspectValue> void addValue(A value) throws XBRLException;
 
    /**
     * Adds the fact to the aspect, generating the aspect
     * value, and storing the fact for later retrieval by that
     * aspect value.  The fact is not stored for this aspect if
     * it does not have a value for this aspect.
     * @param fact The fact to add.
     * @throws XBRLException.
     */
    public void addFact(Fact fact) throws XBRLException;
    
    /**
     * @param fact The fact to get the aspect value for
     * @return the aspect value for the fact or null if the
     * fact does not have a value for this aspect.
     * @throws XBRLException
     */
    public <A extends AspectValue> A getValue(Fact fact) throws XBRLException;


    /**
     * @param fact the fact to get the fragment for.
     * @return the fragment required to generate an aspect value from the 
     * fact.
     * @throws XBRLException if the fragment cannot be obtained.
     */
    public Fragment getFragment(Fact fact) throws XBRLException;
    
    /**
     * @param fact The fact to get the aspect value fragment from
     * @return the fragment, retrieved from the data store, that is
     * required to generate an aspect value from the fact.
     * @throws XBRLException if the fragment cannot be obtained.
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException;
    
    /**
     * @param fact The fact.
     * @returnto the unique string identifying the fragment
     * that is part of the aspect value for the given fact.
     * @throws XBRLException
     */
    public String getFragmentKey(Fact fact) throws XBRLException;
    
    /**
     * @param value The aspect value to match
     * @return the set of facts with the specified aspect value for this aspect.
     * @throws XBRLException if the aspect value is not a value for this aspect.
     */
    public Set<Fact> getFacts(AspectValue value) throws XBRLException;
    
    /**
     * @return the set of facts matching the selection criterion
     * set for this aspect or all facts if the selection criterion
     * is cleared (null).
     * @throws XBRLException
     */
    public Set<Fact> getMatchingFacts() throws XBRLException;
    
    /**
     * @return true if the aspect has a selection criterion aspect 
     * value and false if that criterion is null.
     */
    public boolean hasSelectionCriterion();
    
    /**
     * @return the aspect value used by the aspect model for selecting facts
     * based upon this aspect or null if no such criterion has been set.
     */
    public AspectValue getSelectionCriterion();

    /**
     * @param criterion The selection criterion for this aspect.
     */
    public void setSelectionCriterion(AspectValue criterion);
    
    /**
     * Sets the selection criterion to null.
     */
    public void clearSelectionCriterion();
    
    
}
