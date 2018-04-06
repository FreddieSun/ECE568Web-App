import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class BasicClient {
    public static void main(String[] args) {
        if (args.length != 2) { // Test for correct num. of arguments
            System.err.println(
                    "ERROR server host name AND port number not given");
            System.exit(1);
        }
        int port_num = Integer.parseInt(args[1]);

        try {
            Socket c_sock = new Socket(args[0], port_num);

            boolean flag = true;
            while (flag) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(c_sock.getInputStream())
                );
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(c_sock.getOutputStream()),
                        true);
                BufferedReader userEntry = new BufferedReader(
                        new InputStreamReader(System.in)
                );
                System.out.print("User, enter your message: ");
                String input = userEntry.readLine();

                out.println(input);
                // 如果是EXIT，两端都退出程序
                if ("EXIT".equals(input)) {
                    flag = false;
                } else {

                    // 收到服务器端的响应并打印出来
                    System.out.println("Server says: " + in.readLine());
                }
            }
            if (c_sock != null) {
                c_sock.close();
            }
        } catch (IOException ex) { ex.printStackTrace(); }
        System.exit(0);
    }
}
