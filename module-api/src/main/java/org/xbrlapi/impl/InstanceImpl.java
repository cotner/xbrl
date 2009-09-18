package org.xbrlapi.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.xbrlapi.Concept;
import org.xbrlapi.Context;
import org.xbrlapi.EntityResource;
import org.xbrlapi.ExtendedLink;
import org.xbrlapi.Fact;
import org.xbrlapi.Instance;
import org.xbrlapi.Item;
import org.xbrlapi.SimpleLink;
import org.xbrlapi.Tuple;
import org.xbrlapi.Unit;
import org.xbrlapi.utilities.Constants;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class InstanceImpl extends FragmentImpl implements Instance {

    /**
     * @see org.xbrlapi.Instance#getSchemaRefs()
     */
    public List<SimpleLink> getSchemaRefs() throws XBRLException {
    	List<SimpleLink> candidates = this.<SimpleLink>getChildren("org.xbrlapi.impl.SimpleLinkImpl");
    	int i = 0;
    	while (i<candidates.size()) {
    		SimpleLink c = candidates.get(i);
    		if (! c.getLocalname().equals("schemaRef")) candidates.remove(c); else  i++;
    	}
    	return candidates;
    }
    
    /**
     * @see org.xbrlapi.Instance#getLinkbaseRefs()
     */
    public List<SimpleLink> getLinkbaseRefs() throws XBRLException {
    	List<SimpleLink> candidates = this.<SimpleLink>getChildren("org.xbrlapi.impl.SimpleLinkImpl");
    	int i = 0;
    	while (i<candidates.size()) {
    		SimpleLink c = candidates.get(i);
    		if (! c.getLocalname().equals("linkbaseRef")) candidates.remove(c); else  i++;
    	}
    	return candidates;
    }

    /**
     * @see org.xbrlapi.Instance#getContexts()
     */
    public List<Context> getContexts() throws XBRLException {
    	return this.<Context>getChildren(ContextImpl.class.getName());
    }

    /**
     * @see org.xbrlapi.Instance#getContext(String)
     */
    public Context getContext(String id) throws XBRLException {
    	String xpath = "#roots#[@type='org.xbrlapi.impl.ContextImpl' and @parentIndex='" + getIndex() + "' and */*/@id='" + id + "']";
    	List<Context> list = getStore().<Context>queryForXMLResources(xpath);
    	if (list.size() == 0) throw new XBRLException("The instance does not contain a context with id: " + id);
    	if (list.size() > 1) throw new XBRLException("The instance contains more than one context with id: " + id);
    	return (list.get(0));
    }
    
    /**
     * @see org.xbrlapi.Instance#getUnits()
     */
    public List<Unit> getUnits() throws XBRLException {
    	return this.<Unit>getChildren("org.xbrlapi.impl.UnitImpl");
    }    

    /**
     * @see org.xbrlapi.Instance#getUnit(String)
     */
    public Unit getUnit(String id) throws XBRLException {
    	List<Unit> list = getStore().queryForXMLResources("#roots#[@type='org.xbrlapi.impl.UnitImpl' and @parentIndex='" + this.getIndex() + "' and "+ Constants.XBRLAPIPrefix+ ":" + "data/*/@id='" + id + "']");
    	if (list.size() == 0) throw new XBRLException("The instance does not contain a unit with id: " + id);
    	if (list.size() > 1) throw new XBRLException("The instance contains more than one unit with id: " + id);
    	return list.get(0);
    }
    
    /**
     * @see org.xbrlapi.Instance#getFootnoteLinks()
     */
    public List<ExtendedLink> getFootnoteLinks() throws XBRLException {
    	return this.<ExtendedLink>getChildren("org.xbrlapi.impl.ExtendedLinkImpl");
    }

    /**
     * @return the XQuery used to get all the child facts of the XBRL instance.
     */
    private String getFactsQuery() {
        return "#roots#[@parentIndex='" + this.getIndex() + "' and (@type='org.xbrlapi.impl.SimpleNumericItemImpl' or @type='org.xbrlapi.impl.FractionItemImpl' or @type='org.xbrlapi.impl.NonNumericItemImpl' or @type='org.xbrlapi.impl.TupleImpl')]";    
    }
    
    /**
     * @see org.xbrlapi.Instance#getFacts()
     */
    public List<Fact> getFacts() throws XBRLException {
    	return getStore().<Fact>queryForXMLResources(getFactsQuery());
    }
    
    /**
     * @see org.xbrlapi.Instance#getItems()
     */
    public List<Item> getItems() throws XBRLException {
        return getStore().<Item>queryForXMLResources("#roots#[@parentIndex='" + this.getIndex() + "' and (@type='org.xbrlapi.impl.SimpleNumericItemImpl' or @type='org.xbrlapi.impl.FractionItemImpl' or @type='org.xbrlapi.impl.NonNumericItemImpl')]");
    }

    /**
     * @see org.xbrlapi.Instance#getTuples()
     */
    public List<Tuple> getTuples() throws XBRLException {
        return this.<Tuple>getChildren("Tuple");
    }

    /**
     * @see org.xbrlapi.Instance#getEarliestPeriod()
     */
    public String getEarliestPeriod() throws XBRLException {

        String query = "for $root in #roots#[@uri='" + this.getURI() + "' and @type='" + PeriodImpl.class.getName() + "']/*/*/xbrli:instant return string($root)";
        Set<String> values = getStore().queryForStrings(query);
        query = "for $root in #roots#[@uri='" + this.getURI() + "' and @type='" + PeriodImpl.class.getName() + "']/*/*/xbrli:startDate return string($root)";
        values.addAll(getStore().queryForStrings(query));
        String result = null;
        for (String candidate: values) {
            if (result == null) result = candidate;
            else if (result.compareTo(candidate) > 0) result = candidate;
        }
        return result;        
    }

    /**
     * @see org.xbrlapi.Instance#getFactCount()
     */
    public long getFactCount() throws XBRLException {
        return getStore().queryCount(getFactsQuery());
    }

    /**
     * @see org.xbrlapi.Instance#getLatestPeriod()
     */
    public String getLatestPeriod() throws XBRLException {
        
        String query = "for $root in #roots#[@uri='" + this.getURI() + "' and @type='" + PeriodImpl.class.getName() + "']/*/*/xbrli:instant return string($root)";
        Set<String> values = getStore().queryForStrings(query);
        query = "for $root in #roots#[@uri='" + this.getURI() + "' and @type='" + PeriodImpl.class.getName() + "']/*/*/xbrli:endDate return string($root)";
        values.addAll(getStore().queryForStrings(query));
        String result = null;
        for (String candidate: values) {
            if (result == null) result = candidate;
            else if (result.compareTo(candidate) < 0) result = candidate;
        }
        return result;
        
    }

    /**
     * @see org.xbrlapi.Instance#getEntityResources()
     */
    public List<EntityResource> getEntityResources() throws XBRLException {
        String query = "for $root in #roots#[@uri='" + this.getURI() + "' and @type='" + EntityImpl.class.getName() + "'] let $identifier := $root/xbrlapi:data/xbrli:entity/xbrli:identifier return concat($identifier/@scheme,'|||',$identifier)";
        Set<String> uniqueValues = new HashSet<String>();
        Set<String> values = getStore().queryForStrings(query);
        for (String value: values) {
            String[] qname = value.split("\\|\\|\\|");
            String scheme = qname[0].trim();
            String identifier = qname[1].trim();
            uniqueValues.add(scheme + "|||" + identifier);            
        }
        List<EntityResource> result = new Vector<EntityResource>();
        for (String value: uniqueValues) {
            String[] qname = value.split("\\|\\|\\|");
            String scheme = qname[0];
            String identifier = qname[1];
            query = "#roots#[@type='"+EntityResourceImpl.class.getName()+"' and */*/@scheme='"+ scheme +"' and */*/@value='" + identifier + "']";
            result.addAll(getStore().<EntityResource>queryForXMLResources(query));
        }
        return result;
    }
    
    /**
     * @see org.xbrlapi.Instance#getEntityIdentifiers()
     */
    public Map<String, Set<String>> getEntityIdentifiers() throws XBRLException {
        String query = "for $root in #roots#[@uri='" + this.getURI() + "' and @type='" + EntityImpl.class.getName() + "'] let $identifier := $root/xbrlapi:data/xbrli:entity/xbrli:identifier return concat($identifier/@scheme,'|||',$identifier)";
        Set<String> uniqueValues = new HashSet<String>();
        Set<String> values = getStore().queryForStrings(query);
        for (String value: values) {
            String[] qname = value.split("\\|\\|\\|");
            String scheme = qname[0].trim();
            String identifier = qname[1].trim();
            uniqueValues.add(scheme + "|||" + identifier);            
        }
        Map<String,Set<String>> result = new HashMap<String,Set<String>>();
        for (String value: uniqueValues) {
            String[] qname = value.split("\\|\\|\\|");
            String scheme = qname[0];
            String identifier = qname[1];
            if (result.containsKey(scheme))
                result.get(scheme).add(identifier);
            else {
                Set<String> identifiers = new HashSet<String>();
                identifiers.add(identifier);
                result.put(scheme,identifiers);
            }
        }
        return result;

    }

    /**
     * @see org.xbrlapi.Instance#getChildConcepts()
     */
    public List<Concept> getChildConcepts() throws XBRLException {
        String query = "for $root in #roots#[@parentIndex='"+getIndex()+"'] let $data:=$root/xbrlapi:data/* where namespace-uri($data)!='"+Constants.XBRL21Namespace+"' and namespace-uri($data)!='"+Constants.XBRL21LinkNamespace+"' return concat(namespace-uri($data),'|||',local-name($data))";
        Set<String> qnames = getStore().queryForStrings(query);
        List<Concept> concepts = new Vector<Concept>();
        for (String value: qnames) {
            String[] qname = value.split("\\|\\|\\|");
            String namespace = qname[0];
            String localname = qname[1];
            try {
                concepts.add(getStore().getConcept(new URI(namespace),localname));
            } catch (URISyntaxException e) {
                throw new XBRLException("Invalid URI syntax in a fact's namespace.",e);
            }
        }
        
        return concepts;
    }

    /**
     * @see org.xbrlapi.Instance#getChildConceptCount()
     */
    public int getChildConceptCount() throws XBRLException {
        String query = "for $root in #roots#[@parentIndex='"+getIndex()+"'] let $data:=$root/xbrlapi:data/* where namespace-uri($data)!='"+Constants.XBRL21Namespace+"' and namespace-uri($data)!='"+Constants.XBRL21LinkNamespace+"' return concat(namespace-uri($data),'|||',local-name($data))";
        Set<String> qnames = getStore().queryForStrings(query);
        return qnames.size();
    }
    
    
    
}
