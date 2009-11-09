package org.xbrlapi.aspects;

import java.io.Serializable;
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
public interface Aspect extends Serializable {

    /**
     * @return the label identifying the aspect.
     * @throws XBRLException
     */
    public String getLabel() throws XBRLException;
    
    /**
     * @return the unique aspect identifier.
     * @throws XBRLException if the aspect identifier cannot be determined.
     */
    public String getType() throws XBRLException;

    /**
     * @return true if the aspect has no aspect values (not even missing ones)
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
     * @throws XBRLException
     */
    public boolean isMissing() throws XBRLException;    
    
    /**
     * @return axis name or null if the aspect is an orphan
     * (not assigned to an axis in the containing aspect model).
     */
    public String getAxis();
    
    /**
     * @param axis the name of the axis or null
     * if the aspect is to be an orphan.
     */
    public void setAxis(String axis);    

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
     * @return a list of the values for the aspect.
     */
    public List<AspectValue> getValues();
    
    /**
     * @return a list of the values for the aspect, sorted
     * on the basis of the aspect value hierarchy such that 
     * parent values come immediately before their child values.
     * @throws XBRLException
     */
    public List<AspectValue> getValuesByHierarchy() throws XBRLException;
    
    /**
     * @param id the identifier generated from the value being sought.
     * @return the aspect value that has the given identifier.
     * @see AspectValue#getIdentifier()
     */
    public AspectValue getValue(String id);
    
    /**
     * @param id the identifier generated from the value being sought.
     * @return true if the aspect model has a value with the given id.
     * @see AspectValue#getIdentifier()
     */
    public boolean hasValue(String id);
   
    
    /**
     * @return the number of values for this aspect.
     */
    public int size();
    
    /**
     * @return an iterator over the ordered values for this aspect.
     */
    public Iterator<AspectValue> getIterator();
    
    /**
     * @return the number of combinations of descendant aspect
     * values (for those descendant aspects in the same dimension
     * of the aspect model).
     * @throws XBRLException
     */
    public int getDescendantCount() throws XBRLException;
    
    /**
     * @return the number of combinations of ancestor aspect
     * values (for those ancestor aspects in the same dimension
     * of the aspect model).
     */
    public int getAncestorCount() throws XBRLException;    
    
    
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
     * @return the aspect value for the fact or the
     * MissingAspectValue if the fact does not have a 
     * value for this aspect.
     * @throws XBRLException
     */
    public <A extends AspectValue> A getValue(Fact fact) throws XBRLException;


    /**
     * @param fact the fact to get the fragment for.
     * @return the fragment required to generate an aspect value from the 
     * fact or null if none is available.
     * @throws XBRLException
     */
    public <F extends Fragment> F getFragment(Fact fact) throws XBRLException;
    
    /**
     * @param fact The fact to get the fragment from.
     * @return the fragment, retrieved from the data store, that is
     * required to generate an aspect value for the fact; or null 
     * if the fragment is not available.
     * @throws XBRLException
     */
    public <F extends Fragment> F getFragmentFromStore(Fact fact) throws XBRLException;
    
    /**
     * @param fact The fact to get an aspect key for.
     * @return the unique string identifying the fragment
     * that is part of the aspect value for the given fact.
     * This should be the empty string if the fact does not
     * have a value for the aspect.
     * @throws XBRLException
     */
    public String getKey(Fact fact) throws XBRLException;
    
    /**
     * @param value The aspect value to match
     * @return the set of facts with the specified aspect value for this aspect.
     * @throws XBRLException if the aspect value is not a value for this aspect.
     */
    public Set<Fact> getFacts(AspectValue value) throws XBRLException;
    
    /**
     * @return the set of all facts that have non-missing values for this aspect.
     * @throws XBRLException
     */
    public Set<Fact> getAllFacts() throws XBRLException;
    
    /**
     * @return the set of facts matching the selection criterion
     * set for this aspect
     * @throws XBRLException if the selection criterion
     * is not set for the aspect.
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
    
    /**
     * Removes all facts from the aspect and all aspect values
     * from the aspect.  The aspect retains its
     * map from aspect value IDs to aspect value labels, however.
     * @throws XBRLException
     */
    public void clearFacts() throws XBRLException;
    

    
    


}
