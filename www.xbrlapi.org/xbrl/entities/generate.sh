# You need saxon9 to be available on the classpath to run this script.
java net.sf.saxon.Transform -s:http://www.sec.gov/Archives/edgar/xbrlrss.xml -xsl:getEntityIdentifiers.xsl -o:entities.xml
