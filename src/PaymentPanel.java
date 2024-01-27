import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

//import static javax.swing.GroupLayout.Alignment.LEADING;

public class PaymentPanel extends JPanel {
    ArrayList<Seat> seats;
    private JTextField cardF;
    private JTextField holderNameF;
    private JTextField cvvF;
    private JTextField monthF;
    private JTextField yearF;
    private String[] storedData;


    public PaymentPanel(int bookingNumber, String loggedIn) {
        this.seats = DatabaseHandler.seatsInBooking(bookingNumber);

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

        for (Seat i : seats) {
            leftP.add(new JLabel(i.flight + "  " +i.getText() + "  " + i.price));
            leftP.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        try {
            JLabel total = new JLabel("Total: %.2f".formatted(DatabaseHandler.totalSum(bookingNumber)));
            total.setFont(new Font(null, Font.BOLD, 14));
            leftP.add(total);
        } catch (SQLException e) {
            MainWindowC parent = (MainWindowC)SwingUtilities.getWindowAncestor(this);
            parent.paymentToPassengers();
            DatabaseHandler.removePassengersFromDB(bookingNumber);
            JOptionPane.showMessageDialog(parent, "Unexpected error, please try later!",
                    "Fail", JOptionPane.WARNING_MESSAGE);
        }

        twoSections.add(leftP);

        twoSections.add(Box.createHorizontalGlue());

        Font inpFont = new Font(null, Font.PLAIN, 14);
        JLabel cardL = new JLabel("Card number:");
        cardF = new JTextField(15);
        cardF.setFont(inpFont);
        cardF.setBorder(BorderFactory.createLoweredBevelBorder());
        JLabel holderNameL = new JLabel("Name on card:");
        holderNameF = new JTextField(15);
        holderNameF.setBorder(BorderFactory.createLoweredBevelBorder());
        holderNameF.setFont(inpFont);
        JLabel cvvL = new JLabel("CVV:");
        cvvF = new JTextField(3);
        cvvF.setBorder(BorderFactory.createLoweredBevelBorder());
        cvvF.setFont(inpFont);
        JLabel dateL = new JLabel("Expires:");
        JLabel separatorL = new JLabel("/ ");
        monthF = new JTextField(2);
        monthF.setBorder(BorderFactory.createLoweredBevelBorder());
        monthF.setFont(inpFont);
        yearF = new JTextField(2);
        yearF.setBorder(BorderFactory.createLoweredBevelBorder());
        yearF.setFont(inpFont);

        JCheckBox registerCB = new JCheckBox();
        registerCB.setBorder(BorderFactory.createEmptyBorder(8, 0, 6, 0));
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

        if (loggedIn != null) {
            // For users who are logged in.
            storedData = fillFields(loggedIn);
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


//LISTENERS
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((MainWindowC)SwingUtilities.getWindowAncestor(back)).paymentToPassengers();
                DatabaseHandler.removePassengersFromDB(bookingNumber);
            }
        });

        registerCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (registerCB.isSelected()) {
                    MainWindowC parent = (MainWindowC)(SwingUtilities.getWindowAncestor(registerCB));
                    SignUpDialog sud = new SignUpDialog(parent);
                    String result = sud.result;
                    if (result == null) {
                        registerCB.setSelected(false);
                        return;
                    }
                    parent.loggedIn = result;
                    logInB.setVisible(false);
                    registerCB.setEnabled(false);
                    parent.loggedIn = result;
                    JOptionPane.showMessageDialog(parent, "You are signed up!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        logInB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindowC parent = (MainWindowC)(SwingUtilities.getWindowAncestor(registerCB));
                LoginDialog lid = new LoginDialog(parent);
                String resultLogin = lid.resultLogin;
                if (resultLogin == null) {
                    return;
                }
                parent.loggedIn = resultLogin;
                JOptionPane.showMessageDialog(parent, "You are logged in!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                if (lid.resultBooking != 0) {
                    haveBooking(lid.resultBooking, parent);
                }
                if (parent.loggedIn != null) {
                    logInB.setVisible(false);
                    registerCB.setVisible(false);
                    registerL.setVisible(false);
                    storedData = fillFields(parent.loggedIn);
                }
            }
        });

        confirmB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindowC parent = (MainWindowC)(SwingUtilities.getWindowAncestor(confirmB));
                if (!(cardF.getText().length() == 16 &&
                        cvvF.getText().length() == 3 &&
                        !holderNameF.getText().isEmpty() &&
                        yearF.getText().length() == 2 &&
                        monthF.getText().length() == 2 &&
                        Integer.parseInt(yearF.getText())>=24 &&
                        Integer.parseInt(monthF.getText())>=1 &&
                        Integer.parseInt(monthF.getText())<=12 &&
                        Main.onlyNumbersInString(cardF.getText())  &&
                        Main.noNumbersInString(holderNameF.getText()) )) {
                    JOptionPane.showMessageDialog(parent, "Please check your data!");
                    return;
                }
                if (parent.loggedIn != null) {
                    if (registerCB.isSelected()) {
                        DatabaseHandler.saveCardDetails(
                                parent.loggedIn,
                                cardF.getText(),
                                cvvF.getText(),
                                LocalDate.of(2000+Integer.parseInt(yearF.getText()),
                                        Integer.parseInt(monthF.getText()), 1),
                                holderNameF.getText());
                    } else if (!(cardF.getText().equals(storedData[0]) &&
                            cvvF.getText().equals(storedData[1]) &&
                            holderNameF.getText().equals(storedData[2]) &&
                            yearF.getText().equals(storedData[3]) &&
                            monthF.getText().equals(storedData[4]))) {
                        int res = JOptionPane.showConfirmDialog(parent,
                                "Save your data?",
                                "Card Data",
                                JOptionPane.YES_NO_OPTION);
                        if (res == 0) {
                            DatabaseHandler.saveCardDetails(
                                    parent.loggedIn,
                                    cardF.getText(),
                                    cvvF.getText(),
                                    LocalDate.of(2000+Integer.parseInt(yearF.getText()),
                                            Integer.parseInt(monthF.getText()), 1),
                                    holderNameF.getText());
                        }
                    }
                }
                try {
                    DatabaseHandler.buy(bookingNumber);
                    JOptionPane.showMessageDialog(parent, "Bought");
                    parent.paymentToSummary(DatabaseHandler.myNextFlights(bookingNumber));
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(parent, "Error occurred, try later!",
                            "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private String[] fillFields(String login) {
        String[] data = DatabaseHandler.selectCardDetails(login);
        String[] processedData = new String[] {
                data[0] == null ? "" : data[0],
                data[1] == null ? "" : data[1],
                data[3] == null ? "" : data[3],
                data[2] == null ? "" : data[2].substring(2, 4),
                data[2] == null ? "" : data[2].substring(5, 7)
        };
        cardF.setText(processedData[0]);
        cvvF.setText(processedData[1]);
        holderNameF.setText(processedData[2]);
        yearF.setText(processedData[3]);
        monthF.setText(processedData[4]);
        return processedData;
    }

    public void haveBooking(int booking, MainWindowC parent) {
        Object[] options = {"Cancel booking", "Log out"};
        int res = JOptionPane.showOptionDialog(parent,
                "You already have a booking.\n" +
                        "According to our rules, it's only possible\n" +
                        "to have one active booking.\n" +
                        "Do you want to cancel it now?",
                "Your booking",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (res == 0) {
            try {
                DatabaseHandler.removeBookingTotally(booking);
                JOptionPane.showMessageDialog(parent, "Booking removed!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException exception) {
                JOptionPane.showMessageDialog(parent, "Unexpected error, please try later!",
                        "Fail", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            parent.loggedIn = null;
        }
    }
}
