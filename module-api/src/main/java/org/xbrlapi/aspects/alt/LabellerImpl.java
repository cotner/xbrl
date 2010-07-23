package org.xbrlapi.aspects.alt;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

/**
 * <p>
 * A generic labeller that returns aspect IDs as the aspect labels
 * and that returns aspect value IDs as the aspect value labels.
 * It ignores XLink roles and locales when generating the labels.
 * </p>
 * 
 * <p>
 * Labeller implementations should extend this class.
 * </p>
 * 
 * <p>
 * This labeller can be used for all aspects but it is pretty ordinary in 
 * terms of the quality of the labels it generates.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class LabellerImpl implements Labeller {

    /**
     * 
     */
    private static final long serialVersionUID = 519089390627009535L;

    protected final static Logger logger = Logger.getLogger(LabellerImpl.class);
    
    /**
     * The aspect that this labeller deals with.
     */
    private Aspect aspect;
    
    /**
     * @param aspect The aspect to be a labeller for.
     */
    public LabellerImpl(Aspect aspect) throws XBRLException {
        super();
        if (aspect == null) throw new XBRLException("The aspect must not be null.");
        this.aspect = aspect;
    }

    /**
     * @see Labeller#getAspect()
     */
    public Aspect getAspect() {
        return aspect;
    }

    /**
     * @see Labeller#getDomain()
     */
    public Domain getDomain() {
        return aspect.getDomain();
    }
    
    /**
     * @see Labeller#getStore()
     */
    public Store getStore() throws XBRLException {
        return getDomain().getStore();
    }
    
    /**
     * @see Labeller#getAspectLabel(String, URI, URI)
     */
    public String getAspectLabel(String locale, URI resourceRole, URI linkRole) {
        return aspect.getId().toString();
    }

    /**
     * @see Labeller#getAspectValueLabel(AspectValue, String, URI, URI)
     */
    public String getAspectValueLabel(AspectValue value, String locale,
            URI resourceRole, URI linkRole) {
        return value.getId();
    }

}
