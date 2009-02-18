package org.xbrlapi;

import java.net.URI;

import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public interface SchemaContent extends Fragment {

    /**
     * Gets the schema fragment that is the schema containing this element declaration.
     * @return the schema fragment that is the schema containing this element declaration.
     * @throws XBRLException if the schema content is not inside a schema.
     */
    public Schema getSchema() throws XBRLException;
    
    /**
     * Get the target namespace of the schema that contains this fragment.
     * @return the target namespace of the schema that contains this fragment
     * or null if no targetNamespace attribute is defined for the containing
     * schema fragment.
     * @throws XBRLException
     */
    public URI getTargetNamespace() throws XBRLException;
    
    

}
