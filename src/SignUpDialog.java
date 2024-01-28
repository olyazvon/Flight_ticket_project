import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SignUpDialog extends JDialog {

    //private static final long serialVersionUID = 1L;

    public JTextField tfLogin, tfPassword, tfRepeat;
    public JButton btnOk;
    public String result;

    public JFrame parent;

    public SignUpDialog(JFrame parent) {

        super(parent, "Sign Up");
        this.parent = parent;

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
                result = null;
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
        JPanel passwordToCheck = new JPanel();
        passwordToCheck.setLayout(new BoxLayout(passwordToCheck, BoxLayout.X_AXIS));
        JLabel passwrdToCheckLabel = new JLabel("Repeat:", SwingConstants.RIGHT);
        passwordToCheck.add(passwrdToCheckLabel);
        passwordToCheck.add(Box.createHorizontalStrut(12));
        tfRepeat = new JPasswordField(15);
        tfRepeat.setFont(new Font(null, Font.PLAIN, 14));
        tfRepeat.setBorder(BorderFactory.createLoweredBevelBorder());
        passwordToCheck.add(tfRepeat);

        // Panel for buttons
        JPanel flow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnOk = new JButton("Sign up");
        //btnCancel = new JButton("Cancel");
        flow.add(btnOk);
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    result = DatabaseHandler.SignUp(tfLogin.getText(),
                            tfPassword.getText(), tfRepeat.getText());
                    dispose();
                } catch (RuntimeException exception) {
                    JOptionPane.showMessageDialog(parent, exception.getMessage(),
                            "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        // Label size
        nameLabel.setPreferredSize(new Dimension(65, 20));
        passwrdLabel.setPreferredSize(new Dimension(65, 20));
        passwrdToCheckLabel.setPreferredSize(new Dimension(65, 20));
        // Interface
        panel.add(name);
        panel.add(Box.createVerticalStrut(12));
        panel.add(password);
        panel.add(Box.createVerticalStrut(12));
        panel.add(passwordToCheck);
        panel.add(Box.createVerticalStrut(12));
        panel.add(flow);
        //return
        return panel;
    }




}
