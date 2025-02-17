package matrixx.pages.home_page;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import static matrixx.SoundPlayer.playSound;
import matrixx.pages.Info_Page.Info;
import static matrixx.pages.Info_Page.Info.ShowInfoPage;
import matrixx.pages.Sorting.Matrix.MatrixInputPage;
import matrixx.pages.Sorting.Sorting;
import static matrixx.pages.Sorting.Sorting.ShowSortings;
import matrixx.pages.determinant.DeterminantPage;
import static matrixx.pages.determinant.DeterminantPage.show;
import matrixx.pages.history_Page.History;
import static matrixx.pages.history_Page.History.ShowHistoryPage;
import static matrixx.pages.history_Page.NewMatrixPage.ShowNewMatrixPage;
import matrixx.pages.operations.Panel1;
import static matrixx.pages.operations.Panel1.ShowFrame1;
import matrixx.pages.solve_equations.EquationInputPanel;
import matrixx.pages.solve_equations.EquationResultPanel;
import matrixx.pages.solve_equations.EquationSetupPanel;
import matrixx.tools.RoundedButton;

public class HomePage implements ActionListener {

    public static JFrame homefram;
    public static JPanel homePanel = new JPanel();
    public static JLabel titleLabel;
    public static EquationSetupPanel equationSetupPanel;
    public static EquationInputPanel equationInputPanel;
    public static EquationResultPanel equationResultPanel;
    static RoundedButton determinantButton;
    static RoundedButton SortingButton;
    static RoundedButton AddNewMatrixButton;
    static RoundedButton operationsButton;
    static RoundedButton solveEquationsButton;
    static RoundedButton historyButton; // زر History
    static RoundedButton Info; // زر Show Matrix
    public static JPanel subPanel;

    public HomePage() {
        DeterminantPage p = new DeterminantPage();
        Panel1 pa = new Panel1();
        History history = new History();
        Info page = new Info();
        Sorting pagee = new Sorting();
        MatrixInputPage pageee = new MatrixInputPage();

        int width = 230;
        int height = 80;

        // إعداد الإطار
        homefram = new JFrame("Matrix");
        homefram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homefram.setSize(500, 800);
        homefram.setLayout(null);
        homefram.setResizable(false);
        homefram.getContentPane().setBackground(Color.decode("#101a43"));
        homefram.setLocationRelativeTo(null);

        // إعداد عنوان الصفحة
        titleLabel = new JLabel("Matrix");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(200, 20, 100, 40);
        homefram.add(titleLabel);

        // إعداد اللوحة الرئيسية
        homePanel.setBounds(50, 165, 450, 700);
        homePanel.setLayout(new GridLayout(6, 1, 0, -10));
        homePanel.setBackground(Color.decode("#101a43"));

        // إعداد لوحة فرعية للزرين الجانبيين
        subPanel = new JPanel();
        subPanel.setBounds(-10, 65, 500, 100);
        subPanel.setLayout(new GridLayout(1, 2, -90, 0)); // ترتيب الزرين بجانب بعضهما
        subPanel.setBackground(Color.decode("#101a43"));

        // تهيئة الأزرار
        historyButton = new RoundedButton("History", new Dimension(150, 50), new Color(173, 216, 230), 20);
        historyButton.setForeground(Color.decode("#101a43"));
        Info = new RoundedButton("Info", new Dimension(150, 50), new Color(173, 216, 230), 20);
        Info.setForeground(Color.decode("#101a43"));
        determinantButton = new RoundedButton("Determinant", new Dimension(width, height), new Color(173, 216, 230), 20);
        determinantButton.setForeground(Color.decode("#101a43"));
        AddNewMatrixButton = new RoundedButton("Add New Matrix", new Dimension(width, height), new Color(173, 216, 230), 20);
        AddNewMatrixButton.setForeground(Color.decode("#101a43"));
        operationsButton = new RoundedButton("Operations", new Dimension(width, height), new Color(173, 216, 230), 20);
        operationsButton.setForeground(Color.decode("#101a43"));
        solveEquationsButton = new RoundedButton("Solve Equations", new Dimension(width, height), new Color(173, 216, 230), 20);
        solveEquationsButton.setForeground(Color.decode("#101a43"));
        SortingButton = new RoundedButton("Sorting", new Dimension(width, height), new Color(173, 216, 230), 20);
        SortingButton.setForeground(Color.decode("#101a43"));

        // إعداد خصائص الأزرار وإضافة مستمعات الأزرار
        historyButton.setFocusable(false);
        Info.setFocusable(false);
        determinantButton.setFocusable(false);
        AddNewMatrixButton.setFocusable(false);
        operationsButton.setFocusable(false);
        solveEquationsButton.setFocusable(false);
        SortingButton.setFocusable(false);

        historyButton.addActionListener(this);
        Info.addActionListener(this);
        determinantButton.addActionListener(this);
        AddNewMatrixButton.addActionListener(this);
        operationsButton.addActionListener(this);
        solveEquationsButton.addActionListener(this);
        SortingButton.addActionListener(this);

        // إضافة الزرين إلى اللوحة الفرعية
        subPanel.add(historyButton);
        subPanel.add(Info);

        // إضافة اللوحة الفرعية والأزرار الأخرى إلى اللوحة الرئيسية
        homePanel.add(AddNewMatrixButton);
        homePanel.add(determinantButton);
        homePanel.add(operationsButton);
        homePanel.add(solveEquationsButton);
        homePanel.add(SortingButton);

        // إضافة اللوحة الرئيسية إلى الإطار
        homefram.add(homePanel);
        homefram.add(subPanel);
        homefram.setVisible(true);

        // تهيئة لوحة إعداد المعادلات
        equationSetupPanel = new EquationSetupPanel((m, n) -> showEquationInputPanel(m, n), e -> ShowHomePage());
        equationSetupPanel.setBounds(50, 60, 400, 400);
        equationSetupPanel.setVisible(false);
        homefram.add(equationSetupPanel);
    }

