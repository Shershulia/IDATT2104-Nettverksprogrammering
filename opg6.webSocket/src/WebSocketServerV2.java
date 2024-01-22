import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;

public class WebSocketServerV2 {
    private static List<Socket> clients =new ArrayList<>();
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        final int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Server is running on port: " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.err.println("Client connected");
            clients.add(clientSocket);
            Thread thread = new Thread(() -> {
                try {
                    handleClient(clientSocket);
                } catch (IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }

        }
    private static void handleClient(Socket clientSocket) throws IOException, NoSuchAlgorithmException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
        String request = "";
        String line;
        while ((line = reader.readLine()) != null) {
            request += line + "\r\n";
            if (line.isEmpty()) {
                break;
            }
        }
        //System.out.println(request);

        String secretKey = findSecretKey(request);

        String acceptKey = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((secretKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")));

        PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
        writer.println("HTTP/1.1 101 Switching Protocols");
        writer.println("Connection: Upgrade");
        writer.println("Upgrade: websocket");
        writer.println("Sec-WebSocket-Accept: " + acceptKey);
        writer.println();

        InputStream in = clientSocket.getInputStream();

        while (clientSocket.isConnected()) {
            int header = in.read();
            if (header==-1) break;
            //First 4 significant bits text
            if ((header & 0x0F) == 0x1) {
                String s =getMessage(in);
                System.out.println("Message from client: " +s);
                for (Socket socket : clients) {
                    sendMessage(socket, "New message: " + s);
                }
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter message for all clients:");
            String message = scanner.nextLine();
            for (Socket socket : clients) {
                sendMessage(socket, "Message from server: " + message);
            }


        }

        clientSocket.close();
        clients.remove(clientSocket);
    }

    private static String findSecretKey(String request) {
        String findString = "Sec-WebSocket-Key:";
        int from = request.indexOf(findString);
        String a = request.substring(from, request.length() - 1);
        int to = a.indexOf("\n");
        return a.substring(findString.length(), to).trim();
    }
    private static String getMessage(InputStream in) throws IOException {
        int lengthOfMessage =in.read()-128;
        byte[] decoded = new byte[lengthOfMessage];
        byte[] encoded = new byte[lengthOfMessage];
        byte[] key = new byte[4];

        for(int i=0;i<4;i++){
            key[i]= (byte) in.read();
        }
        int j = 0;
        while (in.available()!=0){
            encoded[j]= (byte) in.read();
            j++;
        }
        for (int i = 0; i < encoded.length; i++) {
            decoded[i] = (byte) (encoded[i] ^ key[i & 0x3]);
        }

        // byte[] to string
        return new String(decoded, StandardCharsets.UTF_8);
    }
    private static void sendMessage(Socket clientSocket, String message) throws IOException {
        OutputStream out = clientSocket.getOutputStream();

        // Build WebSocket frame
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] frame = new byte[2 + messageBytes.length];
        frame[0] = (byte) 0x81; // it is a text
        frame[1] = (byte) messageBytes.length; // length
        System.arraycopy(messageBytes, 0, frame, 2, messageBytes.length); // Payload

        // Send frame
        out.write(frame);
        out.flush();
    }
}