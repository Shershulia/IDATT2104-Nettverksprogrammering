import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Vladimir Lenkov
 * @since 09/02/2023
 */
public class WebServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(7777);
        System.out.println("Server active. Use " + serverSocket.getInetAddress().getHostName());

        //Idle until connection is received
        Socket connection = serverSocket.accept();
        System.out.println("A Client has connected");

        InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(readConnection);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

        StringBuilder html = new StringBuilder();
        html.append("<doctype !html><html><head><title>Hello, world!</title></head><body><h1>Hey, \n" + " welcome to my Java server!</h1> The client header is: ");
        html.append("<ul>");
        String received = reader.readLine();
        while (!received.equals("")) {
            System.out.printf("A client wrote: %s\r\n", received);
            html.append(String.format("<li>%s</li>", received));
            received = reader.readLine();
        }
        html.append("</ul></body></html>");
        writer.print("HTTP/1.0 200 OK \r\n");
        writer.print("Content-Type: text/html; charset=UTF-8\r\n");
        writer.print("\r\n");
        writer.println(html);
        System.out.println("Finished. Closing connection.");
        writer.flush();
        reader.close();
        connection.close();
    }
}
