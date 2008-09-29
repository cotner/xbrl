
                  XBRLAPI  RENDERING SAMPLE APPLICATION

  Author: Steve Yang (steve2yang@yahoo.com)

  What is it?
  -----------

  This is a collection of Java sample applications to demonstrate the usage of 
  XBRLAPI and its various modules. The Rendering Sample Application loads instance 
  document using XBRLAPI and renders the document according to XBRL Inline 
  Specification 0.64. 

  The the purpose of this project is to give developers step by step instructions
  to discover a taxonomy set and use Berkeley DB XML API to store the instance 
  documents and traverse through the various relationship networks to build rendering 
  solutions. This sample application also demonstrates how to build XBRL Inline 
  documents using XBRL API.


  Where is it?
  ------------

  The home page for the XBRL API project can be found on the XBRLAPI 
  project web site (http://www.xbrlapi.org/). There you also find 
  information on how to download the latest release as well as all the other 
  information you might need regarding this project.


  Requirements
  ------------

   o  A Java 1.5 or later compatible virtual machine for your operating system.
   o  Xerces 2.6.2 or later version jar file.
   o  Xalan 2.7.0 or later version jar file.
   o  Log4j 1.2.15 or later version jar file.
   o  XBRLAPI module-utilities 3.2 or later version jar file.
   o  XBRLAPI module-xmlbase 3.2 or later version jar file.
   o  XBRLAPI module-xlink 3.2 or later version jar file.
   o  XBRLAPI module-xpointer 3.2 or later version jar file.
   o  XBRLAPI module-api 3.2 or later version jar file.
   o  XBRLAPI module-bdb-xml 3.2 or later version jar file.  
 
  Commandline Arguments:
  -------------------------------------------
  Java Virtual Machine Parameters:
  
  -Dlog4j.configuration=<location of log4j.properties>
  -Djava.library.path=<Berkeley DB XML 2.3.10/lib>
  -classpath=<java class paths>
  
  Program Arguments: org.xbrlapi.render.Run -database <...> -container <...> -cache <...> -stylesheet <...> [XBRL instance file]
  -database <XML DB location>
  -container <default DB container name>
  -cache <XML DB cache location>
  -stylesheet <stylesheet location>
  
  Look for the most updated documentation on the XBRLAPI.ORG web site under
  the SourceForge Project (http://sourceforge.net/projects/xbrlapi/).


  Licensing and legal issues
  --------------------------

  XBRLAPI Rendering Sample project is released under the lesser GPL license.

  Thanks for using XBRLAPI.
