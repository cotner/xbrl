package org.xbrlapi.impl;


import java.net.URI;
import java.util.List;

import org.xbrlapi.ComplexTypeDeclaration;
import org.xbrlapi.ElementDeclaration;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ElementDeclarationImpl extends SchemaContentDeclarationImpl implements ElementDeclaration {

    /**
     * @see org.xbrlapi.ElementDeclaration#isAbstract()
     */
    public boolean isAbstract() throws XBRLException {
        if (getDataRootElement().getAttribute("abstract").equals("true")) {
            return true;
        }
        return false;
    }
    
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
                 if (declaration.getName().equals("item") && declaration.getTargetNamespace().equals(Constants.XBRL21Namespace)) {
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
                  if (declaration.getName().equals("tuple") && declaration.getTargetNamespace().equals(Constants.XBRL21Namespace)) {
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
       * @see org.xbrlapi.ElementDeclaration#hasSubstitutionGroup()
       */
      public boolean hasSubstitutionGroup() throws XBRLException {
          return getDataRootElement().hasAttribute("substitutionGroup");
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
     * @see org.xbrlapi.ElementDeclaration#getSubstitutionGroupDeclaration()
     */
    public ElementDeclaration getSubstitutionGroupDeclaration() throws XBRLException {
        
        ElementDeclaration result = null;
        if (this.hasSubstitutionGroup()) {
            try {
                result = (ElementDeclaration) getStore().getSchemaContent(this.getSubstitutionGroupNamespace(),this.getSubstitutionGroupLocalname());
                if (result == null) throw new XBRLException("The substitution group element declaration is not declared in a schema contained in the data store.");
            } catch (ClassCastException cce) {
                throw new XBRLException("The Substitution Group XML Schema element declaration is  of the wrong fragment type.",cce);
            }
        }
        return result;
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
     * @see ElementDeclaration#hasLocalComplexType()
     */
    public boolean hasLocalComplexType() throws XBRLException {
        return (getStore().queryCount("#roots#[@parentIndex='"+getIndex()+"' and @type='org.xbrlapi.impl.ComplexTypeDeclarationImpl']") == 1);
    }    
    
    /**
     * @see org.xbrlapi.ElementDeclaration#getLocalComplexType()
     */
    public ComplexTypeDeclaration getLocalComplexType() throws XBRLException {
        List<ComplexTypeDeclaration> ctds = this.getChildren("ComplexTypeDeclaration");
        if (ctds.size() > 1) throw new XBRLException("The element has too many local complex types.");
        if (ctds.size() == 0) throw new XBRLException("The element does not have a local complex type.");
        return ctds.get(0);
    }    

    
    /**
     * @see org.xbrlapi.ElementDeclaration#isFinalForRestriction()
     */
    public boolean isFinalForRestriction() throws XBRLException {
        String value = getDataRootElement().getAttribute("final");
        if (value.matches("restriction")) return true;
        return value.equals("#all");
    }
    
    /**
     * @see org.xbrlapi.ElementDeclaration#isFinalForRestriction()
     */
    public boolean isFinalForExtension() throws XBRLException {
        String value = getDataRootElement().getAttribute("final");
        if (value.matches("extension")) return true;
        return value.equals("#all");
    }
    
    /**
     * @see org.xbrlapi.ElementDeclaration#isBlockingSubstitution()
     */
    public boolean isBlockingSubstitution() throws XBRLException {
        String value = getDataRootElement().getAttribute("block");
        if (value.matches("substitution")) return true;
        return value.equals("#all");
    }
    
    /**
     * @see org.xbrlapi.ElementDeclaration#isBlockingRestriction()
     */
    public boolean isBlockingRestriction() throws XBRLException {
        String value = getDataRootElement().getAttribute("block");
        if (value.matches("restriction")) return true;
        return value.equals("#all");
    }
    
    /**
     * @see org.xbrlapi.ElementDeclaration#isBlockingRestriction()
     */
    public boolean isBlockingExtension() throws XBRLException {
        String value = getDataRootElement().getAttribute("block");
        if (value.matches("extension")) return true;
        return value.equals("#all");
    }


    
    /**
     * @see ElementDeclaration#getMaxOccurs()
     */
    public String getMaxOccurs() throws XBRLException {
        if (this.isGlobal()) throw new XBRLException("The element is global.");
        if (getDataRootElement().hasAttribute("maxOccurs")) return getDataRootElement().getAttribute("maxOccurs");
        return "1";
    }

    /**
     * @see ElementDeclaration#getMinOccurs()
     */
    public String getMinOccurs() throws XBRLException {
        if (this.isGlobal()) throw new XBRLException("The element is global.");
        if (getDataRootElement().hasAttribute("minOccurs")) return getDataRootElement().getAttribute("minOccurs");
        return "1";
    }
    
    
}