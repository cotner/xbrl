package org.xbrlapi.utilities;

//import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

/**
 *
 * <p>Title: GrammarCache </p>
 *
 * <p>Description: Used on ContentHandlerImpl to keep a static copy of the grammar pool
 * used for XML Schema content models.  This is needed to enable construction of the
 * XML Schema content models for schemas that include other schemas, such as the schemas
 * used by XBRL GL.</p>
 * Contributed on 13 February, 2007.
 * @author Howard Ungar.
 * @version 1.0
 */
public class GrammarCacheImpl {
	
  /**
   * Uses the XBRL grammar pool to ensure that pool interactions can be logged
   * and analysed.
   */
  private static XBRLAPIGrammarPoolImpl xmlGrammarPoolImpl = new XBRLAPIGrammarPoolImpl();

  public static XMLGrammarPool getGrammarPool()
  {
    return xmlGrammarPoolImpl;
  }
  
  public static void emptyGrammarPool() {
	  xmlGrammarPoolImpl = new XBRLAPIGrammarPoolImpl();
  }

}
