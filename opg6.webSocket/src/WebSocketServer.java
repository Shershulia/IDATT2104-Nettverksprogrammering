import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSocketServer {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        final int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Server is running on port: "+port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.err.println("Client connected");

            // Input-output streams for web server
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();
            Scanner s = new Scanner(in, "UTF-8");
            String request = "";
            String str;
            while((str = s.nextLine())!=null){
                request+=str +"\r\n";

                if(str.isEmpty()){
                    break;
                }
            }

            System.out.println(request);
            String secretKey = findSecretKey(request);
            byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                    + "Connection: Upgrade\r\n"
                    + "Upgrade: websocket\r\n"
                    + "Sec-WebSocket-Accept: "
                    + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((secretKey + "258EAFA5-E914-47DA-95CA-").getBytes("UTF-8")))
                    + "\r\n\r\n").getBytes("UTF-8");

            out.write(response, 0, response.length);
            String res = new String(response, StandardCharsets.UTF_8);

            System.out.println(res);

            // Close the connection
            clientSocket.close();
        }
    }

    private static String findSecretKey(String datas){
        String find_String="Sec-WebSocket-Key:";
        int from = datas.indexOf(find_String);
        String a = datas.substring(from,datas.length()-1);
        int til = a.indexOf("\n");
        return a.substring(find_String.length(),til);
    }
}
