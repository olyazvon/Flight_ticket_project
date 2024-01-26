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
        ((SearchPanel)pages.getComponents()[0]).headerLoggedIn(loggedIn);
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
        pages.add(new PaymentPanel(bookingNumber, loggedIn), 3);
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

    public void paymentToSummary(int booking) {
        pages.add(new SummaryPanel(booking, null), 4);
        ((CardLayout)pages.getLayout()).next(pages);
    }

    public void  searchToSummary(String passport) {
        pages.add(new JPanel(), 1);
        pages.add(new JPanel(), 2);
        pages.add(new JPanel(), 3);
        pages.add(new SummaryPanel(0, passport), 4);
        ((CardLayout)pages.getLayout()).next(pages);
        ((CardLayout)pages.getLayout()).next(pages);
        ((CardLayout)pages.getLayout()).next(pages);
        ((CardLayout)pages.getLayout()).next(pages);

    }

    public void summaryToSearch() {
        ((CardLayout)pages.getLayout()).previous(pages);
        ((CardLayout)pages.getLayout()).previous(pages);
        ((CardLayout)pages.getLayout()).previous(pages);
        ((CardLayout)pages.getLayout()).previous(pages);
        pages.remove(4);
        pages.remove(3);
        pages.remove(2);
        pages.remove(1);
    }



    public void stepBackward() {
        ((CardLayout)pages.getLayout()).previous(pages);
    }
}
