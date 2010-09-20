package org.xbrlapi.impl;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.w3c.dom.Element;
import org.xbrlapi.Concept;
import org.xbrlapi.Fact;
import org.xbrlapi.Schema;
import org.xbrlapi.TypeDeclaration;
import org.xbrlapi.networks.Network;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ConceptImpl extends ElementDeclarationImpl implements Concept {

    /**
     * The serial version UID.
     * @see 
     * http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
     * for information about what changes will require the serial version UID to be
     * modified.
     */
    private static final long serialVersionUID = -1067935705927988286L;

    /**
     * @see org.xbrlapi.Concept#getPeriodType()
     */
    public String getPeriodType() throws XBRLException {
    	Element root = getDataRootElement();
    	if (root.hasAttributeNS(Constants.XBRL21Namespace.toString(),"periodType"))
    		return root.getAttributeNS(Constants.XBRL21Namespace.toString(),"periodType");
    	if (! this.isTuple()) throw new XBRLException("The period attribute is mandatory on XBRL concepts in the item substitution group.");
    	return null;
    }
    
    /**
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
    	return getStore().<Fact>queryForXMLResources("#roots#[xbrlapi:data/xbrlapi_concept:"+ this.getName() + "]");
    }
    
    /**
     * @see org.xbrlapi.Concept#getRootFacts()
     */
     public List<Fact> getRootFacts() throws XBRLException {
         getStore().setNamespaceBinding(this.getTargetNamespace(),"xbrlapi_concept");
         String query = "for $instance in #roots#[@type='"+InstanceImpl.class.getName()+"'], $root in #roots#[xbrlapi:data/xbrlapi_concept:"+ this.getName() + "] where ($instance/@index = $root/@parentIndex) return $root";
         return getStore().<Fact>queryForXMLResources(query);
     }    
    
    /**
     * @see org.xbrlapi.Concept#getFactCount()
     */
    public long getFactCount() throws XBRLException {
        getStore().setNamespaceBinding(this.getTargetNamespace(),"xbrlapi_concept");
        String query = "for $root in #roots# where $root/xbrlapi:data/xbrlapi_concept:"+ this.getName() + " return $root";
        return getStore().queryCount(query);
    }
    
    /**
     * @see org.xbrlapi.Concept#getFactIndices()
     */
    public Set<String> getFactIndices() throws XBRLException {
        getStore().setNamespaceBinding(this.getTargetNamespace(),"xbrlapi_concept");
        return getStore().queryForIndices("#roots#[*/xbrlapi_concept:"+ this.getName() + "]");
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

    /**
     * @see Concept#isNumeric()
     */
    public boolean isNumeric() throws XBRLException {
        if (this.isTuple()) return false;
        TypeDeclaration typeDeclaration = this.getTypeDeclaration();
        return typeDeclaration.isNumericItemType();
    }
}