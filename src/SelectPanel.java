import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectPanel extends JPanel {
    Dimension normalSize;
    int growShrinkStep = 25;
    public SelectPanel(String name) {
        GroupLayout lo = new GroupLayout(this);
        setLayout(lo);
        lo.setAutoCreateGaps(true);
        lo.setAutoCreateContainerGaps(true);

        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("dd.MM.yyyy");
        dateSettings.setFormatForDatesBeforeCommonEra("dd.MM.uuuu");

        JTable table = new JTable(25, 5);
        table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(
                new Dimension(scrollPane.getPreferredSize().width, 350));

        JLabel nameL = new JLabel(name);
        nameL.setMinimumSize(new Dimension(0,0));
        DatePicker dateP = new DatePicker(dateSettings);
        dateP.setMinimumSize(new Dimension(0,0));
        TimePicker timeP = new TimePicker();
        timeP.setMinimumSize(new Dimension(0,0));
        JButton searchB = new JButton("Search");
        searchB.setMinimumSize(new Dimension(0,0));

        lo.setHorizontalGroup(lo.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(lo.createSequentialGroup()
                        .addComponent(nameL,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(dateP,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(timeP,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchB,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE))
                .addComponent(scrollPane,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
        );
        lo.setVerticalGroup(lo.createSequentialGroup()
                .addGroup(lo.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(nameL,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(dateP,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(timeP,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchB,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE))
                .addComponent(scrollPane,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
        );
    }

    public void disappear() {
        if (normalSize == null) {
            normalSize = getPreferredSize();
        }
        Timer timer = new Timer(1, null);
        timer.addActionListener(new ActionListener(){
            int stepsToDisappear = (int)Math.ceil((float)normalSize.width / growShrinkStep);
            @Override
            public void actionPerformed(ActionEvent e) {
                setMaximumSize(new Dimension(
                        getMaximumSize().width-growShrinkStep,
                        getMaximumSize().height
                ));
                setPreferredSize(getMaximumSize());
                stepsToDisappear--;
                if (stepsToDisappear <= 0) {
                    timer.stop();
                    System.out.println("stop");
                }
                //repaint();
                revalidate();
                //repaint();
            }
        });
        timer.start();
    }

    public void appear() {
        Timer timer = new Timer(1, null);
        timer.addActionListener(new ActionListener(){
            int stepsToGrow = (int)Math.ceil((float)normalSize.width / growShrinkStep);
            @Override
            public void actionPerformed(ActionEvent e) {
                setMaximumSize(new Dimension(
                        getMaximumSize().width+growShrinkStep,
                        getMaximumSize().height
                ));
                setPreferredSize(getMaximumSize());
                stepsToGrow--;
                if (stepsToGrow <= 0) {
                    timer.stop();
                    System.out.println("stop");
                }
                //repaint();
                revalidate();
                //repaint();
            }
        });
        timer.start();
    }
}