cd
cd /d "%~dp0"
cd

SET JDK=C:\Program Files\Java\jdk-15.0.1

SET JDKAPAR=-XX:+ShowCodeDetailsInExceptionMessages
SET JDKAPAR=%JDKAPAR% -agentlib:jdwp=transport=dt_socket,suspend=y,address=localhost:57447 
SET JDKAPAR=%JDKAPAR% "-javaagent:C:\winapp\java64\eclipse\configuration\org.eclipse.osgi\405\0\.cp\lib\javaagent-shaded.jar"
SET JDKAPAR=%JDKAPAR% -Dfile.encoding=UTF-8

SET CLSPATH=C:\java\photon\plotge\target\classes
SET CLSPATH=%CLSPATH%;C:\java\maven\repository\org\projectlombok\lombok\1.18.16\lombok-1.18.16.jar
SET CLSPATH=%CLSPATH%;C:\java\maven\repository\org\apache\logging\log4j\log4j-core\2.8.2\log4j-core-2.8.2.jar
SET CLSPATH=%CLSPATH%;C:\java\maven\repository\org\apache\logging\log4j\log4j-api\2.8.2\log4j-api-2.8.2.jar
SET CLSPATH=%CLSPATH%;C:\java\maven\repository\com\google\code\gson\gson\2.8.6\gson-2.8.6.jar
SET CLSPATH=%CLSPATH%;C:\java\maven\repository\com\fasterxml\jackson\core\jackson-databind\2.12.0\jackson-databind-2.12.0.jar
SET CLSPATH=%CLSPATH%;C:\java\maven\repository\com\fasterxml\jackson\core\jackson-annotations\2.12.0\jackson-annotations-2.12.0.jar
SET CLSPATH=%CLSPATH%;C:\java\maven\repository\com\fasterxml\jackson\core\jackson-core\2.12.0\jackson-core-2.12.0.jar

"%JDK%\bin\javaw.exe" %JDKPAR% -classpath "%CLSPATH%" sm.clagenna.plotge.swing.SwingApp

pause