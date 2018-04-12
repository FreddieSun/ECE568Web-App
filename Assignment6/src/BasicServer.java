import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class BasicServer {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) { // Test for correct num. of arguments
            System.err.println( "ERROR server port number not given");
            System.exit(1);
        }
        int port_num = Integer.parseInt(args[0]);
        // ServerSocket rv_sock = null;
        ServerSocket rv_sock = new ServerSocket(port_num);
        try {
            new ServerSocket(port_num);
        } catch (IOException ex) { ex.printStackTrace(); }

        while (true) { // run forever, waiting for clients to connect
            System.out.println("\nWaiting for client to connect...");
            try {
                Socket s_sock = rv_sock.accept();


                while (true) {

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(s_sock.getInputStream())
                    );
                    PrintWriter out = new PrintWriter(
                            new OutputStreamWriter(s_sock.getOutputStream()),
                            true);
                    String input = in.readLine();

                    if (input == null || "".equals(input)) {
                        System.out.println("Please input the command");
                        out.println("Please input the command");
                    } else {
                        if ("EXIT".equals(input)) {
                            System.out.println("Normal Exit: The connection has ended");
                            out.println("Normal Exit: The connection has ended");
                            break;
                        } else {
                            if (input.length() <= 5) {
                                System.out.println("Error: " + "No Such Command");
                                out.println("Error: " + "No Such Command");
                            } else if (input.length() > 3 && input.substring(0,3).equals("GET")) {
                                // 如果是GET方法，读取content并在server端打印content内容
                                String fileName = input.substring(4, input.length() - 1);
                                File file =
                                        new File(fileName);
                                if (file.exists()) {
                                    Scanner sc = new Scanner(file);
                                    out.println("read the : " + fileName);
                                    while (sc.hasNextLine())
                                        System.out.println(sc.nextLine());
                                } else {
                                    System.out.println("Error: File doesn't exist");
                                    out.println("Error: File doesn't exist");
                                }

                            } else if (input.substring(0,6).equals("BOUNCE")) {
                                // Client input BOUNCE <Hello World>  Server output: Hello World
                                System.out.println(
                                        "Client's message: " + input.substring(7,input.length() - 1));
                                out.println("echo: " + input.substring(7,input.length() - 1));
                            } else if (input.substring(0,4).equals("EXIT")) {
                                System.out.println("The connection has ended with code: " + input.substring(5, input.length() - 1));
                                out.println("The connection has ended with code: " + input.substring(5, input.length() - 1));
                            } else {
                                System.out.println("Error: " + "No Such Command!");
                                out.println("No Such Command!");
                            }
                        }

                    }
                }
                s_sock.close();

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException ex) { ex.printStackTrace(); }

        }
    }
}
