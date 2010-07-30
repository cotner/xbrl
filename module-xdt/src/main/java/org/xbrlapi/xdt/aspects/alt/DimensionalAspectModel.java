package org.xbrlapi.xdt.aspects.alt;

import java.util.List;

import org.xbrlapi.aspects.alt.Aspect;
import org.xbrlapi.aspects.alt.AspectModel;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public interface DimensionalAspectModel extends AspectModel {

    /**
     * @return a list of explicit dimension aspects in the aspect model
     * @throws XBRLException
     */
    public List<Aspect> getExplicitDimensionAspects() throws XBRLException;
    
    /**
     * @return a list of typed dimension aspects in the aspect model
     * @throws XBRLException
     */
    public List<Aspect> getTypedDimensionAspects() throws XBRLException;
    
    /**
     * @return a list of explicit and typed dimension aspects in the aspect model
     * @throws XBRLException
     */
    public List<Aspect> getDimensionAspects() throws XBRLException;
}
