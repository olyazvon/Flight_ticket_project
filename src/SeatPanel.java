import javax.swing.*;
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

        JLabel nameL = new JLabel("Select your seats, please");
        nameL.setFont(new Font(null, Font.PLAIN, 24));
        nameL.setAlignmentX(CENTER_ALIGNMENT);
        add(nameL);

        add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel threeSections = new JPanel();
        threeSections.setLayout(new BoxLayout(threeSections, BoxLayout.X_AXIS));

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
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                ((MainWindowC)SwingUtilities.getWindowAncestor(back)).backToSearch();
            }
        });
    }

    private String[] qSeats(String flight) {
        String[] a = {"1A", "1B", "1C", "1D", "2A", "2B", "2C", "2D", "3A", "3B", "3C", "3D",
                "4A", "4B", "4C", "4D", "5A", "5B", "5C", "5D", "6A", "6B", "6C", "6D"};
        return a;
    }

    private double[] qPrices(String flight) {
        double[] a = {200, 200, 200, 200, 50, 50, 50, 50, 50, 50, 50, 50,
                50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50};
        return a;
    }

    private boolean[] qOccupied(String flight) {
        boolean[] a = {true, false, false, false,
                false, false, true, true,
                false, true, false, false,
                true, false, false, false,
                false, false, true, true,
                false, true, false, false};
        return a;
    }

    public String qFromTo(String flight) {
        if (Objects.equals(flight, "123")) {
            return "DME-TLV";
        }
        return "TLV-DME";
    }

    private class Accountant extends JLabel implements ActionListener {
        public double sum;
        public ArrayList<Seat> seats = new ArrayList<Seat>();

        public Accountant() {
            super("<html>Your selection:<br>Total: 0</html>");
            setPreferredSize(new Dimension(120, 32767));
            setMaximumSize(new Dimension(120, 32767));
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
                setText(getText()+i.flight+" "+qFromTo(i.flight)+" "+i.getText()+"<br>");
            }
            setText(getText()+"Total: %s</html>".formatted(sum));
        }
    }

    private class Seat extends JToggleButton {
        public double price;
        public String flight;
        public Seat(String name, double price, boolean occupied, String flight, ActionListener listener) {
            super(name);
            setMargin(new Insets(1, 0, 1, 0));
            this.price = price;
            this.flight = flight;
            setBackground(new Color(210, 240, 210));
            addActionListener(listener);
            if (occupied) {
                setEnabled(false);
                setBackground(Color.white);
            }
            setPreferredSize(new Dimension(30,30));
        }
    }

    private class SeatMap extends JPanel {
        public SeatMap(ArrayList<String> seats, ArrayList<Double> prices, ArrayList<Boolean> occupied, String flight, Accountant total) {
            super();
            setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

            if (seats.size() < 100) {
                //2+2
                setLayout(new GridLayout(seats.size()/4, 5, 4, 4));
                for (int i = 0; i < seats.size(); i++) {
                    if ((i - 2) % 4 == 0) {
                        add(new JLabel());
                    }
                    add(new Seat(seats.get(i), prices.get(i), occupied.get(i), flight, total));
                }
            } else if (seats.size() < 200) {
                //3+3
                setLayout(new GridLayout(seats.size()/6, 7, 5, 5));
                for (int i = 0; i < seats.size(); i++) {
                    if ((i - 3) % 6 == 0) {
                        add(new JLabel());
                    }
                    add(new Seat(seats.get(i), prices.get(i), occupied.get(i), flight, total));
                }
            } else if (seats.size() < 300) {
                //2+4+2
                setLayout(new GridLayout(seats.size()/8, 10, 5, 5));
                for (int i = 0; i < seats.size(); i++) {
                    if ((i - 2) % 8 == 0 || (i - 6) % 8 == 0) {
                        add(new JLabel());
                    }
                    add(new Seat(seats.get(i), prices.get(i), occupied.get(i), flight, total));
                }
            } else {
                //3+3+3
                setLayout(new GridLayout(seats.size()/9, 11, 5, 5));
                for (int i = 0; i < seats.size(); i++) {
                    if ((i - 3) % 9 == 0 || (i - 6) % 9 == 0) {
                        add(new JLabel());
                    }
                    add(new Seat(seats.get(i), prices.get(i), occupied.get(i), flight, total));
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
