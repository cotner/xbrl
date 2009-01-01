package org.xbrlapi.impl;

import org.xbrlapi.Context;
import org.xbrlapi.Entity;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Item;
import org.xbrlapi.Period;
import org.xbrlapi.Scenario;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ContextImpl extends FactDimensionContainerImpl implements Context {

    /**
     * Get the entity of the context.
     * @throws XBRLException if the entity is missing or if more than one entity is in the context.
     * @see org.xbrlapi.Context#getEntity()
     */
    public Entity getEntity() throws XBRLException {
    	FragmentList<Entity> fs = this.<Entity>getChildren("org.xbrlapi.impl.EntityImpl");
    	if (fs.getLength() == 0) throw new XBRLException("The entity is missing from the context.");
    	if (fs.getLength() == 1) return fs.getFragment(0);
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
    	FragmentList<Period> fs = this.<Period>getChildren("org.xbrlapi.impl.PeriodImpl");
    	if (fs.getLength() == 0) throw new XBRLException("The period is missing from the context.");
    	if (fs.getLength() == 1) return fs.getFragment(0);
    	throw new XBRLException("There is more than one period in this context.");
    }

    /**
     * Get the scenario of the context.
     * @return the scenario if one exists or null if there is no scenario.
     * @throws XBRLException if there is more than one scenario.
     * @see org.xbrlapi.Context#getScenario()
     */
    public Scenario getScenario() throws XBRLException {
    	FragmentList<Scenario> scenarios = this.<Scenario>getChildren("org.xbrlapi.impl.ScenarioImpl");
    	if (scenarios.getLength() == 0) return null;
    	if (scenarios.getLength() == 1) return scenarios.getFragment(0);
    	throw new XBRLException("There is more than one scenario in this context.");
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
    public FragmentList<Item> getReferencingItems() throws XBRLException {
        String query = "/*[@uri='" + this.getURI() + "' and */*/@contextRef='" + this.getId() + "']";
        return getStore().<Item>query(query);
    }    
    
}
