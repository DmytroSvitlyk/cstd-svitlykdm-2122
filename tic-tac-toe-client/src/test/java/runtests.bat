javac *.java
java PackageTest.class
java ServerConnectorTest.class COM5
java GameTest.class COM5
IF EXIST "*.class" DEL "*.class" /s