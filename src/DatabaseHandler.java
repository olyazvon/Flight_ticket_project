import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class  DatabaseHandler extends Configs {
    static Connection dbConnection;

    public DatabaseHandler() {
        makeDbConnection();
    }

    public static void makeDbConnection() {
        //{
        String connectionString = "jdbc:oracle:thin" + ":" + dbUser + "/" +
                dbPass + "@" + dbHost + ":" + dbPort + ":" + dbName;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getDbConnection() {
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
    //и добавляет в 0 элемент Any + название этого столбца
    public String[] read_distinct_column(String table_name,
                                         String column_name, String Where) {
        String where_st = "";

        if (!Where.isEmpty()) {
            where_st = String.format(" WHERE " + Where);
        }
        String query = String.format("SELECT DISTINCT " + column_name + " FROM %s", table_name + where_st);
        String st = "";
        String query1 = query + " order by " + column_name;
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query1)) {

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
            whereString += Const.AIRPORTS_COUNTRY + "= '" + selectedCountry + "'";
        }
        if (!excludedCity.isEmpty() && !excludedCity.equals("Any city")) {
            if (whereString != "") {
                whereString += " AND ";
            }
            whereString += Const.AIRPORTS_CITY + " Not like '" + excludedCity + "'";
        }

        String[] Cities = read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_CITY,
                whereString);
        return Cities;
    }

    String[] IATAs(String selectedCountry, String selectedCity, String excludedCity) {
        String whereString = "";

        if (!selectedCountry.isEmpty() && !selectedCountry.equals("Any country")) {
            whereString += Const.AIRPORTS_COUNTRY + "= '" + selectedCountry + "'";
        }
        if (!selectedCity.isEmpty() && !selectedCity.equals("Any city")) {
            if (whereString != "") {
                whereString += " AND ";
            }
            whereString += Const.AIRPORTS_CITY + "= '" + selectedCity + "'";
        }
        if (!excludedCity.isEmpty() && !excludedCity.equals("Any iata")) {
            if (whereString != "") {
                whereString += " AND ";
            }
            whereString += Const.AIRPORTS_CITY + " Not like '" + excludedCity + "'";
        }

        String[] IATAs = read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_ID,
                whereString);
        return IATAs;
    }

    String cityByIATA(String iata) {
        return read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_CITY,
                (Const.AIRPORTS_ID + "='" + iata + "'"))[1];
    }


    //    public String q_search_flights (String iata_from, String iata_to, LocalDate data_from) {
