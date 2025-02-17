package matrixx.pages.determinant;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import matrixx.logic.equal;
import static matrixx.logic.equal.evaluateExpression;
import matrixx.pages.history_Page.History;
import static matrixx.pages.history_Page.History.addMatrixToMatrices;
import static matrixx.pages.history_Page.History.matrices;
import static matrixx.pages.home_page.HomePage.homefram;

public class CalcDet implements ActionListener {

    public static JLabel calcLabel;
    public static JPanel calcPanel;
    public static JPanel resultPanel;
    public static JTextField powerInputField;
    public static JPanel operationPanel;
    static JButton backButton;
    static JButton powerButton;
    static JButton determinantButton;
    static JButton inverseButton;
    static JButton clearButton;
    static JButton saveButton;
    static JButton SaveResultButton;
    public static int rows;
    public static int cols;
    public int panelWidth;
    public int panelHeight;
    public static JComboBox<String> matrixlistcalc;
    static JPanel ResultmatrixPanel = new JPanel();
    public int openCount;
    public int closeCount;

    public CalcDet(int m, int n) {
        CalcDet.rows = m;
        CalcDet.cols = n;
        panelWidth = cols * 100;
        panelHeight = rows * 40;
        int buttonWidth = panelWidth + 100 / 5;
        int buttonHeight = 60;

        calcLabel = new JLabel("Determinant", JLabel.CENTER);
        calcLabel.setFont(new Font("Arial", Font.BOLD, 26));
        calcLabel.setBounds(cols * 50, rows * 5, 300, 50);
        calcLabel.setForeground(Color.WHITE);

        calcPanel = new JPanel();
        calcPanel.setLayout(new GridLayout(rows, cols, 5, 5));
        calcPanel.setBounds(cols * 50, rows * 55, panelWidth + 50, panelHeight + 50);
        calcPanel.setBackground(Color.decode("#101a43"));

        matrixlistcalc = new JComboBox<>();
        matrixlistcalc.addItem("Select Matrix");
        matrixlistcalc.setBounds(cols * 90, rows * 30, 100, 30);
        matrixlistcalc.setBackground(Color.WHITE);
        update();
        matrixlistcalc.addActionListener(e -> {
            String selectedMatrix = (String) matrixlistcalc.getSelectedItem();
            double[][] matrix = matrices.get(selectedMatrix);

            double Matrixrows = matrix.length;   // عدد الصفوف
            double Matrixcolumns = matrix[0].length; // عدد الأعمدة

            if ("Select Matrix".equals(selectedMatrix)) {
            } else if (Matrixrows == rows && Matrixcolumns == cols) {
                // تحديث القيم داخل JTextField
                Component[] components = calcPanel.getComponents();
                int index = 0;
                for (int i = 0; i < Matrixrows; i++) {
                    for (int j = 0; j < Matrixcolumns; j++) {
                        if (components[index] instanceof JTextField) {
                            JTextField cellField = (JTextField) components[index];
                            cellField.setText(String.valueOf(matrix[i][j]));
                            index++;
                        }
                    }
                }
            } else {

                JOptionPane.showMessageDialog(calcPanel, "Matrix dimensions must match the selected matrix");
                matrixlistcalc.setSelectedItem("Select Matrix");
            }
        });

        resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(0, 1));
        resultPanel.setBounds(cols * 50, rows * 130 + buttonHeight, panelWidth + 100, panelHeight + 50);
        resultPanel.setBackground(Color.decode("#101a43"));

        for (int i = 0; i < rows * cols; i++) {
            JTextField cellField = new JTextField();
            cellField.setBackground(Color.WHITE);
            cellField.setFont(new Font("Arial", Font.BOLD, 30));
            cellField.setHorizontalAlignment(JTextField.CENTER);
            cellField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    String text = cellField.getText();

                    // منع الأحرف غير المسموح بها
                    if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_MINUS && c != KeyEvent.VK_PERIOD
                            && c != '(' && c != ')' && c != '+' && c != '-' && c != '*' && c != '/' && c != '!') {
                        e.consume();
                    }

                    // منع كتابة الفاكتوريال في بداية النص
                    if (c == '!' && text.isEmpty()) {
                        e.consume();
                    }

                    // منع كتابة فاكتوريال بعد فاكتوريال
                    if (c == '!' && !text.isEmpty() && text.charAt(text.length() - 1) == '!') {
                        e.consume();
                    }

