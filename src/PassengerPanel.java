import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        JLabel booking = new JLabel("Your booking number:"+bookingNumber);
        add(booking);

        add(Box.createVerticalGlue());

        for (Seat seat : this.seats) {
            OnePassenger pss = new OnePassenger(seat);
            this.passengers.add(pss);
            add(pss);
            add(Box.createRigidArea(new Dimension(0, 15)));
        }

        int maxW = 0;
        for (OnePassenger i : this.passengers) {
             if (i.label.getPreferredSize().width > maxW) {
                 maxW = i.label.getPreferredSize().width;
             }
        }
        int H = this.passengers.get(0).label.getPreferredSize().height;
        for (OnePassenger i : this.passengers) {
            i.label.setPreferredSize(new Dimension(maxW+5, H));
        }

        add(Box.createVerticalGlue());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(new JLabel("You can save your booking number and complete this and subsequent steps later."));
        footer.add(new JButton("Continue later"));
        JButton back = new JButton("Back");
        footer.add(back);
        JButton proceedB = new JButton("Next");
        footer.add(proceedB);
        footer.setMaximumSize(new Dimension(32767, footer.getPreferredSize().height));
        add(footer);

//LISTENERS
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(dbhand.UnBook(bookingNumber));
                ((MainWindowC)SwingUtilities.getWindowAncestor(back)).backToSeats();
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

            label = new JLabel(seat.flight+" "+dbhand.qFromTo(seat.flight)+" "+seat.getText());
            label.setFont(new Font(null, Font.PLAIN, 18));
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
