package org.xbrlapi.sax.identifiers;

import org.xbrlapi.Fragment;
import org.xbrlapi.impl.ReferencePartImpl;
import org.xbrlapi.sax.ContentHandler;
import org.xbrlapi.utilities.XBRLException;
import org.xml.sax.Attributes;

/**
 * Identifies reference part fragments.
 * 
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ReferencePartIdentifier extends BaseIdentifier implements Identifier {

    /**
     * @see org.xbrlapi.sax.identifiers.BaseIdentifier#BaseIdentifier(ContentHandler)
     */
    public ReferencePartIdentifier(ContentHandler contentHandler) throws XBRLException {
        super(contentHandler);
    }

    /**
     * @see org.xbrlapi.sax.identifiers.BaseIdentifier#startElement(String,String,String,Attributes)
     */
    public void startElement(
            String namespaceURI, 
            String lName, 
            String qName,
            Attributes attrs) throws XBRLException {

        if (! getLoader().isBuildingAFragment()) {
            return;
        }
        
        // Handle reference part fragments inside reference resources
        Fragment fragment = getLoader().getFragment();
        if (fragment.getType().equals("org.xbrlapi.impl.ReferenceResourceImpl")) {
            Fragment referencePartFragment = new ReferencePartImpl();
            processFragment(referencePartFragment,attrs);
        }
        
    }
    
}
