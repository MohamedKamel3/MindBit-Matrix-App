package matrixx.pages.operations;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import static matrixx.logic.equal.evaluateExpression;
import matrixx.pages.history_Page.History;
import static matrixx.pages.history_Page.History.addMatrixToMatrices;
import static matrixx.pages.history_Page.History.matrices;
import static matrixx.pages.home_page.HomePage.homefram;
import static matrixx.pages.operations.Panel1.backButton;
import static matrixx.pages.operations.Panel2.colsList;
import static matrixx.pages.operations.Panel2.matrixPanels;
import static matrixx.pages.operations.Panel2.rowsList;
import static matrixx.pages.operations.Panel2.selectedOperation;

public class Panel3 {

    public static JPanel Panel3;
    public static ArrayList<JTextField[][]> allmatrices;
    public JComboBox<String> matrixComboBox;
    public int openCount;
    public int closeCount;

    public Panel3(int matrixCount) {
        Panel3 = new JPanel();
        Panel3.setLayout(new GridBagLayout());
        Panel3.setBackground(Color.decode("#101a43"));  // Darker background for contrast
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Increased padding for better spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        allmatrices = new ArrayList<>();

        // Loop to create each matrix and its components
        for (int i = 0; i < matrixCount; i++) {
            int rows = Integer.parseInt(rowsList.get(i));
            int cols = Integer.parseInt(colsList.get(i));

            JPanel matrixPanel = new JPanel(new GridLayout(rows, cols));
            matrixPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#2980B9"), 3)); // Blue border for matrix
            matrixPanel.setBackground(Color.decode("#101a43"));
            JTextField[][] matrix = new JTextField[rows][cols];

            // إنشاء ComboBox لكل مصفوفة
            JComboBox<String> matrixComboBox = new JComboBox<>();
            matrixComboBox.setFont(new Font("Roboto", Font.PLAIN, 16));
            matrixComboBox.setBackground(Color.decode("#01baff"));
            matrixComboBox.setForeground(Color.BLACK);
            matrixComboBox.setPreferredSize(new Dimension(200, 35));
            matrixComboBox.addItem("Select Matrix");

            // تحديث القيم في ComboBox
            for (String name : matrices.keySet()) {
                matrixComboBox.addItem(name);
            }

            // إضافة ActionListener خاص بـ ComboBox الحالي
            int currentIndex = i; // حفظ الفهرس الحالي
            matrixComboBox.addActionListener(e -> {
                String selectedMatrixName = (String) matrixComboBox.getSelectedItem();
                if ("Select Matrix".equals(selectedMatrixName)) {
                    return; // لا تفعل شيئًا إذا لم يتم اختيار مصفوفة
                }

                // جلب المصفوفة المحددة
                double[][] selectedMatrix = matrices.get(selectedMatrixName);
                if (selectedMatrix == null) {
                    JOptionPane.showMessageDialog(Panel3, "Error: The selected matrix does not exist.");
                    return;
                }

                int matrixRows = selectedMatrix.length;
                int matrixCols = selectedMatrix[0].length;

                // التحقق من أبعاد المصفوفة
                int targetRows = Integer.parseInt(rowsList.get(currentIndex));
                int targetCols = Integer.parseInt(colsList.get(currentIndex));

                if (matrixRows == targetRows && matrixCols == targetCols) {
                    // تحديث الحقول النصية
                    JTextField[][] targetMatrixFields = allmatrices.get(currentIndex);
                    updateMatrixFields(targetMatrixFields, selectedMatrix);
                } else {
                    JOptionPane.showMessageDialog(Panel3, "Error: Matrix dimensions do not match the target matrix dimensions.");
                    matrixComboBox.setSelectedItem("Select Matrix");
                }
            });

            JButton saveButton = new JButton("Save");
            saveButton.setFont(new Font("Roboto", Font.PLAIN, 20)); // Smaller font size for Save button
            saveButton.setBackground(Color.decode("#831574"));
            saveButton.setForeground(Color.WHITE);
            saveButton.setPreferredSize(new Dimension(100, 35));  // Smaller button size
            saveButton.setFocusPainted(false);
            saveButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Add ActionListener to handle Save functionality for each matrix
            saveButton.addActionListener(e -> {
                double[][] resultMatrix;

                // Get the correct matrix dimensions for the current matrix
                resultMatrix = saveMatrixTo2DArray(rows, cols, matrix);

                if (resultMatrix != null) {
                    addMatrixToMatrices(resultMatrix);  // Save the matrix to the history
                } else {
                    JOptionPane.showMessageDialog(homefram, "Invalid input in cells. Please make sure all cells contain valid numbers.");
                }
            });

            // Add ComboBox and Print button to the same row
            JPanel comboPanel = new JPanel();
            comboPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));  // Align combo box and button side by side
            comboPanel.setBackground(Color.decode("#101a43"));  // Same background as the main panel
            comboPanel.add(matrixComboBox);
            comboPanel.add(saveButton);

