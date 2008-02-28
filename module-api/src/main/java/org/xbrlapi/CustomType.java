package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * Custom type of XLink arcrole or role
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface CustomType extends Fragment {

    /**
     * Get the id of the customType.
     *
     * @throws XBRLException
     */
    public String getCustomTypeId() throws XBRLException;
    
    /**
     * Set the id of the arcroleType.
     * TODO Throw an error if the id is used anywhere else
     * in the schema containing the custom type.
     *
     * @param id The id of the custom type
     * @throws XBRLException
     */
    public void setCustomTypeId(String id) throws XBRLException;

    /**
     * Get the custom URI being defined.
     *
     * @throws XBRLException
     */
    public String getCustomURI() throws XBRLException;
    
    /**
     * Set the custom URI being defined.
     *
     * @param uri The URI value
     * @throws XBRLException
     */
    public void setCustomURI(String uri) throws XBRLException;

	/**
	 * Get the string value of the role Definition.
	 * @return the string value of the role description or null if none is provided.
	 * @throws XBRLException.
	 */
    public String getDefinition() throws XBRLException;
    
    /**
     * Set the definition of the custom URI.
     *
     * @param definitionValue The definition of the custom URI
     * @throws XBRLException
     */
    public void setDefinition(String definitionValue) throws XBRLException;
    
    /**
     * @param fragment The fragment to be tested to determine if the custom
     * role has been used correctly based on usedOn constraints.
     * @return true if the custom role has been used correctly on the fragment
     * being checked.  Returns false otherwise.
     * @throws XBRLException
     */
    public boolean isUsedCorrectly(Fragment fragment) throws XBRLException;

    /**
     * Returns true only if the arcrole can be used on the specified element
     *
     * @param namespaceURI The namespace of the element being tested for
     * @param localname The local name of the element being tested for
     * @throws XBRLException
     */
    public boolean isUsedOn(String namespaceURI, String localname) throws XBRLException;
    
    /**
     * Add a usedOn declaration to the custom URI definition.
     *
     * @param usedOn the usedOn fragment to be added 
     * @throws XBRLException
     */
    public void addUsedOn(UsedOn usedOn) throws XBRLException;

    /**
     * Remove a usedOn declaration from the custom URI definition.
     *
     * @param usedOn the usedOn fragment to be added 
     * @throws XBRLException
     */
    public void removeUsedOn(UsedOn usedOn) throws XBRLException;
    
	/**
	 * @return the list of usedOn child fragments.
	 * @throws XBRLException.
	 */
	public FragmentList<UsedOn> getUsedOns() throws XBRLException;    

}
