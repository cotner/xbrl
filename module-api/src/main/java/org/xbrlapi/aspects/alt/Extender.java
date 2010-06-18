package org.xbrlapi.aspects.alt;

import java.io.Serializable;
import java.util.Collection;

import org.xbrlapi.Fact;
import org.xbrlapi.utilities.XBRLException;

/**
 * <h2>Aspect model Extenders</h2>
 * 
 * <p>
 * The aspects in an aspect model can sometimes be a function of the 
 * facts being analysed in the context of that aspect model.  A classic example
 * is the XDT dimensions.  To handle such situations, it is possible to 
 * augment an aspect model with one or more aspect model extenders.
 * These check for specific kinds of aspects on facts being added to a fact set
 * and augment the aspect model as required.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface Extender extends Serializable {

    /**
     * Adds to the aspect model, specific aspects associated with the 
     * given fact that are not already in the aspect model.
     * @param model The aspect model.
     * @param fact The fact.
     * @throws XBRLException if any parameters are null.
     */
    public Collection<Aspect> getNewAspects(AspectModel model, Fact fact) throws XBRLException;
    
    
}
