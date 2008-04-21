package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Context extends FactDimensionContainer {
    
    /**
     * Get the entity of the context.
     * @throws XBRLException if the entity is missing or if more than one entity is in the context.
     */
    public Entity getEntity() throws XBRLException;

    /**
     * Get the period of the context.
     *
     * @throws XBRLException
     */
    public Period getPeriod() throws XBRLException;

    /**
     * Get the scenario of the context.
     * Returns null if there is no scenario.
     *
     * @throws XBRLException
     */
    public Scenario getScenario() throws XBRLException;

    /**
     * Tests if the context is c-equal to another context
     * returning true if they are c-equal.
     * See the XBRL 2.1 specification for a definition of c-equal. 
     *
     * @param context The context being compared.
     * @throws XBRLException
     */
    public boolean equals(Context context) throws XBRLException;    
    
}
