package org.xbrlapi.aspects;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 * 
 */
public interface AspectModel extends Serializable {

    /**
     * @return the number of facts in the aspect model.
     */
    public int getFactCount();

    /**
     * @return The unique identifier of the aspect model.
     */
    public String getType();

    /**
     * @return a Collection of the aspects in the aspect model.
     * @throws XBRLException
     */
    public Collection<Aspect> getAspects() throws XBRLException;

    /**
     * @param type
     *            the aspect type.
     * @return the aspect with the specified aspect type.
     * @throws XBRLException
     *             if the aspect model does not have the specified aspect.
     */
    public Aspect getAspect(String type) throws XBRLException;

    /**
     * @param type
     *            the aspect type.
     * @return true if the aspect model includes the aspect with the specified
     *         aspect type and false otherwise.
     */
    public boolean hasAspect(String type);

    /**
     * @return a collection of aspects that are not associated with a dimension,
     *         either as the root aspect for the dimension or as a descendant of
     *         another aspect associated with the dimension.
     */
    public Collection<Aspect> getOrphanAspects();

    /**
     * @param axis
     *            The unique (for the aspect model) identifier for an axis of
     *            the aspect model. Aspect models can have zero or more axes,
     *            each of which has a root aspect. Root aspects can have a
     *            series of child aspects, all of which are aspects associated
     *            with the same axis of the aspect model. These axes are useful
     *            for relating aspects to rows and columns of a table, for
     *            example.
     * @return the list of aspects for the axis. The list is empty if there are
     *         no aspects assigned to the chosen axis or the aspect model does
     *         not have the specified axis.
     * @throws XBRLException
     */
    public List<Aspect> getAxisAspects(String axis) throws XBRLException;

    /**
     * @param aspect
     *            The aspect to set in the aspect model, as an orphan.
     */
    public void setAspect(Aspect aspect) throws XBRLException;

    /**
     * @param facts
     *            The collection of facts to add to the aspect model
     * @throws XBRLException
     *             if the facts cannot be added to the aspect model.
     */
    public <F extends Fact> void addFacts(Collection<F> facts)
            throws XBRLException;

    /**
     * @param fact
     *            The fact to add to the aspect model
     * @throws XBRLException
     *             if the fact cannot be added to the aspect model.
     */
    public void addFact(Fact fact) throws XBRLException;

    /**
     * @param fact
     *            The fact to get the aspect values for.
     * @return a list of the aspect values (one for each aspect in the aspect
     *         model) for the supplied fact. The list is empty if the aspect
     *         model has no aspects.
     */
    public List<AspectValue> getAspectValues(Fact fact) throws XBRLException;

    /**
     * @param aspectType
     *            The type of aspect to arrange in the aspect model.
     * @param axis
     *            the axis to put the aspect in, in last place in the ordering.
     * @throws XBRLException
     *             if the aspect is not in the aspect model.
     */
    public void arrangeAspect(String aspectType, String axis)
            throws XBRLException;

    /**
     * @param aspectType
     *            The type of aspect to arrange in the aspect model.
     * @param axis
     *            the axis to put the aspect in.
     * @param parentType
     *            The type of parent aspect for the aspect being set.
     * @throws XBRLException
     *             if the aspect or parent are not in the aspect model.
     */
    public void arrangeAspect(String aspectType, String axis, String parentType)
            throws XBRLException;

    /**
     * @param values
     *            is a collection of AspectValues to be matched.
     * @return the set of all facts in the aspect model that match all of the
     *         aspect values to be matched.
     * @throws XBRLException
     */
    public Set<Fact> getFacts(Collection<AspectValue> values)
            throws XBRLException;

    /**
     * @return the set of all facts in the aspect model.
     * @throws XBRLException
     */
    public Set<Fact> getAllFacts() throws XBRLException;

    /**
     * @return The set of facts matching the selection criteria that have been
     *         added to the aspect model.
     * @throws XBRLException
     */
    public Set<Fact> getMatchingFacts() throws XBRLException;

    /**
     * Adds a fact selection criterion used in selecting a set of the facts in
     * the aspect model.
     * 
     * @param criterion
     *            The aspect value that the selected facts must have.
     * @see AspectModel#getMatchingFacts()
     * @throws XBRLException
     */
    public void setCriterion(AspectValue criterion) throws XBRLException;

    /**
     * Adds a collection of fact selection criteria.
     * 
     * @param criteria
     *            The collection of aspect values that the selected facts must
     *            have.
     * @see AspectModel#getMatchingFacts()
     * @see AspectModel#setCriterion(AspectValue)
     * @throws XBRLException
     */
    public void setCriteria(Collection<AspectValue> criteria)
            throws XBRLException;

    /**
     * Clears all fact selection criteria in the aspect model.
     * 
     * @see AspectModel#setCriterion(AspectValue)
     * @see AspectModel#getMatchingFacts()
     */
    public void clearAllCriteria();

    /**
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
     * @param axis
     *            The name of the axis to get aspect value combinations for.
     * @return a list of combinations of aspect values where each combination is
     *         a combination of one aspect value for each aspect in the axis.
     *         The aspect values in each combination are ordered in the same
     *         order as the aspects in the axis. The lists in the list are
     *         ordered by the orderings of the values for each aspect. The
     *         results are minimal in that any aspect that has just missing
     *         aspect values is omitted. The list is empty if there are no
     *         aspects in the axis.
     */
    public List<List<AspectValue>> getMinimalAspectValueCombinationsForAxis(
            String axis) throws XBRLException;

    /**
     * @param combinations
     *            The aspect value combinations for a given axis of the aspect
     *            model.
     * @param aspectType
     *            The aspect type to do the analysis for.
     * @return a list of integers, each of which represents the number of equal
     *         values for the specified aspect in a row, in the set of
     *         combinations. Thus, if there are 5 combinations and the first
     *         aspect has equal values for the first two and then different
     *         equal values for the last three, the list returned would be [2
     *         3]. This utility method makes it more straightforward to set up
     *         table headings when presenting analyses of aspect models.
     * @throws XBRLException
     */
    public List<Integer> analyseAspectCombinationMatches(
            List<List<AspectValue>> combinations, String aspectType)
            throws XBRLException;

    /**
     * @param type
     *            The type identifying the aspect to delete.
     * @throws XBRLException
     */
    public void deleteAspect(String type) throws XBRLException;

    /**
     * Removes all facts from the aspect model and all aspect values from the
     * aspects in the aspect models. The aspects retain their maps from aspect
     * value IDs to aspect value labels, however.
     * 
     * @throws XBRLException
     */
    public void clearFacts() throws XBRLException;

}
