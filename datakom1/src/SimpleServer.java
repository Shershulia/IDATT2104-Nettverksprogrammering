import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Vladimir Lenkov
 * @since 07/02/2023
 */
public class SimpleServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7777);
        //System.out.println("Server active. Use " + serverSocket.getInetAddress().getHostAddress());
        //Set to idle until client connects
        Socket connection = serverSocket.accept();
        InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(readConnection);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

        //Waiting for input here
        while (true) {
            String input = null;
            assert writer != null;
            writer.println("Enter your equation");
            try {
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Equation received");
            System.out.println(input);
            assert input != null;
            if (input.matches("[0-9]* [+-] [0-9]*")) {
                String[] substrings = input.split(" ");
                int operand1 = Integer.parseInt(substrings[0]);
                String operation = substrings[1];
                int operand2 = Integer.parseInt(substrings[2]);
                System.out.println("Received input : " + input);
                System.out.println("Responding with result");
                {
                    if (operation.equals("+")) {
                        writer.println(operand1 + operand2);
                    } else if (operation.equals("-")) {
                        writer.println(operand1 - operand2);
                    } else {
                        writer.println("An error occurred, please try again");
                    }
                }
            } else {
                writer.println("An error occurred, please try again");
            }
        }
    }
}

