import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MainWindowC mainWindow = new MainWindowC();
        mainWindow.setVisible(true);
        DatabaseHandler dbhand= new DatabaseHandler();
        System.out.println(dbhand.read_seats_for_flight("RU1235"));
        System.out.println(dbhand.prices_for_flight("RU1235"));
        System.out.println(dbhand.occupied("RU1235"));
        System.out.println(dbhand.qFromTo("RU1235"));
        System.out.println(dbhand.q_search_flights(new String[]{"Any iata","LED","SVO"}, new String[]{"Any iata","LED","SVO"},null));
    }
}