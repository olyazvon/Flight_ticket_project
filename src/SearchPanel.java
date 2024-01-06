import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchPanel extends JPanel {

    public SearchPanel() {
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
        add(Box.createVerticalGlue());
        JPanel fromToP = new JPanel();
        fromToP.add(new JLabel("From:"));
        fromToP.add(new JComboBox(new String[] {"Any country"}));
        fromToP.add(new JComboBox(new String[] {"Any city"}));
        fromToP.add(new JComboBox(new String[] {"Any airport"}));
        fromToP.add(Box.createRigidArea(new Dimension(10, 0)));
        fromToP.add(new JLabel("To:"));
        fromToP.add(new JComboBox(new String[] {"Any country"}));
        fromToP.add(new JComboBox(new String[] {"Any city"}));
        fromToP.add(new JComboBox(new String[] {"Any airport"}));
        fromToP.add(Box.createRigidArea(new Dimension(10, 0)));
        JCheckBox twoWaysCB = new JCheckBox("Two ways ticket", true);
        fromToP.add(twoWaysCB);
        add(fromToP);
        add(Box.createVerticalGlue());
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
