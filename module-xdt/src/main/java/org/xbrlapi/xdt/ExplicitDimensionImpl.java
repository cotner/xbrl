package org.xbrlapi.xdt;

import java.net.URI;
import java.util.List;

import org.xbrlapi.Concept;
import org.xbrlapi.FragmentList;
import org.xbrlapi.impl.FragmentListImpl;
import org.xbrlapi.networks.Network;
import org.xbrlapi.networks.Networks;
import org.xbrlapi.networks.Relationship;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class ExplicitDimensionImpl extends DimensionImpl implements ExplicitDimension {

    /**
     * @see org.xbrlapi.xdt.ExplicitDimension#getDefaultDomainMember()
     */
    public Concept getDefaultDomainMember() throws XBRLException {
        Networks networks = this.getNetworksWithArcrole(XDTConstants.defaultDimensionArcrole());
        FragmentList<Concept> defaults = new FragmentListImpl<Concept>();
        List<URI> linkroles = networks.getLinkRoles(XDTConstants.defaultDimensionArcrole());
        for (URI linkrole: linkroles) {
            Network network = networks.getNetwork(linkrole,XDTConstants.defaultDimensionArcrole());
            List<Relationship> relationships = network.getActiveRelationshipsFrom(this.getIndex());
            for (Relationship relationship: relationships) {
                try {
                    defaults.add((Concept) relationship.getTarget());
                } catch (XBRLException e) {
                    ;// Target is not a valid domain member so ignore it.
                }
            }
        }
        
        if (defaults.getLength() == 0) throw new XBRLException("There are no defaults for explicit dimension " + this.getTargetNamespace() + ":" + this.getName());
        
        if (defaults.getLength() > 1) throw new XBRLException("There are multiple defaults for explicit dimension " + this.getTargetNamespace() + ":" + this.getName());
        
        return defaults.get(0);
        
    }

    /**
     * @see org.xbrlapi.xdt.ExplicitDimension#hasDefaultDomainMember()
     */
    public boolean hasDefaultDomainMember() throws XBRLException {

            Networks networks = this.getNetworksWithArcrole(XDTConstants.defaultDimensionArcrole());
            FragmentList<Concept> defaults = new FragmentListImpl<Concept>();
            List<URI> linkroles = networks.getLinkRoles(XDTConstants.defaultDimensionArcrole());
            for (URI linkrole: linkroles) {
                Network network = networks.getNetwork(linkrole,XDTConstants.defaultDimensionArcrole());
                List<Relationship> relationships = network.getActiveRelationshipsFrom(this.getIndex());
                for (Relationship relationship: relationships) {
                    try {
                        defaults.add((Concept) relationship.getTarget());
                    } catch (XBRLException e) {
                        ;// Target is not a valid domain member so ignore it.
                    }
                }
            }
            
            if (defaults.getLength() == 0) return false;
            
            if (defaults.getLength() > 1) return false;
            
            return true;
            
        }
    
}