import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Seat extends JToggleButton {
    public double price;
    public String flight;
    public Seat(String name, double price, boolean occupied, String seatClass, String flight, ActionListener listener) {
        super(name);
        setMargin(new Insets(1, 0, 1, 0));
        this.price = price;
        this.flight = flight;
        setToolTipText(String.format("%.2f", price));
        if (seatClass.equals("Business")) {
            setBackground(new Color(230, 240, 190));
        } else {
            setBackground(new Color(210, 240, 210));
        }
        addActionListener(listener);
        if (occupied) {
            setEnabled(false);
            setBackground(Color.white);
        }
        setPreferredSize(new Dimension(30,30));
    }

    public void setDemo() {
        setModel(new JToggleButton.ToggleButtonModel() {
            public void setPressed(boolean b) {}
        });
    }

}