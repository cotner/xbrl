package org.xbrlapi.networks;

import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.Relationship;
import org.xbrlapi.data.Store;
import org.xbrlapi.utilities.XBRLException;

/**
 * This Analyser returns information based upon all
 * relationships that are not expressed by an arc
 * with a use that is prohibited.
 * Set this as the analyser used by a store when you want that
 * store to produce query results where this set of relationships
 * is relevant.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class NonProhibitedAnalyserImpl extends AnalyserImpl implements Analyser {

    /**
     * 
     */
    private static final long serialVersionUID = -4136715748794742559L;
    
    protected final static Logger logger = Logger.getLogger(NonProhibitedAnalyserImpl.class);
    
    /**
     * @param store The data store to use.
     * @throws XBRLException if the store is null.
     */
    public NonProhibitedAnalyserImpl(Store store) throws XBRLException {
        super(store);
    }    
    
    /**
     * Filters out relationships with use=prohibited on the arcs expressing them.
     * This requires the query to be a simple XPath expression that selects document
     * roots for the persisted relationships in the data store.  It works by appending
     * an extra predicate onto the XPath selection criteria that selections only
     * those relationships without a notUse attribute.  Note that this attribute is omitted
     * on persisted relationships unless the associated arc has a use=prohibited attribute value.
     * @param query The query to run to get the relationships.
     * @return the list of relationships returned by the query.
     * @throws XBRLException
     */
    protected List<Relationship> getRelationships(String query) throws XBRLException {
        List<Relationship> relationships = getStore().<Relationship>queryForXMLResources(query);
        query = query + "[not(@arcUse)]";
        Networks networks = new NetworksImpl(getStore());
        networks.addRelationships(relationships);
        return networks.getActiveRelationships();
    }
    
}
