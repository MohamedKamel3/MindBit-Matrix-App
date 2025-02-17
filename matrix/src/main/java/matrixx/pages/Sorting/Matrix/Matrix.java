package matrixx.pages.Sorting.Matrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.lang.Double.parseDouble;
import java.util.Arrays;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import static matrixx.logic.equal.evaluateExpression;
import static matrixx.pages.Sorting.Matrix.MatrixInputPage.HideAll;
import static matrixx.pages.Sorting.Matrix.MatrixInputPage.ShowMatrixInput;
import matrixx.pages.history_Page.History;
import static matrixx.pages.history_Page.History.addMatrixToMatrices;
import static matrixx.pages.history_Page.History.matrices;
import static matrixx.pages.home_page.HomePage.homefram;

public class Matrix implements ActionListener {

    JPanel topPanel;
    JPanel matrixPanel;
    JComboBox<String> selectMatrixCombo;
    JComboBox<String> selectSortCombo;
    JTextField[][] matrixFields;
    JPanel bottomPanel;
    JButton backButton;
    JButton saveButton;
    JButton sortButton;
    JButton randomButton; // زر القيم العشوائية
    JLabel timerLabel;
    public int openCount;
    public int closeCount;

    public Matrix(int n, int m) {
        homefram.setLayout(new BorderLayout());
        // اللوحة العلوية
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        selectMatrixCombo = new JComboBox<>();
        selectMatrixCombo.addItem("Select Matrix");
        selectMatrixCombo.addActionListener(e -> {
            try {
                String selectedMatrix = (String) selectMatrixCombo.getSelectedItem();
                if (!"Select Matrix".equals(selectedMatrix)) {

                    double[][] matrix = matrices.get(selectedMatrix);
                    int matrixRows = matrix.length;
                    int matrixColumns = matrix[0].length;

                    if (matrixRows != matrixFields.length || matrixColumns != matrixFields[0].length) {
                        JOptionPane.showMessageDialog(matrixPanel, "Matrix dimensions do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                        selectMatrixCombo.setSelectedItem("Select Matrix");
                        return;
                    }

                    for (int i = 0; i < matrixRows; i++) {
                        for (int j = 0; j < matrixColumns; j++) {
                            matrixFields[i][j].setText(String.valueOf(matrix[i][j]));
                        }
                    }

                    JOptionPane.showMessageDialog(matrixPanel, "Matrix loaded successfully.");
                }
            } catch (Exception ex) {

            }
        });

        selectSortCombo = new JComboBox<>(new String[]{"Select Sort", "BubbleSort", "InsertionSort", "SelectionSort", "MergeSort", "QuickSort"});
        topPanel.add(selectMatrixCombo);
        topPanel.add(selectSortCombo);

        update();

        // اللوحة الوسطى (المصفوفة)
        matrixPanel = new JPanel();
        matrixPanel.setLayout(new GridLayout(m, n, 5, 5)); // عدد الصفوف والأعمدة من القيم n و m
        matrixFields = new JTextField[m][n];
        int fontSize = calculateFontSize(matrixFields.length, matrixFields[0].length);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                final int row = i;  // Capture the row index as final
                final int col = j;  // Capture the column index as final
                matrixFields[i][j] = new JTextField();
                matrixFields[i][j].setFont(new Font("Arial", Font.PLAIN, fontSize));
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

                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        // Use row and col here
                        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_MINUS && c != KeyEvent.VK_PERIOD
                                && c != '(' && c != ')' && c != '+' && c != '-' && c != '*' && c != '/' && c != '!') {
                            e.consume();
                        }
                        if (c == '-' && matrixFields[row][col].getText().length() > 0) {
                            e.consume();
                        }
                        if (c == '.' && matrixFields[row][col].getText().contains(".")) {
                            e.consume();
                        }
                        if (c == 'q' || c == 'Q') {
                            matrixFields[row][col].setText(matrixFields[row][col].getText() + "√");
                        }
                        if (c == 'f' || c == 'F') {
                            matrixFields[row][col].setText(matrixFields[row][col].getText() + "!");
                        }
                        if (c == 's' || c == 'S') {
                            matrixFields[row][col].setText(matrixFields[row][col].getText() + "sin(");
                        }
                        if (c == 'c' || c == 'C') {
                            matrixFields[row][col].setText(matrixFields[row][col].getText() + "cos(");
                        }
                        if (c == 't' || c == 'T') {
                            matrixFields[row][col].setText(matrixFields[row][col].getText() + "tan(");
                        }
                        if (c == '(' && !matrixFields[row][col].getText().contains("(")) {
                            e.consume();
                            matrixFields[row][col].setText(matrixFields[row][col].getText() + "(");
                        }
                        if (c == ')' && !matrixFields[row][col].getText().contains(")")) {
                            e.consume();
                            matrixFields[row][col].setText(matrixFields[row][col].getText() + ")");
                        }
                        if (c == '+' || c == '-' || c == '*' || c == '/') {
                            e.consume();
                            if (matrixFields[row][col].getText().length() > 0 && !isOperatorAtEnd(matrixFields[row][col])) {
                                matrixFields[row][col].setText(matrixFields[row][col].getText() + c);
                            }
                        }
                        // التحقق من عدد الأقواس المفتوحة والمغلقة
                        openCount = countOccurrences(matrixFields[row][col].getText(), '(');
                        closeCount = countOccurrences(matrixFields[row][col].getText(), ')');
                    }

