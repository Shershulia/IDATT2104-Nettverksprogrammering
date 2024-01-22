import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;

class SocketTjener {
    private static final String FILENAME = "data.txt";

    public static int PORTNR = 1225;

    public static void main(String[] args) throws IOException {
    clearFile();
    while(true){

        ServerSocket tjener = new ServerSocket(PORTNR);
        System.out.println("Logg for tjenersiden. Nå venter vi...");
        writeToFile(String.valueOf(PORTNR));
        PORTNR++;
        Socket forbindelse = tjener.accept();  // venter inntil noen tar kontakt
        WebThread thread = new WebThread(forbindelse);
        thread.start();

    }
  }
    public static void writeToFile(String data) {
        try {
            FileWriter writer = new FileWriter(FILENAME, true);
            writer.write(data + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void clearFile() {
        try {
            FileWriter writer = new FileWriter(FILENAME);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
