package org.xbrlapi.aspects;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class BaseAspectValueTransformer implements AspectValueTransformer {
    
    public BaseAspectValueTransformer(Aspect aspect) {
        super();
        this.aspect = aspect;
        languages.add("en");
        labelRoles.add(Constants.StandardLabelRole);
        linkRoles.add(Constants.StandardLinkRole);
        
    }    
    
    private Map<AspectValue,String> idMap = new HashMap<AspectValue,String>();
    private Map<String,String> labelMap = new HashMap<String,String>();
    
    /**
     * @param value The aspect value
     * @return true if the transformer has an ID for the
     * aspect value and false otherwise.
     */
    protected boolean hasMapId(AspectValue value) {
        return idMap.containsKey(value);
    }

    /**
     * @param value The aspect value to get the aspect ID from.
     * @return the ID of the value
     */
    protected String getMapId(AspectValue value) {
        return idMap.get(value);
    }
    
    protected String setMapId(AspectValue value, String id) {
        return idMap.put(value,id);
    }

    protected boolean hasMapLabel(String id) {
        return labelMap.containsKey(id);
    }

    /**
     * @param id The aspect ID
     * @return the aspect value label.
     */
    protected String getMapLabel(String id) {
        return labelMap.get(id);
    }
    
    protected String setMapLabel(String id, String label) {
        return labelMap.put(id,label);
    }

    public String getIdentifier(AspectValue value) throws XBRLException {
        Fragment fragment = value.<Fragment>getFragment();
        if (fragment == null) return "";
        return fragment.getIndex();
    }
    
    public String getLabel(AspectValue value) throws XBRLException {
        if (value.getFragment() != null) return getIdentifier(value);
        return null;
    }
    
    public void clearIdentifiers() {
        this.idMap.clear();
    }
        


    





    



    private Aspect aspect;
    
    /**
     * @see org.xbrlapi.aspects.AspectValueTransformer#getAspect()
     */
    public Aspect getAspect() {
        return aspect;
    }


    /**
     * @see AspectValueTransformer#getLabelRoles()
     */
    public List<URI> getLabelRoles() {
        return labelRoles;
    }

    /**
     * @see AspectValueTransformer#getLinkRoles()
     */
    public List<URI> getLinkRoles() {
        return linkRoles;
    }

    /**
     * The list of preferred label resource roles
     */
    private List<URI> labelRoles = new Vector<URI>();
    
    /**
     * @see AspectValueTransformer#setLabelRoles(List)
     */
    public void setLabelRoles(List<URI> roles) {
        labelRoles = roles;
    }

    /**
     * The list of preferred ISO language codes
     */
    private List<String> languages = new Vector<String>();

    /**
     * @see AspectValueTransformer#setLanguageCodes(List)
     */
    public void setLanguageCodes(List<String> languages) {
        this.languages = languages;
    }
    
    /**
     * @see AspectValueTransformer#getLanguageCodes()
     */
    public List<String> getLanguageCodes() {
        return languages;
    }
    

    /**
     * @see AspectValueTransformer#addPreferredLanguageCode(String)
     */
    public void addPreferredLanguageCode(String language) {
        getLanguageCodes().add(0,language);
    }
    
    /**
     * The list of preferred extended link roles
     */
    private List<URI> linkRoles = new Vector<URI>();
    
    /**
     * @see AspectValueTransformer#setLinkRoles(List)
     */
    public void setLinkRoles(List<URI> roles) {
        linkRoles = roles;
    }

    /**
     * @see AspectValueTransformer#addPreferredLabelRole(URI)
     */
    public void addPreferredLabelRole(URI labelRole) {
        this.labelRoles.add(0,labelRole);
    }

    /**
     * @see AspectValueTransformer#addPreferredLinkRole(URI)
     */
    public void addPreferredLinkRole(URI linkRole) {
        this.linkRoles.add(0,linkRole);
    }
    
    
}