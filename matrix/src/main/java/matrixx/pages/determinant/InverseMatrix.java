package matrixx.pages.determinant;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InverseMatrix {

    public static void displayInverseMatrixFields(int size, JPanel detPanel, JButton backButton, JButton homeButton) {
        detPanel.removeAll(); // Clear previous components
        detPanel.setLayout(new GridLayout(size + 1, size, 5, 5)); // Create a new grid layout

        JTextField[][] matrixInputs = new JTextField[size][size]; // Create an array for text fields

        // Create input fields for the square matrix
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrixInputs[i][j] = new JTextField();
                matrixInputs[i][j].setHorizontalAlignment(JTextField.CENTER); // محاذاة النص في المنتصف
                matrixInputs[i][j].setPreferredSize(new Dimension(30, 30)); // تصغير حجم حقول النص
                detPanel.add(matrixInputs[i][j]); // Add the text field to the panel
            }
        }

        // إعداد زر "Calculate"
        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(e -> calculateInverseMatrix(matrixInputs, detPanel));
        detPanel.add(calculateButton); // إضافة زر "Calculate" إلى اللوحة

        // إعداد الأزرار الأخرى
        JPanel buttonPanel = new JPanel(); // استخدام JPanel بدون تخطيط محدد
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // استخدام FlowLayout لترتيب الأزرار أفقياً مع وجود مسافة
        buttonPanel.add(backButton);
        buttonPanel.add(homeButton);
        detPanel.add(buttonPanel); // إضافة لوحة الأزرار إلى detPanel

        detPanel.revalidate(); // Refresh the frame
        detPanel.repaint();
    }

    private static void calculateInverseMatrix(JTextField[][] matrixInputs, JPanel detPanel) {
        int size = matrixInputs.length;
        double[][] inputMatrix = new double[size][size];

        // تحويل المدخلات من JTextField إلى مصفوفة من الأعداد
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                inputMatrix[i][j] = Double.parseDouble(matrixInputs[i][j].getText());
            }
        }

        // حساب المصفوفة العكسية
        double[][] inverseMatrix = inverse(inputMatrix);

        // عرض المصفوفة الناتجة في واجهة المستخدم
        showResultMatrix(inverseMatrix);
    }

    private static double[][] inverse(double[][] matrix) {
        int n = matrix.length;
        double[][] augmented = new double[n][2 * n];

        // إنشاء مصفوفة معززة للمصفوفة
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmented[i][j] = matrix[i][j];
            }
            augmented[i][i + n] = 1; // إضافة الهوية
        }

        // تطبيق خوارزمية جاؤس-جوردان
        for (int i = 0; i < n; i++) {
            // تحويل العنصر الرئيسي إلى 1
            double diag = augmented[i][i];
            for (int j = 0; j < 2 * n; j++) {
                augmented[i][j] /= diag;
            }
            // تحويل بقية العناصر في العمود إلى 0 ```java
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    double factor = augmented[j][i];
                    for (int k = 0; k < 2 * n; k++) {
                        augmented[j][k] -= factor * augmented[i][k];
                    }
                }
            }
        }

        // استخراج المصفوفة العكسية من المصفوفة المعززة
        double[][] inverseMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseMatrix[i][j] = augmented[i][j + n];
            }
        }

        return inverseMatrix;
    }

    private static void showResultMatrix(double[][] resultMatrix) {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(resultMatrix.length, resultMatrix[0].length));

        for (double[] row : resultMatrix) {
            for (double value : row) {
                JLabel label = new JLabel(String.format("%.2f", value)); // عرض القيم بدقة 2
                label.setHorizontalAlignment(JLabel.CENTER);
                resultPanel.add(label);
            }
        }

        // إنشاء إطار جديد لعرض المصفوفة الناتجة
        JFrame resultFrame = new JFrame("The result of the inverse matrix");
        resultFrame.setSize(400, 400);
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.add(resultPanel);
        resultFrame.setVisible(true);
    }
}