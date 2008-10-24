package org.xbrlapi.examples.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.xbrlapi.utilities.XBRLException;

/**
 * This class simplifies handling of CSS stylesheets.
 * @author Steve Yang (steve2yang@yahoo.com)
 */
public class DocumentStylesheet {

    private StringBuffer contents = new StringBuffer();

	public String getStylesheet()
	{
		return contents.toString();
	}

	public void loadStylesheet(String file) throws XBRLException
	{
		File styleFile = new File(file);
		if(!styleFile.exists()) {
			throw new XBRLException("No stylesheet at the specified location.");
		}

        try {
    		BufferedReader reader = new BufferedReader(new FileReader(styleFile));
    		String line = null;
    		while( (line = reader.readLine()) != null) {
    			contents.append(line);
    		}
        } catch (FileNotFoundException e) {
            throw new XBRLException("Stylesheet file could not be found.", e);
		} catch (IOException e) {
		    throw new XBRLException("I/O problems occurred in reading the stylesheet.", e);
		}
	}
}
