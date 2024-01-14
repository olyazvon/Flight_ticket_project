import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Arrays;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MainWindowC mainWindow = new MainWindowC();
        mainWindow.setVisible(true);
        DatabaseHandler dbhand= new DatabaseHandler();


//        System.out.println(dbhand.read_seats_for_flight("RU1235"));
//        System.out.println(dbhand.prices_for_flight("RU1235"));
        System.out.println(dbhand.occupied("RU1235"));
        System.out.println(dbhand.isFree("RU1235","2A"));
        System.out.println(dbhand.seats_left("Business"));
       // System.out.println(dbhand.maxBookedNumber());
        Seat[] SeatsToBook= new Seat[2];
        SeatsToBook[0]=new Seat("1A",12,false,"RU1235",null);
        SeatsToBook[1]=new Seat("2A",12,false,"RU1235",null);
        System.out.println(dbhand.Book(SeatsToBook));
//        System.out.println(dbhand.qFromTo("RU1235"));
//        System.out.println(dbhand.q_search_flights(new String[]{"Any iata","LED","SVO"}, new String[]{"Any iata","LED","SVO"},null));
//        System.out.println(dbhand.read_typeClass_for_flight("RU1235"));
//
    }
}