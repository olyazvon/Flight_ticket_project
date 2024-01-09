import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MainWindowC mainWindow = new MainWindowC();
        mainWindow.setVisible(true);
        DatabaseHandler dbhand= new DatabaseHandler();
        //Connection conn=dbhand.getDbConnection();
        dbhand.search_all_tickets();
        //System.out.println(Arrays.toString(dbhand.Cities("", "Tel Aviv")));
//        System.out.println(Arrays.toString(dbhand.IATAs("Russia", "Moscow","")));
//        System.out.println(Arrays.toString(dbhand.read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_ID,"")));
    }
}