import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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

                boolean flag = true;
                while (flag) {
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
                            System.out.println("The connection has ended");
                            out.println("The connection has ended");
                        } else {
                            if (input.length() <= 5) {
                                System.out.println("Error: " + "No Such Command");
                            } else if (input.length() > 3 && input.substring(0,3).equals("GET")) {
                                String fileName = input.substring(4, input.length() - 1);
                                File file =
                                        new File(fileName);
                                Scanner sc = new Scanner(file);
                                out.println("read the : " + fileName);
                                while (sc.hasNextLine())
                                    System.out.println(sc.nextLine());
                            } else if (input.substring(0,6).equals("BOUNCE")) {
                                System.out.println(
                                        "Client's message: " + input);
                                out.println("echo: " + input);
                            } else if (input.substring(0,4).equals("EXIT")) {
                                System.out.println("Exit Code" + input.substring(5, input.length() - 1));
                            } else {
                                // todo
                                System.out.println("Error: " + "No Such Command!");
                                out.println("No Such Command!");
                            }
                        }

                    }
                }
                s_sock.close();
                break;
            } catch (IOException ex) { ex.printStackTrace(); }
        }
    }
}
