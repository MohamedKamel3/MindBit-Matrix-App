package matrixx.pages.solve_equations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import static matrixx.logic.equal.evaluateExpression;
import static matrixx.pages.history_Page.History.addMatrixToMatrices;
import static matrixx.pages.history_Page.History.matrices;
import static matrixx.pages.home_page.HomePage.homefram;

public class EquationInputPanel extends JPanel {

    private JTextField[][] coefficientFields;
    private JButton calculateButton, backButton, saveButton, clearButton2;
    private JComboBox<String> operationsComboBox; // Add JComboBox
    private int m, n;

    public EquationInputPanel(int m, int n, BiConsumer<double[], JTextField[][]> onResultCallback, ActionListener backActionListener) {
        this.m = m;
        this.n = n;
        setBackground(Color.decode("#101a43"));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title label positioned at the top
        JLabel titleLabel = new JLabel("Enter Equation Coefficients");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0; // Row 0 for title label
        gbc.gridwidth = n + 1;
        add(titleLabel, gbc);

        // Create a JComboBox with some example operations
        operationsComboBox = new JComboBox<>();
        operationsComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        operationsComboBox.setPreferredSize(new Dimension(200, 30));
        operationsComboBox.addActionListener(e -> {
            String selectedMatrix = (String) operationsComboBox.getSelectedItem();

            if ("Select Matrix".equals(selectedMatrix)) {
            } else if (matrices.containsKey(selectedMatrix)) {
                double[][] matrix = matrices.get(selectedMatrix); // الحصول على المصفوفة المرتبطة بالمفتاح
                int matrixRows = matrix.length;   // عدد الصفوف
                int matrixCols = matrix[0].length; // عدد الأعمدة

                if (matrixRows == m && matrixCols == n + 1) {
                    // تحديث القيم داخل JTextField
                    for (int i = 0; i < matrixRows; i++) {
                        for (int j = 0; j < matrixCols; j++) {
                            coefficientFields[i][j].setText(String.valueOf(matrix[i][j])); // تحديث الخانات
                        }
                    }
                } else {
                    // إذا كانت الأبعاد غير متطابقة، أظهر رسالة خطأ
                    JOptionPane.showMessageDialog(this, "Matrix dimensions must match the selected matrix", "Dimension Mismatch", JOptionPane.ERROR_MESSAGE);
                    operationsComboBox.setSelectedItem("Select Matrix");
                }
            }
        });

        update();

        // Position JComboBox below the title label
        gbc.gridy = 1; // Row 1 for JComboBox
        add(operationsComboBox, gbc);

        // Initialize coefficient fields and place them starting from row 2
        coefficientFields = new JTextField[m][n + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j <= n; j++) {
                JTextField field = new JTextField(5);
                field.setPreferredSize(new Dimension(50, 30));
                field.setFont(new Font("Arial", Font.PLAIN, 16));
                field.addActionListener(new EnterKeyListener(i, j));

                ((AbstractDocument) field.getDocument()).setDocumentFilter(new ExpressionInputFilter(field));

                coefficientFields[i][j] = field;

                gbc.gridx = j;
                gbc.gridy = i + 2; // Start from row 2 to place fields below JComboBox
                gbc.gridwidth = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                gbc.insets = new Insets(5, 5, 5, 5);
                add(field, gbc);
            }
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.decode("#101a43"));

