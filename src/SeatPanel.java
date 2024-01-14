import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class SeatPanel extends JPanel {

    DatabaseHandler dbhand;
    public SeatPanel(String flightThere, String flightBack) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        dbhand = new DatabaseHandler();

//INTERFACE
        add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel nameL = new JLabel("Please select your seats");
        nameL.setFont(new Font(null, Font.PLAIN, 24));
        nameL.setAlignmentX(CENTER_ALIGNMENT);
        add(nameL);

        add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel threeSections = new JPanel();
        threeSections.setLayout(new BoxLayout(threeSections, BoxLayout.X_AXIS));

//Legend
        JPanel legend = new JPanel();
        legend.setLayout(new BoxLayout(legend, BoxLayout.Y_AXIS));
        legend.setMaximumSize(new Dimension(160, 190));
        legend.setPreferredSize(new Dimension(160, 190));
        legend.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        JPanel occ = new JPanel(new FlowLayout(FlowLayout.LEFT));;
        occ.add(new Seat("1A", 0.0, true, "Business", "AA1111", null));
        occ.add(new JLabel("Occupied"));
        JPanel bc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Seat bcs = new Seat("1A", 0.0, false, "Business", "AA1111", null);
        bcs.setDemo();
        bc.add(bcs);
        bc.add(new JLabel("Business"));
        JPanel ec = new JPanel(new FlowLayout(FlowLayout.LEFT));;
        Seat ecs = new Seat("1A", 0.0, false, "Economy", "AA1111", null);
        ecs.setDemo();
        ec.add(ecs);
        ec.add(new JLabel("Economy"));
        JPanel sel = new JPanel(new FlowLayout(FlowLayout.LEFT));;
        Seat sels = new Seat("1A", 0.0, false, "Economy", "AA1111", null);
        sels.setBackground(new Color(184, 207, 229));
        sels.setDemo();
        sel.add(sels);
        sel.add(new JLabel("Your selection"));
        legend.add(Box.createVerticalGlue());
        legend.add(occ);
        legend.add(bc);
        legend.add(ec);
        legend.add(sel);
        legend.add(Box.createVerticalGlue());

        threeSections.add(legend);

        threeSections.add(Box.createHorizontalGlue());

        Accountant total = new Accountant();
        SeatMapFrame a = new SeatMapFrame(flightThere, total);
        threeSections.add(a);

        threeSections.add(Box.createHorizontalGlue());

        if (!Objects.equals(flightBack, "")) {
            SeatMapFrame b = new SeatMapFrame(flightBack, total);
            threeSections.add(b);

            threeSections.add(Box.createHorizontalGlue());
        }

        threeSections.add(total);
        threeSections.add(Box.createRigidArea(new Dimension(5, 0)));

        add(threeSections);
        add(Box.createVerticalGlue());

//Back and Next buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton back = new JButton("Back");
        footer.add(back);
        JButton proceedB = new JButton("Next");
        footer.add(proceedB);
        footer.setMaximumSize(new Dimension(32767, footer.getPreferredSize().height));
        add(footer);

//LISTENERS
        proceedB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (total.seats.isEmpty()) {
                    Window parent = SwingUtilities.getWindowAncestor(proceedB);
                    JOptionPane.showMessageDialog(parent, "Seats not selected!",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int bookNum = dbhand.Book(total.seats);
                ((MainWindowC) SwingUtilities.getWindowAncestor(back)).proceedToPassengers(bookNum);
            }
        });
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                ((MainWindowC)SwingUtilities.getWindowAncestor(back)).backToSearch();
            }
        });
    }

    private class Accountant extends JLabel implements ActionListener {
        public double sum;
        public ArrayList<Seat> seats = new ArrayList<>();
        private int fixedWidth = 160;

        public Accountant() {
            super("<html>Your selection is empty<br>Total: 0</html>");
            setPreferredSize(new Dimension(fixedWidth, 40));
            setMaximumSize(new Dimension(fixedWidth, 40));
            Border borderA = BorderFactory.createLineBorder(Color.BLACK, 1);
            Border borderB = BorderFactory.createEmptyBorder(5, 5, 5, 1);
            setBorder(BorderFactory.createCompoundBorder(borderA, borderB));
            setBackground(new Color(210, 240, 210));
            setOpaque(true);
        }
        public void actionPerformed(ActionEvent event) {
            if (((Seat)event.getSource()).isSelected()) {
                seats.add((Seat)event.getSource());
                sum = 0;
                for (Seat i : seats) {
                    sum += i.price;
                }
            } else {
                seats.remove((Seat)event.getSource());
                sum = 0;
                for (Seat i : seats) {
                    sum += i.price;
                }
            }
            seats.sort(Comparator.comparing(s -> s.flight));
            setText("<html>Your selection:<br>");
            for (Seat i : seats) {
                setText(getText()+i.flight+" "+dbhand.qFromTo(i.flight)+" "+i.getText()+"<br>");
            }
            setText(getText()+"Total: %.2f</html>".formatted(sum));
            setPreferredSize(null);
            setMaximumSize(new Dimension(fixedWidth, getPreferredSize().height));
            setPreferredSize(new Dimension(fixedWidth, 40));
        }
    }

    private class SeatMap extends JPanel {
        public SeatMap(ArrayList<String> seats, ArrayList<Double> prices, ArrayList<Boolean> occupied, ArrayList<String> seatClasses,String flight, Accountant total) {
            super();
            setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

            if (seats.size() < 100) {
                //2+2
                setLayout(new GridLayout(seats.size()/4, 5, 4, 4));
                for (int i = 0; i < seats.size(); i++) {
                    if ((i - 2) % 4 == 0) {
                        add(new JLabel());
                    }
                    add(new Seat(seats.get(i), prices.get(i), occupied.get(i), seatClasses.get(i), flight, total));
                }
            } else if (seats.size() < 200) {
                //3+3
                setLayout(new GridLayout(seats.size()/6, 7, 5, 5));
                for (int i = 0; i < seats.size(); i++) {
                    if ((i - 3) % 6 == 0) {
                        add(new JLabel());
                    }
                    add(new Seat(seats.get(i), prices.get(i), occupied.get(i), seatClasses.get(i), flight, total));
                }
            } else if (seats.size() < 300) {
                //2+4+2
                setLayout(new GridLayout(seats.size()/8, 10, 5, 5));
                for (int i = 0; i < seats.size(); i++) {
                    if ((i - 2) % 8 == 0 || (i - 6) % 8 == 0) {
                        add(new JLabel());
                    }
                    add(new Seat(seats.get(i), prices.get(i), occupied.get(i), seatClasses.get(i), flight, total));
                }
            } else {
                //3+3+3
                setLayout(new GridLayout(seats.size()/9, 11, 5, 5));
                for (int i = 0; i < seats.size(); i++) {
                    if ((i - 3) % 9 == 0 || (i - 6) % 9 == 0) {
                        add(new JLabel());
                    }
                    add(new Seat(seats.get(i), prices.get(i), occupied.get(i), seatClasses.get(i), flight, total));
                }
            }
        }
    }

    private class SeatMapFrame extends JScrollPane {
        SeatMap seatMap;

        public SeatMapFrame(String flight, Accountant total) {

            super();
            this.seatMap = new SeatMap(
                    dbhand.read_seats_for_flight(flight),
                    dbhand.prices_for_flight(flight),
                    dbhand.occupied(flight),
                    dbhand.read_typeClass_for_flight(flight),
                    flight,
                    total);
            setViewportView(seatMap);

            JLabel fromToL = new JLabel(dbhand.qFromTo(flight));
            fromToL.setHorizontalAlignment(SwingConstants.CENTER);
            fromToL.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            setColumnHeaderView(fromToL);
            setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            setMaximumSize(getPreferredSize());
        }

    }
}
