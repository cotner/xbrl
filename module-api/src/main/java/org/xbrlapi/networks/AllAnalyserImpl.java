package org.xbrlapi.networks;

import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.Relationship;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

/**
 * This Analyser returns information based upon all
 * relationships (whether they are active or inactive or prohibited).
 * Set this as the analyser used by a store when you want that
 * store to produce query results where this set of relationships
 * is relevant.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public final class AllAnalyserImpl extends AnalyserImpl implements Analyser {

    protected final static Logger logger = Logger.getLogger(AllAnalyserImpl.class);

    /**
     * @param store The data store to use.
     * @throws XBRLException if the store is null.
     */
    public AllAnalyserImpl(Store store) throws XBRLException {
        super(store);
    }
    
    /**
     * This method does not filter out any relationships.
     * @param query The query to run to get the relationships.
     * @return the list of relationships returned by the query.
     * @throws XBRLException
     */
    protected List<Relationship> getRelationships(String query) throws XBRLException {
        return getStore().<Relationship>queryForXMLResources(query);
    }
}
