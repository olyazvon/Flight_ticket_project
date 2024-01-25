import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class SearchPanel extends JPanel {

    public JPanel header;
    public JLabel usernameL;
    public JButton loginB;
    public JButton logoutB;
    public JButton signUpB;
    public JButton myFlightB;
    public DatabaseHandler dbhand;

    public SearchPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        dbhand = new DatabaseHandler();

//INTERFACE

//Header
        header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        loginB = new JButton("Log in");
        signUpB = new JButton("Sign up");
        logoutB = new JButton("Log out");
        usernameL = new JLabel("");
        usernameL.setBorder(BorderFactory.createEmptyBorder(1,10,1,10));
        myFlightB = new JButton("My flight");
        header.add(myFlightB);
        header.add(loginB);
        header.add(signUpB);
        header.setMaximumSize(new Dimension(Short.MAX_VALUE, header.getPreferredSize().height));
        add(header);

//Caption
        JLabel nameL = new JLabel("Our cool ticket search engine");
        nameL.setFont(new Font(null, Font.PLAIN, 30));
        nameL.setAlignmentX(CENTER_ALIGNMENT);
        add(nameL);

        add(Box.createVerticalGlue());

//From, To, Two ways
        JPanel fromToP = new JPanel();
        fromToP.add(new JLabel("From:"));

        JComboBox<String> Countries_from = new JComboBox<>(dbhand.Allcountries());
        fromToP.add(Countries_from);

        String[] cities = dbhand.Cities("","");
        JComboBox<String> Cities_from = new JComboBox<>(cities);
        Cities_from.setPrototypeDisplayValue(longestString(cities));
        fromToP.add(Cities_from);

        JComboBox<String> IATA_from = new JComboBox<>(dbhand.IATAs("","",""));
        fromToP.add(IATA_from);

        fromToP.add(Box.createRigidArea(new Dimension(10, 0)));

        fromToP.add(new JLabel("To:"));
        JComboBox<String> Countries_to = new JComboBox<>(dbhand.Allcountries());
        fromToP.add(Countries_to);

        JComboBox<String> Cities_to = new JComboBox<>(cities);
        Cities_to.setPrototypeDisplayValue(longestString(cities));
        fromToP.add(Cities_to);

        JComboBox<String> IATA_to = new JComboBox<>(dbhand.IATAs("","",""));
        fromToP.add(IATA_to);

        fromToP.add(Box.createRigidArea(new Dimension(10, 0)));

        JCheckBox twoWaysCB = new JCheckBox("Two ways ticket", true);
        fromToP.add(twoWaysCB);

        add(fromToP);

//Selection panels

        JPanel twoSelectors = new JPanel();
        twoSelectors.setLayout(new BoxLayout(twoSelectors, BoxLayout.X_AXIS));
        SelectPanel selectP1 = new SelectPanel("There:", IATA_from, IATA_to);
        SelectPanel selectP2 = new SelectPanel("Back:", IATA_to, IATA_from);
        selectP1.laterPanel = selectP2;
        selectP2.earlierPanel = selectP1;
        twoSelectors.add(selectP1);
        twoSelectors.add(selectP2);
        add(twoSelectors);

//Proceed button
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton proceedB = new JButton("Proceed with selected");
        footer.add(proceedB);
        footer.setMaximumSize(new Dimension(32767, footer.getPreferredSize().height));
        add(footer);

//LISTENERS

        twoWaysCB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //recursiveEnabling(selectP2, twoWaysCB.isSelected());
                if (twoWaysCB.isSelected()) {
                    selectP2.appear();
                } else {
                    selectP2.disappear();
                }
            }
        });

        loginB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

            }
        });

        proceedB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Window parent = SwingUtilities.getWindowAncestor(proceedB);
                if (selectP1.getFlight() == null ||
                        (twoWaysCB.isSelected() && selectP2.getFlight() == null)) {
                    JOptionPane.showMessageDialog(parent, "Flight not selected!",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (twoWaysCB.isSelected() &&
                        !matchFromTo(dbhand.qFromTo(selectP1.getFlight()),
                                dbhand.qFromTo(selectP2.getFlight()))) {
                    JOptionPane.showMessageDialog(parent, "Airports do not match!",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (twoWaysCB.isSelected() && dbhand.getArrival(selectP1.getFlight())
                        .after(dbhand.getDeparture(selectP2.getFlight()))) {
                    JOptionPane.showMessageDialog(parent, "Back before there!",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                ((MainWindowC)parent).searchToSeats(
                        selectP1.getFlight(),
                        twoWaysCB.isSelected() ? selectP2.getFlight() : ""
                );
            }
        });

        Countries_from.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateCitiesBox(Countries_from, Cities_from,
                        (String) Cities_to.getSelectedItem(), dbhand);
                updateIATAbox(Countries_from, Cities_from, IATA_from,
                        (String) Cities_to.getSelectedItem(), dbhand);
                updateCitiesBox(Countries_to, Cities_to,
                        (String) Cities_from.getSelectedItem(), dbhand);
            }
        });

        Cities_from.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Cities_from.hasFocus()) {
                    updateIATAbox(Countries_from, Cities_from, IATA_from,
                            (String) Cities_to.getSelectedItem(), dbhand);
                    updateCitiesBox(Countries_to, Cities_to,
                            (String) Cities_from.getSelectedItem(), dbhand);
                    updateIATAbox(Countries_to, Cities_to, IATA_to,
                            (String) Cities_from.getSelectedItem(), dbhand);
                }
            }
        });

        IATA_to.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (IATA_to.hasFocus()) {
                    Cities_to.setSelectedItem(
                            dbhand.cityByIATA((String) IATA_to.getSelectedItem()));
                    updateIATAbox(Countries_from, Cities_from, IATA_from,
                            (String) Cities_to.getSelectedItem(), dbhand);
                    updateCitiesBox(Countries_from, Cities_from,
                            dbhand.cityByIATA((String) IATA_to.getSelectedItem()), dbhand);
                }
            }
        });

        Countries_to.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateCitiesBox(Countries_to, Cities_to,
                        (String) Cities_from.getSelectedItem(), dbhand);
                updateIATAbox(Countries_to, Cities_to, IATA_to,
                        (String) Cities_from.getSelectedItem(), dbhand);
                updateCitiesBox(Countries_from, Cities_from,
                        (String) Cities_to.getSelectedItem(), dbhand);
            }
        });

        Cities_to.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Cities_to.hasFocus()) {
                    updateIATAbox(Countries_to, Cities_to, IATA_to,
                            (String) Cities_from.getSelectedItem(), dbhand);
                    updateCitiesBox(Countries_from, Cities_from,
                            (String) Cities_to.getSelectedItem(), dbhand);
                    updateIATAbox(Countries_from, Cities_from, IATA_from,
                            (String) Cities_to.getSelectedItem(), dbhand);
                }
            }
        });

        IATA_from.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (IATA_from.hasFocus()) {
                    Cities_from.setSelectedItem(
                            dbhand.cityByIATA((String)IATA_from.getSelectedItem()));
                    updateIATAbox(Countries_to, Cities_to, IATA_to,
                            (String) Cities_from.getSelectedItem(), dbhand);
                    updateCitiesBox(Countries_to, Cities_to,
                            dbhand.cityByIATA((String) IATA_from.getSelectedItem()), dbhand);
                }
            }
        });

        loginB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindowC parent = (MainWindowC)SwingUtilities.getWindowAncestor(loginB);
                LoginDialog lid = new LoginDialog(parent);
                if (lid.resultLogin == null) {return;}
                parent.loggedIn = lid.resultLogin;
                JOptionPane.showMessageDialog(parent, "You are logged in!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                headerLoggedIn(lid.resultLogin);
//                if (lid.resultBooking != 0) {
//                    haveBooking(lid.resultBooking, parent);
//                }
            }
        });

        logoutB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindowC parent = (MainWindowC)SwingUtilities.getWindowAncestor(logoutB);
                parent.loggedIn = null;
                headerLoggedIn(null);
                parent.transferFocus();
            }
        });

        signUpB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindowC parent = (MainWindowC)SwingUtilities.getWindowAncestor(signUpB);
                SignUpDialog sud = new SignUpDialog(parent);
                String result = sud.result;
                if (result == null) {return;}
                parent.loggedIn = result;
                headerLoggedIn(result);
                JOptionPane.showMessageDialog(parent, "You are signed up!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        myFlightB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindowC parent = (MainWindowC)SwingUtilities.getWindowAncestor(myFlightB);
                String res = JOptionPane.showInputDialog(parent,
                        "Please enter your passport");
                if (res.isEmpty()) {return;}
                parent.searchToSummary(res);
            }
        });

    }

    private String longestString(String[] input) {
        String tmp = "";
        for (String i : input) {
            if (i.length() > tmp.length()) {
                tmp = i;
            }
        }
        return tmp;
    }

    private boolean matchFromTo(String one, String two) {
        String[] oneSpl = one.split(" - ");
        String[] twoSpl = two.split(" - ");
        System.out.println(Arrays.toString(oneSpl) + "   " + Arrays.toString(twoSpl));
        return Objects.equals(oneSpl[0], twoSpl[1]) && Objects.equals(oneSpl[1], twoSpl[0]);
    }

    private void updateIATAbox(JComboBox<String> countries, JComboBox<String> cities,
                               JComboBox<String> iatas, String cityToExclude,
                               DatabaseHandler dbh) {

        String SelectedCountry = (String) countries.getSelectedItem();
        String SelectedCity = (String) cities.getSelectedItem();

        Object selectedElement = iatas.getSelectedItem();

        String[] IATAs = dbh.IATAs(SelectedCountry, SelectedCity, cityToExclude);

        iatas.removeAllItems();
        for (String j : IATAs) {
            iatas.addItem(j);
        }
        iatas.setSelectedItem(selectedElement);
    }

    private void updateCitiesBox(JComboBox<String> countries, JComboBox<String> cities,
                                 String cityToExclude, DatabaseHandler dbh) {
        String[] citiesToShow = dbh.Cities(
                (String) countries.getSelectedItem(), cityToExclude);
        Object selectedElement = cities.getSelectedItem();
        cities.removeAllItems();
        for (String i:citiesToShow) {
            cities.addItem(i);
        }
        cities.setSelectedItem(selectedElement);
    }

    public void haveBooking(int booking, MainWindowC parent) {
        Object[] options = {"Pay for booking", "Clear booking", "Log out"};
        int res = JOptionPane.showOptionDialog(parent,
                "You have a booking.\n" +
                        "According to our rules, it's only possible\n" +
                        "to have one active booking.\n" +
                        "Do you want to pay for it now?",
                "Your booking",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (res == 0) {
            parent.searchToPassengers(booking);
        } else if (res == 1) {
            try {
                dbhand.removeBookingTotally(booking);
                JOptionPane.showMessageDialog(parent, "Booking removed!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException exception) {
                JOptionPane.showMessageDialog(parent, "Unexpected error, please try later!",
                        "Fail", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            parent.loggedIn = null;
            headerLoggedIn(null);
            parent.transferFocus();
        }
    }

    public void headerLoggedIn(String login) {
        if (login != null) {
            header.removeAll();
            usernameL.setText(login);
            header.add(usernameL);
            header.add(myFlightB);
            header.add(logoutB);
            header.revalidate();
            header.repaint();
            MainWindowC parent = (MainWindowC)SwingUtilities.getWindowAncestor(this);
            if (dbhand.getUsersBooking(login) != 0) {
                haveBooking(dbhand.getUsersBooking(login), parent);
            }
        } else {
            header.removeAll();
            usernameL.setText("");
            header.add(myFlightB);
            header.add(loginB);
            header.add(signUpB);
            header.revalidate();
            header.repaint();
        }
    }

}
