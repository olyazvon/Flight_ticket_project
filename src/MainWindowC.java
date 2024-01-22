import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;


public class MainWindowC extends JFrame {
    JPanel pages;

    public MainWindowC() {
        super("Search");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pages = new JPanel(new CardLayout());
        pages.add(new SearchPanel(), 0);
        add(pages);
    }

    public void proceedToSeats(String flightThere, String flightBack) {
        pages.add(new SeatPanel(flightThere, flightBack), 1);
        ((CardLayout)pages.getLayout()).next(pages);
    }

    public void backToSearch() {
        pages.remove(1);
        ((CardLayout)pages.getLayout()).previous(pages);
    }

    public void proceedToPassengers(int bookingNumber) {
        pages.add(new PassengerPanel(bookingNumber), 2);
        ((CardLayout)pages.getLayout()).next(pages);
    }

    public void proceedToPayment(int bookingNumber) {
        try {
            pages.add(new PaymentPanel(bookingNumber), 3);
        } catch (IllegalArgumentException e) {
            pages.add(new PaymentPanel(bookingNumber), 2);
        }
        ((CardLayout)pages.getLayout()).next(pages);
    }

    public void jumpToPassengers(int bookingNumber) {
        pages.add(new PassengerPanel(bookingNumber), 1);
        ((CardLayout)pages.getLayout()).next(pages);
    }

    public void backFromPayment() {
        try {
            pages.remove(3);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            pages.remove(2);
        }
        ((CardLayout)pages.getLayout()).previous(pages);
    }

    public void backFromPassengers() {
        try {
            pages.remove(2);
        } catch (ArrayIndexOutOfBoundsException e) {
            pages.remove(1);
        }
        ((CardLayout)pages.getLayout()).previous(pages);
    }

    public void stepBackward() {
        ((CardLayout)pages.getLayout()).previous(pages);
    }
}
