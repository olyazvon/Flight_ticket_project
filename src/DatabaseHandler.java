import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Arrays;
import java.util.Objects;


public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:oracle:thin" + ":" + dbUser + "/" +
                dbPass + "@" + dbHost + ":" + dbPort + ":" + dbName;
        Class.forName("oracle.jdbc.OracleDriver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
//       if (dbConnection != null) {
//            System.out.println("Connected!");
//        }
        return dbConnection;
    }


    // ввыводит на экран, перечисленные поля таблицы,
    // если массив пустой, выводит всю таблицу
    public void read_data(String table_name, String[] column_names) throws SQLException, ClassNotFoundException {
        System.out.println("Reading " + Arrays.toString(column_names) + "from " + table_name);
        String columns = "";
        if (column_names.length == 0) {
            columns = "*";
            column_names = describe_table(getDbConnection(), table_name);
        } else {
            columns = column_names[0];
            for (int i = 1; i < column_names.length; i++) {
                columns = columns + "," + column_names[i];
            }
        }
        String query = String.format("SELECT " + columns + " FROM %s", table_name);

        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                for (int i = 0; i < column_names.length; i++) {
                    System.out.print(rs.getString(column_names[i]) + " ");

                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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
    public String[] read_distinct_column(String table_name,
                                         String column_name, String Where) {
        String where_st = "";

        if (!Where.isEmpty()) {
            where_st = String.format(" WHERE " + Where);
        }
        String query = String.format("SELECT DISTINCT " + column_name + " FROM %s", table_name + where_st);
        System.out.println(query);
        String st = "";
        int i = 0;
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                st += rs.getString(column_name) + ";";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] column = st.split(";");
        String[] new_column = new String[column.length + 1];
        for (int j = 0; j < column.length; j++) {
            new_column[j + 1] = column[j];
        }
        new_column[0] = "Any " + column_name;
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
                columns[i] = metaData.getColumnName(i + 1);
            }
            return columns;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new String[]{};
    }


    //String[] Allcountries() - Массив стран без повторов
    public String[] Allcountries() throws ClassNotFoundException {
        String[] Countries = read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_COUNTRY, "");
        return Countries;
    }

    //Массив без повторов городов для выбраной страны. Если страна не выбрана
    //или Any country, то все города
    String[] Cities(String SelectedCountry, String ExcludedCity) {
        String stExludedCity = "";
        String stSelectedCountry = "";
        if (SelectedCountry.equals("Any country")) {
            stSelectedCountry = "";
        }

        if (!SelectedCountry.isEmpty() && (!SelectedCountry.equals("Any country"))) {
            stSelectedCountry = Const.AIRPORTS_COUNTRY + "= \'" + SelectedCountry + "\'";
        }
        if (!SelectedCountry.isEmpty() && (!SelectedCountry.equals("Any country"))) {
            stSelectedCountry = Const.AIRPORTS_COUNTRY + "= \'" + SelectedCountry + "\'";
        }
        if (!ExcludedCity.isEmpty()) {
            stExludedCity = " AND " + Const.AIRPORTS_CITY + " Not like \'" + ExcludedCity + "\'";
        }
        if (SelectedCountry.isEmpty() && (!ExcludedCity.isEmpty())) {
            stExludedCity = Const.AIRPORTS_CITY + " Not like \'" + ExcludedCity + "\'";
        }
        if (SelectedCountry.isEmpty() && (ExcludedCity.isEmpty())) {
            stExludedCity = "";

        }
        String[] Cities = read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_CITY,
                stSelectedCountry + stExludedCity);
        return Cities;
    }

    String[] IATAs(String selectedCountry, String selectedCity, String ExcludedIATA) {
        String stExcludedIATA="";
        String stCountry="";
        String stCity="";
        if (selectedCity.equals("Any city")){
            stCity="";
        }
        if (selectedCountry.equals("Any country")) {
            stCountry = "";
        }
        if (!selectedCountry.isEmpty() && !selectedCountry.equals("Any country")) {
            stCountry = Const.AIRPORTS_COUNTRY + "= \'" + selectedCountry + "\'";
        }
        if (selectedCountry.isEmpty() && (!selectedCity.isEmpty() && !selectedCity.equals("Any city"))) {
            stCity = Const.AIRPORTS_CITY + "= \'" + selectedCity + "\'";
        }
        if (!selectedCity.isEmpty() && !selectedCity.equals("Any city")) {
            stCity = " AND " + Const.AIRPORTS_CITY + "= \'" + selectedCity + "\'";
        }

        if (!ExcludedIATA.isEmpty()) {
            stExcludedIATA = " AND " + Const.AIRPORTS_ID + " Not like \'" + ExcludedIATA + "\'";
        }
        if (selectedCity.isEmpty() && selectedCountry.isEmpty() && (!ExcludedIATA.isEmpty())) {
            stExcludedIATA = Const.AIRPORTS_ID + " Not like \'" + ExcludedIATA + "\'";
        }


        String[] IATAs = read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_ID,
                (stCountry + stCity + stExcludedIATA));
        return IATAs;
    }


    public void search_tickets(String iata_from, String iata_to, Data data_from) {
        String query_all = String.format("SELECT fl.*,s_economy.seats_left_economy,s_business.seats_left_business " +
                " FROM " + Const.FLIGHT_TABLE + " fl" + ",(" + seats_left("Business") + ") s_business,(" +
                seats_left("Economy") + ") s_economy" +
                " WHERE fl." + Const.FLIGHTS_ID + "=s_economy.flight_id and fl." + Const.FLIGHTS_ID + "=s_business.flight_id  " +
                "and (seats_left_business>0 OR seats_left_economy>0)");
        String from="";
        if (iata_from.equals("Any iata")){from="";}
        if(!iata_from.isEmpty()&&!(iata_from.equals("Any iata"))){
            from=" AND "+ Const.FLIGHTS_FROM+"= '"+iata_from+"'";
        }
        String to="";
        if (iata_to.equals("Any iata")){to="";}
        if(!iata_to.isEmpty()&&!(iata_from.equals("Any iata"))){
            to=" AND "+ Const.FLIGHTS_TO+"= '"+iata_to+"'";
        }
        String stDataFrom="";
        if(data_from != null){
            stDataFrom=" AND "+ Const.FLIGHTS_DEPARTURE+"= '"+stDataFrom+"'";
        }

        String query=query_all+from+to+stDataFrom;
        System.out.println(query);
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                System.out.print(rs.getString(1)+ " ");
                System.out.print(rs.getString(2) + " ");
                System.out.print(rs.getString(3) + " ");
                System.out.print(rs.getString(4) + " ");
                System.out.print(rs.getString(5) + " ");
                System.out.print(rs.getString(6) + " ");
                System.out.print(rs.getString(7) + " ");
                System.out.print(rs.getString(8) + " ");
                System.out.println(rs.getString(9) + " ");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    //возращает запрос свободных сидений по классу
     public String seats_left(String TicketClass) {
        String query = String.format("SELECT " + Const.SEATS_FLIGHT_ID + ", " +
                "(count(" + Const.SEAT + ") - count(" + Const.SEATS_BOUGHT + ")-count(" + Const.SEATS_BOOKED + ")) AS seats_left_" + TicketClass +
                " FROM " + Const.SEAT_TABLE + " WHERE " + Const.SEATS_class + "= '" + TicketClass + "'" + " GROUP BY " + Const.SEATS_FLIGHT_ID);
        System.out.println(query);
        return(query);
    }

}