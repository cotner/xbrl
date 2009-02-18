package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.xbrlapi.Fragment;
import org.xbrlapi.Relationship;
import org.xbrlapi.builder.BuilderImpl;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class RelationshipImpl extends XMLImpl implements Relationship {

    public RelationshipImpl() throws XBRLException {
        super();
        this.setBuilder(new BuilderImpl());
        getBuilder().appendElement(Constants.XBRLAPINamespace,"fragment",Constants.XBRLAPIPrefix + ":fragment");    
    }
    
    /**
     * @param id The unique id of the fragment being created,
     * within the scope of the containing data store.
     * @throws XBRLException
     */
    public RelationshipImpl(org.xbrlapi.networks.Relationship relationship) throws XBRLException {
        this();
        this.setIndex(relationship.getArcIndex() +  "_" + relationship.getSourceIndex() + "_" + relationship.getTargetIndex());
        getBuilder().appendElement(
                Constants.XBRLAPINamespace,
                "fragment",
                Constants.XBRLAPIPrefix + ":fragment");
    }

    /**
     * @see org.xbrlapi.Relationship#getArcName()
     */
    public String getArcName() {
        return getMetadataRootElement().getAttribute("arcName");
    }

    /**
     * @see org.xbrlapi.Relationship#getArcNamespace()
     */
    public URI getArcNamespace() {
        try {
            return new URI(getMetadataRootElement().getAttribute("arcNamespace"));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see org.xbrlapi.Relationship#getArcRole()
     */
    public URI getArcRole() {
        try {
            return new URI(getMetadataRootElement().getAttribute("arcRole"));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see org.xbrlapi.Relationship#getLinkName()
     */
    public String getLinkName() {
        return getMetadataRootElement().getAttribute("linkName");
    }

    /**
     * @see org.xbrlapi.Relationship#getLinkNamespace()
     */
    public URI getLinkNamespace() {
        try {
            return new URI(getMetadataRootElement().getAttribute("linkNamespace"));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see org.xbrlapi.Relationship#getLinkRole()
     */
    public URI getLinkRole() {
        try {
            return new URI(getMetadataRootElement().getAttribute("linkRole"));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * @see org.xbrlapi.Relationship#getSource()
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> F getSource() throws XBRLException {
        return (F) this.getStore().getFragment(getSourceIndex());
    }

    /**
     * @see org.xbrlapi.Relationship#getSourceIndex()
     */
    public String getSourceIndex() {
        return this.getMetadataRootElement().getAttribute("sourceIndex");
    }

    /**
     * @see org.xbrlapi.Relationship#getSourceLanguageCode()
     */
    public String getSourceLanguageCode() {
        if (getMetadataRootElement().hasAttribute("sourceLanguage")) {
            return getMetadataRootElement().getAttribute("sourceLanguage");
        }
        return null;
    }

    /**
     * @see org.xbrlapi.Relationship#getSourceRole()
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
     * @see org.xbrlapi.Relationship#getSourceType()
     */
    public String getSourceType() {
        return getMetadataRootElement().getAttribute("sourceType");
    }

    /**
     * @see org.xbrlapi.Relationship#getTarget()
     */
    @SuppressWarnings("unchecked")
    public <F extends Fragment> F getTarget() throws XBRLException {
        return (F) this.getStore().getFragment(getTargetIndex());
    }

    /**
     * @see org.xbrlapi.Relationship#getTargetIndex()
     */
    public String getTargetIndex() {
        return getMetadataRootElement().getAttribute("targetIndex");
    }

    /**
     * @see org.xbrlapi.Relationship#getTargetLanguageCode()
     */
    public String getTargetLanguageCode() {
        if (getMetadataRootElement().hasAttribute("targetLanguage")) {
            return getMetadataRootElement().getAttribute("targetLanguage");
        }
        return null;
    }

    /**
     * @see org.xbrlapi.Relationship#getTargetRole()
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
     * @see org.xbrlapi.Relationship#getTargetType()
     */
    public String getTargetType() {
        return getMetadataRootElement().getAttribute("targetType");
    }

    /**
     * @see org.xbrlapi.Relationship#setArcName(java.lang.String)
     */
    public void setArcName(String name) {
        getMetadataRootElement().setAttribute("arcName",name);
    }

    /**
     * @see org.xbrlapi.Relationship#setArcNamespace(java.net.URI)
     */
    public void setArcNamespace(URI namespace) {
        getMetadataRootElement().setAttribute("arcNamespace",namespace.toString());
    }

    /**
     * @see org.xbrlapi.Relationship#setArcRole(java.net.URI)
     */
    public void setArcRole(URI role) {
        getMetadataRootElement().setAttribute("arcRole",role.toString());
    }

    /**
     * @see org.xbrlapi.Relationship#setLinkName(java.lang.String)
     */
    public void setLinkName(String name) {
        getMetadataRootElement().setAttribute("linkName",name);
    }

    /**
     * @see org.xbrlapi.Relationship#setLinkNamespace(java.net.URI)
     */
    public void setLinkNamespace(URI namespace) {
        getMetadataRootElement().setAttribute("linkNamespace",namespace.toString());
    }

    /**
     * @see org.xbrlapi.Relationship#setLinkRole(java.net.URI)
     */
    public void setLinkRole(URI role) {
        getMetadataRootElement().setAttribute("linkRole",role.toString());
    }

    /**
     * @see org.xbrlapi.Relationship#setSourceFragmentType(java.lang.String)
     */
    public void setSourceFragmentType(String type) {
        getMetadataRootElement().setAttribute("sourceType",type);
    }

    /**
     * @see org.xbrlapi.Relationship#setSourceIndex(java.lang.String)
     */
    public void setSourceIndex(String index) {
        getMetadataRootElement().setAttribute("sourceIndex",index);
    }

    /**
     * @see org.xbrlapi.Relationship#setSourceLanguageCode(java.lang.String)
     */
    public void setSourceLanguageCode(String code) {
        getMetadataRootElement().setAttribute("sourceLanguage",code);
    }

    /**
     * @see org.xbrlapi.Relationship#setSourceRole(java.net.URI)
     */
    public void setSourceRole(URI role) {
        getMetadataRootElement().setAttribute("sourceRole",role.toString());
    }

    /**
     * @see org.xbrlapi.Relationship#setTargetFragmentType(java.lang.String)
     */
    public void setTargetFragmentType(String type) {
        getMetadataRootElement().setAttribute("targetRole",type);
    }

    /**
     * @see org.xbrlapi.Relationship#setTargetIndex(java.lang.String)
     */
    public void setTargetIndex(String index) {
        getMetadataRootElement().setAttribute("targetIndex",index);
    }

    /**
     * @see org.xbrlapi.Relationship#setTargetLanguageCode(java.lang.String)
     */
    public void setTargetLanguageCode(String code) {
        getMetadataRootElement().setAttribute("targetLanguage",code);
    }

    /**
     * @see org.xbrlapi.Relationship#setTargetRole(java.net.URI)
     */
    public void setTargetRole(URI role) {
        getMetadataRootElement().setAttribute("targetRole",role.toString());
    }
    
}
