package org.xbrlapi.xdt;

import java.util.List;
import java.util.Vector;

import org.xbrlapi.Relationship;
import org.xbrlapi.impl.RelationshipImpl;
import org.xbrlapi.utilities.XBRLException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class HypercubeImpl extends XDTConceptImpl implements Hypercube {

    /**
     * 
     */
    private static final long serialVersionUID = -3395259164875316193L;

    /**
     * @see org.xbrlapi.xdt.Hypercube#getDimensions()
     */
    public List<Dimension> getDimensions() throws XBRLException {
        List<Dimension> result = new Vector<Dimension>();
        String query = "for $root in #roots#[@type='"+RelationshipImpl.class.getName() + "'] where $root/@sourceIndex='"+this.getIndex()+"' and $root/@arcRole='"+XDTConstants.HypercubeDimensionArcrole+"' return $root";
        List<Relationship> relationships = getStore().<Relationship>queryForXMLResources(query);
        ListMultimap<String, Relationship> map = ArrayListMultimap.<String, Relationship> create();
        for (Relationship relationship: relationships) {
            String key = relationship.getTargetIndex() + " " + relationship.getLinkRole();
            map.put(key,relationship);
        }
        for (String key: map.keySet()) {
            List<Relationship> equivalents = map.get(key);
            Relationship relationship = equivalents.get(0);
            for (Relationship candidate: equivalents) {
                if (candidate.getArcPriority() > relationship.getArcPriority())
                    relationship = candidate;
            }
            if (! relationship.isProhibiting()) result.add((Dimension) relationship.getTarget());
        }
        return result;
    }    
}