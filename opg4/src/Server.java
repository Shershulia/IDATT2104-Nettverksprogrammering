import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
    private DatagramSocket datagramSocket;
    private byte[] buffer = new byte[256]; //buffer to store the messages from client

    public Server(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public void recieveThenSend() {
        while (true){
            try {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, 256); //implement connectionless forbindelse
                datagramSocket.receive(datagramPacket); //halt until receive set recieve in datagrampacket
                System.out.println("Packet got established");
                InetAddress inetAddress = datagramPacket.getAddress(); //get header information ip
                int port = datagramPacket.getPort();
                String messageFromClient = new String(datagramPacket.getData(), 0, datagramPacket.getLength()); //transform data to string
                String result = calculate(messageFromClient);
                System.out.println("Message from client :" + messageFromClient + " Result " + result);
                byte[] bytesToSend = result.getBytes();
                datagramPacket = new DatagramPacket(bytesToSend, bytesToSend.length, inetAddress, port);
                datagramSocket.send(datagramPacket);
            }
            catch (IOException exception){
                exception.printStackTrace();
                break;
            }
        }
    }
    public static String calculate(String expression){
        String[] variables;
        int result;
        if (expression.contains("+")){
            variables = expression.split("[+]");
            try {
                result= Integer.parseInt(variables[0]) + Integer.parseInt(variables[1]);
            }catch (NumberFormatException ex){
                return "Wrong format. Error:" +ex.getMessage();
            }
            return expression +" = "+result;
        }else if (expression.contains("-")){
            variables = expression.split("-");
            try {
                result= Integer.parseInt(variables[0]) - Integer.parseInt(variables[1]);
            }catch (NumberFormatException ex){
                return "Wrong format. Error:" +ex.getMessage();
            }
            return expression +" = "+result;
        }else return "Wrong format";
    }
    public static void main(String[] args) throws IOException {
        // The datagramSocket is sending or recieving data using connectionless datagram protocol such as udp over ip
        DatagramSocket datagramSocket = new DatagramSocket(80);
        Server server = new Server(datagramSocket);
        server.recieveThenSend();
    }

}
