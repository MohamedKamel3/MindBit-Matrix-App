package matrixx.pages.history_Page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import static matrixx.SoundPlayer.playSound;
import static matrixx.logic.equal.evaluateExpression;
import static matrixx.pages.history_Page.NewMatrixPage.ShowNewMatrixPage;
import static matrixx.pages.history_Page.NewMatrixPage.calculateFontSize;
import static matrixx.pages.home_page.HomePage.ShowHomePage;
import static matrixx.pages.home_page.HomePage.homefram;

public class History extends JFrame {

    public static JComboBox<String> matrixSelect;
    public static JPanel matrixPanel;
    public static JPanel buttonPanel;
    public static JButton saveButton;
    public static JButton editButton;
    public static JButton cancelButton;
    public static JButton addNewMatrixButton;
    public static JButton backButton;
    public static JButton deleteButton;

    public static HashMap<String, double[][]> matrices = new HashMap<>();
    public static JTextField[][] matrixFields;
    public static double[][] originalMatrix;
    public static boolean isEditing = false;

    public int openCount;
    public int closeCount;

    public static int x = 0;

    public History() {
        matrixSelect = new JComboBox<>();
        matrixSelect.addItem("Select Matrix");

        matrices.put("1", new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        matrices.put("2", new double[][]{{10, 11}, {13, 14}, {16, 17}});
        matrices.put("3", new double[][]{{1, 2, 3}, {5, 6, 7}});
        matrices.put("4", new double[][]{{1, 2}, {5, 6}});
        update();

        matrixPanel = new JPanel();
        matrixPanel.setBackground(Color.decode("#101a43"));

        saveButton = new JButton("Save");
        editButton = new JButton("Edit");
        cancelButton = new JButton("Cancel");
        addNewMatrixButton = new JButton("Add New Matrix");
        backButton = new JButton("Back");
        deleteButton = new JButton("Delete All Matrices");

        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        editButton.setVisible(false);

        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode("#101a43"));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(editButton);
        buttonPanel.add(addNewMatrixButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        matrixFields = new JTextField[4][4];

        matrixSelect.addActionListener(e -> {
            String selectedMatrix = (String) matrixSelect.getSelectedItem();
            if ("Select Matrix".equals(selectedMatrix)) {
                editButton.setVisible(false);
            } else {
                editButton.setVisible(true);
            }
            displayMatrix(selectedMatrix);
        });

        editButton.addActionListener(e -> {
            isEditing = true;
            saveButton.setVisible(true);
            cancelButton.setVisible(true);
            setMatrixEditable(true);

            // إخفاء القائمة وزر التعديل
            matrixSelect.setVisible(false);
            editButton.setVisible(false);
            addNewMatrixButton.setVisible(false);
            backButton.setVisible(false);
            deleteButton.setVisible(false);

        });

        saveButton.addActionListener(e -> {
            if (hasChanges()) {  // تحقق من وجود تغييرات قبل الحفظ
                if (validateBeforeSave()) {
                    saveMatrix();
                    if (x == 0) {
                        isEditing = false;
                        saveButton.setVisible(false);
                        cancelButton.setVisible(false);
                        setMatrixEditable(false);

                        // إعادة إظهار القائمة وزر التعديل
                        matrixSelect.setVisible(true);
                        addNewMatrixButton.setVisible(true);
                        backButton.setVisible(true);
                        deleteButton.setVisible(true);

                    }
                }
            } else {
                showMessageDialog(this, "No changes were made to the matrix.");
            }
        });

        cancelButton.addActionListener(e -> {

            restoreOriginalMatrix();
            isEditing = false;
            saveButton.setVisible(false);
            cancelButton.setVisible(false);
            setMatrixEditable(false);

            // إعادة إظهار القائمة وزر التعديل
            matrixSelect.setVisible(true);
            editButton.setVisible(true);
            addNewMatrixButton.setVisible(true);
            backButton.setVisible(true);
            deleteButton.setVisible(true);

        });

        addNewMatrixButton.addActionListener(e -> {
            try {
                addNewMatrix();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        deleteButton.addActionListener(e -> {
            deleteAllMatrices();
        });

        backButton.addActionListener(e -> {
            HideHistory();
            ShowHomePage();
        });
    }

    public static void ShowHistoryPage() {
        // تشغيل الصوت في خيط منفصل بحيث لا يؤثر على تحميل الصفحة
        new Thread(new Runnable() {
            @Override
            public void run() {
                playSound("matrix/src/main/java/sound/History_Page.wav"); // تأكد من استبدال "null" بمسار أو اسم الملف الصوتي الصحيح
            }
        }).start();

        // إعدادات الصفحة
        homefram.setTitle("History");
        homefram.setSize(500, 500);
        homefram.setLayout(new BorderLayout());

        // جعل الأزرار واللوحات مرئية
        buttonPanel.setVisible(true);
        matrixPanel.setVisible(true);
        matrixSelect.setVisible(true);

        // إضافة اللوحات إلى النافذة
        homefram.add(matrixSelect, BorderLayout.NORTH);
        homefram.add(matrixPanel, BorderLayout.CENTER);
        homefram.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void HideHistory() {
        buttonPanel.setVisible(false);
        matrixPanel.setVisible(false);
        matrixSelect.setVisible(false);
    }

    public void displayMatrix(String matrixName) {
        matrixPanel.removeAll();
        if (matrixName != null && matrices.containsKey(matrixName)) {
            double[][] matrix = matrices.get(matrixName);
            originalMatrix = new double[matrix.length][matrix[0].length];
            matrixFields = new JTextField[matrix.length][matrix[0].length];

            matrixPanel.setLayout(new GridLayout(matrix.length, matrix[0].length, 5, 5));

            int fontSize = calculateFontSize(matrix.length, matrix[0].length);

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    originalMatrix[i][j] = matrix[i][j];
                    JTextField cell = new JTextField(String.valueOf(matrix[i][j]));
                    cell.setHorizontalAlignment(JTextField.CENTER);
                    cell.setOpaque(true);
                    cell.setBackground(Color.white);
                    cell.setEditable(false);
                    cell.setFont(new Font("Arial", Font.PLAIN, fontSize));
                    cell.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            char c = e.getKeyChar();
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

                    matrixFields[i][j] = cell;
                    matrixPanel.add(cell);
                }
            }
        }
        matrixPanel.revalidate();
        matrixPanel.repaint();
    }

    public void restoreOriginalMatrix() {
        for (int i = 0; i < matrixFields.length; i++) {
            for (int j = 0; j < matrixFields[i].length; j++) {
                matrixFields[i][j].setText(String.valueOf(originalMatrix[i][j]));
                matrixFields[i][j].setBackground(new Color(255, 215, 0));
            }
        }
        cancelButton.setVisible(false);
    }

    public void setMatrixEditable(boolean editable) {
        for (int i = 0; i < matrixFields.length; i++) {
            for (int j = 0; j < matrixFields[i].length; j++) {
                if (matrixFields[i][j] != null) {
                    matrixFields[i][j].setEditable(editable);
                    if (editable) {
                        matrixFields[i][j].addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyReleased(KeyEvent e) {
                                JTextField field = (JTextField) e.getSource();
                                if (field.getText().trim().isEmpty()) {
                                    field.setBackground(Color.RED);
                                } else if (openCount != closeCount) {
                                    field.setBackground(Color.YELLOW);
                                } else {
                                    field.setBackground(Color.WHITE);
                                }
                            }
                        });
                    } else {
                        matrixFields[i][j].setBackground(Color.WHITE);
                    }
                }
            }
        }
    }

    public static boolean hasChanges() {
        for (int i = 0; i < matrixFields.length; i++) {
            for (int j = 0; j < matrixFields[i].length; j++) {
                if (!matrixFields[i][j].getText().equals(String.valueOf(originalMatrix[i][j]))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean validateBeforeSave() {
        for (int i = 0; i < matrixFields.length; i++) {
            for (int j = 0; j < matrixFields[i].length; j++) {
                if (matrixFields[i][j].getText().trim().isEmpty()) {
                    showMessageDialog(null, "Fill all matrix fields before saving.", " Message", JOptionPane.ERROR_MESSAGE);

                    return false;
                }
            }
        }
        return true;
    }

    public void saveMatrix() {
        x = 0;
        int rows = matrixFields.length;
        int columns = matrixFields[0].length;
        double[][] newMatrix = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String expression = matrixFields[i][j].getText();
                try {
                    double result = evaluateExpression(expression);
                    newMatrix[i][j] = result;
                } catch (Exception e) {
                    showMessageDialog(this, "Error evaluating expression: " + expression);
                    x = 1;
                }
            }
        }

        if (x == 0) {
            String newMatrixName = null;
            boolean nameExists = true;

            while (nameExists) {
                newMatrixName = JOptionPane.showInputDialog(this, "Enter a name for the new matrix:");

                if (newMatrixName == null) { // إذا تم الضغط على زر "Cancel"
                    matrixSelect.setSelectedItem("Select Matrix"); // اختيار الخيار "Select Matrix" بعد الحفظ
                    return;
                }

                if (newMatrixName.trim().isEmpty()) {
                    showMessageDialog(this, "Matrix name cannot be empty.");
                    return;
                }

                if (matrices.containsKey(newMatrixName)) {
                    int option = JOptionPane.showOptionDialog(
                            this,
                            "A matrix with this name already exists. Would you like to update it?",
                            "Matrix Exists",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new Object[]{"Update", "Cancel"},
                            "Update"
                    );

                    if (option == JOptionPane.YES_OPTION) {
                        matrices.put(newMatrixName, newMatrix); // تحديث القيم الجديدة بنفس الاسم
                        showMessageDialog(this, "Matrix " + newMatrixName + " updated successfully.");
                        update(); // تحديث قائمة المصفوفات في JComboBox
                        matrixSelect.setSelectedItem("Select Matrix");
                        return;
                    }
                } else {
                    nameExists = false;
                }
            }

            matrices.put(newMatrixName, newMatrix);
            showMessageDialog(this, "New matrix saved successfully as " + newMatrixName + ".");
            update(); // تحديث قائمة المصفوفات في JComboBox
            matrixSelect.setSelectedItem("Select Matrix"); // اختيار الخيار "Select Matrix" بعد الحفظ
        }
    }

    public void addNewMatrix() throws Exception {
        HideHistory();
        ShowNewMatrixPage(ignored -> ShowHistoryPage());
    }

    public void deleteAllMatrices() {
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete all matrices?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            matrices.clear();
            update();
            displayMatrix(null);
            showMessageDialog(this, "All matrices have been deleted.");
        }
    }

    public static void update() {
        matrixSelect.removeAllItems();  // إزالة جميع العناصر الحالية
        matrixSelect.addItem("Select Matrix");  // إضافة خيار "Select Matrix"
        // إضافة جميع أسماء المصفوفات إلى الـ JComboBox
        for (String name : matrices.keySet()) {
            matrixSelect.addItem(name);
        }
    }

    public static void addMatrixToMatrices(double[][] newMatrix) {
        if (x == 0) {
            String newMatrixName = null;
            boolean nameExists = true;

            while (nameExists) {
                newMatrixName = JOptionPane.showInputDialog(homefram, "Enter a name for the new matrix:");

                if (newMatrixName == null) { // إذا تم الضغط على زر "Cancel"
                    matrixSelect.setSelectedItem("Select Matrix"); // اختيار الخيار "Select Matrix" بعد الحفظ
                    return;
                }

                if (newMatrixName.trim().isEmpty()) {
                    showMessageDialog(homefram, "Matrix name cannot be empty.");
                    return;
                }

                if (matrices.containsKey(newMatrixName)) {
                    int option = JOptionPane.showOptionDialog(
                            homefram,
                            "A matrix with this name already exists. Would you like to update it?",
                            "Matrix Exists",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new Object[]{"Update", "Cancel"},
                            "Update"
                    );

                    if (option == JOptionPane.YES_OPTION) {
                        matrices.put(newMatrixName, newMatrix); // تحديث القيم الجديدة بنفس الاسم
                        showMessageDialog(homefram, "Matrix " + newMatrixName + " updated successfully.");
                        update(); // تحديث قائمة المصفوفات في JComboBox
                        matrixSelect.setSelectedItem("Select Matrix");
                        return;
                    }
                } else {
                    nameExists = false;
                }
            }

            matrices.put(newMatrixName, newMatrix);
            showMessageDialog(homefram, "New matrix saved successfully as " + newMatrixName + ".");
            update(); // تحديث قائمة المصفوفات في JComboBox
            matrixSelect.setSelectedItem("Select Matrix"); // اختيار الخيار "Select Matrix" بعد الحفظ
        }
    }

}
