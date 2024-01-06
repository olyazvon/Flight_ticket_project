import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class TestWindow extends JFrame {
    private final SelectPanel sp;

    public TestWindow() {
        setSize(500, 500);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel pa = new JPanel();
        pa.setLayout(new BoxLayout(pa, BoxLayout.X_AXIS));
        add(pa);
        pa.add(Box.createHorizontalGlue());
        pa.add(new SelectPanel("Static"));
        this.sp = new SelectPanel("Name");
        pa.add(sp);
        pa.add(Box.createHorizontalGlue());
        JButton btn = new JButton("hide");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sp.disappear();
            }
        });
        pa.add(btn);
        btn = new JButton("show");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sp.appear();
            }
        });
        pa.add(btn);

    }



    public static void main(String[] args) {
        TestWindow win = new TestWindow();
        win.setVisible(true);
    }
}