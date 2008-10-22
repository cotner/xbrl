package org.xbrlapi.examples.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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

	public void loadStylesheet(String file) throws Exception
	{
		File styleFile = new File(file);
		if(!styleFile.exists())
		{
			throw new Exception("No stylesheet specified");
		}

		BufferedReader reader = new BufferedReader(new FileReader(styleFile));
		String line = null;
		while( (line = reader.readLine()) != null)
		{
			contents.append(line);
		}
	}
}
