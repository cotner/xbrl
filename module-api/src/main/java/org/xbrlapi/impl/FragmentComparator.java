package org.xbrlapi.impl;

import java.util.Comparator;

import org.xbrlapi.Fragment;
import org.xbrlapi.utilities.XBRLException;

/**
 * Facilitates sorting of fragments so that 
 * they can be organised into 
 * complete XML document in the right order.
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class FragmentComparator implements Comparator<Fragment> {

	/**
	 * Compare two fragment objects and order such that fragments in different documents
	 * are grouped separately and so that the fragments are ordered from the closest to the end
	 * of the document in document order to the closest to the beginning in document order.
	 * Fragments are sorted:
	 * 1. in increasing alphanumeric ordering of the URI of the document that the fragment
	 * belongs to; then
	 * 2. in increasing order of the index of their parent fragment with root fragments being ordered first of all; then
	 * 3. in increasing order of the XPath to the containing element in the parent fragment; then
	 * 4. in increasing order of the number of preceding siblings to the fragment's root element.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Fragment o1, Fragment o2) throws ClassCastException {
		try {
			Fragment f1 = o1;
			Fragment f2 = o2;

			// Handle direct equality
			if (f1.equals(f2)) {
				return 0;
			}
			
			int uriComparison = f1.getURI().compareTo(f2.getURI());
			if (uriComparison != 0)
				return uriComparison;
			
			String f1p = f1.getParentIndex();
			String f2p = f2.getParentIndex();
						
			// Handle the case where just one fragment is the document root
			if ((f1p == null) & (f2p != null))
				return -1;
			if ((f2p == null) & (f1p != null))
				return 1;
			
			// Compare parent fragment indices (neither fragment is a document root)
			// Order with the highest parent index being ranked first
			int parentIndexComparison = f1p.compareTo(f2p);
			if (parentIndexComparison != 0)
				return parentIndexComparison; // Negative if f1 is ordered first

			// Compare sequence to containing elements
			int sequenceComparison = f1.getSequenceToParentElementAsString().compareTo(f2.getSequenceToParentElementAsString());
			if (sequenceComparison != 0)
				return sequenceComparison; // Negative if f1 is ordered first
			
			// Compare preceding siblings - the final way of distinguishing fragment order
			int precedingSiblingComparison = f1.getPrecedingSiblings().compareTo(f2.getPrecedingSiblings());
			return precedingSiblingComparison;

		} catch (XBRLException e) {
			throw new ClassCastException("XBRL metadata for fragments is not available to facilitate a comparison. " + e.getMessage());
		}
	}

}
