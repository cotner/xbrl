package org.xbrlapi.impl;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Element;
import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.Schema;
import org.xbrlapi.networks.Network;
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
    	if (root.hasAttributeNS(Constants.XBRL21Namespace.toString(),"periodType"))
    		return root.getAttributeNS(Constants.XBRL21Namespace.toString(),"periodType");
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
    	if (root.hasAttributeNS(Constants.XBRL21Namespace.toString(),"balance"))
    		return root.getAttributeNS(Constants.XBRL21Namespace.toString(),"balance");
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
    public List<Fact> getFacts() throws XBRLException {
        getStore().setNamespaceBinding(this.getTargetNamespace(),"xbrlapi_concept");
    	return getStore().<Fact>queryForXMLResources("#roots#[*/xbrlapi_concept:"+ this.getName() + "]");
    }    
 

    
    /**
     * @see org.xbrlapi.Concept#getPresentationNetworkLinkroles()
     */
    public List<URI> getPresentationNetworkLinkroles() throws XBRLException {
        List<URI> roles = new Vector<URI>();
        for (Network network: getStore().getMinimalNetworksWithArcrole(this,Constants.PresentationArcrole)) {
            roles.add(network.getLinkRole());
        }
        return roles;
    }

    
}