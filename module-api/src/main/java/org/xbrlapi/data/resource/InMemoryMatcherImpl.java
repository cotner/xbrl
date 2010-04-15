package org.xbrlapi.data.resource;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.xbrlapi.cache.Cache;
import org.xbrlapi.utilities.XBRLException;

/**
 * The in-memory resource matcher implementation, for use with the
 * DOM data store implementation.  Note that this matcher is not persistent.
 * Sub-class if you want to use an alternative signature.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class InMemoryMatcherImpl extends BaseMatcherImpl implements Matcher {

    /**
     * 
     */
    private static final long serialVersionUID = 1224199711979462774L;
    /**
     * Map from signature strings to lists of URIs with the same signature.
     * The first URI in the list is the URI of the resource in the data
     * store.
     */
    private Map<String,List<URI>> map = new HashMap<String,List<URI>>();
    private Map<String,List<URI>>getMap() {
        return map;
    }

    /**
     * @param cache The resource cache to be used by the matcher when accessing
     * resources to determine their signature.
     * @throws XBRLException if the cache parameter is null.
     */
    public InMemoryMatcherImpl(Cache cache) throws XBRLException {
        super(cache,new MD5SignerImpl());
    }

    /**
     * @see org.xbrlapi.data.resource.Matcher#getMatch(URI)
     */
    public synchronized URI getMatch(URI uri) throws XBRLException {
        String signature = this.getSignature(uri);
        if (signature == null) throw new XBRLException("The signature could not be generated.");
        if (getMap().containsKey(signature)) {
            List<URI> matches = getMap().get(signature);
            if (! matches.contains(uri)) matches.add(uri);
        } else {
            List<URI> list = new Vector<URI>();
            list.add(uri);
            getMap().put(signature,list);
        }
        return getMap().get(signature).get(0);
    }
    
    /**
     * @see org.xbrlapi.data.resource.Matcher#getAllMatchingURIs(java.net.URI)
     */
    public List<URI> getAllMatchingURIs(URI uri) throws XBRLException {

        List<URI> result = new Vector<URI>();

        String signature = this.getSignature(uri);

        if (getMap().containsKey(signature)) {
            List<URI> matches = getMap().get(signature);
            return matches;
        }

        result.add(uri);
        return result;

    }    
    
    /**
     * @see Matcher#delete(URI)
     */
    public URI delete(URI uri) throws XBRLException {
        if (uri == null) throw new XBRLException("The URI must not be null.");
        String signature = this.getSignature(uri);
        
        List<URI> uris = map.get(signature);
        
        for (URI item: uris) {
            if (item.equals(uri)) uris.remove(item);
        }

        if (uris.isEmpty()) {
            map.remove(signature);
            return null;
        }
        return uris.get(0);
    }

    /**
     * @see org.xbrlapi.data.resource.Matcher#hasURI(java.net.URI)
     */
    public boolean hasURI(URI uri) throws XBRLException {
        return getMap().containsKey(getSignature(uri));
    }    

    /**
     * Handles object serialization
     * @param out The input object stream used to store the serialization of the object.
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(map);
/*        out.writeInt(map.size());
        for (String key: map.keySet()) {
            out.writeObject(key);
            List<URI> uris = map.get(key);
            out.writeInt(uris.size());
            for (URI uri: uris) {
                out.writeObject(uri);
            }            
        }
*/   }
    
    /**
     * Handles object inflation.
     * @param in The input object stream used to access the object's serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject( );
        map = (Map<String,List<URI>>) in.readObject();
/*        
        int size = in.readInt();
        for (int i=0; i<size; i++) {
            String key = (String) in.readObject();
            int listSize = in.readInt();
            List<URI> uris = new Vector<URI>();
            for (int j=0; j<listSize; j++) {
                uris.add((URI) in.readObject());
            }
            map.put(key,uris);
        }*/    
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((map == null) ? 0 : map.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        InMemoryMatcherImpl other = (InMemoryMatcherImpl) obj;
        if (map == null) {
            if (other.map != null)
                return false;
        } else if (!map.equals(other.map))
            return false;
        return true;
    }
    
}