//        String query = String.format(
//                "SELECT fl.flight_id, airport_from, airport_to, " +
//                        "to_char(departure, 'dd.mm.yy hh:mi'), " +
//                        "to_char(arrival, 'dd.mm.yy hh:mi')," +
//                        " priceeconom, pricebusiness,s_economy.seats_left_economy," +
//                        "s_business.seats_left_business " +
//                " FROM " + Const.FLIGHT_TABLE + " fl" + ",(" +
//                        seats_left("Business") + ") s_business,(" +
//                        seats_left("Economy") + ") s_economy" +
//                " WHERE fl." + Const.FLIGHTS_ID + "=s_economy.flight_id and fl." +
//                        Const.FLIGHTS_ID + "=s_business.flight_id  " +
//                        "and (seats_left_business>0 OR seats_left_economy>0)");
//
//        if (!iata_from.equals("Any iata") && !iata_from.isEmpty()) {
//            query += " AND " + Const.FLIGHTS_FROM + "= '" + iata_from + "'";
//        }
//
//        if (!iata_to.equals("Any iata") && !iata_to.isEmpty()) {
//            query += " AND " + Const.FLIGHTS_TO + "= '" + iata_to + "'";
//        }
//
//        if (data_from != null) {
//            query += " AND trunc(" + Const.FLIGHTS_DEPARTURE + ") = to_date('"
//                    + data_from.toString() + "', 'yyyy-mm-dd')";
//        }
//        return query;
//
//    }
    public String q_search_flights(String[] iata_from, String[] iata_to, LocalDate date_from, LocalTime time_from) {
        String query = String.format(
                "SELECT fl." + Const.FLIGHTS_ID + "," + Const.FLIGHTS_FROM + "," +
                        Const.FLIGHTS_TO + ", to_char(" + Const.FLIGHTS_DEPARTURE + ", 'dd.mm.yy hh24:mi'), " +
                        "to_char(" + Const.FLIGHTS_ARRIVAL + ", 'dd.mm.yy hh24:mi'), " +
                        Const.FLIGHTS_PRICE_ECONOM + "," + Const.FLIGHTS_PRICE_BUSINESS +
                        ", s_economy.seats_left_economy, " + "s_business.seats_left_business " +
                        " FROM " + Const.FLIGHT_TABLE + " fl" + ",(" +
                        seats_left("Business") + ") s_business,(" +
                        seats_left("Economy") + ") s_economy" +
                        " WHERE fl." + Const.FLIGHTS_ID + "=s_economy.flight_id and fl." +
                        Const.FLIGHTS_ID + "=s_business.flight_id  " +
                        "and (seats_left_business > 0 OR seats_left_economy > 0)") +
                " AND " + Const.FLIGHTS_DEPARTURE +
                " > (SElect SYSDATE + 1/24 from dual)";

        if (!((iata_from.length == 1 && iata_from[0].equals("Any iata")) || iata_from.length == 0)) {
            String stIata_from = "";

            for (String i : iata_from) {
                stIata_from += "'" + i + "'" + ",";
            }
            System.out.println(Arrays.toString(iata_from));
            if ((iata_from.length != 1 || iata_from[0].equals("Any iata"))) {
                stIata_from = "(" + stIata_from.substring(iata_from[0].length() + 3, stIata_from.length() - 1) + ")";
            } else {
                stIata_from = "(" + stIata_from.substring(0, stIata_from.length() - 1) + ")";
            }
            query += " AND " + Const.FLIGHTS_FROM + " in " + stIata_from;
        }

        if (!((iata_to.length == 1 && iata_to[0].equals("Any iata")) || iata_to.length == 0)) {
            String stIata_to = "";

            for (String i : iata_to) {
                stIata_to += "'" + i + "'" + ",";
            }
            if (iata_to.length != 1) {
                stIata_to = "(" + stIata_to.substring(iata_to[0].length() + 3, stIata_to.length() - 1) + ")";
            } else {
                stIata_to = "(" + stIata_to.substring(0, stIata_to.length() - 1) + ")";
            }
            query += " AND " + Const.FLIGHTS_TO + " in " + stIata_to;
        }

        if (date_from != null) {
            if (time_from == null) {
                query += " AND trunc(" + Const.FLIGHTS_DEPARTURE + ") = to_date('"
                        + date_from.toString() + "', 'yyyy-mm-dd')";
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                LocalDateTime departureDateTime= LocalDateTime.of(date_from, time_from);

                query += " AND (" + Const.FLIGHTS_DEPARTURE + ") >= to_date('"
                        + departureDateTime.minusHours(2).format(formatter) +
                        "', 'yyyy-mm-dd hh24:mi')" +
                        " AND (" + Const.FLIGHTS_DEPARTURE + ") <= to_date('"
                        + departureDateTime.plusHours(2).format(formatter) +
                        "', 'yyyy-mm-dd hh24:mi')";
            }
        }
        System.out.println(query);
        return query;

    }


    public ArrayList[] search_flights(String query) {
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            int columns = rs.getMetaData().getColumnCount();
            ArrayList[] searchResult = new ArrayList[columns];
            for (int i = 0; i < columns; i++) {
                searchResult[i] = new ArrayList();
            }
            ;
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    searchResult[i - 1].add(rs.getString(i));
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
                "(count(" + Const.SEAT + ") - count(" + Const.SEATS_BOOKED + ")) AS seats_left_" + TicketClass +
                " FROM " + Const.SEAT_TABLE + " WHERE " + Const.SEATS_class + "= '" + TicketClass + "'" + " GROUP BY " + Const.SEATS_FLIGHT_ID);
        return (query);
    }

    //Seat Panel functions
    //ввыводит в arrayList значения столбца, упорядоченый по  столбцу
    public String q_read_column_for_flight(String table_name, String column_name, String flightNumber, String OrderBy,
                                           Boolean Desc) {
        String HowOrd = "";
        String OrdBy = "";

        if (Desc) {
            HowOrd = " desc";
        }
        if (!OrderBy.isEmpty()) {
            OrdBy = " ORDER BY LENGTH(" + OrderBy + "), " + OrderBy;
        }
        String query = String.format("SELECT " + column_name +
                " FROM " + table_name +
                " WHERE " + Const.SEATS_FLIGHT_ID + " = " + "'" + flightNumber +
                "' " + OrdBy + HowOrd);
        return query;
    }

    public ArrayList<String> read_seats_for_flight(String flight) {
        String query = q_read_column_for_flight(Const.SEAT_TABLE, Const.SEAT, flight, Const.SEAT, false);
        ArrayList arList = new ArrayList<>();
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                arList.add(rs.getString(Const.SEAT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arList;
    }

    public ArrayList<String> read_typeClass_for_flight(String flight) {
        String query = q_read_column_for_flight(Const.SEAT_TABLE, Const.SEATS_class,
                flight, Const.SEAT, false);
        ArrayList arList = new ArrayList<>();
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                arList.add(rs.getString(Const.SEATS_class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arList;
    }

    public ArrayList<Double> prices_for_flight(String flight) {
        String query = String.format(
                "SELECT %1$s.%2$s, %3$s, " +
                        "CASE %4$s WHEN 'Economy' THEN %5$s ELSE %6$s END price " +
                        "FROM %7$s, %8$s " +
                        "WHERE %9$s.%10$s = %11$s.%12$s " +
                        "AND %13$s.%14$s = '%15$s' " +
                        "ORDER BY LENGTH(%16$s), %17$s",
                Const.SEAT_TABLE, Const.SEATS_FLIGHT_ID, Const.SEAT,
                Const.SEATS_class, Const.FLIGHTS_PRICE_ECONOM, Const.FLIGHTS_PRICE_BUSINESS,
                Const.SEAT_TABLE, Const.FLIGHT_TABLE,
                Const.SEAT_TABLE, Const.SEATS_FLIGHT_ID, Const.FLIGHT_TABLE, Const.FLIGHTS_ID,
                Const.FLIGHT_TABLE, Const.FLIGHTS_ID, flight,
                Const.SEAT, Const.SEAT);

        ArrayList<Double> arList = new ArrayList<>();
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                arList.add(rs.getDouble("price"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arList;

    }

    public ArrayList<Boolean> occupied(String flight) {
        String query = String.format(
                "SELECT (CASE WHEN %1$s IS NULL AND %2$s = 0 THEN 0 ELSE 1 END) oc " +
                        "FROM %3$s " +
                        "WHERE %4$s = '%5$s' " +
                        "ORDER BY LENGTH(%6$s), %7$s",
                Const.SEATS_BOOKED,
                Const.SEATS_BOUGHT,
                Const.SEAT_TABLE,
                Const.FLIGHTS_ID, flight,
                Const.SEAT, Const.SEAT);
        ArrayList<Boolean> arList = new ArrayList<>();
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                arList.add(rs.getBoolean("oc"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arList;
    }

    public String qFromTo(String flight) {
        String query = String.format(
                "SELECT %1$s, %2$s " +
                        "FROM %3$s " +
                        "WHERE %4$s = '%5$s'",
                Const.FLIGHTS_FROM, Const.FLIGHTS_TO,
                Const.FLIGHT_TABLE,
                Const.FLIGHTS_ID, flight);
        System.out.println(query);
        String result = "";
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            rs.next();
            result = rs.getString(1).trim();
            result += " - " + rs.getString(2).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public LocalDateTime getArrival(String flight) {
        String query = String.format(
                "SELECT %1$s " +
                        "FROM %2$s " +
                        "WHERE %3$s = '%4$s'",
                Const.FLIGHTS_ARRIVAL,
                Const.FLIGHT_TABLE,
                Const.FLIGHTS_ID, flight);
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            rs.next();
            Date d = rs.getDate(1);
            Time t = rs.getTime(1);
            return LocalDateTime.of(d.toLocalDate(), t.toLocalTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LocalDateTime getDeparture(String flight) {
        String query = String.format(
                "SELECT %1$s " +
                        "FROM %2$s " +
                        "WHERE %3$s = '%4$s'",
                Const.FLIGHTS_DEPARTURE,
                Const.FLIGHT_TABLE,
                Const.FLIGHTS_ID, flight);
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            rs.next();
            Date d = rs.getDate(1);
            Time t = rs.getTime(1);
            return LocalDateTime.of(d.toLocalDate(), t.toLocalTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isFree(String flight, String seat) {
        String query = " SELECT " + Const.SEATS_BOOKED +
                " FROM " + Const.SEAT_TABLE +
                " WHERE " + Const.FLIGHTS_ID + " = '" + flight +
                "' AND " + Const.SEAT + " = '" + seat + "'";
        //System.out.println(query);
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            rs.next();
            String bookNumber = rs.getString(1);
            return bookNumber == null;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //max номер брони, если ошибка в запросе выводит -2
    public int maxBookedNumber() {
        int Max = 0;
        String query = " SELECT  MAX(" + Const.SEATS_BOOKED + ")" +
                " FROM " + Const.SEAT_TABLE;
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            rs.next();
            Max = rs.getInt(1);
            return Max;

        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }
    //забронировать место UPdate booked in bd,
    // если место занято-1, если ошибка -2, если забронировано выводит № брони
//    public int Book(String flight, String seat){
//       int newBookedNumber=(maxBookedNumber()+1);
//       if (isFree(flight,seat)){
//        String query=" UPDATE "+Const.SEAT_TABLE+
//                     " SET "+Const.SEATS_BOOKED + " = "+newBookedNumber+
//                     " WHERE "+Const.FLIGHTS_ID + " = '"+ flight+
//                     "' AND "+ Const.SEAT+" = '"+seat+"'";
//        try (Statement statement = getDbConnection().createStatement();
//             ResultSet rs = statement.executeQuery(query)) {
//            rs.next();
//            return newBookedNumber;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return  -2;
//        }
//    }
//       else{return -1;}
//
//}

    //    забронировать местa UPdate booked in bd,
//     если место занято-1, если ошибка -2, если забронировано выводит № брони
    public int book(ArrayList<Seat> seatsToBook, double total) throws RuntimeException, SQLException {

        for (Seat i : seatsToBook) {
            if (!isFree(i.flight, i.getText())) {
                throw new RuntimeException("Selected seat is already occupied!");
            }
        }

        int bookingNumber = maxBookedNumber() + 1;
        String stFlightToBook;
        String stSeatToBook;
        Statement statement;
        ResultSet rs;
        String query;

        for (Seat i : seatsToBook) {
            stFlightToBook = "'" + i.flight + "'";
            stSeatToBook = "'" + i.getText() + "'";
            query = " UPDATE " + Const.SEAT_TABLE +
                    " SET " + Const.SEATS_BOOKED + " = " + bookingNumber +
                    " WHERE " + Const.FLIGHTS_ID + " = " + stFlightToBook +
                    " AND " + Const.SEAT + " = " + stSeatToBook;
            statement = getDbConnection().createStatement();
            rs = statement.executeQuery(query);
            rs.next();
        }

        query = " INSERT INTO " + Const.BOOKING_TABLE +
                " VALUES ("+bookingNumber+", "+total+
                ", TO_DATE('"+LocalDate.now()+"', 'YYYY-MM-DD'"+"))";
        System.out.println(query);
        statement = getDbConnection().createStatement();
        statement.executeUpdate(query);

        return bookingNumber;
    }

    public void addBookingToUser(int booking, String login) throws SQLException {
        String query = " UPDATE " + Const.USER_TABLE +
                " SET " + Const.USER_BOOKING_NUMBER + " = " + booking +
                " WHERE " + Const.USER_LOGIN + " = '" + login + "'";
        Statement statement = getDbConnection().createStatement();
        statement.executeUpdate(query);
    }

    public void unBookSeats(int BookingNumber) throws SQLException {
        String query = " UPDATE " + Const.SEAT_TABLE +
                " SET " + Const.SEATS_BOOKED + " = " + "null" +
                " WHERE " + Const.SEATS_BOOKED + " = " + BookingNumber;
        //System.out.println(query);
        Statement statement = getDbConnection().createStatement();
        statement.executeUpdate(query);
    }

    public void delBooking(int BookingNumber) throws SQLException {
        String query = " DELETE FROM " + Const.BOOKING_TABLE +
                             " WHERE " + Const.BOOKING_NUMBER + " = " + BookingNumber;
        Statement statement = getDbConnection().createStatement();
        statement.executeUpdate(query);
    }

    public void clearUsersBooking(int booking) throws SQLException {
        String query = " UPDATE " + Const.USER_TABLE +
                " SET " + Const.USER_BOOKING_NUMBER + " = " + "null" +
                " WHERE " + Const.USER_BOOKING_NUMBER + " = " + booking;
        Statement statement = getDbConnection().createStatement();
        statement.executeUpdate(query);
    }

    public void removeBookingTotally(int booking) throws SQLException {
        delBooking(booking);
        unBookSeats(booking);
        clearUsersBooking(booking);
    }

    public String DelUnValidBooking() {
        String query = " DELETE FROM " + Const.BOOKING_TABLE +
                " WHERE " + Const.BOOKING_DATE+ " + 1 < SYSDATE " ;
        System.out.println(query);
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            return "не удалось удалить бронирование из Броней";
        }
        return "бронирование удалено из таблицы броней";

    }


    public ArrayList<Seat> seatsInBooking(int bookingNumber) {
        String query = String.format(
                "SELECT %1$s.*," +
                        "CASE %2$s WHEN 'Economy' THEN %3$s ELSE %4$s END price " +
                        "FROM %5$s, %6$s " +
                        "WHERE %7$s.%8$s = %9$s.%10$s " +
                        "AND %11$s.%12$s = %13$s ",
                Const.SEAT_TABLE,
                Const.SEATS_class, Const.FLIGHTS_PRICE_ECONOM, Const.FLIGHTS_PRICE_BUSINESS,
                Const.SEAT_TABLE, Const.FLIGHT_TABLE,
                Const.SEAT_TABLE, Const.SEATS_FLIGHT_ID, Const.FLIGHT_TABLE, Const.FLIGHTS_ID,
                Const.SEAT_TABLE, Const.SEATS_BOOKED, bookingNumber);
        //System.out.println(query);
        ArrayList arList = new ArrayList<Seat>();
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                Boolean occuppied = false;
                if (rs.getString(Const.SEATS_BOOKED) != null) {
                    occuppied = true;
                }
                Seat seat = new Seat(rs.getString(Const.SEAT),
                        rs.getDouble("price"),
                        occuppied,
                        rs.getString(Const.SEATS_class),
                        rs.getString(Const.SEATS_FLIGHT_ID), null);
                arList.add(seat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arList;
    }

    public boolean isBookingValid(int BookingNumber) {

        String query = " SELECT " + Const.BOOKING_DATE +
                " FROM " + Const.BOOKING_TABLE +
                " WHERE " + Const.BOOKING_NUMBER + " = '" + BookingNumber + "'";
        //System.out.println(query);
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            rs.next();
            LocalDateTime bookDate = rs.getDate(Const.BOOKING_DATE).toLocalDate().atTime(
                    rs.getTime(Const.BOOKING_DATE).toLocalTime());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/YYYY HH:mm");
            LocalDateTime ValidationTime = bookDate.plusDays(1);
            if (LocalDateTime.now().isBefore(ValidationTime)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public String SignUp(String login, String password, String passTocheck) throws RuntimeException{
        if (!Objects.equals(password, passTocheck)) {
            throw new RuntimeException("Passwords don't match!");
        } else if (password.length() < 4){
            throw new RuntimeException("Password is too short!");
        } else if (login.length() < 3){
            throw new RuntimeException("Login is too short!");
        } else {
            String query = " INSERT INTO " + Const.USER_TABLE +
                    "(" + Const.USER_LOGIN + "," + Const.USER_PASS + ")" +
                    "VALUES ('" + login + "','" + password + "')";
            System.out.println(query);
            try (Statement statement = getDbConnection().createStatement();
                 ResultSet rs = statement.executeQuery(query)) {

                return login;
            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                throw new RuntimeException("Login is not unique!");
            } catch (SQLException e) {
                throw new RuntimeException("Unknown error, please try later!");
            }
        }
    }

    //returns booking number
    public int SignIn(String login, String password) {
        String query = " SELECT COUNT(*) FROM " + Const.USER_TABLE +
                " WHERE " + Const.USER_LOGIN + "='" + login + "'" +
                " AND " + Const.USER_PASS + "='" + password + "'";
        //System.out.println(query);
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            rs.next();
            int isLogin = rs.getInt(1);
            if (isLogin == 0) {
                throw new RuntimeException("Incorrect login or password!");
            } else {
                String query1 = " SELECT " + Const.USER_BOOKING_NUMBER +
                        " FROM " + Const.USER_TABLE +
                        " WHERE " + Const.USER_LOGIN + "='" + login + "'" +
                        " AND " + Const.USER_PASS + "='" + password + "'";
                //System.out.println(query1);
                try (Statement statement1 = getDbConnection().createStatement();
                     ResultSet rs1 = statement1.executeQuery(query1)) {
                    rs1.next();
                    int isBooking = rs1.getInt(1);
                    return isBooking;
                } catch (SQLException e) {
                    throw new RuntimeException("Unknown error, please try again later!");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getUsersBooking(String login) {
        String query1 = " SELECT " + Const.USER_BOOKING_NUMBER +
                " FROM " + Const.USER_TABLE +
                " WHERE " + Const.USER_LOGIN + "='" + login + "'";
        //System.out.println(query1);
        try (Statement statement1 = getDbConnection().createStatement();
             ResultSet rs1 = statement1.executeQuery(query1)) {
            rs1.next();
            int isBooking = rs1.getInt(1);
            return isBooking;
        } catch (SQLException e) {
            throw new RuntimeException("Unknown error, please try again later!");
        }
    }

    public double totalSum(int BookingNumber) throws SQLException {
        String query = " SELECT " + Const.BOOKING_Total +
                " FROM " + Const.BOOKING_TABLE +
                " WHERE " + Const.BOOKING_NUMBER + " = '" + BookingNumber + "'";
        //System.out.println(query);
        Statement statement = getDbConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        return rs.getDouble(1);
    }

       public void saveCardDetails(String login, String cardnumber,
                                  String cardcvv,LocalDate carddate, String name) {
           String query = " UPDATE " + Const.USER_TABLE +
                   " SET " + Const.USER_CARD_NUMBER + " = " + cardnumber + " , " +
                   Const.USER_CARD_CVV + " = " + cardcvv + " , "
                   + Const.USER_CARD_DATE + " = " +
                   "TO_DATE('" + carddate + "', 'YYYY-MM-DD')" + " , " +
                   Const.USER_NAME + " = '" + name + "'" +
                   " WHERE " + Const.USER_LOGIN + " = '" + login + "'";
           System.out.println(query);
           try (Statement statement = getDbConnection().createStatement();
                ResultSet rs = statement.executeQuery(query)) {
               rs.next();

           } catch (SQLException e) {
               throw new RuntimeException(e);
           }
       }
       public void buy(int bookingNumber) throws SQLException {
           String query = " UPDATE " + Const.SEAT_TABLE +
                   " SET "+Const.SEATS_BOUGHT+"= 1"+
                   " WHERE " + Const.SEATS_BOOKED + " = " + bookingNumber;
           Statement statement = getDbConnection().createStatement();
           ResultSet rs = statement.executeQuery(query);
           rs.next();
           String query1 = " UPDATE " + Const.USER_TABLE +
                   " SET "+Const.USER_BOOKING_NUMBER+"= null"+
                   " WHERE " + Const.USER_BOOKING_NUMBER + " = " + bookingNumber;
           System.out.println(query);
           Statement statement1 = getDbConnection().createStatement();
           ResultSet rs1 = statement1.executeQuery(query1);
           rs1.next();
       }

       public void addPassengersToDB(ArrayList<PassengerPanel.OnePassenger> Passengers){
           for (PassengerPanel.OnePassenger passenger:Passengers) {
               String Passport=passenger.getPassport();
               String LastName= passenger.getLastName();
               String FirstName= passenger.getFirstName();
               String Seat= passenger.seat.getText();
               String Flight=passenger.seat.flight;
               String query=String.format("INSERT INTO "+Const.PASSENGER_TABLE+
                       " VALUES ("+"'%1$s','%2$s','%3$s','%4$s','%5$s')",
                       Passport,LastName,FirstName,Seat,Flight);
               System.out.println(query);
               try (Statement statement = getDbConnection().createStatement();
                    ResultSet rs = statement.executeQuery(query)) {
                   rs.next();
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       }
       public  void removePassengersFromDB(int BookingNumber){
        String query= String.format(
                      "DELETE FROM "+Const.PASSENGER_TABLE+
                           " WHERE ("+Const.PASSENGER_SEAT+ " , " +Const.PASSENGER_FLIGHT+") in("+
                                    " SELECT "+Const.SEAT + " , " +Const.SEATS_FLIGHT_ID+
                                      " FROM "+Const.SEAT_TABLE+
                                     " WHERE "+ Const.SEATS_BOOKED+" = " +BookingNumber+
                                     ")");

        System.out.println(query);
           try (Statement statement = getDbConnection().createStatement()){
               statement.executeUpdate(query);
           } catch (Exception e) {
               e.printStackTrace();
           }
    }

    public String[] selectCardDetails(String login) {
        String query = " SELECT "+
                 Const.USER_CARD_NUMBER + "," + Const.USER_CARD_CVV + ","
                + Const.USER_CARD_DATE + "," + Const.USER_NAME +
                " FROM " + Const.USER_TABLE +
                " WHERE " +Const.USER_LOGIN + "= '" + login +"'";
        System.out.println(query);
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            rs.next();
            return new String[] {
                    rs.getString(Const.USER_CARD_NUMBER),
                    rs.getString(Const.USER_CARD_CVV),
                    rs.getString(Const.USER_CARD_DATE),
                    rs.getString(Const.USER_NAME)
            };
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String qFromToCities(String flight) {

        String queryFrom= String.format(
                "SELECT a."+ Const.AIRPORTS_CITY+ "  , " +Const.FLIGHTS_FROM +
                " FROM "+     Const.FLIGHT_TABLE + " , " + Const.AIRPORT_TABLE +
                " a WHERE a."+Const.AIRPORTS_ID+" = "+Const.FLIGHTS_FROM +
                " AND "+Const.FLIGHTS_ID+" = '"+ flight+"'");

        System.out.println(queryFrom);
        String result = "";
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(queryFrom)) {
            rs.next();
            result = rs.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String queryTo= String.format(
                "SELECT a."+ Const.AIRPORTS_CITY+ " , " +Const.FLIGHTS_TO +
                        " FROM "+     Const.FLIGHT_TABLE + " , " + Const.AIRPORT_TABLE+
                        " a WHERE a."+Const.AIRPORTS_ID+" = "+Const.FLIGHTS_TO+
                        " AND "+Const.FLIGHTS_ID+" = '"+ flight+"'");
        System.out.println(queryTo);
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs1 = statement.executeQuery(queryTo)) {
            rs1.next();
            result += " - " + rs1.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
            return result;
    }

    public String MyFlight(String passport){
        String query="SELECT * FROM "+Const.PASSENGER_TABLE+
                " WHERE "+Const.PASSENGER_ID+"= '"+passport+"'";
        String MyFlight="";
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            rs.next();
            MyFlight+="This amazing person: "+ rs.getString(Const.PASSENGER_FIRST_NAME)+
                    " "+rs.getString(Const.PASSENGER_LAST_NAME)+'\n';
            MyFlight+="Will flight on comfortable chair number "+rs.getString(Const.PASSENGER_SEAT)+'\n';
            String Flight= rs.getString(Const.PASSENGER_FLIGHT);
            MyFlight+="On flight number "+Flight+'\n';
            MyFlight+="To wonderful journey:"+qFromToCities(Flight)+"("+
                    qFromTo(Flight)+")"+'\n';
            MyFlight+= "Which starts at "+getDeparture(Flight).toString()+'\n';
            MyFlight+= "And will arrive to destination at "+getArrival(Flight)+'\n';
            MyFlight+= "Have a nice flight!";


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return MyFlight;

    }
    public String getClass(String seat,String flight) {
     String query = "SELECT " + Const.SEATS_class +
             " FROM " + Const.SEAT_TABLE +
             " WHERE " + Const.SEAT + "= '" + seat + "' AND " +
             Const.SEATS_FLIGHT_ID + "= '" + flight + "'";
     System.out.println(query);
     String Class;
     try (Statement statement = getDbConnection().createStatement();
          ResultSet rs = statement.executeQuery(query)) {
         rs.next();
         Class = rs.getString(1);
     } catch (SQLException e) {
         throw new RuntimeException(e);
     }
     return Class;
    }

    public String[] getPassenger(String Seat, String Flight) {
        String query = String.format(
                      "SELECT *" +
                        " FROM " + Const.PASSENGER_TABLE +
                        " WHERE " + Const.PASSENGER_SEAT + " = '" + Seat + "' AND " +
                        Const.PASSENGER_FLIGHT + " = '" + Flight + "'");

        System.out.println(query);

        String[] Passenger = new String[3];
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {;
           rs.next();
                Passenger[0] = rs.getString(Const.PASSENGER_ID);
                Passenger[1] = rs.getString(Const.PASSENGER_FIRST_NAME);
                Passenger[2] = rs.getString(Const.PASSENGER_LAST_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Passenger;
    }
    public ArrayList<String[]> myNextFlights(String passport){
        String query="SELECT * FROM "+Const.PASSENGER_TABLE+
                " WHERE "+Const.PASSENGER_ID+"= '"+passport+"'";
        String[] MyFlight= new String[7];
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            ArrayList arrList= new ArrayList<>();
            while (rs.next()){
                MyFlight[0] = rs.getString(Const.PASSENGER_FIRST_NAME) +
                        " " + rs.getString(Const.PASSENGER_LAST_NAME) ;
                MyFlight [1]= rs.getString(Const.PASSENGER_SEAT);
                String Flight = rs.getString(Const.PASSENGER_FLIGHT);
                MyFlight [2] = Flight;
                System.out.println(Flight);
                MyFlight [3] = qFromToCities(Flight) + " (" +
                               qFromTo(Flight) + ")";
                MyFlight [4] = getDeparture(Flight).format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm"));
                Duration duration = Duration.between(getDeparture(Flight), getArrival(Flight));
                MyFlight [5] = duration.toHoursPart()+":"+duration.toMinutesPart()%60;
                MyFlight[6] = getClass(rs.getString(Const.PASSENGER_SEAT),Flight);
                if (getDeparture(Flight).isAfter(LocalDateTime.now())) {
                    arrList.add(MyFlight.clone());
                }
            }
            return arrList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<String[]> myNextFlights(int bookingNumber) {
        String query="SELECT "+Const.SEAT+", "+Const.SEATS_FLIGHT_ID+", "+Const.SEATS_class+
                " FROM "+Const.SEAT_TABLE+
                " WHERE "+Const.SEATS_BOOKED+" = "+bookingNumber;
        String[] MyFlight= new String[7];
        try (Statement statement = getDbConnection().createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            ArrayList arrList= new ArrayList<>();
            while (rs.next()){
                MyFlight[0] = getPassenger(rs.getString(Const.SEAT),rs.getString(Const.SEATS_FLIGHT_ID))[1]+" "+
                              getPassenger(rs.getString(Const.SEAT),rs.getString(Const.SEATS_FLIGHT_ID))[2];
                MyFlight [1]= rs.getString(Const.SEAT);
                String Flight = rs.getString(Const.SEATS_FLIGHT_ID);
                MyFlight [2] = Flight;
                MyFlight [3] = qFromToCities(Flight) + " (" +
                        qFromTo(Flight) + ")";
                MyFlight [4] = getDeparture(Flight).format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm"));
                Duration duration = Duration.between(getDeparture(Flight), getArrival(Flight));
                MyFlight [5] = duration.toHoursPart()+":"+duration.toMinutesPart()%60;
                MyFlight[6] = rs.getString(Const.SEATS_class);
                arrList.add(MyFlight.clone());
            }
            return arrList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}



