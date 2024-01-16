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

    public static final String  BOOKING_TABLE="Bookings";
    public static final String  BOOKING_NUMBER="BookingNumber";
    public static final String  BOOKING_Total="Total";
    public static final String  BOOKING_DATE="BookingDate";



    public static final String  USER_TABLE="USERS";
    public static final String  USER_ID="User_id";
    public static final String  USER_NAME="CardHolderName";
    public static final String  USER_CARD_DATE="CardDate";
    public static final String  USER_CARD_NUMBER="CardNumber";
    public static final String  USER_CARD_CVV="CardCVV";
    public static final String  USER_BOOKING_NUMBER="BookingNumber";

    public static final String  PASSENGER_TABLE="PASSENGERS";
    public static final String  PASSENGER_ID="passport";
    public static final String  PASSENGER_FIRST_NAME="FirstName";
    public static final String PASSENGER_LAST_NAME="LastName";
    public static final String  PASSENGER_SEAT="Seat";
    public static final String  PASSENGER_FLIGHT="Flight";
    public static final String  SEAT_TABLE="seats";
    public static final String  SEATS_FLIGHT_ID="flight_id";
    public static final String  SEAT="seat";
    public static final String  SEATS_BOOKED="booked";
    public static final String  SEATS_BOUGHT="bought";
    public static final String  SEATS_class="TicketClass";

    public static DatabaseHandler dbhand= new DatabaseHandler();









}
