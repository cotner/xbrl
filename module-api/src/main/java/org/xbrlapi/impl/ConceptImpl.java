package org.xbrlapi.impl;

import org.w3c.dom.Element;
import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.FragmentList;
import org.xbrlapi.Schema;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ConceptImpl extends ElementDeclarationImpl implements Concept {

    /**
     * Get the concept's periodType, one of instant or duration
     * @return the period attribute value.
     * @throws XBRLException if the periodType is missing.
     * @see org.xbrlapi.Concept#getPeriodType()
     */
    public String getPeriodType() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttributeNS(Constants.XBRL21Namespace,"periodType"))
    		return root.getAttributeNS(Constants.XBRL21Namespace,"periodType");
    	throw new XBRLException("The period attribute is mandatory on XBRL concepts.");
    }
    


    /**
     * Get the concept's balance, one of debit or credit
     * @return the balance attribute value or null if none is specified.
     * @throws XBRLException
     * @see org.xbrlapi.Concept#getBalance()
     */
    public String getBalance() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttributeNS(Constants.XBRL21Namespace,"balance"))
    		return root.getAttributeNS(Constants.XBRL21Namespace,"balance");
    	return null;
    }
    
    /**
     * Retrieves the fragment that is the schema for this concept.
     * @return a Schema fragment for the parent schema of this concept.
     * @throws XBRLException
     * @see org.xbrlapi.Concept#getSchema()
     */
    public Schema getSchema() throws XBRLException {
    	return (Schema) this.getAncestorOrSelf("org.xbrlapi.impl.SchemaImpl");
    }
    
    /**
    * @see org.xbrlapi.Concept#getFacts()
    */
    public FragmentList<Fact> getFacts() throws XBRLException {
        getStore().setNamespaceBinding(this.getTargetNamespaceURI(),"xbrlapi_concept");
    	return getStore().<Fact>query("/*[*/xbrlapi_concept:"+ this.getName() + "]");
    }    
    
}