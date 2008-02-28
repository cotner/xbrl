package org.xbrlapi;

public interface EntryPoint {
	
	/**
	 * The method to call to use the specified entry point.
	 * @param args The array of command line arguments
	 */
	public void run(String[] args);

}
