package matrixx.pages.history_Page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import static matrixx.SoundPlayer.playSound;
import static matrixx.logic.equal.evaluateExpression;
import static matrixx.pages.history_Page.History.ShowHistoryPage;
import static matrixx.pages.history_Page.History.matrices;
import static matrixx.pages.home_page.HomePage.homefram;

public class NewMatrixPage extends JPanel {

    private JComboBox<String> rowsComboBox;
    private JComboBox<String> columnsComboBox;
    private JPanel matrixInputPanel;
    private JButton saveMatrixButton;
    private JTextField[][] matrixFields;
    int x = 0;
    Consumer<String> navigateBackk;

    public static OnMatrixSaveListener saveListener;

    public interface OnMatrixSaveListener {

        void onMatrixSave(double[][] matrix);
    }

    public NewMatrixPage(OnMatrixSaveListener listener, Color backColor, Consumer<String> navigateBack) {
        navigateBackk = navigateBack;
        this.saveListener = listener;
        setLayout(new BorderLayout());
        setBackground(backColor);
        initializeComponents(navigateBack);
    }

    public void initializeComponents(Consumer<String> navigateBack) {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.decode("#101a43"));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            HideNewMatrixPage();
            navigateBack.accept("home");
        });
        topPanel.add(backButton, BorderLayout.WEST);

        JPanel dimensionsPanel = new JPanel();
        dimensionsPanel.setBackground(Color.decode("#101a43"));

        rowsComboBox = new JComboBox<>(new String[]{"2", "3", "4"});
        columnsComboBox = new JComboBox<>(new String[]{"2", "3", "4"});

        JLabel rawJLabel = new JLabel("rows :");
        rawJLabel.setForeground(Color.WHITE);

        JLabel colJLabel = new JLabel("columns :");
        colJLabel.setForeground(Color.WHITE);

        dimensionsPanel.add(rawJLabel);
        dimensionsPanel.add(rowsComboBox);
        dimensionsPanel.add(colJLabel);
        dimensionsPanel.add(columnsComboBox);

        JButton confirmDimensionsButton = new JButton("Confirming");
        confirmDimensionsButton.addActionListener(e -> setupMatrixFields());
        dimensionsPanel.add(confirmDimensionsButton);

        JButton randomizeMatrixButton = new JButton("Random");
        randomizeMatrixButton.addActionListener(e -> randomizeMatrixValues());
        dimensionsPanel.add(randomizeMatrixButton);

        topPanel.add(dimensionsPanel, BorderLayout.CENTER);

        homefram.add(topPanel, BorderLayout.NORTH);
    }

    private void randomizeMatrixValues() {
        if (matrixFields == null) {
            JOptionPane.showMessageDialog(this, "Please confirm dimensions first!", "Message", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Random random = new Random();
        int min = -1000;
        int max = 1000;

        for (int i = 0; i < matrixFields.length; i++) {
            for (int j = 0; j < matrixFields[i].length; j++) {
                // تعيين قيمة عشوائية بين -10 و 10
                double randomValue = random.nextInt(max - min + 1) + min;
                matrixFields[i][j].setText(String.format("%.2f", randomValue));
            }
        }
    }

    private void setupMatrixFields() {
        if (matrixInputPanel != null) {
            homefram.remove(matrixInputPanel);
        }
        if (saveMatrixButton != null) {
            homefram.remove(saveMatrixButton);
        }

        int rows = Integer.parseInt((String) rowsComboBox.getSelectedItem());
        int columns = Integer.parseInt((String) columnsComboBox.getSelectedItem());

        int fontSize = calculateFontSize(rows, columns);

        matrixInputPanel = new JPanel(new GridLayout(rows, columns, 5, 5));
        matrixInputPanel.setBackground(Color.LIGHT_GRAY);
        matrixFields = new JTextField[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JTextField field = new JTextField(5);
                field.setFont(new Font("Arial", Font.PLAIN, fontSize));
                field.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();

                        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_MINUS && c != KeyEvent.VK_PERIOD
                                && c != '(' && c != ')' && c != '+' && c != '-' && c != '*' && c != '/' && c != '!') {
                            e.consume();
                        }
                        if (c == '-' && field.getText().length() > 0) {
                            e.consume();
                        }
                        if (c == '.' && field.getText().contains(".")) {
                            e.consume();
                        }
                        if (c == 'q' || c == 'Q') {
                            field.setText(field.getText() + "√");
                        }
                        if (c == 'f' || c == 'F') {
                            field.setText(field.getText() + "!");
                        }
                        if (c == 's' || c == 'S') {
                            field.setText(field.getText() + "sin(");
                        }
                        if (c == 'c' || c == 'C') {
                            field.setText(field.getText() + "cos(");
                        }
                        if (c == 't' || c == 'T') {
                            field.setText(field.getText() + "tan(");
                        }
                        if (c == '(' && !field.getText().contains("(")) {
                            e.consume();
                            field.setText(field.getText() + "(");
                        }
                        if (c == ')' && !field.getText().contains(")")) {
                            e.consume();
                            field.setText(field.getText() + ")");
                        }
                        if (c == '+' || c == '-' || c == '*' || c == '/') {
                            e.consume();
                            if (field.getText().length() > 0 && !isOperatorAtEnd(field)) {
                                field.setText(field.getText() + c);
                            }
                        }

                        int openCount = countOccurrences(field.getText(), '(');
                        int closeCount = countOccurrences(field.getText(), ')');

                        if (openCount != closeCount) {
                            field.setBackground(Color.RED);
                        } else {
                            field.setBackground(Color.WHITE);
                        }
                    }

                    private boolean isOperatorAtEnd(JTextField field) {
                        String text = field.getText();
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
                matrixFields[i][j] = field;
                matrixInputPanel.add(field);
            }
        }

        homefram.add(matrixInputPanel, BorderLayout.CENTER);

        saveMatrixButton = new JButton("Save Matrix");
        saveMatrixButton.setFont(new Font("Arial", Font.PLAIN, fontSize));
        saveMatrixButton.addActionListener(new SaveMatrixAction());
        homefram.add(saveMatrixButton, BorderLayout.SOUTH);

        homefram.revalidate();
        homefram.repaint();
    }

    public static int calculateFontSize(int rows, int columns) {
        int totalCells = rows * columns;
        int fontSize = 20;

        if (totalCells <= 4) {
            fontSize = 40;
        } else if (totalCells <= 9) {
            fontSize = 30;
        } else {
            fontSize = 20;
        }

        return fontSize;
    }

    public class SaveMatrixAction implements ActionListener {

        public boolean validateBeforeSave() {
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

        @Override
        public void actionPerformed(ActionEvent e) {

            if (validateBeforeSave()) {
                int rows = matrixFields.length;
                int columns = matrixFields[0].length;
                double[][] newMatrix = new double[rows][columns];
                x = 0;
                try {
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                            String expression = matrixFields[i][j].getText();
                            try {
                                double resultt = evaluateExpression(expression);
                                newMatrix[i][j] = resultt;
                            } catch (Exception ex) {
                                showMessageDialog(homefram, "Error evaluating expression: " + expression, "Message", JOptionPane.ERROR_MESSAGE);
                                x = 1;
                            }
                        }
                    }

                    if (x == 0) {
                        String newMatrixName = null;
                        boolean nameExists = true;

                        while (nameExists) {
                            newMatrixName = JOptionPane.showInputDialog(columnsComboBox, "Enter a name for the new matrix:", " Message", JOptionPane.QUESTION_MESSAGE);

                            if (newMatrixName == null || newMatrixName.trim().isEmpty()) {
                                JOptionPane.showMessageDialog(columnsComboBox, "Matrix name cannot be empty.");
                                return;
                            }

                            if (matrices.containsKey(newMatrixName)) {
                                // إنشاء خيارات الأزرار
                                int option = JOptionPane.showOptionDialog(
                                        columnsComboBox,
                                        "A matrix with this name already exists. Do you want to update it?",
                                        "Matrix Exists",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.WARNING_MESSAGE,
                                        null,
                                        new Object[]{"Cancel", "Update"},
                                        "Cancel"
                                );

                                if (option == 1) { // 1 يعني "Update"
                                    matrices.put(newMatrixName, newMatrix); // تحديث القيم في المصفوفة الحالية
                                    History.update(); // تحديث السجل

                                    // تشغيل صوت حفظ المصفوفة بنجاح
                                    new Thread(() -> playSound("matrix/src/main/java/sound/The_array_has_been_saved_successfully.wav")).start();
                                    JOptionPane.showMessageDialog(NewMatrixPage.this, "The matrix has been updated successfully.", "Message", JOptionPane.INFORMATION_MESSAGE);

                                    if (saveListener != null) {
                                        saveListener.onMatrixSave(newMatrix);
                                    }
                                    HideNewMatrixPage();
                                    ShowHistoryPage();
                                    return;
                                }
                            } else {
                                nameExists = false;
                            }
                        }

                        // حفظ المصفوفة الجديدة في حالة الاسم غير موجود مسبقاً
                        matrices.put(newMatrixName, newMatrix);
                        History.update();

                        if (saveListener != null) {
                            saveListener.onMatrixSave(newMatrix);
                            new Thread(() -> playSound("matrix/src/main/java/sound/The_array_has_been_saved_successfully.wav")).start();
                            JOptionPane.showMessageDialog(NewMatrixPage.this, "The array has been saved successfully.", "Message", JOptionPane.INFORMATION_MESSAGE);
                            HideNewMatrixPage();
                            navigateBackk.accept("home");
                        } else {
                            new Thread(() -> playSound("matrix/src/main/java/sound/The_listener.wav")).start();
                            JOptionPane.showMessageDialog(NewMatrixPage.this, "The listener has not been assigned to save the array.", " Message", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException ex) {
                    new Thread(() -> playSound("matrix/src/main/java/sound/Please_ensure.wav")).start();
                    JOptionPane.showMessageDialog(NewMatrixPage.this, "Please ensure that all inputs are valid numbers.");
                }
            }

        }

    }

    public static void ShowNewMatrixPage(Consumer<String> onBackPressed) {
        homefram.setTitle("Enter a new matrix");
        homefram.setLayout(new BorderLayout());

        homefram.getContentPane().add(new NewMatrixPage(matrix -> {
            System.out.println("The matrix has been saved:" + java.util.Arrays.deepToString(matrix));
        }, Color.decode("#101a43"), onBackPressed)); // تعديل هنا لاستخدام onBackPressed

        homefram.revalidate();
        homefram.repaint();
    }

    public static void HideNewMatrixPage() {
        // إخفاء جميع المكونات داخل الـ JFrame
        Component[] components = homefram.getContentPane().getComponents();
        for (Component component : components) {
            component.setVisible(false);
        }
        homefram.revalidate();
        homefram.repaint();
    }
}
