import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.sql.SQLException;
import java.util.ArrayList;


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

    public void proceedToPassengers(ArrayList<Seat> seats) {
        pages.add(new PassengerPanel(seats), 2);
        ((CardLayout)pages.getLayout()).next(pages);
    }

    public void backToSeats() {
        pages.remove(2);
        ((CardLayout)pages.getLayout()).previous(pages);
    }

    public void stepBackward() {
        ((CardLayout)pages.getLayout()).previous(pages);
    }
}
