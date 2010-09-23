package org.xbrlapi.impl;

import java.net.URI;

import org.apache.log4j.Logger;
import org.xbrlapi.AspectValuePair;
import org.xbrlapi.Fragment;
import org.xbrlapi.aspects.alt.AspectValue;
import org.xbrlapi.builder.BuilderImpl;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class AspectValuePairImpl extends NonFragmentXMLImpl implements AspectValuePair {
	
    /**
     * 
     */
    private static final long serialVersionUID = 9141633045533213712L;
    
    private static final Logger logger = Logger.getLogger(AspectValuePairImpl.class);
    
    /**
	 * @throws XBRLException
	 */
	public AspectValuePairImpl() throws XBRLException {
		super();
        setBuilder(new BuilderImpl());
	}
	
	public AspectValuePairImpl(AspectValue aspectValue, Fragment fragment) throws XBRLException {
		this();
        if (aspectValue == null) throw new XBRLException("The aspect value must not be null.");
        if (fragment == null) throw new XBRLException("The fragment must not be null.");
        String id = fragment.getStore().getId(aspectValue.getAspectId() + aspectValue.getId() + fragment.getIndex());
        this.setIndex(id);
        this.setFragmentIndex(fragment.getIndex());
        this.setFragmentType(fragment.getType());
        this.setAspectId(aspectValue.getAspectId());
        this.setAspectValueId(aspectValue.getId());
	}
    
    /**
     * @param valueId The aspect value ID
     * @throws XBRLException if a parameter is null.
     */
    private void setAspectValueId(String valueId) throws XBRLException {
        if (valueId == null) throw new XBRLException("The aspect value ID must not be null.");
        this.setMetaAttribute("aspectValueId",valueId);
    }

    /**
     * @param aspectId The aspect ID
     * @throws XBRLException if a parameter is null.
     */
    private void setAspectId(URI aspectId) throws XBRLException {
        if (aspectId == null) throw new XBRLException("The aspectId must not be null.");
        this.setMetaAttribute("aspectId",aspectId.toString());
    }
    
    /**
     * @param fragmentIndex The index of the fragment with this aspect value.
     * @throws XBRLException if a parameter is null.
     */
    private void setFragmentIndex(String fragmentIndex) throws XBRLException {
        if (fragmentIndex == null) throw new XBRLException("The fragment index must not be null.");
        this.setMetaAttribute("fragmentIndex",fragmentIndex);
    }

    /**
     * @param fragmentType The type of the fragment with this aspect value.
     * @throws XBRLException if a parameter is null.
     */
    private void setFragmentType(String fragmentType) throws XBRLException {
        if (fragmentType == null) throw new XBRLException("The fragment type must not be null.");
        this.setMetaAttribute("fragmentType",fragmentType);
    }
    
    /**
     * @see AspectValuePair#getFragmentIndex()
     */
    public String getFragmentIndex() throws XBRLException {
        return this.getMetaAttribute("fragmentIndex");
    }
    
    /**
     * @see AspectValuePair#getFragmentType()
     */
    public String getFragmentType() throws XBRLException {
        return this.getMetaAttribute("fragmentType");
    }    

    /**
     * @see AspectValuePair#getAspectId()
     */
    public URI getAspectId() throws XBRLException {
        return URI.create(this.getMetaAttribute("aspectId"));
    }

    /**
     * @see AspectValuePair#getAspectValueId()
     */
    public String getAspectValueId() throws XBRLException {
        return this.getMetaAttribute("aspectValueId");
    }

    /**
     * @see AspectValuePair#getFragment()
     */
    public Fragment getFragment() throws XBRLException {
        return this.getStore().<Fragment>getXMLResource(getFragmentIndex());
    }

}
