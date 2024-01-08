import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MainWindowC mainWindow = new MainWindowC();
        mainWindow.setVisible(true);
        DatabaseHandler dbhand= new DatabaseHandler();
        Connection conn=dbhand.getDbConnection();
        dbhand.read_data(Const.AIRPORT_TABLE, new String[]{Const.AIRPORTS_COUNTRY});
        String WhereSt=Const.AIRPORTS_COUNTRY+"="+'\''+"Russia"+'\'';
        //System.out.println(WhereSt);
        System.out.println(Arrays.toString(dbhand.read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_CITY,WhereSt)));
        System.out.println(Arrays.toString(dbhand.describe_table(conn,Const.BUYER_Table)));
    }
        }