                    // السماح بـ operator بعد الفاكتوريال
                    if ((c == '+' || c == '-' || c == '*' || c == '/') && !text.isEmpty() && text.charAt(text.length() - 1) == '!') {
                        return; // السماح بالكتابة
                    }

                    // منع كتابة فاكتوريال إذا لم يكن هناك رقم أو قوس مغلق قبله
                    if (c == '!' && !text.isEmpty() && !Character.isDigit(text.charAt(text.length() - 1))
                            && text.charAt(text.length() - 1) != ')') {
                        e.consume();
                    }

                    // منع كتابة النقطة إذا كانت موجودة بالفعل في النص
                    if (c == '.' && text.contains(".")) {
                        e.consume();
                    }

                    // منع كتابة '-' إذا كان النص يحتوي على أحرف بالفعل
                    if (c == '-' && text.length() > 0) {
                        e.consume();
                    }

                    // تحويل الحروف الخاصة إلى رموز أو دوال
                    if (c == 'q' || c == 'Q') {
                        cellField.setText(cellField.getText() + "√");
                    }
                    if (c == 'f' || c == 'F') {
                        cellField.setText(cellField.getText() + "!");
                    }
                    if (c == 's' || c == 'S') {
                        cellField.setText(cellField.getText() + "sin(");
                    }
                    if (c == 'c' || c == 'C') {
                        cellField.setText(cellField.getText() + "cos(");
                    }
                    if (c == 't' || c == 'T') {
                        cellField.setText(cellField.getText() + "tan(");
                    }

                    // منع كتابة الأقواس المغلقة إذا لم يكن هناك قوس مفتوح
                    if (c == ')' && openCount <= closeCount) {
                        e.consume();
                    }

                    // منع كتابة الأقواس المفتوحة إذا لم يكن لها مكان مناسب
                    if (c == '(') {
                        e.consume();
                        cellField.setText(text + "(");
                    }

