package org.xbrlapi.loader;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class HistoryImpl implements History {

    /**
     * 
     */
    private static final long serialVersionUID = -4599201126216530103L;
    
    private static final Logger logger = Logger.getLogger(HistoryImpl.class);
    
    /**
     * @see History#addRecord(URI, String)
     */
    public void addRecord(URI uri, String identifier) {
        logger.debug(uri + " has identifier: " + identifier);
    }
    
    /**
     * @see History#getIdentifier(URI)
     */
    public String getIdentifier(URI uri) {
        return null;
    }    

    public Set<URI> getURIs() {
        return new HashSet<URI>();
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();       
    }

    
    
    
}
