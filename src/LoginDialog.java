import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.*;

public class LoginDialog extends JDialog {

    //private static final long serialVersionUID = 1L;

    public JTextField tfLogin, tfPassword;
    public JButton btnOk;
    public String resultLogin;
    public int resultBooking;
    public JFrame parent;

    public LoginDialog(JFrame parent) {
        super(parent, "Log In");
        this.parent = parent;
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
                resultLogin = null;
            }
        });
        getContentPane().add(createGUI());
        setResizable(false);
        pack();
        setSize(getSize());
        setLocationRelativeTo(parent);
        setModal(true);
        setVisible(true);
    }

    private JPanel createGUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Panel for login
        JPanel name = new JPanel();
        name.setLayout(new BoxLayout(name, BoxLayout.X_AXIS));
        JLabel nameLabel = new JLabel("Login:", SwingConstants.RIGHT);
        name.add(nameLabel);
        name.add(Box.createHorizontalStrut(12));
        tfLogin = new JTextField(15);
        tfLogin.setFont(new Font(null, Font.PLAIN, 14));
        tfLogin.setBorder(BorderFactory.createLoweredBevelBorder());
        name.add(tfLogin);

      // Panel for password
        JPanel password = new JPanel();
        password.setLayout(new BoxLayout(password, BoxLayout.X_AXIS));
        JLabel passwrdLabel = new JLabel("Password:", SwingConstants.RIGHT);
        password.add(passwrdLabel);
        password.add(Box.createHorizontalStrut(12));
        tfPassword = new JPasswordField(15);
        tfPassword.setFont(new Font(null, Font.PLAIN, 14));
        tfPassword.setBorder(BorderFactory.createLoweredBevelBorder());
        password.add(tfPassword);
        // Panel for buttons
        JPanel flow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnOk = new JButton("Log in");
        //btnCancel = new JButton("Cancel");
        flow.add(btnOk);

        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    resultBooking = DatabaseHandler.SignIn(
                            tfLogin.getText(), tfPassword.getText());
                    resultLogin = tfLogin.getText();
                    dispose();
                } catch (RuntimeException exception) {
                    JOptionPane.showMessageDialog(parent, exception.getMessage(),
                            "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Label size

        nameLabel.setPreferredSize(new Dimension(63, 20));
        passwrdLabel.setPreferredSize(new Dimension(63, 20));

        // Interface
        panel.add(name);
        panel.add(Box.createVerticalStrut(12));
        panel.add(password);
        panel.add(Box.createVerticalStrut(12));
        panel.add(flow);
        // return
        return panel;
    }





}
