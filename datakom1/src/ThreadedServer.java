import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Vladimir Lenkov
 * @since 09/02/2023
 */
public class ThreadedServer extends Thread {
    private final Socket socketConnection;

    public ThreadedServer(Socket socketConnection) {
        this.socketConnection = socketConnection;
    }

    @Override
    public void run() {
        PrintWriter writer = null;
        InputStreamReader readConnection = null;
        try {
            readConnection = new InputStreamReader(socketConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert readConnection != null;
        BufferedReader reader = new BufferedReader(readConnection);
        try {
            writer = new PrintWriter(socketConnection.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            /* Waiting for input */
            String input = null;
            assert writer != null;
            writer.println("Enter your equation");
            try {
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Equation received from :" + socketConnection);
            System.out.println(input);

            //User input check
            assert input != null;
            if (input.matches("[0-9]* [+-] [0-9]*")) {
                String[] substrings = input.split(" ");
                int operand1 = Integer.parseInt(substrings[0]);
                String operation = substrings[1];
                int operand2 = Integer.parseInt(substrings[2]);
                System.out.println("Equation received");
                System.out.println(input);
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

    public static void main(String[] args) {
        Socket socketConnection = null;

        try {
            ServerSocket serverSocket = new ServerSocket(7777);
            System.out.println("Server active. Use " + serverSocket.getInetAddress());

            //Idle until connection is received
            while (true) {
                socketConnection = serverSocket.accept();
                System.out.println("New client connected: " + socketConnection);
                System.out.println("Assigning new thread");
                Thread newClient = new ThreadedServer(socketConnection);
                newClient.start();
            }

        } catch (IOException e) {
            e.printStackTrace();

        }

        try {
            assert socketConnection != null;
            socketConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