    public static void showEquationSetupPanel() {
        new Thread(() -> {
            playSound("matrix/src/main/java/sound/Solve_NEquation_page.wav"); // تأكد من استبدال "null" بمسار أو اسم الملف الصوتي الصحيح
        }).start();
        homePanel.setVisible(false);
        homefram.setSize(500, 500);
        equationSetupPanel.setVisible(true);
        if (equationInputPanel != null) {
            equationInputPanel.setVisible(false);
        }
        if (equationResultPanel != null) {
            equationResultPanel.setVisible(false);
        }
    }

    public static void HideHomePage() {
        homePanel.setVisible(false);
        subPanel.setVisible(false);
        titleLabel.setVisible(false);
        historyButton.setVisible(false);
        Info.setVisible(false);
    }

    public static void showEquationInputPanel(int m, int n) {
        equationInputPanel = new EquationInputPanel(m, n,
                (results, coefficientFields) -> showEquationResultPanel(results, coefficientFields),
                e -> showEquationSetupPanel());

        equationSetupPanel.setVisible(false);

        equationInputPanel.setBounds(50, 60, 400, 400);
        homefram.add(equationInputPanel);

        equationInputPanel.setVisible(true);
        if (equationResultPanel != null) {
            equationResultPanel.setVisible(false);
        }
    }

    public static void showEquationResultPanel(double[] results, JTextField[][] coefficientFields) {
        equationResultPanel = new EquationResultPanel(results, coefficientFields,
                e -> showEquationInputPanel(coefficientFields.length, coefficientFields[0].length - 1), e -> ShowHomePage());
        equationResultPanel.setBounds(50, 60, 400, 400);
        homefram.add(equationResultPanel);

        equationInputPanel.setVisible(false);
        equationResultPanel.setVisible(true);
    }

    public static void ShowHomePage() {
        homefram.setSize(500, 800);
        homefram.setTitle("Matrix");
        homefram.setLayout(null);

        historyButton.setVisible(true);
        Info.setVisible(true);
        subPanel.setVisible(true);
        homePanel.setVisible(true);
        titleLabel.setVisible(true);
        SortingButton.setVisible(true);

        homefram.add(subPanel);
        homefram.add(homePanel);
        homefram.add(titleLabel);

        if (equationSetupPanel != null) {
            equationSetupPanel.setVisible(false);
        }
        if (equationInputPanel != null) {
            equationInputPanel.setVisible(false);
        }
        if (equationResultPanel != null) {
            equationResultPanel.setVisible(false);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == historyButton) {
            HideHomePage();
            ShowHistoryPage();

        }
        if (e.getSource() == Info) {
            HideHomePage();
            ShowInfoPage();
        }
        if (e.getSource() == determinantButton) {
            HideHomePage();
            show();
        }
        if (e.getSource() == operationsButton) {
            HideHomePage();
            ShowFrame1();
        }
        if (e.getSource() == solveEquationsButton) {
            HideHomePage();
            showEquationSetupPanel();
        }
        if (e.getSource() == AddNewMatrixButton) {
            HideHomePage();
            ShowNewMatrixPage(ignored -> ShowHomePage()); // تمرير ShowHomePage كدالة
        }
        if (e.getSource() == SortingButton) {
            HideHomePage();
            ShowSortings();
        }

    }
}
