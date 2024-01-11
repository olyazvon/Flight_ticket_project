import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MainWindowC mainWindow = new MainWindowC();
        mainWindow.setVisible(true);
        DatabaseHandler dbhand= new DatabaseHandler();
        System.out.println(dbhand.read_seats_for_flight("HY1235"));
        System.out.println(dbhand.prices_for_flight("HY1234"));
        System.out.println(dbhand.occupied("HY1235"));
          }
}