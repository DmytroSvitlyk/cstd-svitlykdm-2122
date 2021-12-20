javac -cp "../../../lib/*" *.java
java -cp "../../../lib/*" Main.class COM5
IF EXIST "*.class" DEL "*.class" /s
