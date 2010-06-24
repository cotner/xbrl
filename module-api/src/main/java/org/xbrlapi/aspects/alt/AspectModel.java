package org.xbrlapi.aspects.alt;

import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.List;
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
 * Aspect model extenders add all new aspects to the "orphan" axis.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface AspectModel extends Serializable {

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
     * The aspect will be added as the last aspect in the given axis.
     * If the aspect model does not already have the given axis, that axis
     * will be added to the aspect model.
     * @param aspect
     *            The aspect to add to the aspect model.
     * @param axis
     *            the axis to put the aspect in, in last place in the ordering.
     */
    public void addAspect(String axis, Aspect aspect);

    /**
     * The aspect will be added to the aspect model in the specified axis
     * and it will be immediately follow the parent aspect ID in the list 
     * of aspects associated with the given axis if that axis has the parent aspect
     * in it.  Otherwise the aspect will be added as the last aspect for the given axis.
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
     * @param aspect
     *            The aspect to delete from the aspect model.
     */
    public void deleteAspect(Aspect aspect);

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
     * @param extender The aspect model extender.
     * @throws XBRLException if the extender is null.
     */
    public void addExtender(Extender extender) throws XBRLException;

    /**
     * Removes the given extender from the aspect model if the aspect model
     * is currently using the given extender.
     * @param extender The aspect model extender to remove from the aspect model.
     */
    public void removeExtender(Extender extender);
    
    /**
     * Removes all extenders from the aspect model.
     */
    public void removeAllExtenders();

   /**
    * @param fact The fact to get the new aspects from.
    * @return the set of aspects not in the model that are associated
    * with the fact and that are detected by the extenders being used 
    * by the aspect model.
    */
    public Set<Aspect> getNewAspects(Fact fact) throws XBRLException;
    
}
