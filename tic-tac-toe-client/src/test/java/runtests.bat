javac *.java
java PackageTest.class
java ServerConnectorTest.class COM5
java GameTest.class COM5
java GameFileIOTest.class
java DBMigrationTest.class
IF EXIST "*.class" DEL "*.class" /s