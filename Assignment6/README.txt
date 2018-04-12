I use the regular expression to filter all the illegal input. So when you input an input with illegal format, it will output an Error: Illegal Input.

To complie the code. open a terminal and input the following command:
javac BasicServer.java
javac BasicClient.java

To run the code, open a terminal and input the 
java BasicServer 5501
java BasicClient 127.0.0.1 5501
127.0.0.1 is the localhost for my laptop, and the 5501 is the port number.

Then the connection has been set up, and you can input the command in the client terminal.

GET:
GET<hello.txt>
GET<rutgers.txt>
GET<test.txt>

BOUNCE:
BOUNCE<Any content>

EXIT:
EXIT
EXIT<code>