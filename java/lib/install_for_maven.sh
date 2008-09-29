# Execute this script to install the relevant JAR files in the local Maven repository.
 
mvn install:install-file -DgroupId=org.apache -DartifactId=xindice -Dversion=1.1b4 -Dpackaging=jar -Dfile=xindice-1.1b4.jar -DgeneratePom=true

mvn install:install-file -DgroupId=xmldb -DartifactId=xmldb-api -Dversion=custom -Dpackaging=jar -Dfile=xmldb.jar -DgeneratePom=true

mvn install:install-file -DgroupId=xmlrpc -DartifactId=xmlrpc -Dversion=1.2-patched -Dpackaging=jar -Dfile=xmlrpc-1.2-patched.jar -DgeneratePom=true

mvn install:install-file -DgroupId=exist -DartifactId=exist -Dversion=1.1.1 -Dpackaging=jar -Dfile=exist-1.1.1.jar -DgeneratePom=true

mvn install:install-file -DgroupId=com.oracle.berkeley -DartifactId=db -Dversion=2.4.13 -Dpackaging=jar -Dfile=db.jar -DgeneratePom=true

mvn install:install-file -DgroupId=com.oracle.berkeley -DartifactId=dbxml -Dversion=2.4.13 -Dpackaging=jar -Dfile=dbxml.jar -DgeneratePom=true
