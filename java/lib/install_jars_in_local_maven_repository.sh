# Execute this script to install the relevant JAR files in the local Maven repository.

mvn install:install-file -DgroupId=exist -DartifactId=exist -Dversion=1.2.6 -Dpackaging=jar -Dfile=exist-1.2.6.jar -DgeneratePom=true
mvn install:install-file -DgroupId=exist -DartifactId=xmldb-api -Dversion=custom -Dpackaging=jar -Dfile=xmldb.jar -DgeneratePom=true

mvn install:install-file -DgroupId=org.apache.xmlrpc -DartifactId=xmlrpc-common -Dversion=3.1.1 -Dpackaging=jar -Dfile=xmlrpc-common-3.1.1.jar -DgeneratePom=true
mvn install:install-file -DgroupId=org.apache.xmlrpc -DartifactId=xmlrpc-client -Dversion=3.1.1 -Dpackaging=jar -Dfile=xmlrpc-client-3.1.1.jar -DgeneratePom=true

mvn install:install-file -DgroupId=com.oracle.berkeley -DartifactId=db -Dversion=2.5.16 -Dpackaging=jar -Dfile=db.jar -DgeneratePom=true
mvn install:install-file -DgroupId=com.oracle.berkeley -DartifactId=dbxml -Dversion=2.5.16 -Dpackaging=jar -Dfile=dbxml.jar -DgeneratePom=true

mvn install:install-file -DgroupId=net.sf.saxon -DartifactId=saxon -Dversion=9.1 -Dpackaging=jar -Dfile=saxon9.jar -DgeneratePom=true
mvn install:install-file -DgroupId=net.sf.saxon -DartifactId=saxon-dom -Dversion=9.1 -Dpackaging=jar -Dfile=saxon9-dom.jar -DgeneratePom=true
mvn install:install-file -DgroupId=net.sf.saxon -DartifactId=saxon-s9api -Dversion=9.1 -Dpackaging=jar -Dfile=saxon9-s9api.jar -DgeneratePom=true

mvn install:install-file -DgroupId=com.google.code.google-collections -DartifactId=google-collect -Dversion=1.0 -Dpackaging=jar -Dfile=google-collect-1.0.jar -DgeneratePom=true

