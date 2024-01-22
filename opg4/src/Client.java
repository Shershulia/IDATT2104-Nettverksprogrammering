import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    private DatagramSocket datagramSocket;
    private InetAddress inetAddress; //ip address
    private byte[] buffer = new byte[10]; //data

    public Client(DatagramSocket datagramSocket, InetAddress inetAddress) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
    }
    public void fillUpBuffer(byte[] buffer,String message){
        byte[] bytesToSend = message.getBytes();
        int i = 0;
        for(byte byteToSend:bytesToSend){
            buffer[i]=byteToSend;
            i++;
        }
    }

    public void sendAndReceiev() {
        Scanner scanner = new Scanner(System.in);
        while(true){
            try {
                String message_to_send = scanner.nextLine();
                buffer=message_to_send.getBytes(StandardCharsets.UTF_8);
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length,inetAddress,80);
                datagramSocket.send(datagramPacket);
                byte[] recievedBytes = new byte[256];
                DatagramPacket datagramPacket_recieved = new DatagramPacket(recievedBytes,recievedBytes.length);
                datagramSocket.receive(datagramPacket_recieved); // Он получает полностью но не может записать из-за недостаточной длины буфера
                String messageFromServer= new String(datagramPacket_recieved.getData(),0, datagramPacket_recieved.getLength());
                System.out.println("Server said:" + messageFromServer);
            }catch (IOException exception){
                exception.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress inetAddress = InetAddress.getByName("localhost");
        Client client = new Client(datagramSocket,inetAddress);
        System.out.println("Type in expression and we will calculate it");
        client.sendAndReceiev();
    }
}
