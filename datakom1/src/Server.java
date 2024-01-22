import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author : Vladimir Lenkov
 * @since : 12/02/2023
 */
public class Server {
    public static final int PORTNR = 7777;

    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(PORTNR);
        byte[] bytes = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
        System.out.println("Server activated. Use port " + PORTNR + " to communicate.");

        //Endlessly listen for client input through port
        while (true) {
            //Using input check from TCP Calculator
            InetAddress ip = datagramPacket.getAddress();
            datagramSocket.receive(datagramPacket);
            String input = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
            if (input.matches("[0-9]* [+-] [0-9]*")) {

                String[] substrings = input.split(" ");
                int operand1 = Integer.parseInt(substrings[0]);
                String operation = substrings[1];
                int operand2 = Integer.parseInt(substrings[2]);
                System.out.println("Input received: " + input + " - Sending the result back to the client.");
                {
                    if (operation.equals("+")) {
                        int result = operand1 + operand2;
                        byte[] b2 = String.valueOf(result).getBytes();
                        DatagramPacket outputPacket = new DatagramPacket(b2, b2.length, datagramPacket.getAddress(), datagramPacket.getPort());
                        datagramSocket.send(outputPacket);
                    } else if (operation.equals("-")) {
                        int result = operand1 - operand2;
                        byte[] b2 = String.valueOf(result).getBytes();
                        DatagramPacket outputPacket = new DatagramPacket(b2, b2.length, datagramPacket.getAddress(), datagramPacket.getPort());
                        datagramSocket.send(outputPacket);
                    } else {
                        String result = "An error occurred during calculation";
                        byte[] b2 = result.getBytes();
                        DatagramPacket outputPacket = new DatagramPacket(b2, b2.length, datagramPacket.getAddress(), datagramPacket.getPort());
                        datagramSocket.send(outputPacket);
                    }
                }
            } else {
                String result = "An error occurred during calculation";
                byte[] b2 = result.getBytes();
                DatagramPacket outputPacket = new DatagramPacket(b2, b2.length, InetAddress.getLocalHost(), datagramPacket.getPort());
                datagramSocket.send(outputPacket);
            }
        }
    }
}
