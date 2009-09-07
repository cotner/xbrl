package org.xbrlapi;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface ReferencePart extends Fragment {

    /**
     * Get the value of the reference part. 
     * @return The value of the reference part with spaces 
     * trimmed from the front and end.
     * @throws XBRLException
     */
    public String getValue() throws XBRLException;

    /**
     * Currently implemented by a brute force search of the 
     * reference part declarations with the given local name.
     * @return an XML Schema declaration of the reference part.  If more than
     * one such declaration is in the data store then the declaration returned
     * is application dependent.
     * @throws XBRLException if no declarations can be found in the data store.
     */
    public ReferencePartDeclaration getDeclaration() throws XBRLException; 
    
}
