public class Const {
    public static final String  AIRPORT_TABLE="airports";
    public static final String  AIRPORTS_ID="iata";
    public static final String  AIRPORTS_CITY="city";
    public static final String  AIRPORTS_COUNTRY="country";
    public static final String  FLIGHT_TABLE="flights";
    public static final String  FLIGHTS_ID="flight_id";
    public static final String  FLIGHTS_FROM="airport_from";
    public static final String  FLIGHTS_TO="airport_to";
    public static final String  FLIGHTS_DEPARTURE="Departure";
    public static final String  FLIGHTS_ARRIVAL="Arrival";
    public static final String  FLIGHTS_PRICE_ECONOM="PriceEconom";
    public static final String  FLIGHTS_PRICE_BUSINESS="PriceBusiness";
    public static final String  BUYER_Table="buyers";
    public static final String  BUYERS_ID="buyer_id";
    public static final String  BUYERS_NAME="name";
    public static final String  BUYERS_SURNAME="surname";
    public static final String  BUYERS_PASSPORT="passport";
    public static final String  SEAT_TABLE="seats";
    public static final String  SEATS_FLIGHT_ID="flight_id";
    public static final String  SEAT="seat";
    public static final String  SEATS_BOOKED="booked";
    public static final String  SEATS_BOUGHT="bought";
    public static final String  SEATS_class="TicketClass";

    public static DatabaseHandler dbhand= new DatabaseHandler();

    public static int NumberSeatsBooked= dbhand.maxBookedNumber();







}
