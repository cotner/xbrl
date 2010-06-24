package org.xbrlapi.xdt.aspects.alt;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.xbrlapi.Scenario;
import org.xbrlapi.aspects.alt.AspectHandler;
import org.xbrlapi.aspects.alt.AspectValue;
import org.xbrlapi.aspects.alt.AspectValueImpl;
import org.xbrlapi.aspects.alt.IDGenerator;
import org.xbrlapi.utilities.XBRLException;
import org.xbrlapi.xdt.XDTConstants;

public class ScenarioRemainderAspectValue extends AspectValueImpl implements AspectValue {

    /**
     * 
     */
    private static final long serialVersionUID = 7618757738645441910L;

    protected final static Logger logger = Logger.getLogger(ScenarioRemainderAspectValue.class);

    /** 
     * The list of child elements of the segment or null if
     * the segment aspect value is missing.
     */
    List<Element> children = null;
    
    /**
     * @return the list of child elements in the segment, in document order.
     */
    public List<Element> getChildren() {
        return children;
    }

    /**
     * Missing aspect value constructor.
     */
    public ScenarioRemainderAspectValue() {
        super();
    }

    /**
     * If the scenario contains no non-XDT content then this aspect value is
     * missing.
     * 
     * @param scenario
     *            The scenario fragment.
     */
    public ScenarioRemainderAspectValue(Scenario scenario) throws XBRLException {
        super();
        List<Element> result = new Vector<Element>();
        if (scenario != null) {
            List<Element> children = scenario.getChildElements();
            CHILDREN: for (Element child: children) {
                if (child.getNamespaceURI().equals(XDTConstants.XBRLDINamespace.toString()) && 
                (child.getLocalName().equals("TypedMember") || child.getLocalName().equals("explicitMember"))) {
                    continue CHILDREN;
                }
                result.add(child);
            }
        }
        if (result.size() > 0) this.children = result;
    }
    
    /**
     * @see AspectHandler#getAspectId()
     */
    public URI getAspectId() {
        return ScenarioRemainderAspect.ID;
    }
    
    /**
     * @see AspectValue#isMissing()
     */
    public boolean isMissing() {
        return getChildren() == null;
    }

    /**
     * The missing aspect value ID is the empty string.
     * @see AspectValue#getId()
     */
    String id = null;
    public String getId() {
        if (id != null) return id;
        if (isMissing()) id = "";
        else id = IDGenerator.getLabel(children);
        return id;
    }

}
