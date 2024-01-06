import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame {
    JPanel panel;
    JPanel storedSearchPanel;

    public MainWindow() {
        super("Search");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel = new SearchPanel();
        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        add(panel);
    }

    public void stepForward() {
        remove(panel);
        storedSearchPanel = panel;
        panel = new SeatPanel("a", "b");
        add(panel);
        //invalidate();//validate();//repaint();
        revalidate();
    }

    public void stepBackward() {
        remove(panel);
        panel = storedSearchPanel;
        add(panel);
        //invalidate();validate();
        revalidate();
        repaint();
    }
}