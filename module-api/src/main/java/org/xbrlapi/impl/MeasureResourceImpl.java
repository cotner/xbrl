package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xbrlapi.MeasureResource;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * Unit resource implementation.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class MeasureResourceImpl extends MixedContentResourceImpl implements MeasureResource {

    /**
     * 
     */
    private static final long serialVersionUID = -8882117229431178772L;

    /**
     * @see MeasureResource#getEquivalents()
     */
    public Set<MeasureResource> getEquivalents() throws XBRLException { 
        logger.debug("Getting equivalents to " + this.getStringIdentifier());
        Set<MeasureResource> result = new HashSet<MeasureResource>();
        this.getEquivalentsSet(result);
        return result;        
    }    
    
    /**
     * @return a string identifier for the measure resource
     * @throws XBRLException
     */
    protected String getStringIdentifier() throws XBRLException {
        return this.getMeasureNamespace() + "#" + this.getMeasureLocalname();
    }

    /**
     * @return a list of measure resources that are parent or children 
     * of equivalent-measure relationships to this measure resource.
     * @throws XBRLException
     */
    protected List<MeasureResource> getDirectEquivalents() throws XBRLException {
        List<MeasureResource> equivalents = this.getParentEquivalents();
        equivalents.addAll(this.getChildEquivalents());
        return equivalents;
    }
    
    /**
     * @return a list of measure resources that are parents
     * of equivalent-entity relationships with this entity resource
     * as a target.
     * @throws XBRLException
     */
    protected List<MeasureResource> getParentEquivalents() throws XBRLException {
        List<MeasureResource> equivalents = getStore().<MeasureResource>getTargets(this.getIndex(),null,Constants.XBRLAPIEquivalentEntitiesArcrole);
        return equivalents;
    }
    
    /**
     * @return a list of unit resources that are children
     * of equivalent-entity relationships with this unit resource
     * as a source.
     * @throws XBRLException
     */
    protected List<MeasureResource> getChildEquivalents() throws XBRLException {
        return getStore().<MeasureResource>getSources(this.getIndex(),null,Constants.XBRLAPIEquivalentEntitiesArcrole);
    }    
    
    /**
     * Augments a map of equivalent unit resources
     * @throws XBRLException
     */
    protected void getEquivalentsSet(Set<MeasureResource> set) throws XBRLException {

        if (set == null) {
            set = new HashSet<MeasureResource>();
        }
        
        set.add(this);
        
        Set<MeasureResource> newEquivalents = new HashSet<MeasureResource>(this.getDirectEquivalents());
        newEquivalents.removeAll(set);
        if (newEquivalents.isEmpty()) return;
        set.addAll(newEquivalents);
        
        for (MeasureResource UnitResource: newEquivalents) {
            ((MeasureResourceImpl) UnitResource).getEquivalentsSet(set);
        }
        
    }

    /**
     * @see MeasureResource#getMeasureLocalname()
     */
    public String getMeasureLocalname() throws XBRLException {
        return this.getDataRootElement().getAttribute("name");
    }

    /**
     * @see MeasureResource#getMeasureNamespace()
     */
    public URI getMeasureNamespace() throws XBRLException {
        try {
            return new URI(this.getDataRootElement().getAttribute("namespace"));
        } catch (URISyntaxException e) {
            throw new XBRLException("The unit measure namespace "+this.getDataRootElement().getAttribute("namespace") +" has invalid syntax.",e);
        }
    }

}
