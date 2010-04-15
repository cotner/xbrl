package org.xbrlapi.impl;

import java.util.List;

import org.xbrlapi.Context;
import org.xbrlapi.Entity;
import org.xbrlapi.Item;
import org.xbrlapi.Period;
import org.xbrlapi.Scenario;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ContextImpl extends FactDimensionContainerImpl implements Context {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = 875204843953696951L;

    /**
     * Get the entity of the context.
     * @throws XBRLException if the entity is missing or if more than one entity is in the context.
     * @see org.xbrlapi.Context#getEntity()
     */
    public Entity getEntity() throws XBRLException {
    	List<Entity> fs = this.<Entity>getChildren("org.xbrlapi.impl.EntityImpl");
        if (fs.size() == 1) return fs.get(0);
    	if (fs.size() == 0) throw new XBRLException("The entity is missing from the context.");
    	throw new XBRLException("There is more than one entity in this context.");
    }
    
    /**
     * Get the period of the context.
     *
     * @throws XBRLException if the period is missing from the context
     * or there is more than one period in the context.
     * @see org.xbrlapi.Context#getPeriod()
     */
    public Period getPeriod() throws XBRLException {
    	List<Period> fs = this.<Period>getChildren("org.xbrlapi.impl.PeriodImpl");
    	if (fs.size() == 0) throw new XBRLException("The period is missing from the context.");
    	if (fs.size() == 1) return fs.get(0);
    	throw new XBRLException("There is more than one period in this context.");
    }

    /**
     * Get the scenario of the context.
     * @return the scenario if one exists or null if there is no scenario.
     * @throws XBRLException if there is more than one scenario.
     * @see org.xbrlapi.Context#getScenario()
     */
    public Scenario getScenario() throws XBRLException {
    	List<Scenario> scenarios = this.<Scenario>getChildren("org.xbrlapi.impl.ScenarioImpl");
    	if (scenarios.size() == 0) return null;
    	if (scenarios.size() == 1) return scenarios.get(0);
    	throw new XBRLException("There is more than one scenario in context " + this.getIndex());
    }

    /**
     * Tests if the context is c-equal to another context
     * See the XBRL 2.1 specification for a definition of c-equal. 
     * @return true if this context is c-equal to the specified one and false otherwise.
     * @param context The context being compared.
     * @throws XBRLException
     * @see org.xbrlapi.Context#equals(Context)
     */
    public boolean equals(Context context) throws XBRLException {
    	
    	if (! getEntity().equals(context.getEntity()))
    		return false;

    	if (! getPeriod().equals(context.getPeriod()))
    		return false;

    	if ((getScenario()==null) && (context.getScenario()==null)) return true;
    	
    	if (! getScenario().equals(context.getScenario()))
    		return false;
    	
    	return true;
    }
    
    /**
     * @see org.xbrlapi.FactDimensionContainer#getReferencingItems()
     */
    public List<Item> getReferencingItems() throws XBRLException {
        String query = "#roots#[@uri='" + this.getURI() + "' and */*/@contextRef='" + this.getId() + "']";
        return getStore().<Item>queryForXMLResources(query);
    }    
    
}
