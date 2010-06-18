package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface Fact extends Fragment {

    /**
     * Return true if the fact is a tuple and false otherwise
     */
    public boolean isTuple() throws XBRLException;

    /**
     * Get the instance that this fact is a part of
     * 
     * @return the xbrl instance fragment
     * @throws XBRLException
     *             if the fact is not part of an instance.
     */
    public Instance getInstance() throws XBRLException;

    /**
     * Get the tuple that this fact is a part of
     * 
     * @return the xbrl tuple fragment or null if this
     * @throws XBRLException
     */
    public Tuple getTuple() throws XBRLException;

    /**
     * Get the concept that defines the syntax for this fact.
     * 
     * @return the appropriate concept fragment from the data store.
     * @throws XBRLException
     *             if the concept cannot be found.
     */
    public Concept getConcept() throws XBRLException;

    /**
     * @return true if the fact is reported with units and false otherwise.
     * @throws XBRLException
     */
    public boolean isNumeric() throws XBRLException;

    /**
     * @return true if the fact is a fraction item type or derived therefrom and
     *         false otherwise.
     * @throws XBRLException
     */
    public boolean isFraction() throws XBRLException;

    /**
     * Return true if the fact has a nill value.
     * 
     * @throws XBRLException
     */
    public boolean isNil() throws XBRLException;

}
