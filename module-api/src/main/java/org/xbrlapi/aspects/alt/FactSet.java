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
     * @param fact The fact to add.
     * @throws XBRLException
     */
    public void addFact(Fact fact) throws XBRLException;


    /**
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
     * @param <V> The subclass of aspect values.
     * @param aspectId The ID of the aspect.
     * @return the set of all values for the aspect.
     */
    public <V extends AspectValue> Collection<V> getAspectValues(URI aspectId);

    /**
     * @param fact The fact.
     * @return the set of aspect values for the given fact.
     */
    public Collection<AspectValue> getAspectValues(Fact fact);

    /**
     * @param value The aspect value.
     * @return the set of facts with the given aspect value.
     */
    public Collection<Fact> getFacts(AspectValue value);
 
    /**
     * This method is intended to simplify generation of things like table
     * row or column headings with multiple levels, one per aspect.  At each
     * level, the headings reflect the values for the aspect at that level.
     * 
     * The data structure that is returned by this method allows easy traversal 
     * to generate the table headings.
     * 
     * @param axis
     *            The name of the axis.
     * @return a list of combinations of aspect values where each combination is
     *         a list of one aspect value for each aspect in the axis. The
     *         aspect values in each combination are ordered in the same order
     *         as the aspects in the axis. The lists in the list are ordered by
     *         the orderings of the values for each aspect.
     */
    public List<List<AspectValue>> getAspectValueCombinationsForAxis(String axis)
            throws XBRLException;    

    /**
     * @return the number of facts in the fact set.
     */
    public long getSize();
    
}