        calculateButton = new RoundedButton("Calculate");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 18));
        calculateButton.setForeground(Color.decode("#ADD8E6"));
        calculateButton.addActionListener(e -> {
            try {
                // Step 1: Initialize the matrix
                double[][] matrix = new double[m][n + 1];
                int validEquations = 0;

                // Step 2: Loop to check and fill the coefficients
                for (int i = 0; i < m; i++) {
                    String[] inputRow = new String[n];
                    boolean allZeroRow = true;

                    // Process the row input
                    for (int j = 0; j < n; j++) {
                        String input = coefficientFields[i][j].getText().trim();
                        inputRow[j] = input;

                        // Check if the input is not zero
                        if (!input.equals("0") && !input.isEmpty()) {
                            allZeroRow = false;
                        }
                    }

                    // Skip all-zero rows
                    if (allZeroRow) {
                        continue;
                    }

                    // Fill the matrix with non-zero rows
                    for (int j = 0; j < n; j++) {
                        try {
                            matrix[validEquations][j] = evaluateExpression(inputRow[j]);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Invalid input in row " + (i + 1) + ", column " + (j + 1),
                                    "Input Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    // Add the constant term (right-hand side value)
                    try {
                        matrix[validEquations][n] = evaluateExpression(coefficientFields[i][n].getText().trim());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid input in constant column for row " + (i + 1),
                                "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    validEquations++;
                }

                // Step 3: If no valid equations are left
                if (validEquations == 0) {
                    JOptionPane.showMessageDialog(this, "There are no valid equations!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Step 4: Resize the matrix to the number of valid equations
                double[][] validMatrix = new double[validEquations][n + 1];
                System.arraycopy(matrix, 0, validMatrix, 0, validEquations);

                // Step 5: Determine the type of solution
                String solutionType = checkSolutionType(validMatrix);

                switch (solutionType) {
                    case "Unique Solution":
                        double[] results = solveUsingGaussianElimination(validMatrix);
                        StringBuilder solutionMessage = new StringBuilder("Unique Solution Found:\n");
                        for (int i = 0; i < results.length; i++) {
                            solutionMessage.append("x").append(i + 1).append(" = ").append(results[i]).append("\n");
                        }
                        onResultCallback.accept(results, coefficientFields);
                        JOptionPane.showMessageDialog(this, solutionMessage.toString(), "Success", JOptionPane.INFORMATION_MESSAGE);
                        break;

                    case "No Solution":
                        JOptionPane.showMessageDialog(this, "The system has no solution.", "Error", JOptionPane.ERROR_MESSAGE);
                        break;

                    case "Infinite Solutions":
                        JOptionPane.showMessageDialog(this, "The system has infinitely many solutions.", "Warning", JOptionPane.WARNING_MESSAGE);
                        break;

                    default:
                        JOptionPane.showMessageDialog(this, "Unexpected error during calculation.", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Calculation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(calculateButton);

        saveButton = new RoundedButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 18));
        saveButton.setForeground(Color.decode("#ADD8E6"));
        saveButton.addActionListener(e -> {
            // Save the matrix
            double[][] matrix;
            try {
                matrix = saveMatrixTo2DArray();
                if (matrix != null) {
                    addMatrixToMatrices(matrix);
                    JOptionPane.showMessageDialog(this, "Matrix saved successfully!", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(homefram, "Invalid input in cells. Please make sure all cells contain valid numbers.");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred while saving the matrix: " + e1.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(saveButton);

        // Clear button
        clearButton2 = new RoundedButton("Clear");
        clearButton2.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton2.setPreferredSize(new Dimension(400, 40));
        clearButton2.setForeground(Color.decode("#ADD8E6"));
        clearButton2.addActionListener(e -> {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j <= n; j++) {
                    if (coefficientFields[i][j] != null) {
                        coefficientFields[i][j].setDocument(new javax.swing.text.PlainDocument());
                    }
                }
            }
            operationsComboBox.setSelectedIndex(0);
            this.repaint();
            this.revalidate();
        });

        GridBagConstraints clearGbc = new GridBagConstraints();
        clearGbc.gridx = 1;
        clearGbc.gridy = m + 3;
        clearGbc.fill = GridBagConstraints.HORIZONTAL;
        clearGbc.insets = new Insets(5, 5, 5, 5);
        add(clearButton2, clearGbc);
        repaint();
        revalidate();

        // Back button
        backButton = new RoundedButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setForeground(Color.decode("#ADD8E6"));
        backButton.addActionListener(backActionListener);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = m + 2;
        gbc.gridwidth = n + 1;
        add(buttonPanel, gbc);
    }

    private String checkSolutionType(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int numVariables = cols - 1;

        // Step 1: Perform Gaussian elimination to row reduce the matrix
        int rank = performGaussianElimination(matrix);

        // Step 2: Check for inconsistency
        for (int i = 0; i < rows; i++) {
            boolean allZeroCoefficients = true;
            for (int j = 0; j < numVariables; j++) {
                if (Math.abs(matrix[i][j]) > 1e-10) {
                    allZeroCoefficients = false;
                    break;
                }
            }
            // If all coefficients are zero but the constant term is non-zero, there's no solution
            if (allZeroCoefficients && Math.abs(matrix[i][numVariables]) > 1e-10) {
                return "No Solution";
            }
        }

        // Step 3: Determine the type of solution
        if (rank < numVariables) {
            return "Infinite Solutions"; // The system has infinitely many solutions
        } else if (rank == numVariables) {
            return "Unique Solution"; // The system has a unique solution
        } else {
            return "No Solution"; // This case should not occur
        }
    }

    private int performGaussianElimination(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int rank = 0;

        for (int i = 0; i < rows; i++) {
            // Find the pivot element
            int maxRow = i;
            for (int k = i + 1; k < rows; k++) {
                if (Math.abs(matrix[k][i]) > Math.abs(matrix[maxRow][i])) {
                    maxRow = k;
                }
            }

            // Swap the current row with the pivot row
            double[] temp = matrix[i];
            matrix[i] = matrix[maxRow];
            matrix[maxRow] = temp;

            // If the pivot is zero, skip this row
            if (Math.abs(matrix[i][i]) < 1e-10) {
                continue;
            }

            // Normalize the pivot row
            for (int j = i + 1; j < rows; j++) {
                double factor = matrix[j][i] / matrix[i][i];
                for (int k = i; k < cols; k++) {
                    matrix[j][k] -= factor * matrix[i][k];
                }
            }
            rank++;
        }

        return rank;
    }

    private int calculateRank(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] tempMatrix = new double[rows][cols];

        // Copy the original matrix to avoid modifying it
        for (int i = 0; i < rows; i++) {
            System.arraycopy(matrix[i], 0, tempMatrix[i], 0, cols);
        }

        int rank = 0;
        for (int row = 0; row < rows; row++) {
            boolean nonZeroRow = false;
            for (int col = 0; col < cols; col++) {
                if (Math.abs(tempMatrix[row][col]) > 1e-9) { // Use tolerance for floating-point comparison
                    nonZeroRow = true;
                    break;
                }
            }
            if (nonZeroRow) {
                rank++;
            }
        }
        return rank;
    }

    private double[][] saveMatrixTo2DArray() throws Exception {
        double[][] matrix = new double[m][n + 1];

        try {
            // التحقق من جميع الحقول المدخلة
            for (int i = 0; i < m; i++) {
                for (int j = 0; j <= n; j++) {
                    String input = coefficientFields[i][j].getText().trim();

                    // إذا كان الحقل فارغًا، أظهر رسالة للمستخدم
                    if (input.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please fill all fields with valid expressions", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }

                    // تقييم التعبير المدخل
                    matrix[i][j] = evaluateExpression(input);
                }
            }

            return matrix; // إرجاع المصفوفة بعد ملئها
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input detected. Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null; // إعادة null في حال وجود خطأ في التنسيق
        }
    }

    public void update() {
        operationsComboBox.removeAllItems();  // إزالة جميع العناصر الحالية
        operationsComboBox.addItem("Select Matrix");  // إضافة خيار "Select Matrix"
        // إضافة جميع أسماء المصفوفات إلى الـ JComboBox
        for (String name : matrices.keySet()) {
            operationsComboBox.addItem(name);
        }
    }

    private static class ExpressionInputFilter extends DocumentFilter {

        private final JTextField field;
        private boolean awaitingClosingParenthesis = false;

        private final String allowedCharacters = "cstq!*/-+().0123456789";

        public ExpressionInputFilter(JTextField field) {
            this.field = field;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (isValidCharacter(string)) {
                handleFunctionInput(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (isValidCharacter(text)) {
                handleFunctionInput(fb, offset, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());

            if (awaitingClosingParenthesis && !currentText.contains("Sin(") && !currentText.contains("Cos(")
                    && !currentText.contains("Tan(") && !currentText.contains("Sqrt(")) {
                field.setBackground(Color.WHITE);
                awaitingClosingParenthesis = false;
            }
        }

        private boolean isValidCharacter(String text) {
            for (char c : text.toCharArray()) {
                if (allowedCharacters.indexOf(c) == -1) {
                    return false;
                }
            }
            return true;
        }

        private void handleFunctionInput(FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException {
            if (text.equals("s") && !awaitingClosingParenthesis) {
                fb.replace(offset, 0, "sin(", attrs);
                field.setBackground(Color.RED);
                awaitingClosingParenthesis = true;
            } else if (text.equals("c") && !awaitingClosingParenthesis) {
                fb.replace(offset, 0, "cos(", attrs);
                field.setBackground(Color.RED);
                awaitingClosingParenthesis = true;
            } else if (text.equals("t") && !awaitingClosingParenthesis) {
                fb.replace(offset, 0, "tan(", attrs);
                field.setBackground(Color.RED);
                awaitingClosingParenthesis = true;
            } else if (text.equals("q") && !awaitingClosingParenthesis) {
                fb.replace(offset, 0, "√", attrs);
                field.setBackground(Color.RED);
                awaitingClosingParenthesis = true;
            } else if (text.equals(")") && awaitingClosingParenthesis) {
                fb.insertString(offset, ")", attrs);
                field.setBackground(Color.WHITE);
                awaitingClosingParenthesis = false;
            } else {
                super.insertString(fb, offset, text, attrs);
            }
        }

    }

    private class EnterKeyListener implements ActionListener {

        private int row, col;

        public EnterKeyListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int nextRow = row;
            int nextCol = col + 1;
            if (nextCol > n) {
                nextCol = 0;
                nextRow++;
            }
            if (nextRow >= m) {
                calculateButton.requestFocus();
                return;
            }
            coefficientFields[nextRow][nextCol].requestFocus();
        }
    }

    private boolean isSolvable(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // Perform Gaussian elimination with partial pivoting to solve the system
        for (int i = 0; i < rows; i++) {
            // Find the pivot row for the current column
            int maxRow = i;
            for (int j = i + 1; j < rows; j++) {
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[maxRow][i])) {
                    maxRow = j;
                }
            }

            // Swap rows if necessary
            double[] temp = matrix[i];
            matrix[i] = matrix[maxRow];
            matrix[maxRow] = temp;

            // Check if the pivot element is zero
            if (Math.abs(matrix[i][i]) < 1e-10) {
                // If this is a zero row (i.e., 0X + 0Y = 0), continue without doing anything
                if (matrix[i][cols - 1] == 0) {
                    continue; // It's effectively a redundant equation, skip it
                } else {
                    // If the constant term is non-zero, it's inconsistent (e.g., 0X + 0Y = non-zero)
                    return false;
                }
            }

            // Eliminate the variable in subsequent rows
            for (int j = i + 1; j < rows; j++) {
                double factor = matrix[j][i] / matrix[i][i];
                for (int k = i; k < cols; k++) {
                    matrix[j][k] -= matrix[i][k] * factor;
                }
            }
        }

        // After elimination, check if any row is entirely zero (indicating a dependent system)
        for (int i = 0; i < rows; i++) {
            boolean allZero = true;
            for (int j = 0; j < cols - 1; j++) {
                if (matrix[i][j] != 0) {
                    allZero = false;
                    break;
                }
            }

            // If the row is all zeros, but the constant term is non-zero, the system is inconsistent
            if (allZero && matrix[i][cols - 1] != 0) {
                return false; // Inconsistent system (0X + 0Y = non-zero)
            }
        }

        return true;  // The system is consistent (either has a unique solution or infinitely many solutions)
    }

    private double[] solveUsingGaussianElimination(double[][] matrix) throws ArithmeticException {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[] result = new double[rows];

        // Forward Elimination with Partial Pivoting
        for (int i = 0; i < rows; i++) {
            // Find the pivot row
            int maxRow = i;
            for (int j = i + 1; j < rows; j++) {
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[maxRow][i])) {
                    maxRow = j;
                }
            }

            // Swap the current row with the pivot row
            if (i != maxRow) {
                double[] temp = matrix[i];
                matrix[i] = matrix[maxRow];
                matrix[maxRow] = temp;
            }

            // If the pivot is zero or nearly zero, the system is singular
            if (Math.abs(matrix[i][i]) < 1e-10) {
                throw new ArithmeticException("Matrix is singular or nearly singular. The system is inconsistent.");
            }

            // Eliminate the column below the pivot
            for (int j = i + 1; j < rows; j++) {
                double factor = matrix[j][i] / matrix[i][i];
                for (int k = i; k < cols; k++) {
                    matrix[j][k] -= matrix[i][k] * factor;
                }
            }
        }

        // Check for inconsistency (if a row is all zeros except the last column)
        for (int i = 0; i < rows; i++) {
            boolean allZero = true;
            for (int j = 0; j < cols - 1; j++) {
                if (Math.abs(matrix[i][j]) > 1e-10) {
                    allZero = false;
                    break;
                }
            }
            // If all coefficients are zero but the constant term is non-zero, no solution exists
            if (allZero && Math.abs(matrix[i][cols - 1]) > 1e-10) {
                throw new ArithmeticException("The system has no solution.");
            }
        }

        // Back Substitution
        for (int i = rows - 1; i >= 0; i--) {
            result[i] = matrix[i][cols - 1];
            for (int j = i + 1; j < rows; j++) {
                result[i] -= matrix[i][j] * result[j];
            }
            result[i] /= matrix[i][i];
        }

        return result;
    }

    private static class RoundedButton extends JButton {

        public RoundedButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.decode("#1E3A8A"));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), x, y);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            g.setColor(Color.decode("#ADD8E6"));
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        }
    }
}
