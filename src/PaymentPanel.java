import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

//import static javax.swing.GroupLayout.Alignment.LEADING;

public class PaymentPanel extends JPanel {
    ArrayList<Seat> seats;

    public PaymentPanel(int bookingNumber) {
        DatabaseHandler dbhand = new DatabaseHandler();
        this.seats = dbhand.seatsInBooking(bookingNumber);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel nameL = new JLabel("Payment details");
        nameL.setFont(new Font(null, Font.PLAIN, 24));
        nameL.setAlignmentX(CENTER_ALIGNMENT);
        add(nameL);
        add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel commentL = new JLabel("Please check your order and enter your credit card details");
        commentL.setFont(new Font(null, Font.PLAIN, 14));
        commentL.setAlignmentX(CENTER_ALIGNMENT);
        add(commentL);

        add(Box.createVerticalGlue());

        JPanel twoSections = new JPanel();
        twoSections.setLayout(new BoxLayout(twoSections, BoxLayout.X_AXIS));
        add(twoSections);

        twoSections.add(Box.createHorizontalGlue());

        JPanel leftP = new JPanel();
        leftP.setLayout(new BoxLayout(leftP, BoxLayout.Y_AXIS));
        leftP.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),"Your order"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        leftP.add(new JLabel("Booking "+bookingNumber));
        leftP.add(Box.createRigidArea(new Dimension(0, 5)));
        double sum = 0;
        for (Seat i : seats) {
            leftP.add(new JLabel(i.flight + "  " +i.getText() + "  " + i.price));
            leftP.add(Box.createRigidArea(new Dimension(0, 5)));
            sum += i.price;
        }
        JLabel total = new JLabel("Total: %.2f".formatted(sum));
        total.setFont(new Font(null, Font.BOLD, 14));
        leftP.add(total);

        twoSections.add(leftP);

        twoSections.add(Box.createHorizontalGlue());

        Font inpFont = new Font(null, Font.PLAIN, 14);
        JLabel cardL = new JLabel("Card number:");
        JTextField cardF = new JTextField(15);
        cardF.setFont(inpFont);
        cardF.setBorder(BorderFactory.createLoweredBevelBorder());
        JLabel holderNameL = new JLabel("Name on card:");
        JTextField holderNameF = new JTextField(15);
        holderNameF.setBorder(BorderFactory.createLoweredBevelBorder());
        holderNameF.setFont(inpFont);
        JLabel cvvL = new JLabel("CVV:");
        JTextField cvvF = new JTextField(3);
        cvvF.setBorder(BorderFactory.createLoweredBevelBorder());
        cvvF.setFont(inpFont);
        JLabel dateL = new JLabel("Expires:");
        JLabel separatorL = new JLabel("/ ");
        JTextField monthF = new JTextField(2);
        monthF.setBorder(BorderFactory.createLoweredBevelBorder());
        monthF.setFont(inpFont);
        JTextField yearF = new JTextField(2);
        yearF.setBorder(BorderFactory.createLoweredBevelBorder());
        yearF.setFont(inpFont);

        JCheckBox registerCB = new JCheckBox();
        registerCB.setBorder(BorderFactory.createEmptyBorder());
        JLabel registerL = new JLabel("Save my data:");
        JButton logInB = new JButton("I have an account");

        JPanel rightP = new JPanel();
        rightP.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY), "Credit card details"),
                BorderFactory.createEmptyBorder(10, 10, 10 , 10)));
        //rightP.setMaximumSize(new Dimension(0, 32767));
        GroupLayout lo = new GroupLayout(rightP);
        rightP.setLayout(lo);
        lo.setAutoCreateGaps(true);
        lo.setAutoCreateContainerGaps(true);

        if (false) {
            // For users who are logged in.
            lo.setHorizontalGroup(lo.createSequentialGroup()
                    .addGroup(lo.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(cardL)
                            .addComponent(holderNameL)
                            .addComponent(dateL)
                    )
                    .addGroup(lo.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(cardF)
                            .addComponent(holderNameF)
                            .addGroup(lo.createSequentialGroup()
                                    .addComponent(monthF)
                                    .addComponent(separatorL)
                                    .addComponent(yearF)
                                    .addComponent(cvvL)
                                    .addComponent(cvvF)
                            )
                    )
            );

            lo.setVerticalGroup(lo.createSequentialGroup()
                    .addGroup(lo.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                            .addComponent(cardL)
                            .addComponent(cardF)
                    )
                    .addGroup(lo.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                            .addComponent(holderNameL)
                            .addComponent(holderNameF)
                    )
                    .addGroup(lo.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                            .addComponent(dateL)
                            .addComponent(monthF)
                            .addComponent(separatorL)
                            .addComponent(yearF)
                            .addComponent(cvvL)
                            .addComponent(cvvF)
                    )
            );
        } else {
            // For users who aren't logged in.
            lo.setHorizontalGroup(lo.createSequentialGroup()
                    .addGroup(lo.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(registerL)
                            .addComponent(cardL)
                            .addComponent(holderNameL)
                            .addComponent(dateL)
                    )
                    .addGroup(lo.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addGroup(lo.createSequentialGroup()
                                    .addComponent(registerCB)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
                                            GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(logInB)
                            )
                            .addComponent(cardF)
                            .addComponent(holderNameF)
                            .addGroup(lo.createSequentialGroup()
                                    .addComponent(monthF)
                                    .addComponent(separatorL)
                                    .addComponent(yearF)
                                    .addComponent(cvvL)
                                    .addComponent(cvvF)
                            )
                    )
            );

            lo.setVerticalGroup(lo.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(lo.createSequentialGroup()
                            .addGroup(lo.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(registerL)
                                    .addComponent(registerCB)
                                    .addComponent(logInB)
                            )
                            .addGroup(lo.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(cardL)
                                    .addComponent(cardF)
                            )
                            .addGroup(lo.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(holderNameL)
                                    .addComponent(holderNameF)
                            )
                            .addGroup(lo.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(dateL)
                                    .addComponent(monthF)
                                    .addComponent(separatorL)
                                    .addComponent(yearF)
                                    .addComponent(cvvL)
                                    .addComponent(cvvF)
                            )
                    )
            );
        }


        twoSections.add(rightP);

        twoSections.add(Box.createHorizontalGlue());

        add(Box.createVerticalGlue());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton back = new JButton("Back");
        footer.add(back);
        JButton confirmB = new JButton("Confirm payment");
        footer.add(confirmB);
        footer.setMaximumSize(new Dimension(32767, footer.getPreferredSize().height));
        add(footer);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((MainWindowC)SwingUtilities.getWindowAncestor(back)).paymentToPassengers();
                dbhand.removePassengersFromDB(bookingNumber);
            }
        });

    }
}
