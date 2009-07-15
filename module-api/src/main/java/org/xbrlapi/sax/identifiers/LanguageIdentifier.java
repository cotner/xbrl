package org.xbrlapi.sax.identifiers;

import org.xbrlapi.Fragment;
import org.xbrlapi.impl.LanguageImpl;
import org.xbrlapi.sax.ContentHandler;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;
import org.xml.sax.Attributes;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class LanguageIdentifier extends BaseIdentifier implements Identifier {

    /**
     * @see org.xbrlapi.sax.identifiers.BaseIdentifier#BaseIdentifier(ContentHandler)
     */
    public LanguageIdentifier(ContentHandler contentHandler) throws XBRLException {
        super(contentHandler);
    }

    /**
     * Find fragments defining and labelling XML language codes.
     * 
     * @see org.xbrlapi.sax.identifiers.BaseIdentifier#startElement(String,String,String,Attributes)
     */
    public void startElement(
            String namespaceURI, 
            String lName, 
            String qName,
            Attributes attrs) throws XBRLException {

        Fragment languageFragment = null;
        if (namespaceURI.equals(Constants.XBRLAPILanguagesNamespace.toString())) {
            if (lName.equals("language")) {
                languageFragment = new LanguageImpl();
            }
        }

        if (languageFragment != null) {
            this.processFragment(languageFragment,attrs);
        }
        
    }
    
}