                    private boolean isOperatorAtEnd(JTextField cell) {
                        String text = cell.getText();
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
                matrixPanel.add(matrixFields[i][j]);
            }
        }

        matrixPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // اللوحة السفلية
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        backButton = new JButton("Back");
        saveButton = new JButton("Save");
        sortButton = new JButton("Sort");
        randomButton = new JButton("Random"); // إنشاء زر القيم العشوائية
        timerLabel = new JLabel("Time Elapsed:"); // الزمن الافتراضي

        backButton.addActionListener(this);
        sortButton.addActionListener(this); // إضافة مستمع لزر الفرز
        randomButton.addActionListener(this); // إضافة مستمع لزر القيم العشوائية
        saveButton.addActionListener(this);

        bottomPanel.add(backButton);
        bottomPanel.add(saveButton);
        bottomPanel.add(sortButton);
        bottomPanel.add(randomButton); // إضافة زر القيم العشوائية إلى اللوحة
        // bottomPanel.add(timerLabel);

        // إضافة اللوحات إلى الإطار
        homefram.add(topPanel, BorderLayout.NORTH);
        homefram.add(matrixPanel, BorderLayout.CENTER);
        homefram.add(bottomPanel, BorderLayout.SOUTH);
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

    public void update() {
        selectMatrixCombo.removeAllItems();  // إزالة جميع العناصر الحالية
        selectMatrixCombo.addItem("Select Matrix");  // إضافة خيار "Select Matrix"
        // إضافة جميع أسماء المصفوفات إلى الـ JComboBox
        for (String name : matrices.keySet()) {
            selectMatrixCombo.addItem(name);
        }
    }

    private double[][] evaluate(int rows, int cols, double[][] matrix) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                String cellText = matrixFields[i][j].getText();
                double result;
                try {
                    result = evaluateExpression(cellText);
                    matrix[i][j] = result;
                    matrixFields[i][j].setBackground(Color.WHITE); // تعيين اللون الأبيض للخلفية عند النجاح
                    matrixFields[i][j].setForeground(Color.BLACK); // تعيين النص باللون الأسود عند النجاح

                } catch (Exception e) {
                    matrixFields[i][j].setBackground(Color.RED); // تعيين اللون الأحمر في حالة الخطأ
                    matrixFields[i][j].setForeground(Color.BLACK); // تعيين النص باللون الأسود عند النجاح
                    History.x = 1;
                }
            }
        }
        return matrix;
    }

    private void CheckEvaluate(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                String cellText = matrixFields[i][j].getText();
                double result;
                try {
                    result = evaluateExpression(cellText);
                    matrixFields[i][j].setText(result + "");
                    matrixFields[i][j].setBackground(Color.WHITE); // تعيين اللون الأبيض للخلفية عند النجاح
                    matrixFields[i][j].setForeground(Color.BLACK); // تعيين النص باللون الأسود عند النجاح

                } catch (Exception e) {
                    matrixFields[i][j].setBackground(Color.RED); // تعيين اللون الأحمر في حالة الخطأ
                    matrixFields[i][j].setForeground(Color.BLACK); // تعيين النص باللون الأسود عند النجاح
                    History.x = 1;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == backButton) {
            HideAll();
            ShowMatrixInput();
        }
        if (e.getSource() == saveButton) {
            History.x = 0;
            int rows = matrixFields.length;
            int cols = matrixFields[0].length;
            double[][] matrix = new double[rows][cols];
            matrix = evaluate(rows, cols, matrix);
            if (matrix != null) {
                addMatrixToMatrices(matrix);
            }
        }

        if (e.getSource() == sortButton) {
            String selectedSort = (String) selectSortCombo.getSelectedItem();
            if (selectedSort == null || selectedSort.equals("Select Sort")) {
                JOptionPane.showMessageDialog(matrixPanel, "Please select a sort method!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // تحويل القيم من حقول النص إلى مصفوفة
            int rows = matrixFields.length;
            int cols = matrixFields[0].length;
            double[] flatArray = new double[rows * cols];
            int index = 0;

            CheckEvaluate(rows, cols);
            try {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        flatArray[index++] = parseDouble(matrixFields[i][j].getText());
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(matrixPanel, "Invalid input in matrix fields!", "Error", JOptionPane.ERROR_MESSAGE);

                return;
            }

            // تسجيل الزمن قبل الفرز
            long startTime = System.nanoTime();

            // تطبيق نوع الفرز
            switch (selectedSort) {
                case "BubbleSort":
                    bubbleSort(flatArray);
                    break;
                case "InsertionSort":
                    insertionSort(flatArray);
                    break;
                case "SelectionSort":
                    selectionSort(flatArray);
                    break;
                case "MergeSort":
                    flatArray = mergeSort(flatArray);
                    break;
                case "QuickSort":
                    quickSort(flatArray, 0, flatArray.length - 1);
                    break;
            }

            // تسجيل الزمن بعد الفرز
            long endTime = System.nanoTime();

            // حساب الزمن المستغرق
            long elapsedTimeNano = endTime - startTime; // بالنانوثانية
            long elapsedTimeMillis = elapsedTimeNano / 1_000_000; // تحويل إلى ملي ثانية

            // تحديث القيم في الحقول النصية
            index = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrixFields[i][j].setText(String.valueOf(flatArray[index++]));
                }
            }

            // عرض الزمن المستغرق
            JOptionPane.showMessageDialog(matrixPanel, "Time Elapsed: " + elapsedTimeMillis + " ms (" + elapsedTimeNano + " ns)", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getSource() == randomButton) {
            // تعبئة المصفوفة بقيم عشوائية
            Random random = new Random();
            int rows = matrixFields.length;
            int cols = matrixFields[0].length;
            int min = -1000;
            int max = 1000;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int randomValue = random.nextInt(max - min + 1) + min; // توليد رقم عشوائي بين min و max
                    matrixFields[i][j].setText(String.valueOf(randomValue));
                }
            }

            JOptionPane.showMessageDialog(matrixPanel, "Random values generated.", "Message", JOptionPane.INFORMATION_MESSAGE);

        }
    }

    // خوارزميات الفرز
    private void bubbleSort(double[] array) {
        Timer timer = new Timer(300, null); // مؤقت لتحديث واجهة المستخدم كل 300 مللي ثانية
        int[] indices = {0, 0}; // المؤشرات الحالية
        int[] iteration = {0}; // تتبع عدد التكرارات

        timer.addActionListener(e -> {
            if (indices[1] >= array.length - 1 - iteration[0]) {
                indices[1] = 0;
                iteration[0]++;

                // تعيين العنصر الأخير في الدورة الحالية إلى اللون الأخضر (مرتب)
                if (iteration[0] < array.length) {
                    int sortedIndex = array.length - iteration[0];
                    matrixFields[sortedIndex / matrixFields[0].length][sortedIndex % matrixFields[0].length].setBackground(Color.GREEN);
                }
            }

            if (iteration[0] >= array.length - 1) {
                // تعيين جميع العناصر كمرتبة (أخضر)
                for (int i = 0; i < array.length; i++) {
                    matrixFields[i / matrixFields[0].length][i % matrixFields[0].length].setBackground(Color.GREEN);
                }
                ((Timer) e.getSource()).stop(); // إنهاء المؤقت عند اكتمال الفرز
                JOptionPane.showMessageDialog(matrixPanel, "Sorting Complete!", "Message", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // مقارنة العنصرين
            int i = indices[1];
            matrixFields[i / matrixFields[0].length][i % matrixFields[0].length].setBackground(Color.YELLOW); // العنصر الأول قيد المقارنة
            matrixFields[(i + 1) / matrixFields[0].length][(i + 1) % matrixFields[0].length].setBackground(Color.YELLOW); // العنصر الثاني قيد المقارنة

            if (array[i] > array[i + 1]) {
                // تبديل العناصر
                double temp = array[i];
                array[i] = array[i + 1];
                array[i + 1] = temp;

                // تحديث الألوان للعناصر التي تم تبديلها
                matrixFields[i / matrixFields[0].length][i % matrixFields[0].length].setBackground(Color.RED);
                matrixFields[(i + 1) / matrixFields[0].length][(i + 1) % matrixFields[0].length].setBackground(Color.RED);
            }

            // تحديث القيم في الحقول النصية
            int index = 0;
            for (int row = 0; row < matrixFields.length; row++) {
                for (int col = 0; col < matrixFields[0].length; col++) {
                    matrixFields[row][col].setText(String.valueOf(array[index++]));
                    matrixFields[row][col].repaint(); // تحديث واجهة المستخدم
                    matrixFields[row][col].revalidate(); // ضمان تحديث الحقل
                }
            }

            indices[1]++;
        });

        timer.start(); // بدء الرسوم المتحركة
    }

    private void insertionSort(double[] array) {
        Timer timer = new Timer(300, null);
        int[] i = {1};
        int[] j = {i[0] - 1};
        double[] key = {array[i[0]]};

        timer.addActionListener(e -> {
            if (j[0] >= 0 && array[j[0]] > key[0]) {
                // تبديل العناصر
                array[j[0] + 1] = array[j[0]];

                // تحديث الألوان
                matrixFields[j[0] / matrixFields[0].length][j[0] % matrixFields[0].length].setBackground(Color.RED);
                matrixFields[(j[0] + 1) / matrixFields[0].length][(j[0] + 1) % matrixFields[0].length].setBackground(Color.YELLOW);

                j[0]--;
            } else {
                // إدراج العنصر في الموقع الصحيح
                array[j[0] + 1] = key[0];

                // إعادة تعيين الألوان
                for (int row = 0; row < matrixFields.length; row++) {
                    for (int col = 0; col < matrixFields[0].length; col++) {
                        matrixFields[row][col].setBackground(Color.WHITE);
                    }
                }

                i[0]++;
                if (i[0] < array.length) {
                    key[0] = array[i[0]];
                    j[0] = i[0] - 1;
                } else {
                    // إنهاء الفرز
                    ((Timer) e.getSource()).stop();
                    JOptionPane.showMessageDialog(matrixPanel, "Insertion Sort Complete!", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            // تحديث القيم في الحقول النصية
            int index = 0;
            for (int row = 0; row < matrixFields.length; row++) {
                for (int col = 0; col < matrixFields[0].length; col++) {
                    matrixFields[row][col].setText(String.valueOf(array[index++]));
                }
            }
        });

        timer.start();
    }

    private void selectionSort(double[] array) {
        Timer timer = new Timer(300, null);
        int[] i = {0};
        int[] j = {i[0] + 1};
        int[] minIndex = {i[0]};

        timer.addActionListener(e -> {
            if (j[0] < array.length) {
                // مقارنة العنصر الحالي مع العنصر الأدنى
                if (array[j[0]] < array[minIndex[0]]) {
                    minIndex[0] = j[0];
                }

                // تحديث الألوان
                matrixFields[j[0] / matrixFields[0].length][j[0] % matrixFields[0].length].setBackground(Color.YELLOW);
                j[0]++;
            } else {
                // تبديل العنصر الأدنى مع العنصر في الموضع i
                double temp = array[i[0]];
                array[i[0]] = array[minIndex[0]];
                array[minIndex[0]] = temp;

                // تحديث الألوان
                matrixFields[i[0] / matrixFields[0].length][i[0] % matrixFields[0].length].setBackground(Color.GREEN);
                matrixFields[minIndex[0] / matrixFields[0].length][minIndex[0] % matrixFields[0].length].setBackground(Color.WHITE);

                i[0]++;
                if (i[0] < array.length - 1) {
                    j[0] = i[0] + 1;
                    minIndex[0] = i[0];
                } else {
                    ((Timer) e.getSource()).stop();
                    JOptionPane.showMessageDialog(matrixPanel, "Selection Sort Complete!", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            // تحديث القيم في الحقول النصية
            int index = 0;
            for (int row = 0; row < matrixFields.length; row++) {
                for (int col = 0; col < matrixFields[0].length; col++) {
                    matrixFields[row][col].setText(String.valueOf(array[index++]));
                }
            }
        });

        timer.start();
    }

    private double[] mergeSort(double[] array) {
        if (array.length <= 1) {
            return array;
        }
        int mid = array.length / 2;
        double[] left = mergeSort(Arrays.copyOfRange(array, 0, mid));
        double[] right = mergeSort(Arrays.copyOfRange(array, mid, array.length));
        return merge(left, right);
    }

    private double[] merge(double[] left, double[] right) {
        double[] result = new double[left.length + right.length];
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }
        while (i < left.length) {
            result[k++] = left[i++];
        }
        while (j < right.length) {
            result[k++] = right[j++];
        }
        return result;
    }

    private void quickSort(double[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

    private int partition(double[] array, int low, int high) {
        double pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                double temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        double temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1;
    }

    // لتحديث الـ timerLabel بالزمن المنقضي
    public void updateTimerLabel(int elapsedSeconds) {
        timerLabel.setText("Time Elapsed: " + elapsedSeconds + "s");
    }
}
