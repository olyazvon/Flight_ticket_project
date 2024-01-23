import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class PassengerPanel extends JPanel {
    ArrayList<Seat> seats;
    ArrayList<OnePassenger> passengers = new ArrayList<>();
    DatabaseHandler dbhand;
    public PassengerPanel(int bookingNumber) {
        dbhand = new DatabaseHandler();
        this.seats = dbhand.seatsInBooking(bookingNumber);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

//INTERFACE
        add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel nameL = new JLabel("Please enter passenger information");
        nameL.setFont(new Font(null, Font.PLAIN, 24));
        nameL.setAlignmentX(CENTER_ALIGNMENT);
        add(nameL);
        add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel listP = new JPanel();
        listP.setLayout(new BoxLayout(listP, BoxLayout.Y_AXIS));

        listP.add(Box.createVerticalGlue());

        String currentFlight = "";
        for (Seat seat : this.seats) {
            if (!seat.flight.equals(currentFlight)) {
                JLabel flightL = new JLabel(seat.flight+"  "+dbhand.qFromTo(seat.flight));
                flightL.setFont(new Font(null, Font.PLAIN, 18));
                flightL.setAlignmentX(CENTER_ALIGNMENT);
                listP.add(flightL);
                listP.add(Box.createRigidArea(new Dimension(0, 15)));
                currentFlight = seat.flight;
            }
            OnePassenger pss = new OnePassenger(seat);
            this.passengers.add(pss);
            listP.add(pss);
            listP.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        listP.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(listP);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton back = new JButton("Back");
        footer.add(back);
        JButton proceedB = new JButton("Next");
        footer.add(proceedB);
        footer.setMaximumSize(new Dimension(32767, footer.getPreferredSize().height));
        add(footer);

//LISTENERS
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int ans = JOptionPane.showConfirmDialog(
                        SwingUtilities.getWindowAncestor(back),
                        "Are you sure?\nYour booking will be canceled.",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (ans == 0) {
                    MainWindowC parent = (MainWindowC)SwingUtilities.getWindowAncestor(back);
                    try {
                        dbhand.removeBookingTotally(bookingNumber);
                        JOptionPane.showMessageDialog(parent, "Booking cancelled!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        parent.backFromPassengers();
                    } catch (SQLException exception) {
                        JOptionPane.showMessageDialog(parent, "Unexpected error, please try later!",
                                "Fail", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        proceedB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((MainWindowC)SwingUtilities.getWindowAncestor(proceedB)).proceedToPayment(bookingNumber);
            }
        });
    }

    private class OnePassenger extends JPanel{
        Seat seat;
        private JTextField nameField;
        private JTextField surnameField;
        private JTextField passportField;
        public JLabel label;

        public OnePassenger(Seat seat) {
            this.seat = seat;

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            //b.setPreferredSize(new Dimension(100, 100));

            add(Box.createHorizontalGlue());

            label = new JLabel(seat.getText());
            label.setFont(new Font(null, Font.PLAIN, 18));
            label.setPreferredSize(new Dimension(40, label.getPreferredSize().height));
            add(label);

            add(Box.createRigidArea(new Dimension(15, 0)));

            add(new JLabel("First name:"));
            this.nameField = inputField();

            add(Box.createRigidArea(new Dimension(15, 0)));

            add(new JLabel("Last name:"));
            this.surnameField = inputField();

            add(Box.createRigidArea(new Dimension(15, 0)));

            add(new JLabel("Passport:"));
            this.passportField = inputField();

            add(Box.createHorizontalGlue());
        }

        private JTextField inputField() {
            add(Box.createRigidArea(new Dimension(5, 0)));
            JTextField b = new JTextField(12);
            b.setMaximumSize(new Dimension(100, 100));
            b.setBorder(BorderFactory.createLoweredBevelBorder());
            b.setFont(new Font(null, Font.PLAIN, 14));
            add(b);
            return b;
        }

        public String getFirstName() {
            return nameField.getText();
        }
        public String getLastName() {
            return surnameField.getText();
        }
        public String getPassport() {
            return passportField.getText();
        }
    }
}
