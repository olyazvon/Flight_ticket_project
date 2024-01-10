import com.privatejgoodies.common.base.Objects;

import javax.xml.crypto.Data;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;


public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection() {
        //{
        String connectionString = "jdbc:oracle:thin" + ":" + dbUser + "/" +
                dbPass + "@" + dbHost + ":" + dbPort + ":" + dbName;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        } catch (SQLException | ClassNotFoundException  e) {
            throw new RuntimeException(e);
        }
        return dbConnection;
    }


//    // ввыводит на экран, перечисленные поля таблицы,
//    // если массив пустой, выводит всю таблицу
//    public void read_data(String table_name, String[] column_names) {
//        System.out.println("Reading " + Arrays.toString(column_names) + "from " + table_name);
//        String columns = "";
//        if (column_names.length == 0) {
//            columns = "*";
//            column_names = describe_table(getDbConnection(), table_name);
//        } else {
//            columns = column_names[0];
//            for (int i = 1; i < column_names.length; i++) {
//                columns = columns + "," + column_names[i];
//            }
//        }
//        String query = String.format("SELECT " + columns + " FROM %s", table_name);
//
//        Statement statement = null;
//        try {
//            statement = getDbConnection().createStatement();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        ResultSet rs = null;
//        try {
//            rs = statement.executeQuery(query);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        while (true) {
//            try {
//                if (!rs.next()) break;
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//            for (int i = 0; i < column_names.length; i++) {
//                try {
//                    System.out.print(rs.getString(column_names[i]) + " ");
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//            System.out.println();
//        }
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
    public String[] Allcountries() {
        String[] Countries = read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_COUNTRY, "");
        return Countries;
    }

    //Массив без повторов городов для выбраной страны. Если страна не выбрана
    //или Any country, то все города
    String[] Cities(String selectedCountry, String excludedCity) {
        //if (excludedCity == null) return new String[] {};
        String whereString = "";

        if (!selectedCountry.isEmpty() && !selectedCountry.equals("Any country")) {
            whereString += Const.AIRPORTS_COUNTRY + "= \'" + selectedCountry + "\'";
        }
        if (!excludedCity.isEmpty() && !excludedCity.equals("Any city")) {
            if (whereString != "") {
                whereString += " AND ";
            }
            whereString += Const.AIRPORTS_CITY + " Not like \'" + excludedCity + "\'";
        }

        String[] Cities = read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_CITY,
                whereString);
        return Cities;
    }

    String[] IATAs(String selectedCountry, String selectedCity, String excludedCity) {
        String whereString = "";

        if (!selectedCountry.isEmpty() && !selectedCountry.equals("Any country")) {
            whereString += Const.AIRPORTS_COUNTRY + "= \'" + selectedCountry + "\'";
        }
        if (!selectedCity.isEmpty() && !selectedCity.equals("Any city")) {
            if (whereString != "") {
                whereString += " AND ";
            }
            whereString += Const.AIRPORTS_CITY + "= \'" + selectedCity + "\'";
        }
        if (!excludedCity.isEmpty() && !excludedCity.equals("Any iata")) {
            if (whereString != "") {
                whereString += " AND ";
            }
            whereString += Const.AIRPORTS_CITY + " Not like \'" + excludedCity + "\'";
        }

        String[] IATAs = read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_ID,
                whereString);
        return IATAs;
    }

    String cityByIATA(String iata) {
        return read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_CITY,
                (Const.AIRPORTS_ID + "=\'" + iata + "\'"))[1];
    }



    public String q_search_flights (String iata_from, String iata_to, LocalDate data_from) {
        String query = String.format(
                "SELECT fl.flight_id, airport_from, airport_to, to_char(departure, 'dd.mm.yy hh:mi'), to_char(arrival, 'dd.mm.yy hh:mi'), priceeconom, pricebusiness,s_economy.seats_left_economy,s_business.seats_left_business " +
                " FROM " + Const.FLIGHT_TABLE + " fl" + ",(" +
                seats_left("Business") + ") s_business,(" +
                seats_left("Economy") + ") s_economy" +
                " WHERE fl." + Const.FLIGHTS_ID + "=s_economy.flight_id and fl." +
                Const.FLIGHTS_ID + "=s_business.flight_id  " +
                "and (seats_left_business>0 OR seats_left_economy>0)");

        if (!iata_from.equals("Any iata") && !iata_from.isEmpty()) {
            query += " AND " + Const.FLIGHTS_FROM + "= '" + iata_from + "'";
        }

        if (!iata_to.equals("Any iata") && !iata_to.isEmpty()) {
            query += " AND " + Const.FLIGHTS_TO + "= '" + iata_to + "'";
        }

        if (data_from != null) {
            query += " AND trunc(" + Const.FLIGHTS_DEPARTURE + ") = to_date('"
                    + data_from.toString() + "', 'yyyy-mm-dd')";
        }

        System.out.println(query);
        return query;

    }
    public ArrayList[] search_flights(String query) {
        try (Statement statement = getDbConnection().createStatement();
            ResultSet rs = statement.executeQuery(query)){
            int columns = rs.getMetaData().getColumnCount();
            System.out.println(columns);
            ArrayList[] searchResult = new ArrayList[columns];
            for (int i = 0; i < columns; i++) {
                searchResult[i] = new ArrayList();
            };
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    searchResult[i-1].add(rs.getString(i));
                }
            }
            return searchResult;

        } catch (SQLException e) {
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