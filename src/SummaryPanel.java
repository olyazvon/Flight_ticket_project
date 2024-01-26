import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class SummaryPanel extends JPanel {
    public SummaryPanel(int booking, String passport) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        DatabaseHandler dbhand = new DatabaseHandler();

        ArrayList<String[]> data;
        if (passport == null) {
            data = dbhand.myNextFlights(booking);
        } else {
            data = dbhand.myNextFlights(passport);
        }

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
        for (String[] i : data) {
            if (!i[2].equals(currentFlight)) {
                JLabel flightL = new JLabel(
                        "<html><b>Flight:</b> " + i[2]+" &emsp "+i[3]+" &emsp <b>Departure:</b> "+i[4]+" &emsp <b>Duration:</b> "+i[5]+"</html>", JLabel.CENTER);
                flightL.setFont(new Font(null, Font.PLAIN, 18));
                flightL.setAlignmentX(CENTER_ALIGNMENT);
                listP.add(Box.createRigidArea(new Dimension(0, 20)));
                listP.add(flightL);
                listP.add(Box.createRigidArea(new Dimension(0, 10)));
                currentFlight = i[2];
            }
            JLabel seatData = new JLabel("<html><b>Passenger:</b> " + i[0] + " &emsp <b>Seat:</b> " + i[1] + " &emsp <b>Class:</b> " + i[6] + "</html>", JLabel.CENTER);
            seatData.setFont(new Font(null, Font.PLAIN, 18));
            seatData.setAlignmentX(CENTER_ALIGNMENT);
            listP.add(seatData);
            listP.add(Box.createRigidArea(new Dimension(0, 10)));
        }

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
