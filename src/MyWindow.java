import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyWindow extends JFrame {
    public MyWindow(String name) {
        super(name);
        //setSize(300, 200);
        setExtendedState(MAXIMIZED_BOTH);
        //setTitle(name);


        //Toolkit toolkit = getToolkit();
        //Dimension size = toolkit.getScreenSize();
        //setLocation(size.width/2 - getWidth()/2,
        //        size.height/2 - getHeight()/2);

        JPanel panel = new JPanel();
        getContentPane().add(panel);
        panel.setLayout(null);

        JButton close = new JButton("Open");
        close.setBounds(50, 20, 80, 30);
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                MyWindow wind2 = new MyWindow("Secondary window");
                wind2.setVisible(true);
            }
        });
        panel.add(close);
    }


    public static void main(String[] args) {
        MyWindow wind = new MyWindow("Main window");
        wind.setDefaultCloseOperation(EXIT_ON_CLOSE);
        wind.setVisible(true);
    }
}