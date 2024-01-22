package part2;

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

            writeHTML_asRespons("load_page.html",skriveren);
            skriveren.println(outputString);


            System.err.println("Client connection closed!");
            in.close();
            skriveren.close();
            clientSocket.close();

        }
    }
    static void writeHTML_asRespons(String source,PrintWriter skriveren) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(source));
            String str;
            while ((str=in.readLine() )!=null){
                skriveren.println(str);
            }
            skriveren.flush(); //make sure that all data buffered
            in.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}