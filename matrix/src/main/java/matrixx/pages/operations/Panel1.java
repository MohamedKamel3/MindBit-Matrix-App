package matrixx.pages.operations;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import static matrixx.SoundPlayer.playSound;
import static matrixx.pages.home_page.HomePage.ShowHomePage;
import static matrixx.pages.home_page.HomePage.homefram;

public class Panel1 implements ActionListener {

    public static JPanel Panel1;
    public static JLabel label;
    public static JComboBox<String> matrixCountBox;
    public static JButton startButton;
    public static JButton backButton;

    public Panel1() {
        // إنشاء JPanel وإعداد التخطيط باستخدام GridBagLayout
        Panel1 = new JPanel();
        Panel1.setLayout(new GridBagLayout()); // تعيين التخطيط إلى GridBagLayout
        Panel1.setBounds(0, 0, 400, 300);
        Panel1.setBackground(Color.decode("#101a43")); // لون الخلفية

        // إعداد النص
        label = new JLabel("Number of matrices");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.WHITE);

        // صندوق اختيار عدد المصفوفات
        matrixCountBox = new JComboBox<>(new String[]{"2", "3"});

        // زر البدء
        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.addActionListener(this);

        // زر الرجوع
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.addActionListener(this);

        // إعدادات الموضع لكل عنصر داخل GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // المسافة بين العناصر

        // إضافة JLabel في الصف الأول
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        Panel1.add(label, gbc);

        // إضافة JComboBox في الصف الثاني
        gbc.gridy = 1;
        Panel1.add(matrixCountBox, gbc);

        // إضافة زر Back في الصف الثالث، العمود الأول
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        Panel1.add(backButton, gbc);

        // إضافة زر Start في الصف الثالث، العمود الثاني
        gbc.gridx = 1;
        Panel1.add(startButton, gbc);
    }

    public static void ShowFrame1() {
        
        Panel1 hgf = new Panel1();
                // تشغيل الصوت في خيط منفصل بحيث لا يؤثر على تحميل الصفحة
        new Thread(() -> {
        playSound("matrix/src/main/java/sound/Operations_Page.wav"); // تأكد من استبدال "null" بمسار أو اسم الملف الصوتي الصحيح
        }).start();
        // إعداد إطار homefram
        homefram.setSize(400, 300);
        homefram.setLayout(new GridBagLayout()); // استخدام GridBagLayout لمركزية Panel1 في homefram
        homefram.setTitle("Matrix Calculator");

        // إعدادات الموضع لـ Panel1 داخل homefram
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        homefram.add(Panel1, gbc); // إضافة Panel1 إلى homefram
        Panel1.setVisible(true); // فعل العرض ل�� Panel1

    }

    public static void HideFrame1() {
        Panel1.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            HideFrame1();
            // احصل على عدد المصفوفات المختار
            int matrixCount = Integer.parseInt((String) matrixCountBox.getSelectedItem());

            // انتقل إلى صفحة Fram2 وأرسل عدد المصفوفات
            Panel2.ShowFrame2(matrixCount);
        } else if (e.getSource() == backButton) {
            HideFrame1();
            // ارجع إلى الصفحة الرئيسية
            ShowHomePage();
        }
    }
}
