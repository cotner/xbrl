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
        setSourceType(source.getType());
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
     * @param index the index of the arc defining this relationship.
     */
    private void setArcIndex(String index) {
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
     * @param name The local name of the arc element.
     */
    private void setArcName(String name) {
        getMetadataRootElement().setAttribute("arcName",name);
    }

    /**
     * @param namespace The namespace of the arc element.
     */
    private void setArcNamespace(URI namespace) {
        getMetadataRootElement().setAttribute("arcNamespace",namespace.toString());
    }

    /**
     * @param role The XLink arcrole value
     */
    private void setArcrole(URI role) {
        getMetadataRootElement().setAttribute("arcRole",role.toString());
    }

    /**
     * @param name The local name of the containing extended link element.
     */
    private void setLinkName(String name) {
        getMetadataRootElement().setAttribute("linkName",name);
    }

    /**
     * @param namespace The namespace of the containing extended link element.
     */
    private void setLinkNamespace(URI namespace) {
        getMetadataRootElement().setAttribute("linkNamespace",namespace.toString());
    }

    /**
     * @param role The containing extended link XLink role value
     */
    private void setLinkRole(URI role) {
        getMetadataRootElement().setAttribute("linkRole",role.toString());
    }

    /**
     * @param code the source fragment type.
     */
    private void setSourceType(String type) {
        getMetadataRootElement().setAttribute("sourceType",type);
    }

    /**
     * @param index The source fragment index.
     */
    private void setSourceIndex(String index) {
        getMetadataRootElement().setAttribute("sourceIndex",index);
    }

    /**
     * @param code the source XML language code.
     */
    private void setSourceLanguageCode(String code) {
        getMetadataRootElement().setAttribute("sourceLanguage",code);
    }

    /**
     * @param role the source XLink role.
     */
    private void setSourceRole(URI role) {
        getMetadataRootElement().setAttribute("sourceRole",role.toString());
    }
    
    /**
     * @param name The local name of the source element.
     */
    private void setSourceName(String name) {
        getMetadataRootElement().setAttribute("sourceName",name);
    }    
    
    /**
     * @param namespace The namespace of the source element.
     */
    private void setSourceNamespace(URI namespace) {
        getMetadataRootElement().setAttribute("sourceNamespace",namespace.toString());
    }    

    /**
     * @param code the target fragment type.
     */
    private void setTargetFragmentType(String type) {
        getMetadataRootElement().setAttribute("targetRole",type);
    }

    /**
     * @param index The target fragment index.
     */
    private void setTargetIndex(String index) {
        getMetadataRootElement().setAttribute("targetIndex",index);
    }

    /**
     * @param code the target XML language code.
     */
    private void setTargetLanguageCode(String code) {
        getMetadataRootElement().setAttribute("targetLanguage",code);
    }

    /**
     * @param role the target XLink role.
     */
    private void setTargetRole(URI role) {
        getMetadataRootElement().setAttribute("targetRole",role.toString());
    }

    /**
     * @param name The local name of the target element.
     */
    private void setTargetName(String name) {
        getMetadataRootElement().setAttribute("targetName",name);
    }    
    
    /**
     * @param namespace The namespace of the target element.
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
