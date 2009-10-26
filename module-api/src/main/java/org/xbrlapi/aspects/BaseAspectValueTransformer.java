package org.xbrlapi.aspects;

import java.util.HashMap;
import java.util.Map;

import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class BaseAspectValueTransformer implements AspectValueTransformer {
    
    public BaseAspectValueTransformer() {
        super();
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
        
}