import java.io.*;
import java.net.*;
import java.util.Scanner;

class SocketKlient{
  private static final String FILENAME = "data.txt";

  public static void main(String[] args) throws IOException {

    /* Bruker en scanner til � lese fra kommandovinduet */
    Scanner leserFraKommandovindu = new Scanner(System.in);



    /* Setter opp forbindelsen til tjenerprogrammet */
    Socket forbindelse = new Socket("localhost", 8080);
    System.out.println("Nå er forbindelsen opprettet.");


    /* �pner en forbindelse for kommunikasjon med tjenerprogrammet */
    InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
    BufferedReader leseren = new BufferedReader(leseforbindelse);
    PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);


    /* Leser tekst fra kommandovinduet (brukeren) */
    String enLinje = leserFraKommandovindu.nextLine();
    while (!enLinje.equals("")) {
      skriveren.println(enLinje);  // sender teksten til tjeneren
      String respons = leseren.readLine();  // mottar respons fra tjeneren
      System.out.println("Fra tjenerprogrammet: " + respons);
      enLinje = leserFraKommandovindu.nextLine();
    }

    /* Lukker forbindelsen */
    leseren.close();
    skriveren.close();
    forbindelse.close();
  }

}
