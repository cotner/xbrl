package org.xbrlapi.sax.identifiers;

import org.xbrlapi.Fragment;
import org.xbrlapi.impl.FragmentImpl;
import org.xbrlapi.sax.ContentHandler;
import org.xbrlapi.utilities.XBRLException;
import org.xml.sax.Attributes;

/**
 * Identifies XML Schema fragments.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class GenericDocumentRootIdentifier extends BaseIdentifier implements Identifier {

    /**
     * @see org.xbrlapi.sax.identifiers.BaseIdentifier#BaseIdentifier(ContentHandler)
     */
    public GenericDocumentRootIdentifier(ContentHandler contentHandler) throws XBRLException {
        super(contentHandler);
    }

    /**
     * Add a generic fragment for a document root element if we 
     * have not already done so.
     * @see org.xbrlapi.sax.identifiers.BaseIdentifier#startElement(String,String,String,Attributes)
     */
    public void startElement(
            String namespaceURI, 
            String lName, 
            String qName,
            Attributes attrs) throws XBRLException {

        //if (! getLoader().get().isNewFragment()) {
            if (! getElementState().hasParent()) {
                Fragment root = new FragmentImpl();
                processFragment(root,attrs);
            }
        //}
            
    }
    
}
