import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class SearchPanel extends JPanel {

    public SearchPanel() throws SQLException, ClassNotFoundException {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

//INTERFACE

//Log In button
        JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loginB = new JButton("Log in");
        header.add(loginB);
        header.setMaximumSize(new Dimension(32767, header.getPreferredSize().height));
        add(header);

//Caption
        JLabel nameL = new JLabel("Our cool ticket search engine");
        nameL.setFont(new Font(null, Font.PLAIN, 30));
        nameL.setAlignmentX(CENTER_ALIGNMENT);
        add(nameL);

        add(Box.createVerticalGlue());

//From, To, Two ways
        DatabaseHandler dbhand = new DatabaseHandler();
        add(Box.createVerticalGlue());
        JPanel fromToP = new JPanel();
        fromToP.add(new JLabel("From:"));
        JComboBox Countries_from=new JComboBox(dbhand.Allcountries());
        fromToP.add(Countries_from);
        String[] cities= dbhand.Cities("","");
        JComboBox Cities_from=new JComboBox(cities);
//        ActionListener al = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String SelectedCountry = (String) Countries_from.getSelectedItem();
//                Cities_from.removeAllItems();
//                if (SelectedCountry.equals(Const.AIRPORTS_COUNTRY+"=\'Any country\'")){
//                    SelectedCountry="";
//                }
//                String[] cities= new String[0];
//                try {
//                    cities =dbhand.Cities(SelectedCountry,"");
//                } catch (ClassNotFoundException ex) {
//                    throw new RuntimeException(ex);
//                }
//                System.out.println(Arrays.toString(cities));
//                for (String i:cities) {
//                    Cities_from.addItem(i);
//                }
//            }
//        };
//        Countries_from.addActionListener(al);
        fromToP.add(Cities_from);
        JComboBox IATA_from= new JComboBox(dbhand.IATAs("","","SVO"));
        System.out.println(Arrays.toString(dbhand.IATAs("", "", "SVO")));
        ActionListener alIATA_from = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String SelectedCountry = (String) Countries_from.getSelectedItem();
                String SelectedCity = (String) Cities_from.getSelectedItem();
                Cities_from.removeAllItems();
                IATA_from.removeAllItems();
                if (SelectedCountry.equals(Const.AIRPORTS_COUNTRY + "=\'Any country\'")) {
                    SelectedCountry = "";
                }
                if (SelectedCity.equals(Const.AIRPORTS_COUNTRY + "=\'Any city\'")) {
                    SelectedCity = "";}
                String[]cities=new String[0];
                try {
                    cities =dbhand.Cities(SelectedCountry,"");
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                String[] IATAs = new String[0];
                try {
                    IATAs = dbhand.IATAs(SelectedCountry, dbhand.Cities(SelectedCountry,"")[0], "");
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println(Arrays.toString(IATAs));
                for (String i : cities) {
                    Cities_from.addItem(i);}
                for (String j : IATAs) {
                        IATA_from.addItem(j);}
            }
        };
        Countries_from.addActionListener(alIATA_from);
        //Cities_from.addActionListener(alIATA_from);
        //IATA_from.addActionListener(alIATA_from);
        fromToP.add(Cities_from);
        fromToP.add(IATA_from);

        fromToP.add(Box.createRigidArea(new Dimension(10, 0)));
        fromToP.add(new JLabel("To:"));
        fromToP.add(new JComboBox(dbhand.read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_COUNTRY,"")));
        fromToP.add(new JComboBox(dbhand.read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_CITY,"")));
        fromToP.add(new JComboBox(dbhand.read_distinct_column(Const.AIRPORT_TABLE, Const.AIRPORTS_ID,"")));
        fromToP.add(Box.createRigidArea(new Dimension(10, 0)));
        JCheckBox twoWaysCB = new JCheckBox("Two ways ticket", true);
        fromToP.add(twoWaysCB);
        add(fromToP);

//Selection panels

        JPanel twoSelectors = new JPanel();
        twoSelectors.setLayout(new BoxLayout(twoSelectors, BoxLayout.X_AXIS));
        SelectPanel selectP1 = new SelectPanel("There:");
        twoSelectors.add(selectP1);
        SelectPanel selectP2 = new SelectPanel("Back:");
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
                ((MainWindowC)SwingUtilities.getWindowAncestor(proceedB)).proceedToSeats(
                        "123", twoWaysCB.isSelected() ? "321" : ""
                );
            }
        });
    }
}
