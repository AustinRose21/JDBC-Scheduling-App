package AppointmentCalendar.model;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;


public class Logger {


    private static final String FILENAME = "log.txt";


    //empty constructor for logger
    public Logger(){}

    public static void log (String username){
        try {
            FileWriter fw = new FileWriter(FILENAME,true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(ZonedDateTime.now() + " " + username);
            pw.close();
        } catch (IOException e){

            System.out.println("Logger error: " + e.getMessage());
        }
    }
}
