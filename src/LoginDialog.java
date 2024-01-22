import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.*;

public class LoginDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    public JTextField tfLogin, tfPassword;
    public JButton btnOk, btnCancel;

    public LoginDialog(JFrame parent) {
        super(parent, "Вход в систему");
        // При выходе из диалогового окна работа заканчивается
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
                System.exit(0);
            }
        });
        // добавляем расположение в центр окна
        getContentPane().add(createGUI());
        // задаем предпочтительный размер
        pack();
        // выводим окно на экран
        setVisible(true);
    }

    // этот метод будет возвращать панель с созданным расположением
    private JPanel createGUI() {
        // Создание панели для размещение компонентов
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // Определение отступов от границ ранели. Для этого используем пустую рамку
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        // Создание панели для размещения метки и текстового поля логина
        JPanel name = new JPanel();
        name.setLayout(new BoxLayout(name, BoxLayout.X_AXIS));
        JLabel nameLabel = new JLabel("Имя:");
        name.add(nameLabel);
        name.add(Box.createHorizontalStrut(12));
        tfLogin = new JTextField(15);
        name.add(tfLogin);
        // Создание панели для размещения метки и текстового поля пароля
        JPanel password = new JPanel();
        password.setLayout(new BoxLayout(password, BoxLayout.X_AXIS));
        JLabel passwrdLabel = new JLabel("Пароль:");
        password.add(passwrdLabel);
        password.add(Box.createHorizontalStrut(12));
        tfPassword = new JPasswordField(15);
        password.add(tfPassword);
        // Создание панели для размещения кнопок управления
        JPanel flow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        //JPanel grid = new JPanel(new GridLayout(1, 2, 5, 0));
        btnOk = new JButton("OK");
        btnCancel = new JButton("Отмена");
        flow.add(btnOk);
        flow.add(btnCancel);

        //flow.add(grid);
        // Выравнивание вложенных панелей по горизонтали
        JComponent[] components = new JComponent[]{name, password, panel, flow};
        for (int i = 0; i < components.length; i++) {
            components[i].setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        // Выравнивание вложенных панелей по вертикали

        JComponent[] components1 = new JComponent[]{tfLogin, tfPassword, nameLabel, passwrdLabel};
        for (int i = 0; i < components1.length; i++) {
            components1[i].setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        // Определение размеров надписей к текстовым полям

        JComponent[] components2 = new JComponent[]{nameLabel, passwrdLabel};

        // Установка компонентам одинаковых размеров
        for (int i = 0; i < components2.length; i++) {
            components2[i].setPreferredSize(new Dimension(60,20));
            components2[i].setMinimumSize(new Dimension(60,20));
            components2[i].setMaximumSize(new Dimension(60,20));
        }


        // Определение стандартного вида для кнопок

        JButton[] buttons = new JButton[]{btnOk, btnCancel};
//        // Устранение "бесконечной" высоты текстовых полей
//        for (int i = 0; i < buttons.length; i++) {
//            // Объект Insets хранит расстояние от текста до границ кнопки
//            Insets margin = buttons[i].getMargin();
//            margin.left = 12;
//            margin.right = 12;
//            buttons[i].setMargin(margin);
//        }


        Dimension size = tfLogin.getPreferredSize();
        //	чтобы текстовое поле по-прежнему могло увеличивать свой размер в длину
        size.width = tfLogin.getMaximumSize().width;
        //	Определение максимального размера текстового поля
        tfLogin.setMaximumSize(size);

        Dimension size1 = tfPassword.getPreferredSize();
        //	чтобы текстовое поле по-прежнему могло увеличивать свой размер в длину
        size1.width = tfPassword.getMaximumSize().width;
        //	Определение максимального размера текстового поля
        tfPassword.setMaximumSize(size1);

        // Сборка интерфейса
        panel.add(name);
        panel.add(Box.createVerticalStrut(12));
        panel.add(password);
        panel.add(Box.createVerticalStrut(17));
        panel.add(flow);
        // готово
        return panel;

    }
    // тестовый метод для проверки диалогового окна
    public static void main(String[] args) {
        new LoginDialog(new JFrame());
    }


    private static int maximumElementPosition(int[] array)
    {
        int maxPos = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array [maxPos])
                maxPos = i;
        }
        return maxPos;
    }
    }


