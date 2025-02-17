package matrixx.pages.Info_Page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import static matrixx.pages.home_page.HomePage.ShowHomePage;
import static matrixx.pages.home_page.HomePage.homefram;

public class Info extends JFrame implements ActionListener {

    public static JEditorPane InfoEditorPane;
    public static JButton backButton;

    public static JPanel buttonPanel;

    public Info() {
        InfoEditorPane = new JEditorPane();
        InfoEditorPane.setContentType("text/html"); // تعيين نوع المحتوى كـ HTML
        InfoEditorPane.setEditable(false); // منع التحرير

        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setForeground(Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setOpaque(false);
        backButton.setBorderPainted(false);
        // backButton.addActionListener(this);
        backButton.setFocusable(false);
        backButton.setBounds(0, 0, 20, 20);
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode("#101a43"));
        buttonPanel.add(backButton);

        String htmlContent = """
            <html>
            <head>
                <style>
                    body {
                        background-color: #101a43;
                        color: #ffffff;
                        font-family: Arial, sans-serif, Alnaqaaa;
                        text-align: center;
                        padding: 20px;
                    }
                    .title {
                        font-size: 36px;
                        color: #b668aa;
                        font-weight: bold;
                        margin-bottom: 20px;
                    }
                    .title2 {
                        font-size: 25px;
                        color: #fffff;
                        font-weight: bold;
                        margin-bottom: 0px;
                    }

                    .Lead {
                        font-size: 20px;
                        color: #fffff;
                        font-weight: bold;
                        margin-bottom: 20px;
                    }
                    .instructions {
                        color: #ffffff;
                        text-align: left;
                        margin: 20px auto;
                        width: 85%;
                        font-size: 16px;
                    }
                    .instructions p {
                        margin-bottom: 15px;
                        line-height: 1.5;
                    }
                    .logo {
                        text-align: center;
                        margin: 0px auto;
                    }
                    .logo img {
                        width:30px; /* تعديل العرض لتصغير الشعار */
                        height: 30px; /* تعديل الارتفاع لتصغير الشعار */
                    }
                    .team {
                        color: #b668aa;
                        margin-top: 0px;
                        font-size: 18px;
                    }
                    .team p {
                        font-family: Alnaqaaa;
                        margin-bottom: 10px;
                    }
                </style>
            </head>
            <body>
                <div class="title">MindBit Matrix</div>
                <div class="title2">&#9755; instructions &#9754;</div>
        
                <div class="instructions">
                    <p><b>&#10003; Ensure the matrix is square when using: the determinant<br>the inverse <br>nth-degree matrix<b></p>
                    <p><b>&#10003; Ensure the number of columns in the first matrix equals the number of rows in the second when multiplying<b></p>
                    <p><b>&#10003; Ensure the number of equations is greater than or equal to the number of variables when solving equations<b></p>
                    <p><b>&#10003; You can use functions by letter like:<br> s → sin<br>c → cos<br>t&nbsp; → tan<br>q → sqrt<b></p>
                    <p><b>&#10003; You can use operations like<br> ( + , - , / , ! , ^ , % , * )<b></p>
                </div>
        
                <div class="logo">
                    <img src="file:matrix/logo2.png" alt="MindBitLogo">
                </div>
        
                <div class="team">
                    <p class="title">Team Members:</p>
                    <p class="Lead">Mostafa Shaddad</p>
                    <p>Moahmed Kamel</p>
                    <p>Abdulalem abdalfatah</p>
                    <p>Mahmoud Wahdan</p>
                    <p>Nasr Ehab</p>
                    <p>Mahmoud Ayman</p>
                    <p>Mohamed Waled</p>
                    <p>Omar Hany</p>
                    <p>Azza yahya</p>
                    <p>Seham kamel</p>
                    <p>saja mohamed</p>
                </div>
            </body>
            </html>
            """;
        InfoEditorPane.setText(htmlContent);

        // إضافة الـ JEditorPane إلى JScrollPane لدعم التمرير
        // JScrollPane scrollPane = new JScrollPane(InfoEditorPane);
        // scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0)); // إزالة الإطار حول الـ JScrollPane
        // scrollPane.setPreferredSize(new Dimension(600, 700)); // ملء الشاشة بالكامل
        backButton.addActionListener(e -> {
            HideInfoPage();
            ShowHomePage();
        });
    }

    public static void ShowInfoPage() {
        homefram.setSize(600, 700);
        homefram.setLayout(new BorderLayout()); // استخدام BorderLayout لملء الصفحة

        homefram.setTitle("Info Page");
        // backButton.setVisible(true);
        buttonPanel.setVisible(true);
        homefram.add(new JScrollPane(InfoEditorPane), BorderLayout.CENTER); // إضافة الـ JScrollPane إلى المركز
        // homefram.add(backButton, BorderLayout.SOUTH); // ��ضافة الزر الخلفي ��لى المركز
        homefram.add(buttonPanel, BorderLayout.SOUTH);

        homefram.revalidate(); // تحديث الإطار بعد إضافة المكونات
        homefram.repaint(); // إعادة رسم الإطار لتحديث العرض
    }

    public static void HideInfoPage() {
        try {
            // إخفاء جميع المكونات داخل الـ JFrame
            Component[] components = homefram.getContentPane().getComponents();
            for (Component component : components) {
                component.setVisible(false);
            }

            // إعادة تحديث الـ JFrame
            homefram.revalidate();

            // إعادة رسم الـ JFrame لتحديث العرض
            homefram.repaint();

        } catch (Exception e) {
            // طباعة الخطأ في حال حدوث استثناء
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
}
