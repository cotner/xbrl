package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.xbrlapi.ActiveRelationship;
import org.xbrlapi.Arc;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fragment;
import org.xbrlapi.Resource;
import org.xbrlapi.builder.BuilderImpl;
import org.xbrlapi.networks.Relationship;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ActiveRelationshipImpl extends XMLImpl implements ActiveRelationship {


    
    /**
     * @param id The unique id of the fragment being created,
     * within the scope of the containing data store.
     * @throws XBRLException
     */
    public ActiveRelationshipImpl(Relationship relationship) throws XBRLException {
        super();
        this.setBuilder(new BuilderImpl());
        getBuilder().appendElement(Constants.XBRLAPINamespace,"fragment",Constants.XBRLAPIPrefix + ":fragment");    
        Fragment source = relationship.getSource();

        setSourceIndex(relationship.getSourceIndex());
        setSourceFragmentType(source.getType());
        setSourceName(source.getLocalname());
        setSourceNamespace(source.getNamespace());
        if (source.isa("org.xbrlapi.impl.ResourceImpl")) {
            setSourceLanguageCode(((Resource) source).getLanguage());
            setSourceRole(((Resource) source).getResourceRole());
        }

        Fragment target = relationship.getTarget();
        setTargetIndex(relationship.getTargetIndex());
        setTargetFragmentType(target.getType());
        setTargetName(target.getLocalname());
        setTargetNamespace(target.getNamespace());
        if (target.isa("org.xbrlapi.impl.ResourceImpl")) {
            setTargetLanguageCode(((Resource) target).getLanguage());
            setTargetRole(((Resource) target).getResourceRole());
        }

        Arc arc = relationship.getArc();
        setArcIndex(relationship.getArcIndex());
        setArcName(arc.getLocalname());
        setArcNamespace(arc.getNamespace());
        setArcrole(arc.getArcrole());
        
        ExtendedLink link = relationship.getLink();
        setLinkName(link.getLocalname());
        setLinkNamespace(link.getNamespace());
        setLinkRole(link.getLinkRole());
        
        setIndex(getArcIndex() +  "_" + getSourceIndex() + "_" + getTargetIndex());
        setLabelStatus();
        
        this.updateInStore();
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getArcIndex()
     */
    public String getArcIndex() {
        return getMetadataRootElement().getAttribute("arcIndex");
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getArcIndex()
     */
    public void setArcIndex(String index) throws XBRLException {
        getMetadataRootElement().setAttribute("arcIndex",index);
    }
    
    /**
     * @see org.xbrlapi.ActiveRelationship#getArcName()
     */
    public String getArcName() {
        return getMetadataRootElement().getAttribute("arcName");
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getArcNamespace()
     */
    public URI getArcNamespace() {
        try {
            return new URI(getMetadataRootElement().getAttribute("arcNamespace"));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getArcRole()
     */
    public URI getArcrole() {
        try {
            return new URI(getMetadataRootElement().getAttribute("arcRole"));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getLinkName()
     */
    public String getLinkName() {
        return getMetadataRootElement().getAttribute("linkName");
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getLinkNamespace()
     */
    public URI getLinkNamespace() {
        try {
            return new URI(getMetadataRootElement().getAttribute("linkNamespace"));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getLinkRole()
     */
    public URI getLinkRole() {
        try {
            return new URI(getMetadataRootElement().getAttribute("linkRole"));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getSource()
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> F getSource() throws XBRLException {
        return (F) this.getStore().getFragment(getSourceIndex());
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getSourceIndex()
     */
    public String getSourceIndex() {
        return this.getMetadataRootElement().getAttribute("sourceIndex");
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getSourceLanguageCode()
     */
    public String getSourceLanguageCode() {
        if (getMetadataRootElement().hasAttribute("sourceLanguage")) {
            return getMetadataRootElement().getAttribute("sourceLanguage");
        }
        return null;
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getSourceRole()
     */
    public URI getSourceRole() {
        try {
            if (getMetadataRootElement().hasAttribute("sourceRole")) {
                return new URI(getMetadataRootElement().getAttribute("sourceRole"));
            }
            return null;
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getSourceType()
     */
    public String getSourceType() {
        return getMetadataRootElement().getAttribute("sourceType");
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getSourceName()
     */
    public String getSourceName() {
        return getMetadataRootElement().getAttribute("sourceName");
    }    
    
    /**
     * @see org.xbrlapi.ActiveRelationship#getSourceNamespace()
     */
    public URI getSourceNamespace() {
        try {
            return new URI(getMetadataRootElement().getAttribute("sourceNamespace"));
        } catch (URISyntaxException e) {
            return null;// Cannot be thrown.
        }
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getTarget()
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> F getTarget() throws XBRLException {
        return (F) this.getStore().getFragment(getTargetIndex());
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getTargetIndex()
     */
    public String getTargetIndex() {
        return getMetadataRootElement().getAttribute("targetIndex");
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getTargetLanguageCode()
     */
    public String getTargetLanguageCode() {
        if (getMetadataRootElement().hasAttribute("targetLanguage")) {
            return getMetadataRootElement().getAttribute("targetLanguage");
        }
        return null;
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getTargetRole()
     */
    public URI getTargetRole() {
        try {
            if (getMetadataRootElement().hasAttribute("targetRole")) {
                return new URI(getMetadataRootElement().getAttribute("targetRole"));
            }
            return null;
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getTargetType()
     */
    public String getTargetType() {
        return getMetadataRootElement().getAttribute("targetType");
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#getTargetName()
     */
    public String getTargetName() {
        return getMetadataRootElement().getAttribute("targetName");
    }    
    
    /**
     * @see org.xbrlapi.ActiveRelationship#getTargetNamespace()
     */
    public URI getTargetNamespace() {
        try {
            return new URI(getMetadataRootElement().getAttribute("targetNamespace"));
        } catch (URISyntaxException e) {
            return null;// Cannot be thrown.
        }
    }
    
    /**
     * @see org.xbrlapi.ActiveRelationship#setArcName(java.lang.String)
     */
    public void setArcName(String name) throws XBRLException {
        getMetadataRootElement().setAttribute("arcName",name);
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#setArcNamespace(java.net.URI)
     */
    public void setArcNamespace(URI namespace) throws XBRLException {
        getMetadataRootElement().setAttribute("arcNamespace",namespace.toString());
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#setArcrole(java.net.URI)
     */
    public void setArcrole(URI role) throws XBRLException {
        getMetadataRootElement().setAttribute("arcRole",role.toString());
    }


    /**
     * @see org.xbrlapi.ActiveRelationship#setLinkName(java.lang.String)
     */
    public void setLinkName(String name) throws XBRLException {
        getMetadataRootElement().setAttribute("linkName",name);
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#setLinkNamespace(java.net.URI)
     */
    public void setLinkNamespace(URI namespace) throws XBRLException {
        getMetadataRootElement().setAttribute("linkNamespace",namespace.toString());
    }


    /**
     * @see org.xbrlapi.ActiveRelationship#setLinkRole(java.net.URI)
     */
    public void setLinkRole(URI role) throws XBRLException {
        getMetadataRootElement().setAttribute("linkRole",role.toString());
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#setSourceFragmentType(java.lang.String)
     */
    public void setSourceFragmentType(String type) throws XBRLException {
        getMetadataRootElement().setAttribute("sourceType",type);
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#setSourceIndex(java.lang.String)
     */
    public void setSourceIndex(String index) throws XBRLException {
        getMetadataRootElement().setAttribute("sourceIndex",index);
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#setSourceLanguageCode(java.lang.String)
     */
    public void setSourceLanguageCode(String code) throws XBRLException {
        getMetadataRootElement().setAttribute("sourceLanguage",code);
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#setSourceRole(java.net.URI)
     */
    public void setSourceRole(URI role) throws XBRLException {
        getMetadataRootElement().setAttribute("sourceRole",role.toString());
    }
    
    /**
     * @see org.xbrlapi.ActiveRelationship#setSourceName(String)
     */
    public void setSourceName(String name) {
        getMetadataRootElement().setAttribute("sourceName",name);
    }    
    
    /**
     * @see org.xbrlapi.ActiveRelationship#setSourceNamespace(URI)
     */
    public void setSourceNamespace(URI namespace) {
        getMetadataRootElement().setAttribute("sourceNamespace",namespace.toString());
    }    

    /**
     * @see org.xbrlapi.ActiveRelationship#setTargetFragmentType(java.lang.String)
     */
    public void setTargetFragmentType(String type) throws XBRLException {
        getMetadataRootElement().setAttribute("targetRole",type);
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#setTargetIndex(java.lang.String)
     */
    public void setTargetIndex(String index) throws XBRLException {
        getMetadataRootElement().setAttribute("targetIndex",index);
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#setTargetLanguageCode(java.lang.String)
     */
    public void setTargetLanguageCode(String code) throws XBRLException {
        getMetadataRootElement().setAttribute("targetLanguage",code);
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#setTargetRole(java.net.URI)
     */
    public void setTargetRole(URI role) throws XBRLException {
        getMetadataRootElement().setAttribute("targetRole",role.toString());
    }

    /**
     * @see org.xbrlapi.ActiveRelationship#setTargetName(String)
     */
    public void setTargetName(String name) {
        getMetadataRootElement().setAttribute("targetName",name);
    }    
    
    /**
     * @see org.xbrlapi.ActiveRelationship#setTargetNamespace(URI)
     */
    public void setTargetNamespace(URI namespace) {
        getMetadataRootElement().setAttribute("targetNamespace",namespace.toString());
    }        
    
    /**
     * @see org.xbrlapi.ActiveRelationship#isToLabel()
     */
    public boolean isToLabel() {
        if (this.getMetadataRootElement().hasAttribute("label")) return true;
        return false;
    }

    /**
     * Stores in the XML resource a 'label' attribute of
     * the root element if the relationship is to an XBRL 2.1 
     * or a Generic label.
     */
    private void setLabelStatus() {
        if (getTargetName().equals("label")) {
            URI namespace = getTargetNamespace();
            if (namespace.equals(Constants.XBRL21LinkNamespace()) || namespace.equals(Constants.GenericLabelNamespace())) {
                URI arcrole = getArcrole();
                if (arcrole.equals(Constants.LabelArcRole()) || arcrole.equals(Constants.GenericLabelArcRole())) {
                    getMetadataRootElement().setAttribute("label","true");
                }
            }
        }
    }
}
