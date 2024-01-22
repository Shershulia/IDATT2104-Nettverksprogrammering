import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String datas = "GET / HTTP/1.1\n" +
                "Upgrade: websocket\n" +
                "Connection: Upgrade\n" +
                "Sec-WebSocket-Key: x3JJHMbDL1EzLkh9GBhXDw==\n" +
                "Sec-WebSocket-Version: 13";
        String find_String="Sec-WebSocket-Key: ";
        int from = datas.indexOf(find_String);
        String a = datas.substring(from,datas.length()-1);
        int til = a.indexOf("\n");
        System.out.println(a.substring(find_String.length(),til));

    }
}