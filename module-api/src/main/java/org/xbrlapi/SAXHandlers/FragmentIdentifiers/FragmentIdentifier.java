package org.xbrlapi.SAXHandlers.FragmentIdentifiers;

import org.xbrlapi.Fragment;
import org.xbrlapi.loader.Loader;
import org.xbrlapi.utilities.XBRLException;
import org.xml.sax.Attributes;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FragmentIdentifier {

    /**
     * The loader that is using this fragment identifier
     */
    private Loader loader = null;
    protected Loader getLoader() {
        return loader;
    }
    protected void setLoader(Loader loader) {
        this.loader = loader;
    }
    
    /**
     * @param loader The loader using this fragment identifier.
     * @throws XBRLException if the XLink processor is null.
     */
    public FragmentIdentifier(Loader loader) throws XBRLException {
        if (loader == null) throw new XBRLException("The loader is null.");
        this.loader = loader;
    }
    
    /**
     * @param namespaceURI the namespace of the element
     * @param lName the local name of the element 
     * @param qName the qname of the element
     * @param attrs the attributes of the element
     * @throws XBRLException
     */
    public void idFragment(
            String namespaceURI, 
            String lName, 
            String qName,
            Attributes attrs) throws XBRLException {
        ;
    }

    
    
}
