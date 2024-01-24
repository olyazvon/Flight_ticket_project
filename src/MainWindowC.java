import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;


public class MainWindowC extends JFrame {
    JPanel pages;
    String loggedIn = null;

    public MainWindowC() {
        super("Search");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pages = new JPanel(new CardLayout());
        pages.add(new SearchPanel(), 0);
        add(pages);
    }

    public void searchToSeats(String flightThere, String flightBack) {
        pages.add(new SeatPanel(flightThere, flightBack), 1);
        ((CardLayout)pages.getLayout()).next(pages);
    }

    public void seatsToSearch() {
        ((CardLayout)pages.getLayout()).previous(pages);
        pages.remove(1);
    }

    public void seatsToPassengers(int bookingNumber) {
        pages.add(new PassengerPanel(bookingNumber, false), 2);
        ((CardLayout)pages.getLayout()).next(pages);
    }

    public void passengersToSeats() {
        ((CardLayout)pages.getLayout()).previous(pages);
        pages.remove(2);
        if (!(pages.getComponents()[1] instanceof SeatPanel)) {
            seatsToSearch();
        }

    }

    public void passengersToPayment(int bookingNumber) {
        pages.add(new PaymentPanel(bookingNumber), 3);
        ((CardLayout)pages.getLayout()).next(pages);
    }

    public void paymentToPassengers() {
        ((CardLayout)pages.getLayout()).previous(pages);
        pages.remove(3);
    }

    public void searchToPassengers(int bookingNumber) {
        pages.add(new JPanel(), 1);
        ((CardLayout)pages.getLayout()).next(pages);
        pages.add(new PassengerPanel(bookingNumber, true), 2);
        ((CardLayout)pages.getLayout()).next(pages);
    }





    public void stepBackward() {
        ((CardLayout)pages.getLayout()).previous(pages);
    }
}
