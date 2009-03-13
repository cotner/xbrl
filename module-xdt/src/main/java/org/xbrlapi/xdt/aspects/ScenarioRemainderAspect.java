package org.xbrlapi.xdt.aspects;

import java.util.List;
import java.util.Vector;

import org.w3c.dom.Element;
import org.xbrlapi.Context;
import org.xbrlapi.Fact;
import org.xbrlapi.Fragment;
import org.xbrlapi.Scenario;
import org.xbrlapi.aspects.Aspect;
import org.xbrlapi.aspects.AspectModel;
import org.xbrlapi.aspects.AspectValue;
import org.xbrlapi.aspects.AspectValueTransformer;
import org.xbrlapi.aspects.BaseAspectValueTransformer;
import org.xbrlapi.aspects.ContextAspect;
import org.xbrlapi.aspects.MissingAspectValue;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.XDTConstants;

/**
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class ScenarioRemainderAspect extends ContextAspect implements Aspect {

    /**
     * @param aspectModel The aspect model with this aspect.
     * @throws XBRLException.
     */
    public ScenarioRemainderAspect(AspectModel aspectModel) throws XBRLException {
        setAspectModel(aspectModel);
        setTransformer(new Transformer());
    }

    /**
     * @see Aspect#getType()
     */
    public String getType() {
        return "scenarioremainder";
    }

    private class Transformer extends BaseAspectValueTransformer implements AspectValueTransformer {

        
        /**
         * @see AspectValueTransformer#validate(AspectValue)
         */
        public void validate(AspectValue value) throws XBRLException {
            super.validate(value);
            if (value.getFragment() == null) return;
            if (! value.getFragment().isa("org.xbrlapi.impl.ScenarioImpl")) {
                throw new XBRLException("The aspect value must have a scenario fragment.");
            }
        }

        /**
         * @see AspectValueTransformer#getIdentifier(AspectValue)
         */
        public String getIdentifier(AspectValue value) throws XBRLException {
            validate(value);
            if (hasMapId(value)) {
                return getMapId(value);
            }
            if (value.getFragment() == null) {
                setMapId(value,"");
                return "";
            }
            Scenario f = ((Scenario) value.getFragment());
            List<Element> children = f.getChildElements();
            List<Element> remainder = new Vector<Element>();
            CHILDREN: for (Element child: children) {
                if (child.getNamespaceURI().equals(XDTConstants.XBRLDINamespace)) {
                    if (child.hasAttribute("dimension")) {
                        continue CHILDREN;
                    }
                }
                remainder.add(child);
            }
            String id = getLabelFromElements(remainder);
            setMapId(value,id);
            return id;
        }
        
        /**
         * @see AspectValueTransformer#getLabel(AspectValue)
         */
        public String getLabel(AspectValue value) throws XBRLException {
            return getIdentifier(value);
        }        

    }    
    
    /**
     * @see org.xbrlapi.aspects.Aspect#getValue(org.xbrlapi.Fact)
     */
    @SuppressWarnings("unchecked")
    public AspectValue getValue(Fact fact) throws XBRLException {
        Fragment fragment = getFragment(fact);
        if (fragment == null) {
            return new MissingAspectValue(this);
        }            
        return new ScenarioRemainderAspectValue(this,fragment);
    }        
    
    /**
     * @see Aspect#getFragmentFromStore(Fact)
     */
    public Fragment getFragmentFromStore(Fact fact) throws XBRLException {
        Context context = (Context) super.getFragmentFromStore(fact);
        Scenario scenario = context.getScenario();
        if (scenario == null) return null;
        return scenario;
    }    
}
