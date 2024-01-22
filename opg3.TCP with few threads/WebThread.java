import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WebThread extends Thread{
    private final Socket forbindelse;
    public WebThread(Socket socket){
        this.forbindelse=socket;
    }
    @Override
    public void run() {
        try {
            /* �pner str�mmer for kommunikasjon med klientprogrammet */
            InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
            BufferedReader leseren = new BufferedReader(leseforbindelse);
            PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

            /* Sender innledning til klienten */
            skriveren.println("Hei, du har kontakt med tjenersiden!");
            skriveren.println("Skriv uttrykk og vi skal beregne den");

            /* Mottar data fra klienten */
            String enLinje = leseren.readLine();  // mottar en linje med tekst
            while (enLinje != null) {  // forbindelsen p� klientsiden er lukket
                System.out.println("En klient skrev: " + enLinje);
                enLinje = calculate(enLinje);
                skriveren.println(enLinje);  // sender svar til klienten
                enLinje = leseren.readLine();
            }

            /* Lukker forbindelsen */
            leseren.close();
            skriveren.close();
            forbindelse.close();
        } catch (IOException e) {
        throw new RuntimeException(e);
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
}
