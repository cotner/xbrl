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
	 * 
	 * Fragments are sorted:
	 * <ol>
	 *  <li>in INCREASING alphanumeric ordering of the URI of the document that the fragment belongs to; then</li>
     *  <li>in INCREASING order of the index of their parent fragment with root fragments being ordered first of all; then</li>
     *  <li>in DECREASING order of the XPath to the containing element in the parent fragment; then</li>
     *  <li>in INCREASING order of the fragment index which implies being in increasing order of the number of preceding sibling fragments.</li>
	 * </ol>
	 * 
	 * This sorting rule implies that a set of child fragments will be sorted into the order
	 * necessary for insertion into a parent fragment without the insertion of child fragments
	 * causing the later fragment insertions to be done in the wrong place.  This is because
	 * the fragment insertions will be done from the end of the parent fragment, in document order
	 * to the beginning of the parent fragment, again in document order.
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Fragment f1, Fragment f2) throws ClassCastException {
		try {

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
				return -sequenceComparison;
			
			// Compare the fragment index as a last resort to discriminate between sibling fragments
			return f1.getIndex().compareTo(f2.getIndex());

		} catch (XBRLException e) {
			throw new ClassCastException("XBRL metadata for fragments is not available to facilitate a comparison. " + e.getMessage());
		}
	}

}