                    // التأكد من الأقواس المفتوحة والمغلقة
                    openCount = countOccurrences(text, '(');
                    closeCount = countOccurrences(text, ')');
                }

                private boolean isOperatorAtEnd(JTextField cellField) {
                    String text = cellField.getText();
                    if (text.isEmpty()) {
                        return false;
                    }
                    char lastChar = text.charAt(text.length() - 1);
                    return lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/';
                }

                private int countOccurrences(String text, char c) {
                    int count = 0;
                    for (int i = 0; i < text.length(); i++) {
                        if (text.charAt(i) == c) {
                            count++;
                        }
                    }
                    return count;
                }
            });

            addInputRestrictions(cellField);
            calcPanel.add(cellField);
        }

        powerButton = new JButton("^n");
        powerButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        powerButton.setBackground(Color.decode("#101a43"));
        powerButton.setForeground(Color.WHITE);
        powerButton.setBorder(null);
        powerButton.setFont(new Font("Arial", Font.BOLD, 20));

        determinantButton = new JButton("Calc");
        determinantButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        determinantButton.setBackground(Color.decode("#101a43"));
        determinantButton.setForeground(Color.WHITE);
        determinantButton.setBorder(null);
        determinantButton.setFont(new Font("Arial", Font.BOLD, 20));

        inverseButton = new JButton("^-1");
        inverseButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        inverseButton.setBackground(Color.decode("#101a43"));
        inverseButton.setForeground(Color.WHITE);
        inverseButton.setBorder(null);
        inverseButton.setFont(new Font("Arial", Font.BOLD, 20));

        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        clearButton.setBackground(Color.decode("#101a43"));
        clearButton.setForeground(Color.WHITE);
        clearButton.setBorder(null);
        clearButton.setFont(new Font("Arial", Font.BOLD, 20));

        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        backButton.setBackground(Color.decode("#101a43"));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(null);
        backButton.setFont(new Font("Arial", Font.BOLD, 20));

        saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        saveButton.setBackground(Color.decode("#101a43"));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBorder(null);
        saveButton.setFont(new Font("Arial", Font.BOLD, 20));

        SaveResultButton = new JButton("Save Result");
        SaveResultButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        SaveResultButton.setBackground(Color.decode("#101a43"));
        SaveResultButton.setForeground(Color.WHITE);
        SaveResultButton.setFont(new Font("Arial", Font.PLAIN, 20));
        SaveResultButton.setVisible(false); // إخفاء زر الحفظ في الب
        SaveResultButton.setBorder(null); // إخفاء زر الحفظ في الب

        SaveResultButton.addActionListener(this);
        saveButton.addActionListener(this);
        powerButton.addActionListener(this);
        determinantButton.addActionListener(this);
        inverseButton.addActionListener(this);
        clearButton.addActionListener(this);
        backButton.addActionListener(this);

        operationPanel = new JPanel(new GridLayout(1, 6, 2, 2));  // زيادة التباعد بين الأعمدة والصفوف
        operationPanel.setBounds(cols * 10, rows * 55 * 2 + 50, panelWidth + 250, buttonHeight);
        operationPanel.setBackground(Color.decode("#101a43"));

        // إضافة الأزرار إلى اللوحة
        operationPanel.add(powerButton);
        operationPanel.add(determinantButton);
        operationPanel.add(inverseButton);
        operationPanel.add(clearButton);
        operationPanel.add(backButton);
        operationPanel.add(saveButton);

    }

    public void update() {
        matrixlistcalc.removeAllItems();  // إزالة جميع العناصر الحالية
        matrixlistcalc.addItem("Select Matrix");  // إضافة خيار "Select Matrix"
        // إضافة جميع أسماء المصفوفات إلى الـ JComboBox
        for (String name : matrices.keySet()) {
            matrixlistcalc.addItem(name);
        }
    }

    public void show() {
        homefram.setSize(cols * 150 + 220, rows * 200);
        homefram.add(calcLabel);
        homefram.add(calcPanel);
        homefram.add(resultPanel);
        homefram.add(operationPanel);
        homefram.add(matrixlistcalc);
        matrixlistcalc.setVisible(true);
        calcLabel.setVisible(true);
        calcPanel.setVisible(true);
        operationPanel.setVisible(true);
        homefram.repaint();
        homefram.setVisible(true);
    }

    public static void showCalcPage(int m, int n) {
        CalcDet calcDet = new CalcDet(m, n);
        calcDet.show();
    }

    public void hide() {
        homefram.remove(calcLabel);
        homefram.remove(calcPanel);
        homefram.remove(resultPanel);
        homefram.remove(operationPanel);
        homefram.remove(matrixlistcalc);
        clearTextFields();
        resultPanel.removeAll();
        resultPanel.revalidate();
        resultPanel.repaint();
        homefram.repaint();
    }

    private void reset() {
        clearTextFields();
        homefram.setSize(cols * 150 + 200, rows * 200);
        resultPanel.removeAll();
        resultPanel.revalidate();
        resultPanel.repaint();

    }

    private boolean isSquareMatrix() {

        if (rows != cols) {
            JOptionPane.showMessageDialog(homefram, "Matrix must be square for this operation.");
            return false;
        }
        return true;
    }

    private boolean identifyMatrix(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // التحقق من أن المصفوفة مربعة (عدد الصفوف يساوي عدد الأعمدة)
        if (rows != cols) {
            return false; // ليست مصفوفة مربعة
        }

        // التحقق إذا كانت مصفوفة وحدة
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == j && matrix[i][j] != 1) {  // العناصر على القطر الرئيسي يجب أن تكون 1
                    return false;
                } else if (i != j && matrix[i][j] != 0) {  // العناصر غير القطرية يجب أن تكون 0
                    return false;
                }
            }
        }
        return true;  // هي مصفوفة وحدة
    }

    private double[][] getMatrixFromFields() {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JTextField cellField = (JTextField) calcPanel.getComponent(i * cols + j);
                String cellText = cellField.getText();
                if (!isValidExpression(cellText)) {
                    JOptionPane.showMessageDialog(homefram, "Invalid expression in cell (" + ++i + ", " + ++j + ")");
                    return null;
                }
                try {
                    matrix[i][j] = equal.evaluateExpression(cellText);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(homefram, "Invalid expression in cell (" + ++i + ", " + ++j + ")");
                    return null;
                }
            }
        }
        return matrix;
    }

    private void clearTextFields() {
        for (Component component : calcPanel.getComponents()) {
            if (component instanceof JTextField jTextField) {
                jTextField.setText("");
            }
        }
    }

    private double[][] saveMatrixTo2DArray() {
        History.x = 0;
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JTextField cellField = (JTextField) calcPanel.getComponent(i * cols + j);
                String cellText = cellField.getText();

                double result;
                try {
                    result = evaluateExpression(cellText);

                    matrix[i][j] = result;
                } catch (Exception e) {

                    JOptionPane.showMessageDialog(homefram, "Invalid input in cell (" + ++i + ", " + ++j + "). Please enter valid numbers.");
                    History.x = 1;
                }

            }
        }
        return matrix;
    }

    private double[][] saveResultMatrixTo2DArray() {
        int resultRows = ResultmatrixPanel.getComponentCount() / cols; // حساب عدد الصفوف في لوحة المصفوفة
        double[][] matrix = new double[resultRows][cols];

        for (int i = 0; i < resultRows; i++) {
            for (int j = 0; j < cols; j++) {
                JTextField cellField = (JTextField) ResultmatrixPanel.getComponent(i * cols + j);
                String cellText = cellField.getText().trim(); // إزالة المسافات الفارغة

                if (!cellText.isEmpty()) { // التحقق من أن الخلية ليست فارغة
                    double result;
                    try {
                        result = evaluateExpression(cellText);

                        matrix[i][j] = result;
                    } catch (Exception e) {

                        JOptionPane.showMessageDialog(homefram, "Invalid input in result cell (" + ++i + ", " + ++j + "). Please enter valid numbers.");
                        History.x = 1;
                        return null;
                    }
                }

            }
        }

        return matrix; // إرجاع المصفوفة بعد التعبئة
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Save Result" -> {

                double[][] matrix;

                matrix = saveResultMatrixTo2DArray();

                if (matrix != null) {
                    addMatrixToMatrices(matrix);
                } else {
                    JOptionPane.showMessageDialog(homefram, "Invalid input in cells. Please make sure all cells contain valid numbers.");
                }

            }
            case "Save" -> {

                double[][] matrix;

                matrix = saveMatrixTo2DArray();

                if (matrix != null) {
                    addMatrixToMatrices(matrix);
                } else {
                    JOptionPane.showMessageDialog(homefram, "Invalid input in cells. Please make sure all cells contain valid numbers.");
                }

            }

            case "Back" -> {
                hide();
                DeterminantPage.show();
            }
            case "Clear" -> {
                reset();
                matrixlistcalc.setSelectedItem("Select Matrix");
            }
            case "^n" -> {
                if (!isSquareMatrix()) {
                    return;
                }

                String nValue;
                boolean validInput = false;

                // استمرار الطلب حتى تكون المدخلات عدد صحيح صالح
                while (!validInput) {
                    nValue = JOptionPane.showInputDialog(homefram, "Enter the value of N (integer only):");

                    if (nValue != null && nValue.matches("-?\\d+")) { // التحقق من أن المدخلات تحتوي على أرقام فقط، مع دعم الأعداد السالبة
                        try {
                            int n = Integer.parseInt(nValue);
                            double[][] matrix = getMatrixFromFields();
                            double[][] poweredMatrix = raiseMatrixToPower(matrix, n);
                            displayMatrix(poweredMatrix, "Power of " + n);
                            validInput = true; // إيقاف الحلقة بعد إدخال عدد صحيح صالح
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(homefram, "Invalid input for N. Please enter a valid integer.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(homefram, "Invalid input. Please enter an integer only.");
                    }
                }
            }
            case "Calc" -> {
                if (!isSquareMatrix()) {
                    return;
                }
                double[][] matrix = getMatrixFromFields();
                if (matrix != null) {
                    double determinantValue = calculateDeterminantValue(matrix);
                    displayDeterminantResult(determinantValue);
                }
            }
            case "^-1" -> {
                if (!isSquareMatrix()) {
                    return;
                }
                double[][] matrix = getMatrixFromFields();

                double[][] inverseMatrix = inverse(matrix);
                if (inverseMatrix != null) {
                    displayMatrix(inverseMatrix, "Inverse Matrix");
                } else {
                    JOptionPane.showMessageDialog(homefram, "Matrix is not invertible.");
                }
            }
        }
    }

    private void displayDeterminantResult(double determinantValue) {
        resultPanel.removeAll();
        JLabel titleLabel = new JLabel("Determinant Result: " + determinantValue, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        resultPanel.add(titleLabel);
        resultPanel.revalidate();
        resultPanel.repaint();
        homefram.setSize(cols * 150 + 180, rows * 200 + 150);
        homefram.repaint();
    }

    private double[][] raiseMatrixToPower(double[][] matrix, int n) {
        if (identifyMatrix(matrix)) {
            // إذا كانت مصفوفة وحدة، يتم إرجاعها كما هي
            return matrix;
        }

        // إذا كان n = 0، إعادة مصفوفة الوحدة (Identity Matrix)
        if (n == 0) {
            double[][] identityMatrix = new double[matrix.length][matrix[0].length];
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    identityMatrix[i][j] = (i == j) ? 1 : 0; // تعيين 1 على القطر و0 في باقي العناصر
                }
            }
            return identityMatrix;
        }

        double[][] result;

        if (n < 0) {
            // إذا كان n سالبًا، احسب المعكوس أولاً
            result = inverse(matrix);
            if (result == null) {
                JOptionPane.showMessageDialog(homefram, "Matrix is not invertible, cannot raise to negative power.");
                return null;
            }
            n = -n; // حول الأس السالب إلى موجب بعد إيجاد المعكوس
        } else {
            result = matrix;
        }

        // رفع المصفوفة للأس n
        for (int i = 1; i < n; i++) {
            result = multiplyMatrices(result, matrix);
        }

        return result;
    }

    private double[][] multiplyMatrices(double[][] matrixA, double[][] matrixB) {
        int aRows = matrixA.length;
        int aCols = matrixA[0].length;
        int bCols = matrixB[0].length;
        double[][] result = new double[aRows][bCols];

        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bCols; j++) {
                for (int k = 0; k < aCols; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        showResultMatrix(result);
        return result;
    }

    private static double[][] inverse(double[][] matrix) {
        int n = matrix.length;
        double[][] augmented = new double[n][2 * n];

        // إنشاء مصفوفة معززة للمصفوفة
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, augmented[i], 0, n);
            augmented[i][i + n] = 1; // إضافة الهوية
        }

        // تطبيق خوارزمية جاؤس-جوردان
        for (int i = 0; i < n; i++) {
            // تحويل العنصر الرئيسي إلى 1
            double diag = augmented[i][i];
            if (diag == 0) {
                return null; // المصفوفة غير قابلة للعكس
            }
            for (int j = 0; j < 2 * n; j++) {
                augmented[i][j] /= diag;
            }
            // تحويل بقية العناصر في العمود إلى 0
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
        showResultMatrix(inverseMatrix);
        return inverseMatrix;
    }

    // دالة لعرض المصفوفة الناتجة
    private static void showResultMatrix(double[][] matrix) {
        resultPanel.removeAll(); // إزالة أي نتائج سابقة

        // إعداد بانل جديد لعرض المصفوفة
        ResultmatrixPanel = new JPanel();
        ResultmatrixPanel.setLayout(new GridLayout(matrix.length, matrix[0].length, 10, 10)); // تخطيط الشبكة
        ResultmatrixPanel.setBackground(Color.decode("#101a43")); // لون خلفية للنتائج

        // إضافة العناصر إلى ResultmatrixPanel
        for (double[] row : matrix) {
            for (double value : row) {
                JTextField cellField = new JTextField(String.valueOf(value));
                cellField.setHorizontalAlignment(JTextField.CENTER);
                cellField.setBackground(Color.WHITE);
                cellField.setForeground(Color.black);
                cellField.setFont(new Font("Arial", Font.BOLD, 20));
                cellField.setEditable(false); // عدم السماح بالتعديل
                ResultmatrixPanel.add(cellField);
            }
        }

        // إضافة العنوان
        JLabel titleLabel = new JLabel("Inverse Matrix", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // إضافة العنوان والبانل إلى resultPanel
        resultPanel.add(titleLabel);
        resultPanel.add(ResultmatrixPanel);

        // تحديث واجهة المستخدم
        resultPanel.revalidate(); // تحديث البانل
        resultPanel.repaint(); // إعادة رسم البان
        homefram.setSize(cols * 150 + 180, rows * 200 + 130);
        homefram.repaint(); // إعادة رسم الإطار
    }
    double determinant = 0;
// دالة لحساب قيمة المحدد

    private double calculateDeterminantValue(double[][] matrix) {
        System.out.println("Calculating determinant...");
        int n = matrix.length;
        if (n == 1) {
            return matrix[0][0];
        }
        if (n == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }

        double determinant = 0;
        for (int i = 0; i < n; i++) {
            double sign = (i % 2 == 0) ? 1 : -1;
            double[][] minor = getMinor(matrix, 0, i);
            double subDeterminant = calculateDeterminantValue(minor);
            determinant += sign * matrix[0][i] * subDeterminant;
        }
        return determinant;
    }

    private double[][] getMinor(double[][] matrix, int row, int col) {
        int n = matrix.length;
        double[][] minor = new double[n - 1][n - 1];
        for (int i = 0, minorRow = 0; i < n; i++) {
            for (int j = 0, minorCol = 0; j < n; j++) {
                if (i != row && j != col) {
                    minor[minorRow][minorCol++] = matrix[i][j];
                    if (minorCol == minor.length) {
                        minorCol = 0;
                        minorRow++;
                    }
                }
            }
        }
        return minor;
    }

    private void displayMatrix(double[][] matrix, String title) {
        resultPanel.removeAll(); // إزالة أي نتائج سابقة
// إعداد بانل جديد لعرض المصفوفة
        JPanel matrixPanel = new JPanel();
        matrixPanel.setLayout(new GridLayout(matrix.length, matrix[0].length, 10, 10)); // تخطيط الشبكة
        matrixPanel.setBackground(Color.decode("#101a43")); // لون خلفية للنتائج

// إضافة العناصر إلى matrixPanel
        for (double[] row : matrix) {
            for (double value : row) {
                JTextField cellField = new JTextField(String.valueOf(value));
                cellField.setHorizontalAlignment(JTextField.CENTER);
                cellField.setBackground(Color.white);
                cellField.setEditable(false); // عدم السماح بالتعديل
                matrixPanel.add(cellField);
            }
        }

// إضافة العنوان
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

// إضافة العنوان والبانل إلى resultPanel
        resultPanel.add(titleLabel);
        resultPanel.add(matrixPanel);
        resultPanel.add(SaveResultButton);
        SaveResultButton.setVisible(true); // إظهار زر الحفظ
        // تحديث حجم الإطار بناءً على حجم النتائج
// الفرام مع مساحة للنتائج
        homefram.setSize(cols * 150 + 180, rows * 200 + 130);

        resultPanel.revalidate(); // تحديث البانل
        resultPanel.repaint(); // إعادة رسم البانل
        homefram.repaint(); // إعادة رسم الإطار
    }

    private boolean isValidExpression(String text) {
        String regex = "^[\\d\\s+\\-*/().sin\\ cos\\ tan\\ sqrt\\ !]+$";
        return Pattern.matches(regex, text);
    }

    private void addInputRestrictions(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                String currentText = textField.getText();
                int caretPosition = textField.getCaretPosition();

                // التحقق من المدخل إذا كان نقطة (.) أو سالب (-)
                if (keyChar == '.') {
                    // السماح بإدخال النقطة فقط إذا لم توجد نقطة في الرقم الحالي
                    int lastOperatorIndex = Math.max(
                            Math.max(currentText.lastIndexOf('+'), currentText.lastIndexOf('-')),
                            Math.max(currentText.lastIndexOf('*'), currentText.lastIndexOf('/'))
                    );
                    String lastNumber = currentText.substring(lastOperatorIndex + 1, caretPosition);
                    if (lastNumber.contains(".")) {
                        e.consume(); // منع إدخال النقطة إذا كانت موجودة في الرقم الحالي
                    }
                } else if (keyChar == '-') {
                    // السماح بإدخال السالب في بداية النص
                    if (caretPosition == 0) {
                        if (currentText.startsWith("-")) {
                            e.consume(); // منع إدخال سالب إضافي في بداية النص
                        }
                    } else {
                        // السماح بإدخال السالب بعد أي عملية حسابية أو رقم
                        char prevChar = currentText.charAt(caretPosition - 1);
                        if (!Character.isDigit(prevChar) && !"+-*/".contains(String.valueOf(prevChar))) {
                            e.consume(); // منع إدخال السالب إذا لم يكن بعد رقم أو عملية حسابية
                        } else {
                            // السماح بإدخال سالب واحد فقط إذا كان مسبوقًا بعلامة حسابية
                            if (caretPosition > 1 && currentText.charAt(caretPosition - 2) == '-') {
                                e.consume(); // منع تكرار السالب
                            }
                        }
                    }
                }
            }
        });
    }

}
