import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DatabaseHandler.makeDbConnection();
        MainWindowC mainWindow = new MainWindowC();
        mainWindow.setVisible(true);
    }

    public static  boolean  noNumbersInString(String st){
        for (int i = 0; i <= st.length()-1; i++) {
            if (Character.isDigit(st.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static  boolean  onlyNumbersInString(String st){
        for (int i = 0; i <= st.length()-1; i++) {
            if (!(Character.isDigit(st.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

}