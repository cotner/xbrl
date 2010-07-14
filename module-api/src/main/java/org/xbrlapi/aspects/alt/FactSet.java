package org.xbrlapi.aspects.alt;

import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Fact Set</h2>
 * 
 * <p>
 * A FactSet is a set of facts and a set of aspect values and a two-way mapping between the facts and the aspect values
 * </p>
 * 
 * <p>
 * The FactSet guarantees to have all aspect values for all of the facts that it contains.
 * It also guarantees not to have any aspect values that are not values for facts in the fact set.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface FactSet extends Serializable {

    /**
     * @return the aspect model underpinning the fact set.
     */
    public AspectModel getModel();
    
    /**
     * Adds the fact to the fact set, computing the aspect values
     * for the fact for each aspect in the fact set's aspect model.
     * @param fact The fact to add.
     * @throws XBRLException
     */
    public void addFact(Fact fact) throws XBRLException;


    /**
     * Adds the facts to the fact set, computing the aspect values
     * for the facts for each aspect in the fact set's aspect model.
     * @param facts The facts to add.
     * @throws XBRLException
     */
    public void addFacts(Collection<Fact> facts) throws XBRLException;
    
    /**
     * @param fact The fact to test for.
     * @return true if the fact is in the set and false otherwise.
     */
    public boolean hasFact(Fact fact);

    /**
     * @param value The aspect value to test for.
     * @return true if the aspect value is in the set and false otherwise.
     */
    public boolean hasAspectValue(AspectValue value);

    /**
     * @return the set of all facts.
     */
    public Set<Fact> getFacts();

    /**
     * @return the set of all aspect values.
     */
    public Set<AspectValue> getAspectValues();
    
    /**
     * @param aspectId The ID of the aspect.
     * @return the set of all values for the aspect, also always including the missing value.
     */
    public Collection<AspectValue> getAspectValues(URI aspectId);

    /**
     * @param aspectId
     *            The ID of the aspect.
     * @return true if the fact set has non-missing values for the specified
     *         aspect.
     */
    public boolean isPopulated(URI aspectId);
    
    /**
     * @param fact The fact.
     * @return the set of aspect values for the given fact, including missing
     * values for those aspects for which the fact does not have a value.
     */
    public Collection<AspectValue> getAspectValues(Fact fact) throws XBRLException;
    
    /**
     * @param aspectId the ID of the aspect to get the aspect value for.
     * @param fact The fact.
     * @return the aspect value for the given fact and aspect.
     * @throws XBRLException
     */
    public AspectValue getAspectValue(URI aspectId, Fact fact) throws XBRLException;

    /**
     * @param value The aspect value.
     * @return the set of facts with the given aspect value.
     */
    public Collection<Fact> getFacts(AspectValue value);
 
    

    /**
     * @return the number of facts in the fact set.
     */
    public long getSize();
    
    /**
     * @return the aspect model underpinning this fact set.
     */
    public AspectModel getAspectModel();

    /**
     * @param aspectId
     *            The ID of the aspect of interest.
     * @return a list of the facts in the fact set that have values for the
     *         specified aspect that are roots of the heirarchy of aspect values
     *         defined by the domain of the specified aspect.
     * @throws XBRLException
     */
    public List<Fact> getRootFacts(URI aspectId) throws XBRLException;

}
