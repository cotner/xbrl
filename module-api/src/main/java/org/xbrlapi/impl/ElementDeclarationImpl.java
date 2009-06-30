package org.xbrlapi.impl;


import java.net.URI;
import java.util.List;

import org.w3c.dom.Element;
import org.xbrlapi.ElementDeclaration;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ElementDeclarationImpl extends SchemaDeclarationImpl implements ElementDeclaration {

    /**
     * @see org.xbrlapi.ElementDeclaration#isNillable()
     */
    public boolean isNillable() throws XBRLException {
    	if (getDataRootElement().getAttribute("nillable").equals("true")) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * @see org.xbrlapi.ElementDeclaration#isItem()
     */
     public boolean isItem() throws XBRLException {
         String sgName = this.getSubstitutionGroupLocalname();
         logger.debug(sgName);
         if (sgName == null) return false;
         URI sgNamespace = this.getSubstitutionGroupNamespace();
         logger.debug(sgNamespace);
         String query = "#roots#[*/xsd:element/@name='" + sgName + "']";
         logger.debug(query);
         List<ElementDeclaration> declarations = getStore().<ElementDeclaration>queryForXMLResources(query);
         for (ElementDeclaration declaration: declarations) {
             if (declaration.getTargetNamespace().equals(sgNamespace)) {
                 if (declaration.getName().equals("item") && declaration.getTargetNamespace().toString().equals(org.xbrlapi.utilities.Constants.XBRL21Namespace)) {
                     return true;
                 }
                 try {
                     return declaration.isItem();
                 } catch (XBRLException e) {
                     return false;
                 }
             }
         }
         throw new XBRLException("The substitution group is invalid.");
     }
     
     /**
      * @see org.xbrlapi.ElementDeclaration#isTuple()
      */
      public boolean isTuple() throws XBRLException {
          String sgName = this.getSubstitutionGroupLocalname();
          if (sgName == null) return false;
          URI sgNS = this.getSubstitutionGroupNamespace();
          String query = "#roots#[*/xsd:element/@name='" + sgName + "']";
          List<ElementDeclaration> declarations = getStore().<ElementDeclaration>queryForXMLResources(query);
          for (ElementDeclaration declaration: declarations) {
              if (declaration.getTargetNamespace().equals(sgNS)) {
                  if (declaration.getName().equals("tuple") && declaration.getTargetNamespace().toString().equals(org.xbrlapi.utilities.Constants.XBRL21Namespace)) {
                      return true;
                  }
                  try {
                      return declaration.isTuple();
                  } catch (XBRLException e) {
                      return false;
                  }
              }
          }
          throw new XBRLException("The substitution group is invalid.");
      }     
    



    /**
     * @see org.xbrlapi.ElementDeclaration#getTypeNamespace()
     */
    public URI getTypeNamespace() throws XBRLException {
    	String type = getTypeQName();
    	if (type.equals("") || (type == null)) throw new XBRLException("The element declaration does not declare its XML Schema data type via a type attribute.");
        return getNamespaceFromQName(type, getDataRootElement());
    }
    
    /**
     * @see org.xbrlapi.ElementDeclaration#getTypeNamespaceAlias()
     */
    public String getTypeNamespaceAlias() throws XBRLException {
    	String type = getTypeQName();
    	if (type.equals("") || (type == null))
			throw new XBRLException("The element declaration does not declare its XML Schema data type via a type attribute.");    	
    	return getPrefixFromQName(type);
    }

    /**
     * @see org.xbrlapi.ElementDeclaration#getTypeQName()
     */
    public String getTypeQName() throws XBRLException {
    	if (getDataRootElement().hasAttribute("type")) {
    		return getDataRootElement().getAttribute("type");
    	}
    	return null;
    }

    /**
     * @see org.xbrlapi.ElementDeclaration#getTypeLocalname()
     */  
    public String getTypeLocalname() throws XBRLException {
    	String type = getTypeQName();
    	if (type.equals("") || (type == null))
			throw new XBRLException("The element declaration does not declare its XML Schema data type via a type attribute.");
    	return getLocalnameFromQName(type);
    }



    /**
     * @see org.xbrlapi.ElementDeclaration#getSubstitutionGroupNamespace()
     */
    public URI getSubstitutionGroupNamespace() throws XBRLException {
    	String qname = getSubstitutionGroupQName();
    	if (qname.equals("") || (qname == null)) throw new XBRLException("The element declaration does not declare its XML Schema substitution group via a substitutionGroup attribute.");   	
	    return getNamespaceFromQName(qname, getDataRootElement());
    }
    
    /**
     * @see org.xbrlapi.ElementDeclaration#getSubstitutionGroupNamespaceAlias()
     */    
    public String getSubstitutionGroupNamespaceAlias() throws XBRLException {
    	String sg = getSubstitutionGroupQName();
    	if (sg.equals("") || (sg == null))
			throw new XBRLException("The element declaration does not declare its substitution group via a substitutionGroup attribute.");    	
    	return getPrefixFromQName(sg);
    }

    /**
     * @see org.xbrlapi.ElementDeclaration#getSubstitutionGroupQName()
     */  
    public String getSubstitutionGroupQName() throws XBRLException {
    	if (getDataRootElement().hasAttribute("substitutionGroup"))
    		return getDataRootElement().getAttribute("substitutionGroup");
    	return null;
    }

    /**
     * @see org.xbrlapi.ElementDeclaration#getSubstitutionGroupLocalname()
     */  
    public String getSubstitutionGroupLocalname() throws XBRLException {
    	String sg = getSubstitutionGroupQName();
    	if (sg == null) return null;
    	if (sg.equals(""))
			throw new XBRLException("The element declaration must not have an empty substitution group attribute.");    	
    	return getLocalnameFromQName(sg);
    }




    /**
     * Get the default attribute value for the element
     * @return null if there is no default attribute, otherwise return its value.
     * @throws XBRLException
     * @see org.xbrlapi.ElementDeclaration#getDefault()
     */
    public String getDefault() throws XBRLException {
    	Element root =getDataRootElement(); 
    	if (! root.hasAttribute("default")) return null;
    	return root.getAttribute("default");
    }
    


    /**
     * Get the fixed attribute value for the element
     * @return null if there is no fixed attribute, otherwise return its value.
     * @throws XBRLException
     * @see org.xbrlapi.ElementDeclaration#getFixed()
     */
    public String getFixed() throws XBRLException {
    	Element root =getDataRootElement(); 
    	if (! root.hasAttribute("fixed")) return null;
    	return root.getAttribute("fixed");    	
    }
    



}