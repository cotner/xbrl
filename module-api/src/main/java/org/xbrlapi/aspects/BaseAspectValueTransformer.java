package org.xbrlapi.aspects;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public abstract class BaseAspectValueTransformer implements AspectValueTransformer {
    
    private Map<AspectValue,String> map = new HashMap<AspectValue,String>();
    
    protected boolean hasTransform(AspectValue value) {
        return map.containsKey(value);
    }

    protected String getTransform(AspectValue value) {
        return map.get(value);
    }
    
    protected String setTransform(AspectValue value, String transform) {
        return map.put(value,transform);
    }
    
}