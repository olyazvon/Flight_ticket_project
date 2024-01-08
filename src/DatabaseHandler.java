import java.sql.*;
import java.util.Arrays;
import java.util.Objects;


public class DatabaseHandler extends Configs{
    Connection dbConnection;
    public Connection getDbConnection()
            throws ClassNotFoundException,SQLException{
        String connectionString="jdbc:oracle:thin"+":"+dbUser+"/"+
                dbPass+"@"+dbHost+":"+dbPort+":"+dbName;
        Class.forName("oracle.jdbc.OracleDriver");
        dbConnection=DriverManager.getConnection(connectionString,dbUser,dbPass);
        if (dbConnection != null) {
            System.out.println("Connected!");
        }
        return dbConnection;
    }



    // ввыводит на экран, перечисленные поля таблицы,
    // если массив пустой, выводит всю таблицу
    public void read_data(Connection conn, String table_name,String[] column_names) {
    System.out.println("Reading " + Arrays.toString(column_names) +"from "+ table_name);
    String columns="";
    if (column_names.length==0){
        columns="*";
        column_names=describe_table(conn,table_name);
    }
    else {
        columns= column_names[0];
        for (int i = 1; i < column_names.length ; i++) {
            columns = columns + "," + column_names[i];
        }
    }
    String query = String.format("SELECT "+ columns +" FROM %s", table_name);

    try (Statement statement = conn.createStatement();
         ResultSet rs = statement.executeQuery(query)) {

        while (rs.next()) {
            for (int i = 0; i <column_names.length ; i++) {
                System.out.print(rs.getString(column_names[i]) + " ");

            }
            System.out.println();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

////ввыводит в строковый массив уникальные значения столбца
//    public String[] read_distinct_column(Connection conn, String table_name,String column_name) {
//        System.out.println("Reading distinct " + column_name + " from " + table_name);
//        String query = String.format("SELECT DISTINCT " + column_name + " FROM %s", table_name);
//        String st="";
//        int i = 0;
//        try (Statement statement = conn.createStatement();
//             ResultSet rs = statement.executeQuery(query)) {
//
//            while (rs.next()) {
//                st+= rs.getString(column_name)+" ";
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        String[]column= st.split(" ");
//        String[]new_column=new String[column.length+1];
//        for (int j=0;j<column.length;j++){
//            new_column[j+1]=column[j];
//        }
//        new_column[0]="Any "+column_name;
//        return new_column;
//    }

    //ввыводит в строковый массив уникальные значения столбца c условием
    public String[] read_distinct_column(Connection conn, String table_name,
                                         String column_name,String Where) {
        System.out.println("Reading distinct " + column_name + " from " + table_name);
        String where_st="";

        if (!Where.isEmpty())  {
            where_st = String.format(" WHERE " + Where);
        }
        String query = String.format("SELECT DISTINCT " + column_name + " FROM %s", table_name+where_st);

        String st="";
        int i = 0;
        try (Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                st+= rs.getString(column_name)+";";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[]column= st.split(";");
        String[]new_column=new String[column.length+1];
        for (int j=0;j<column.length;j++){
            new_column[j+1]=column[j];
        }
        new_column[0]="Any "+column_name;
        return new_column;
    }









//записывет список столбцов в таблице в массив строк
public String[] describe_table(Connection conn, String table_name) {
    String query = String.format("Select *  from " + table_name);

    try (Statement statement = conn.createStatement();
         ResultSet rs = statement.executeQuery(query)) {
        ResultSetMetaData metaData = rs.getMetaData();
        String[] columns = new String[metaData.getColumnCount()];
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            columns[i] = metaData.getColumnName(i+1);
        }
        return columns;
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return new String[]{};
}

}
