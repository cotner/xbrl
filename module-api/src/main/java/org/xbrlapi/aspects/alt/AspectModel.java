package org.xbrlapi.aspects.alt;

import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Aspect models</h2>
 * 
 * <p>
 * Aspect models are collections of aspects. They allow the aspects in the
 * aspect model to be arranged into ordered groups, each of which is referred to
 * as an axis of the aspect model. Aspect models imply no ordering over axes.
 * Axes are useful in enabling structures like tabular representations of data
 * to be defined. For example, by specifying a set of aspects as being in the
 * column axis and another set of aspects as being in the row axis, it is
 * possible to define a simple two dimensional table arrangement of XBRL facts.
 * </p>
 * 
 * <p>
 * Aspect models provide a means of getting the set of facts in the aspect model
 * that are reported with specified values for a chosen set of aspects.
 * </p>
 * 
 * <p>
 * All aspect model classes need to have a constructor with a single parameter,
 * that being the data store.  @see org.xbrlapi.data.Store
 * This constructor is used by the aspect model duplicate method.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface AspectModel extends Serializable {

    /**
     * Initialises the aspects and the aspect value labellers
     * in the aspect model.  This should be called after the constructor
     * has been run.
     * Implementations of this class should always start by calling
     * the initialise method of their super-class, if such a method exists.
     * This use of initialise enables us to separate the resource intensive
     * initialisation process from the constructor process.  This can be useful
     * if there are ways to avoid the initialisation process because of information
     * that is already available, eg in another aspect model.  In particular,
     * this is useful for aspect model duplication.
     * @throws XBRLException
     */
    public void initialise() throws XBRLException;
    
    /**
     * @return a collection of all aspects in the aspect model, regardless
     * of their axis.
     * @throws XBRLException
     */
    public Collection<Aspect> getAspects() throws XBRLException;

    /**
     * @param aspectId
     *            the aspect ID.
     * @return true if the aspect model includes the aspect and false otherwise.
     */
    public boolean hasAspect(URI aspectId);
    
    /**
     * @param aspectId
     *            the aspect ID.
     * @return The aspect if it is in the model.
     * @throws XBRLException if the aspect is not in the model.
     */
    public Aspect getAspect(URI aspectId) throws XBRLException;

    /**
     * @param axis
     *            The unique (for the aspect model) identifier for an axis of
     *            the aspect model. Aspect models can have zero or more axes,
     *            each of which has an ordered sequence of aspects.
     * @return the list of aspects for the specified axis. The list is empty if
     *         there are no aspects assigned to the chosen axis or the aspect
     *         model does not have the specified axis.
     */
    public List<Aspect> getAspects(String axis);

    /**
     * The aspect will be added as the last aspect in the given axis. If the
     * aspect model does not already have the given axis, that axis will be
     * added to the aspect model. If the aspect model does not have a labeller,
     * the labeller for the aspect is set to the default labeller.
     * 
     * @see LabellerImpl
     * @param aspect
     *            The aspect to add to the aspect model.
     * @param axis
     *            the axis to put the aspect in, in last place in the ordering.
     */
    public void addAspect(String axis, Aspect aspect);
    
    /**
     * The aspect will be added as the last aspect in the default axis.
     * If the aspect model does not already have the default axis, that axis
     * will be added to the aspect model.
     * @param aspect
     *            The aspect to add to the aspect model.
     */
    public void addAspect(Aspect aspect);

    /**
     * The aspect will be added to the aspect model in the specified axis and it
     * will be immediately follow the parent aspect ID in the list of aspects
     * associated with the given axis if that axis has the parent aspect in it.
     * Otherwise the aspect will be added as the last aspect for the given axis.
     * The labeller for the aspect is set to the default labeller.
     * 
     * @see LabellerImpl
     * @param axis
     *            the axis to put the aspect in.
     * @param parentAspect
     *            The parent aspect for the aspect being added.
     * @param aspect
     *            The aspect to add to the aspect model.
     */
    public void addAspect(String axis, Aspect parentAspect, Aspect aspect)
            throws XBRLException;

    /**
     * The aspect will be added to the aspect model in the default axis and it
     * will be immediately follow the parent aspect ID in the list of aspects
     * associated with the default axis if that axis has the parent aspect in
     * it. Otherwise the aspect will be added as the last aspect for the default
     * axis.
     * 
     * @param parentAspect
     *            The parent aspect for the aspect being added.
     * @param aspect
     *            The aspect to add to the aspect model.
     */
    public void addAspect(Aspect parentAspect, Aspect aspect)
            throws XBRLException;



    /**
     * @param axis
     *            the axis.
     * @return true if the aspect model has the specified axis and false
     *         otherwise.
     * @throws XBRLException
     */
    public boolean hasAxis(String axis) throws XBRLException;

    /**
     * @param axis
     *            The axis
     * @param aspectId
     *            the aspect
     * @return true if the axis is in the aspect model and contains the aspect
     *         and false otherwise.
     */
    public boolean axisContainsAspect(String axis, URI aspectId);

    /**
     * @return a set of strings, each of which is an axis of the aspect model.
     * @throws XBRLException
     */
    public Set<String> getAxes() throws XBRLException;
    



    



    


   

    
 
    /**
     * @return the axis that new aspects are added to by default.
     */
    public String getDefaultAxis();

    /**
     * @param defaultAxis the axis to add aspects to by default.
     * @throws XBRLException if the axis is null.
     */
    public void setDefaultAxis(String defaultAxis) throws XBRLException;
    
    /**
     * @param fact
     *            The fact to get the aspect values for.
     * @return the map from Aspect IDs to values for those aspects, for the
     *         fact, with missing values for all aspects in the aspect model,
     *         that do not have a value for the given fact.
     * @throws XBRLException
     */
    public Map<URI, AspectValue> getAspectValues(Fact fact) throws XBRLException;
    


    /**
     * @param fact
     *            The fact to get the aspect values for.
     * @param existingValues
     *            The map of aspect values that we already have and so do not
     *            need to get now.
     * @return the map of existing and new aspect values, one for each aspect in
     *         this aspect model.
     * @throws XBRLException
     */
    public Map<URI, AspectValue> getAspectValues(Fact fact,
            Map<URI, AspectValue> existingValues) throws XBRLException;
    
    /**
     * @param originalAxis The axis to move the aspects from
     * @param newAxis The axis to move the aspects to
     * @throws XBRLException if the original axis does not exist.
     */
    public void moveAspects(String originalAxis, String newAxis) throws XBRLException;
    
    /**
     * @param aspectId The ID of the aspect that the labeller is to be used for.
     * @param labeller The labeller to use.
     * @throws XBRLException if a parameter is null or if the labeller is not
     * for the specified aspect.
     */
    public void setLabeller(URI aspectId, Labeller labeller) throws XBRLException;
    
    /**
     * @param aspectId The ID of the aspect to get the labeller for.
     * @return the labeller for the specified aspect.  If no labeller has been
     * explicitly set for the aspect, then the default labeller is returned.
     * @see LabellerImpl
     * @throws XBRLException if the aspect ID is null.
     */
    public Labeller getLabeller(URI aspectId) throws XBRLException;
    
    /**
     * @param aspectId The ID of the aspect.
     * @return true if the aspect model already has a labeller for the
     * specified aspect and false otherwise.  Returns false if the aspect ID
     * is null.
     */
    public boolean hasLabeller(URI aspectId);
    
    /**
     * @return a new aspect model that is a duplicate of this model.
     * The aspects in the new aspect model are references to the same
     * aspects as are in this aspect model (so this is not generating 
     * a real clone of the original aspect model) but the 
     * mapping from axis to aspects are cloned so that they can be changed
     * in the duplicate aspect model without altering those mappings in the
     * original aspect model.  The data store is also not duplicated.
     * In general, this does the work of the initialise method without doing
     * a formal initialisation on the duplicate aspect model.
     * @throws XBRLException if the aspect model does not support duplication.
     */
    public AspectModel duplicate() throws XBRLException;
    
    /**
     * This is a convenience method to give access to the labels generated by
     * the labellers for each aspect in the aspect model.
     * @param aspectId the ID of the aspect being labelled.
     * @param locale The label locale
     * @param resourceRole The label XLink resource role
     * @param linkRole The label link role
     * @return the label for the given aspect.
     * @throws XBRLException if the aspect is not in the aspect model.
     */
    public String getAspectLabel(URI aspectId, String locale, URI resourceRole, URI linkRole) throws XBRLException;

    /**
     * 
     * This is a convenience method to give access to the labels generated by
     * the labellers for the values of each aspect in the aspect model.
     * @param value The aspect value
     * @param locale The label locale
     * @param resourceRole The label XLink resource role
     * @param linkRole The label link role
     * @return the label for the given aspect value.
     * @throws XBRLException if the aspect is not in the aspect model.
     */
    public String getAspectValueLabel(AspectValue value, String locale, URI resourceRole, URI linkRole) throws XBRLException;
    
    
}
