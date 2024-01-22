import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Vladimir Lenkov
 * @since 07/02/2023
 */
public class SimpleClient {

    public static void main(String[] args) throws IOException {

        Scanner inn = new Scanner(System.in);
        System.out.println(
                "Input server IP address");
        String serverMachine = inn.nextLine();

        //Connects to server
        Socket connection = new Socket(serverMachine, 7777);
        System.out.println("Connection established.");
        System.out.println("Please use the format 'A operator B");

        //Opens communication with server
        InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(readConnection);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

        //Loops for client input
        String line = inn.nextLine().trim();
        boolean hasExited;
        while (!line.equals("")) {
            System.out.println(reader.readLine());
            writer.println(line);

            /* Reply */
            String reply = reader.readLine();
            System.out.printf(
                    "Reply: %s%n", reply);
            hasExited = (reply.contains(
                    "Closing connection"));
            if (hasExited) {
                break;
            }
            line = inn.nextLine();
        }
    }
}