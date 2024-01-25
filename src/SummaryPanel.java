import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SummaryPanel extends JPanel {
    public SummaryPanel(int booking, String passport) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        DatabaseHandler dbhand = new DatabaseHandler();

        add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel nameL = new JLabel("Summary");
        nameL.setFont(new Font(null, Font.PLAIN, 24));
        nameL.setAlignmentX(CENTER_ALIGNMENT);
        add(nameL);

        add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel listP = new JPanel();
        listP.setLayout(new BoxLayout(listP, BoxLayout.Y_AXIS));

        listP.add(Box.createVerticalGlue());

        String currentFlight = "";
//        for (Seat seat : this.seats) {
//            if (!seat.flight.equals(currentFlight)) {
//                JLabel flightL = new JLabel(seat.flight+"  "+dbhand.qFromTo(seat.flight));
//                flightL.setFont(new Font(null, Font.PLAIN, 18));
//                flightL.setAlignmentX(CENTER_ALIGNMENT);
//                listP.add(flightL);
//                listP.add(Box.createRigidArea(new Dimension(0, 15)));
//                currentFlight = seat.flight;
//            }
//            PassengerPanel.OnePassenger pss = new PassengerPanel.OnePassenger(seat);
//            this.passengers.add(pss);
//            listP.add(pss);
//            listP.add(Box.createRigidArea(new Dimension(0, 15)));
//        }

        listP.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(listP);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okB = new JButton("OK");
        footer.add(okB);
        footer.setMaximumSize(new Dimension(32767, footer.getPreferredSize().height));
        add(footer);

//LISTENERS
        okB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindowC parent = (MainWindowC)SwingUtilities.getWindowAncestor(okB);
                parent.summaryToSearch();
            }
        });

    }
}
