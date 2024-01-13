import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import java.time.LocalDate;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class SelectPanel extends JPanel {
    Dimension normalSize;
    int growShrinkStep = 25;
    JComboBox<String> iataFrom;
    JComboBox<String> iataTo;
    LocalDate minDate = LocalDate.now();
    LocalDate maxDate = LocalDate.MAX;
    DatePickerSettings dateSettings = new DatePickerSettings();
    SelectPanel earlierPanel;
    SelectPanel laterPanel;
    DatePicker dateP;
    JTable table;

    public SelectPanel(String name, JComboBox<String> iataFromSource, JComboBox<String> iataToSource) {
        iataFrom = iataFromSource;
        iataTo = iataToSource;
        DatabaseHandler dbhand = new DatabaseHandler();
        GroupLayout lo = new GroupLayout(this);
        setLayout(lo);
        lo.setAutoCreateGaps(true);
        lo.setAutoCreateContainerGaps(true);

        dateSettings.setFormatForDatesCommonEra("dd.MM.yyyy");
        dateSettings.setFormatForDatesBeforeCommonEra("dd.MM.uuuu");
        FlightTableModel tabMod = new FlightTableModel(dbhand.search_flights(dbhand.q_search_flights(new String[]{"Any iata"},new String[]{"Any iata"},null)));
        table = new JTable(tabMod);
        //table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(30);
        TableColumn column;
        for (int i = 0; i < 5; i++) {
            column = table.getColumnModel().getColumn(i);
            switch (i) {
                case (0):
                    column.setPreferredWidth(64);
                    break;
                case (1):
                    column.setPreferredWidth(90);
                    break;
                case (2):
                    column.setPreferredWidth(105);
                    break;
                case (3):
                    column.setPreferredWidth(105);
                    break;
                case (4):
                    column.setPreferredWidth(100);
                    break;
            }
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(
                new Dimension(550, 350));

        JLabel nameL = new JLabel(name);
        nameL.setMinimumSize(new Dimension(0,0));

        dateP = new DatePicker(dateSettings);
        dateSettings.setDateRangeLimits(minDate, maxDate);
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

        dateP.addDateChangeListener(new DateChangeListener() {
            @Override
            public void dateChanged(DateChangeEvent dateChangeEvent) {
                if (earlierPanel != null) earlierPanel.setMaxDate(dateP.getDate());
                if (laterPanel != null) laterPanel.setMinDate(dateP.getDate());
            }
        });

        searchB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] arrayFrom;
                if (iataFrom.getSelectedItem().equals("Any iata")) {
                    ArrayList<String> allItems = new ArrayList<>();
                    int size = iataFrom.getItemCount();
                    for (int i = 0; i < size; i++) {
                        String item = iataFrom.getItemAt(i);
                        allItems.add(item);
                    }
                    arrayFrom = allItems.toArray(new String[iataFrom.getItemCount()]);
                } else {
                    arrayFrom = new String[] {(String) iataFrom.getSelectedItem()};
                }

                String[] arrayTo;
                if (iataTo.getSelectedItem().equals("Any iata")) {
                    ArrayList<String> allItems = new ArrayList<>();
                    int size = iataTo.getItemCount();
                    for (int i = 0; i < size; i++) {
                        String item = iataTo.getItemAt(i);
                        allItems.add(item);
                    }
                    arrayTo = allItems.toArray(new String[iataTo.getItemCount()]);
                } else {
                    arrayTo = new String[] {(String) iataTo.getSelectedItem()};
                }
                System.out.println(Arrays.toString(arrayFrom));
                tabMod.data = dbhand.search_flights(dbhand.q_search_flights(arrayFrom, arrayTo, dateP.getDate()));
                tabMod.fireTableDataChanged();
            }
        });
    }

    public void setMaxDate(LocalDate newMax) {
        maxDate = newMax;
        dateSettings.setDateRangeLimits(dateSettings.getDateRangeLimits().firstDate, newMax);
    }

    public void setMinDate(LocalDate newMin) {
        minDate = newMin;
        dateSettings.setDateRangeLimits(newMin, dateSettings.getDateRangeLimits().lastDate);
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
                    dateP.setDate(null);
                    earlierPanel.setMaxDate(LocalDate.MAX);
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

    private class FlightTableModel extends AbstractTableModel {
        private String[] columnNames =
                {"<html><p style='text-align:center'>Flight<br>number</p></html>",
                        "From → To", "Departure", "Arrival", "Prices",
                        "<html>Economy<br>seats left</html>",
                        "<html>Business<br>seats left</html>"};
        private ArrayList[] data;

        public FlightTableModel(ArrayList[] data) {
            this.data = data;
        }

        @Override
        public int getRowCount() {
            return data[0].size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case (0):
                    return data[0].get(rowIndex);
                case (1):
                    return data[1].get(rowIndex).toString()+"→ "+data[2].get(rowIndex).toString();
                case (2):
                    return data[3].get(rowIndex);
                case (3):
                    return data[4].get(rowIndex);
                case (4):
                    return data[5].get(rowIndex).toString()+"-"+data[6].get(rowIndex).toString();
                case (5):
                    return data[7].get(rowIndex);
                default:
                    return data[8].get(rowIndex);
            }
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }
    }

    public String getFlight() {
        if (table.getSelectedRow() == -1) return null;
        return (String) table.getValueAt(table.getSelectedRow(),0);
    }
}