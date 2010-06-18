package org.xbrlapi.aspects;

import java.net.URI;
import java.util.List;

import org.xbrlapi.utilities.XBRLException;


/**
 * <h2>Aspect Value Transformer Explanation</h2>
 * 
 * <p>
 * Each aspect uses its own transformer.
 * Transformers are responsible for:
 * </p>
 * 
 * <ul>
 *  <li>Getting aspect value identifiers from aspect values</li>
 *  <li>Getting aspect value labels from aspect values</li>
 * </ul>
 *  
 * <p>
 *  Right now, aspect value transformers are also responsible for tracking
 *  the role URIs and the language code details necessary for deciding which 
 *  label to return.  This should be information that could be passed
 *  in as parameters when obtaining labels for aspect values.  Some transformers
 *  would ignore the information passed in (where it is not relevant to the aspect
 *  value and some would make use of the information passed in).  That way, an aspect
 *  and its values are not locale-specific and role-choice specific.
 * </p>
 * 
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface AspectValueTransformer {    
    
    /**
     * @return the aspect that uses this transformer
     */
    public Aspect getAspect();    
    
    /**
     * TODO This method should become a method of the aspect value interface.
     * @param value The aspect value to process.
     * @return the string value of this aspect value to a string
     * that uniquely identifies the semantic value of the aspect (
     * as opposed to the object that is the aspect value itself).
     * @throws XBRLException if the transformation fails.
     */
    public String getIdentifier(AspectValue value) throws XBRLException;
    
    /**
     * @param value The aspect value to process.
     * @return the human readable version of the aspect value or null if
     * the aspect value is a missing aspect value (i.e. the aspect value 
     * fragment is null).
     * @throws XBRLException if the transformation fails.
     */
    public String getLabel(AspectValue value) throws XBRLException;

    /**
     * Empties the map of entity identifiers so that there are no
     * aspect values pointing to identifiers anymore.  This leaves the
     * map from identifiers to labels intact though.
     */
    public void clearIdentifiers();
    /**
     * @return the list of extended link roles, from most preferred to
     * least preferred.
     */
    public List<URI> getLinkRoles();
    
    /**
     * @param roles The extended link roles to use in
     * selecting labels.
     */
    public void setLinkRoles(List<URI> roles);
    
    /**
     * @return the list of label resource roles, from most preferred to
     * least preferred.
     */
    public List<URI> getLabelRoles();
    
    /**
     * @param roles The label resource roles to use in
     * selecting labels from most preferred to least preferred.
     */
    public void setLabelRoles(List<URI> roles);

    /**
     * @return the list of language codes from 
     * most preferred to least preferred.
     */
    public List<String> getLanguageCodes();
    
    /**
     * @param languages The list of ISO language codes to 
     * use in retrieving labels for aspects and their values.
     */
    public void setLanguageCodes(List<String> languages);
    
    /**
     * @param language The language code to make the most preferred language code.
     */
    public void addPreferredLanguageCode(String language);
    
    /**
     * @param labelRole The label role to make the most preferred label role.
     */
    public void addPreferredLabelRole(URI labelRole);    
    
    /**
     * @param linkRole The link role to make the most preferred link role.
     */
    public void addPreferredLinkRole(URI linkRole);    
    
}
