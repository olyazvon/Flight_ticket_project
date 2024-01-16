import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class SearchPanel extends JPanel {

    public SearchPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        DatabaseHandler dbhand = new DatabaseHandler();

//INTERFACE

//Log In button
        JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loginB = new JButton("Log in");
        header.add(loginB);
        JButton signUpB = new JButton("Sign up");
        header.add(signUpB);
        JButton payBookingB = new JButton("Pay for booking");
        header.add(payBookingB);
        header.setMaximumSize(new Dimension(32767, header.getPreferredSize().height));
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
                ((MainWindowC)parent).proceedToSeats(
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

        payBookingB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
}
