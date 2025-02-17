package matrixx.pages.Sorting.Matrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import static matrixx.pages.Sorting.Sorting.ShowSortings;
import static matrixx.pages.home_page.HomePage.homefram;

public class MatrixInputPage implements ActionListener {

    public static JTextField mField;
    public static JTextField nField;
    public static JButton startButton;
    public static JButton backButton;
    public static JPanel mainPanel;
    public static JLabel titleLabel;
    public static JLabel mLabel;
    public static JLabel nLabel;

    public MatrixInputPage() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.decode("#101a43"));

        titleLabel = new JLabel("Sorting Matrix");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(150, 20, 200, 30);

        mLabel = new JLabel("M =");
        mLabel.setForeground(Color.WHITE);
        mLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mLabel.setBounds(100, 80, 50, 30);

        mField = new JTextField();
        mField.setBounds(150, 80, 150, 30);
        mField.setFont(new Font("Arial", Font.BOLD, 24));
        restrictToPositiveNumbers(mField); // تقييد الإدخال للأرقام فقط

        nLabel = new JLabel("n =");
        nLabel.setForeground(Color.WHITE);
        nLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nLabel.setBounds(100, 130, 50, 30);

        nField = new JTextField();
        nField.setBounds(150, 130, 150, 30);
        nField.setFont(new Font("Arial", Font.BOLD, 24));
        restrictToPositiveNumbers(nField); // تقييد الإدخال للأرقام فقط

        backButton = new JButton("Back");
        backButton.setBounds(100, 200, 100, 40);
        backButton.addActionListener(this);

        startButton = new JButton("Start");
        startButton.setBounds(250, 200, 100, 40);
        startButton.addActionListener(this);

        mainPanel.add(titleLabel);
        mainPanel.add(mLabel);
        mainPanel.add(mField);
        mainPanel.add(nLabel);
        mainPanel.add(nField);
        mainPanel.add(backButton);
        mainPanel.add(startButton);
    }

    public static void ShowMatrixInput() {
        homefram.setTitle("Matrix Input");
        homefram.setLayout(new BorderLayout());
        homefram.setSize(450, 350);

        homefram.add(mainPanel);
        mainPanel.setVisible(true);

        homefram.revalidate();
        homefram.repaint();
    }

    public static void HideAll() {
        try {
            Component[] components = homefram.getContentPane().getComponents();
            for (Component component : components) {
                component.setVisible(false);
            }
            homefram.revalidate();
            homefram.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            try {
                // التحقق من الحقول النصية
                if (mField != null && nField != null) {
                    String mText = mField.getText().trim();
                    String nText = nField.getText().trim();

                    // التأكد من عدم إدخال قيم فارغة
                    if (mText.isEmpty() || nText.isEmpty()) {
                        JOptionPane.showMessageDialog(mainPanel, "Please enter valid numbers for M and N.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return; // لا تفعل شيء آخر
                    }

                    int m = Integer.parseInt(mText);
                    int n = Integer.parseInt(nText);

                    // التأكد من أن القيم أكبر من الصفر
                    if (m <= 0 || n <= 0) {
                        JOptionPane.showMessageDialog(mainPanel, "M and N must be greater than 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return; // لا تفعل شيء آخر
                    }

                    // إذا كانت القيم صالحة، انتقل إلى صفحة المصفوفة
                    HideAll();
                    new Matrix(n, m);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainPanel, "Please enter valid integers for M and N!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == backButton) {
            mField.setText("");
            nField.setText("");
            HideAll();
            ShowSortings();
        }
    }

    private void restrictToPositiveNumbers(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = new StringBuilder(currentText).insert(offset, string).toString();

                if (isValidPositiveNumber(newText)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = new StringBuilder(currentText).replace(offset, offset + length, text).toString();

                if (isValidPositiveNumber(newText)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = new StringBuilder(currentText).delete(offset, offset + length).toString();

                if (isValidPositiveNumber(newText)) {
                    super.remove(fb, offset, length);
                }
            }

            private boolean isValidPositiveNumber(String text) {
                return text.isEmpty() || text.matches("[1-9][0-9]*");
            }
        });
    }

}
