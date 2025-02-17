package matrixx.pages.determinant;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import static matrixx.SoundPlayer.playSound;
import static matrixx.pages.determinant.CalcDet.showCalcPage;
import static matrixx.pages.home_page.HomePage.ShowHomePage;
import static matrixx.pages.home_page.HomePage.homefram;
import matrixx.tools.RoundedButton;

public class DeterminantPage implements ActionListener {

    public static JLabel determinantLabel;
    public static final JPanel determinantPanel = new JPanel();
    public static RoundedButton startButton;
    public static RoundedButton backButton;
    public static JComboBox<String> mDropdown;
    // public  static JComboBox<String> nDropdown;

    public DeterminantPage() {
        int width = 120;
        int height = 50;

        // إعداد عنوان الصفحة
        determinantLabel = new JLabel("Determinant", JLabel.CENTER);
        determinantLabel.setFont(new Font("Arial", Font.BOLD, 36));
        determinantLabel.setForeground(Color.WHITE);
        determinantLabel.setBounds(150, 30, 300, 50);

        // إعداد اللوحة الرئيسية للأزرار والقوائم المنسدلة
        determinantPanel.setBounds(70, 100, 450, 400);
        determinantPanel.setLayout(new GridLayout(2, 2, 10, 10)); // 3 صفوف، 2 أعمدة مع فجوات
        determinantPanel.setBackground(Color.decode("#101a43"));
        determinantPanel.setBorder(null);

        // إعداد القوائم المنسدلة m و n
        String[] options = {"2", "3", "4"};
        mDropdown = new JComboBox<>(options);
        // mDropdown.setPreferredSize(new Dimension(100, 30)); // تحديد الحجم
        mDropdown.setBackground(Color.white);
        mDropdown.setFont(new Font("Arial", Font.BOLD, 20));

        // إعداد الأزرار
        startButton = new RoundedButton("Start", new Dimension(width, height), new Color(173, 216, 230), 20);
        startButton.setForeground(Color.decode("#101a43"));
        backButton = new RoundedButton("Back", new Dimension(width, height), new Color(173, 216, 230), 20);
        backButton.setForeground(Color.decode("#101a43"));
        startButton.setFocusable(false);
        backButton.setFocusable(false);

        // إضافة المستمعين للأزرار
        startButton.addActionListener(this);
        backButton.addActionListener(this);

        JLabel numequations = new JLabel("Number of M and N:");
        numequations.setForeground(Color.white);
        numequations.setFont(new Font("Arial", Font.BOLD, 17));
        // إضافة القوائم المنسدلة والأزرار إلى اللوحة
        determinantPanel.add(numequations, JLabel.CENTER);
        determinantPanel.add(mDropdown);
        determinantPanel.add(backButton);
        determinantPanel.add(startButton);
    }

    public static void show() {
        // تشغيل الصوت في خيط منفصل
        new Thread(new Runnable() {
            @Override
            public void run() {
                playSound("matrix/src/main/java/sound/Determinant_page.wav");
            }
        }).start();

        // باقي الأوامر
        homefram.setSize(600, 500);
        homefram.setLayout(null);
        determinantLabel.setVisible(true);
        mDropdown.setVisible(true);
        startButton.setVisible(true);
        backButton.setVisible(true);
        determinantPanel.setVisible(true);
        homefram.add(determinantLabel);
        homefram.add(determinantPanel);
    }

    public void hideDeterminantPage() {
        determinantLabel.setVisible(false);
        determinantPanel.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            hideDeterminantPage();
            // الحصول على حجم المصفوفة من القوائم المنسدلة
            int m = Integer.parseInt((String) mDropdown.getSelectedItem());

            // إنشاء صفحة حساب المحدد وعرضها
            showCalcPage(m, m);
        } else if (e.getSource() == backButton) {
            hideDeterminantPage();
            ShowHomePage();
        }
    }

}
