package org.xbrlapi.xdt.aspects.alt;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.AspectModel;
import org.xbrlapi.aspects.alt.AspectModelImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class DimensionalAspectModel extends AspectModelImpl implements AspectModel {

    /**
     * 
     */
    private static final long serialVersionUID = -5012949679199322397L;
    
    private static final Logger logger = Logger.getLogger(DimensionalAspectModel.class);
    
    /**
     * XDT Aspects are only added as they are required.
     * @throws XBRLException if the store is null.
     */
    public DimensionalAspectModel() throws XBRLException {
        super();
        this.addExtender(new XDTExtender());
    }

    public List<Aspect> getExplicitDimensionAspects() throws XBRLException {
        List<Aspect> result = new Vector<Aspect>();
        for (Aspect aspect: this.getAspects()) {
            if (aspect.getClass().equals(ExplicitDimensionAspect.class))
                result.add(aspect);
        }
        return result;
    }
    
    public List<Aspect> getTypedDimensionAspects() throws XBRLException {
        List<Aspect> result = new Vector<Aspect>();
        for (Aspect aspect: this.getAspects()) {
            if (aspect.getClass().equals(TypedDimensionAspect.class))
                result.add(aspect);
        }
        return result;
    }
    
    public List<Aspect> getDimensionAspects() throws XBRLException {
        List<Aspect> result = new Vector<Aspect>();
        for (Aspect aspect: this.getAspects()) {
           if (aspect.getClass().equals(ExplicitDimensionAspect.class) || aspect.getClass().equals(TypedDimensionAspect.class)) 
               result.add(aspect);
       }
       return result;
    }
}