            // Add comboPanel to layout
            gbc.gridx = 0;
            gbc.gridy = i * 3;  // Adjusted position
            Panel3.add(comboPanel, gbc);

            // Create empty matrix fields for user input
            // Create empty matrix fields for user input
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    matrix[r][c] = new JTextField();  // Larger text fields
                    matrix[r][c].setFont(new Font("Roboto", Font.PLAIN, 18));  // Modern font for input fields
                    matrix[r][c].setHorizontalAlignment(SwingConstants.CENTER);
                    matrix[r][c].setBackground(Color.decode("#FFFFFF"));
                    matrix[r][c].setBorder(BorderFactory.createLineBorder(Color.decode("#2980B9")));

                    // Apply the KeyListener here
                    matrix[r][c].addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            char c = e.getKeyChar();
                            JTextField cell = (JTextField) e.getSource();

                            if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_MINUS && c != KeyEvent.VK_PERIOD
                                    && c != '(' && c != ')' && c != '+' && c != '-' && c != '*' && c != '/' && c != '!') {
                                e.consume();
                            }
                            if (c == '-' && cell.getText().length() > 0) {
                                e.consume();
                            }
                            if (c == '.' && cell.getText().contains(".")) {
                                e.consume();
                            }
                            if (c == 'q' || c == 'Q') {
                                cell.setText(cell.getText() + "√");
                            }
                            if (c == 'f' || c == 'F') {
                                cell.setText(cell.getText() + "!");
                            }
                            if (c == 's' || c == 'S') {
                                cell.setText(cell.getText() + "sin(");
                            }
                            if (c == 'c' || c == 'C') {
                                cell.setText(cell.getText() + "cos(");
                            }
                            if (c == 't' || c == 'T') {
                                cell.setText(cell.getText() + "tan(");
                            }
                            if (c == '(' && !cell.getText().contains("(")) {
                                e.consume();
                                cell.setText(cell.getText() + "(");
                            }
                            if (c == ')' && !cell.getText().contains(")")) {
                                e.consume();
                                cell.setText(cell.getText() + ")");
                            }
                            if (c == '+' || c == '-' || c == '*' || c == '/') {
                                e.consume();
                                if (cell.getText().length() > 0 && !isOperatorAtEnd(cell)) {
                                    cell.setText(cell.getText() + c);
                                }
                            }
                            // التحقق من عدد الأقواس المفتوحة والمغلقة
                            openCount = countOccurrences(cell.getText(), '(');
                            closeCount = countOccurrences(cell.getText(), ')');
                        }

                        private boolean isOperatorAtEnd(JTextField cell) {
                            String text = cell.getText();
                            if (text.isEmpty()) {
                                return false;
                            }
                            char lastChar = text.charAt(text.length() - 1);
                            return lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/';
                        }

                        // دالة لحساب عدد مرات ظهور الرمز في النص
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

                    // Add the matrix cell to the panel
                    matrixPanel.add(matrix[r][c]);
                }
            }

            // Add matrix panel to layout
            gbc.gridx = 0;
            gbc.gridy = i * 3 + 1;  // Adjusted position to make room for buttons
            Panel3.add(matrixPanel, gbc);

            allmatrices.add(matrix);
        }

        // Create a panel to contain the buttons side by side with modern look
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 15)); // Adjust spacing for buttons
        buttonPanel.setBackground(Color.decode("#101a43")); // Same background as the main panel

        // "Calculate" button with improved design
        JButton calculateButton = new JButton("Calculate");
        calculateButton.setFont(new Font("Roboto", Font.BOLD, 20));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setBackground(Color.decode("#01baff"));
        calculateButton.setPreferredSize(new Dimension(150, 55));  // Larger button size
        calculateButton.setFocusPainted(false);
        calculateButton.setBorder(BorderFactory.createLineBorder(Color.decode("#16A085")));
        calculateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        calculateButton.addActionListener(e -> performCalculation());

        // "Back" button with updated design
        backButton = new JButton("Back");
        backButton.setFont(new Font("Roboto", Font.BOLD, 20));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.decode("#831574"));
        backButton.setPreferredSize(new Dimension(150, 55));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            Panel3.setVisible(false);
            Panel2.ShowFrame2(matrixPanels.size());
        });

        buttonPanel.add(calculateButton);
        buttonPanel.add(backButton);

        // Add the button panel to the layout
        gbc.gridy = matrixCount * 3 + 1;  // Position buttons after all matrices
        gbc.gridwidth = 2;  // Span across two columns
        Panel3.add(buttonPanel, gbc);

        // Increase frame size to provide more space for matrices and buttons
        homefram.setTitle("Enter Matrix Values");
        homefram.setLayout(new BorderLayout());
        homefram.add(Panel3, BorderLayout.CENTER);
        homefram.setSize(550, 650);  // Larger frame size for better design
        homefram.setVisible(true);
    }

    private double[][] saveMatrixTo2DArray(int rows, int cols, JTextField[][] matrix) {
        double[][] matrixValues = new double[rows][cols];

        // Loop through each cell in the matrix
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JTextField cellField = matrix[i][j];  // Get the correct JTextField for this cell
                String cellText = cellField.getText().trim();  // Get text and trim extra spaces

                double result;
                try {
                    // Evaluate the expression and store the result in the matrix
                    result = evaluateExpression(cellText);
                    matrixValues[i][j] = result;
                } catch (Exception e) {
                    // Show an error message if the input is invalid
                    JOptionPane.showMessageDialog(homefram, "Invalid input in cell (" + (i + 1) + ", " + (j + 1) + "). Please enter valid numbers.");
                    History.x = 1;  // Set the error status
                    return null;  // Exit the method if there's an error
                }
            }
        }

        return matrixValues;  // Return the valid matrix
    }

    public void update() {
        matrixComboBox.removeAllItems();  // إزالة جميع العناصر الحالية
        matrixComboBox.addItem("Select Matrix");  // إضافة خيار "Select Matrix"
        // إضافة جميع أسماء المصفوفات إلى الـ JComboBox
        for (String name : matrices.keySet()) {
            matrixComboBox.addItem(name);
        }
    }

    private void updateMatrixFields(JTextField[][] targetFields, double[][] matrix) {
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[0].length; c++) {
                targetFields[r][c].setText(String.valueOf(matrix[r][c]));
            }
        }
    }

    public static void ShowPanel3() {
        Panel3.setVisible(true);
        homefram.revalidate();
    }

    private void performCalculation() {
        try {
            int matrixCount = allmatrices.size();
            double[][] matrix1 = null, matrix2 = null, matrix3 = null;

            // تحقق من صحة القيم في كل مصفوفة
            for (int i = 0; i < matrixCount; i++) {
                JTextField[][] matrixFields = allmatrices.get(i);
                int rows = Integer.parseInt(rowsList.get(i));
                int cols = Integer.parseInt(colsList.get(i));

                // التحقق من جميع الخلايا في المصفوفة
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {
                        String cellValue = matrixFields[r][c].getText().trim();
                        if (!cellValue.isEmpty()) {
                            try {
                                // تقييم التعبير الرياضي
                                double result = evaluateExpression(cellValue);
                                matrixFields[r][c].setText(String.valueOf(result));  // تحديث الخلية بالقيمة المحسوبة
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(Panel3, "Error in cell (" + r + ", " + c + "): " + ex.getMessage());
                                return;  // توقف إذا كان هناك خطأ في تقييم التعبير
                            }
                        }
                    }
                }

                // تحويل المصفوفات المدخلة إلى مصفوفات رقمية بعد التحقق من صحتها
                if (i == 0) {
                    matrix1 = getMatrixValues(allmatrices.get(0), Integer.parseInt(rowsList.get(0)), Integer.parseInt(colsList.get(0)));
                } else if (i == 1) {
                    matrix2 = getMatrixValues(allmatrices.get(1), Integer.parseInt(rowsList.get(1)), Integer.parseInt(colsList.get(1)));
                } else if (i == 2) {
                    matrix3 = getMatrixValues(allmatrices.get(2), Integer.parseInt(rowsList.get(2)), Integer.parseInt(colsList.get(2)));
                }
            }

            // إجراء العمليات الحسابية بناءً على المصفوفات المدخلة
            double[][] resultMatrix = null;
            if (matrixCount == 2) {
                if (selectedOperation.equals("Addition")) {
                    resultMatrix = addMatrices(matrix1, matrix2);
                } else if (selectedOperation.equals("Subtraction")) {
                    resultMatrix = subtractMatrices(matrix1, matrix2);
                } else {
                    resultMatrix = multiplyMatrices(matrix1, matrix2);
                }
            } else if (matrixCount == 3) {
                if (selectedOperation.equals("Addition")) {
                    resultMatrix = addMatrices(matrix1, matrix2);
                    resultMatrix = addMatrices(resultMatrix, matrix3);
                } else if (selectedOperation.equals("Subtraction")) {
                    resultMatrix = subtractMatrices(matrix1, matrix2);
                    resultMatrix = subtractMatrices(resultMatrix, matrix3);
                } else {
                    resultMatrix = multiplyMatrices(matrix1, matrix2);
                    resultMatrix = multiplyMatrices(resultMatrix, matrix3);
                }
            } else {
                JOptionPane.showMessageDialog(Panel3, "The number of entered matrices is invalid.");
            }

            showResult(resultMatrix);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(Panel3, "Error: Make sure all values are entered correctly.");
        }
    }

    private double[][] getMatrixValues(JTextField[][] matrixFields, int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                matrix[r][c] = Double.parseDouble(matrixFields[r][c].getText().trim());
            }
        }
        return matrix;
    }

    private double[][] addMatrices(double[][] matrix1, double[][] matrix2) {
        int rows = matrix1.length;
        int cols = matrix1[0].length;
        double[][] result = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[r][c] = matrix1[r][c] + matrix2[r][c];
            }
        }
        return result;
    }

    private double[][] subtractMatrices(double[][] matrix1, double[][] matrix2) {
        int rows = matrix1.length;
        int cols = matrix1[0].length;
        double[][] result = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[r][c] = matrix1[r][c] - matrix2[r][c];
            }
        }
        return result;
    }

    private double[][] multiplyMatrices(double[][] matrix1, double[][] matrix2) {
        int rows = matrix1.length;
        int cols = matrix2[0].length;
        double[][] result = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    result[r][c] += matrix1[r][k] * matrix2[k][c];
                }
            }
        }
        return result;
    }

    private void showResult(double[][] resultMatrix) {
        JFrame resultFrame = new JFrame("Result of the mathematical operation");
        resultFrame.setLayout(new GridBagLayout());
        resultFrame.setSize(300, 300);
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.setLocationRelativeTo(null);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Dimension cellSize = new Dimension(85, 85);
        for (int r = 0; r < resultMatrix.length; r++) {
            for (int c = 0; c < resultMatrix[0].length; c++) {
                JLabel resultLabel = new JLabel(String.valueOf(resultMatrix[r][c]));
                resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
                resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
                resultLabel.setBackground(Color.decode("#FFFAF0"));
                resultLabel.setOpaque(true);
                resultLabel.setPreferredSize(cellSize);

                gbc.gridx = c;
                gbc.gridy = r;
                resultFrame.add(resultLabel, gbc);
            }
        }

        resultFrame.setVisible(true);
        addMatrixToMatrices(resultMatrix);
    }

}
