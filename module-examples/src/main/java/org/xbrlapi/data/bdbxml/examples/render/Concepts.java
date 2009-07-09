package org.xbrlapi.data.bdbxml.examples.render;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import org.xbrlapi.Concept;

/**
 * Convenience class to help with the rendering process.
 * @author Geoff Shuetrim (geoff@galexy.net)
 */
public class Concepts {
    
    public List<Concept> concepts = new Vector<Concept>();
    public List<String> labels = new Vector<String>();
    public List<URI> labelRoles = new Vector<URI>();
    
    public int size() {
        return concepts.size();
    }
    
    public void addAll(Concepts newConcepts) {
        concepts.addAll(newConcepts.concepts);
        labels.addAll(newConcepts.labels);
        labelRoles.addAll(newConcepts.labelRoles);
    }
    
    public void add(Concept concept,String label, URI labelRole) {
        concepts.add(concept);
        labels.add(label);
        labelRoles.add(labelRole);
    }        
    
}
