package org.xbrlapi.aspects.alt;

import java.io.Serializable;
import java.net.URI;

import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Labeller</h2>
 * 
 * <p>
 * Classes implementing the Labeller interface are intended to provide access to
 * labels for aspects and their values.
 * </p>
 * 
 * <p>
 * The implementation of a labeller will be specific to one or more particular types of aspect.
 * The constructor for the labeller MUST throw an exception if the labeller is initialised
 * with an aspect that is not of the right type.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface Labeller extends Serializable {
    
    /**
     * @return the aspect that this is a labeller for.
     */
    public Aspect getAspect();
    
    /**
     * @return the domain for the aspect that this is a labeller for.
     */
    public Domain getDomain();

    /**
     * @return the store underpinning the domain for the aspect that this is a labeller for.
     */
    public Store getStore() throws XBRLException;    
    
    /**
     * @param locale The locale (language code etc) of the label. Set to null if the label is not locale dependent.
     * @param resourceRole The resource role of the XLink resource providing the label.  Set to null if the label 
     * does not depend on the resource role.
     * @param linkRole The link role of the extended link network that is to be analysed to obtain the label. Set to 
     * null if the label does not depend on the link role.
     * @return the label for the aspect based upon the given parameters.
     */
    public String getAspectLabel(String locale, URI resourceRole, URI linkRole);
    
    /**
     * @param value The aspect value to get a label for.
     * @param locale The locale (language code etc) of the label. Set to null if the label is not locale dependent.
     * @param resourceRole The resource role of the XLink resource providing the label.  Set to null if the label 
     * does not depend on the resource role.
     * @param linkRole The link role of the extended link network that is to be analysed to obtain the label. Set to 
     * null if the label does not depend on the link role.
     * @return the label for the aspect based upon the given parameters.
     */
    public String getAspectValueLabel(AspectValue value, String locale, URI resourceRole, URI linkRole);
    
}
