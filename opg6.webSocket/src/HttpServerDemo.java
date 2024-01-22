import java.net.*;
import java.io.*;

public class HttpServerDemo{
    public static void main(String[] args) throws IOException {
        int port = 8081;

        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Server is running on port: "+port);
        String outputString ="<ul>\r\n";
        while(true){
            Socket clientSocket = serverSocket.accept();
            System.err.println("Client connected");
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String s;
            while((s = in.readLine())!=null){
                outputString+="<li>"+ s +"</li>\r\n";
                System.out.println(s);
                if(s.isEmpty()){
                    break;
                }
            }
            outputString +="<ul>";
            PrintWriter skriveren = new PrintWriter(clientSocket.getOutputStream(), true);
            skriveren.println("HTTP/1.1 200 OK\r\n");

            skriveren.println(outputString);


            System.err.println("Client connection closed!");
            in.close();
            skriveren.close();
            clientSocket.close();

        }
    }
}