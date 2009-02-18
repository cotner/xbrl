package org.xbrlapi.aspects;

import java.util.HashMap;
import java.util.Map;

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
    
    protected boolean hasMapId(AspectValue value) {
        return idMap.containsKey(value);
    }

    protected String getMapId(AspectValue value) {
        return idMap.get(value);
    }
    
    protected String setMapId(AspectValue value, String id) {
        return idMap.put(value,id);
    }

    protected boolean hasMapLabel(String id) {
        return labelMap.containsKey(id);
    }

    protected String getMapLabel(String id) {
        return labelMap.get(id);
    }
    
    protected String setMapLabel(String id, String label) {
        return labelMap.put(id,label);
    }

    public void validate(AspectValue value) throws XBRLException {
        ;
    }    
    
    public String getIdentifier(AspectValue value) throws XBRLException {
        return value.getFragment().getIndex();
    }
    
    public String getLabel(AspectValue value) throws XBRLException {
        return getIdentifier(value);
    }
    
    public void clearIdentifiers() {
        this.idMap.clear();
    }
        
}