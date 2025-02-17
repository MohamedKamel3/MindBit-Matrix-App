package matrixx.pages.Sorting.List;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import static matrixx.pages.Sorting.Matrix.MatrixInputPage.HideAll;
import static matrixx.pages.Sorting.Sorting.ShowSortings;
import static matrixx.pages.home_page.HomePage.homefram;

public class ListPage implements ActionListener {

    public static JPanel mainPanel;
    public static JTextField inputField;
    public static JLabel sortedListLabel;
    public static JLabel timeElapsedLabel;
    public static JLabel resultLabel;
    public static JComboBox<String> sortComboBox;
    public static JRadioButton ascendingRadioButton;
    public static JRadioButton descendingRadioButton;
    public static JButton loadFileButton;
    public static JButton saveButton;
    public static JButton sortButton;
    public static JButton backButton;

    public ListPage() {

        homefram.setLayout(new BorderLayout());
        homefram.setSize(500, 350);

        // Main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(16, 26, 67));

        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 1, 5, 5));
        topPanel.setBackground(new Color(16, 26, 67));

        // Sort ComboBox
        sortComboBox = new JComboBox<>(new String[]{
            "Compare Sort Times",
            "Bubble Sort",
            "Selection Sort",
            "Insertion Sort",
            "Merge Sort",
            "Quick Sort",
            "Counting Sort",
            "Heap Sort",
            "Bucket Sort",
            "Radix Sort",
            "Shell Sort"
        });
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setBackground(new Color(16, 26, 67));
        comboBoxPanel.add(sortComboBox);
        topPanel.add(comboBoxPanel);

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(16, 26, 67));
        inputField = new JTextField(30);  // تكبير الـ inputField
        inputField.setFont(new Font("Arial", Font.PLAIN, 18));  // تكبير الخط في inputField
        inputField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();

                // السماح للأرقام والفاصلة فقط
                if (!(Character.isDigit(c) || c == ',' || c == KeyEvent.VK_BACK_SPACE)) {
                    evt.consume(); // منع أي مدخل غير الأرقام أو الفواصل
                }

                // التحقق من عدم إدخال فاصلة متتالية
                String text = inputField.getText();
                if (c == ',' && (text.isEmpty() || text.charAt(text.length() - 1) == ',')) {
                    evt.consume(); // منع الفاصلة المتتالية
                }
            }
        });

        inputField.setToolTipText("Enter numbers separated by commas (e.g., 10, 5, 3, 8)");
        loadFileButton = new JButton("Load from File");
        loadFileButton.setFont(new Font("Arial", Font.PLAIN, 16));  // تكبير الخط في loadFileButton
        JLabel label = new JLabel("Enter Numbers:");
        label.setForeground(Color.white);
        label.setFont(new Font("Arial", Font.PLAIN, 16));  // تكبير الخط في JLabel
        inputPanel.add(label);
        inputPanel.add(inputField);
        inputPanel.add(loadFileButton);

        // Results panel
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new GridLayout(4, 1, 5, 5));
        resultsPanel.setBackground(new Color(16, 26, 67));

        sortedListLabel = new JLabel("Sorted List: ");
        sortedListLabel.setFont(new Font("Arial", Font.PLAIN, 16));  // تكبير الخط في sortedListLabel
        timeElapsedLabel = new JLabel("Time Elapsed: ");
        timeElapsedLabel.setFont(new Font("Arial", Font.PLAIN, 16));  // تكبير الخط في timeElapsedLabel
        resultLabel = new JLabel("Result: ");
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 16));  // تكبير الخط في resultLabel
        resultLabel.setForeground(Color.BLUE);

        resultsPanel.add(sortedListLabel);
        resultsPanel.add(timeElapsedLabel);
        resultsPanel.add(resultLabel);
        // Sort type panel
        JPanel sortTypePanel = new JPanel();
        sortTypePanel.setBackground(new Color(16, 26, 67));

        JRadioButton Ascending = new JRadioButton("Ascending");
        Ascending.setFont(new Font("Arial", Font.PLAIN, 20));
        Ascending.setForeground(Color.WHITE);

        JRadioButton Descending = new JRadioButton("Descending");
        Descending.setFont(new Font("Arial", Font.PLAIN, 20));
        Descending.setForeground(Color.WHITE);

        ascendingRadioButton = Ascending;
        ascendingRadioButton.setFont(new Font("Arial", Font.PLAIN, 16));
        ascendingRadioButton.setFocusPainted(false); // إزالة التركيز
        ascendingRadioButton.setBorderPainted(false); // إزالة الحواف
        ascendingRadioButton.setContentAreaFilled(false); // إزالة الخلفية

        descendingRadioButton = Descending;
        descendingRadioButton.setFont(new Font("Arial", Font.PLAIN, 16));
        descendingRadioButton.setFocusPainted(false); // إزالة التركيز
        descendingRadioButton.setBorderPainted(false); // إزالة الحواف
        descendingRadioButton.setContentAreaFilled(false); // إزالة الخلفية

        ButtonGroup sortDirectionGroup = new ButtonGroup();
        sortDirectionGroup.add(ascendingRadioButton);
        sortDirectionGroup.add(descendingRadioButton);

        sortTypePanel.add(ascendingRadioButton);
        sortTypePanel.add(descendingRadioButton);

        // Action button panel
        JPanel actionButtonPanel = new JPanel();
        actionButtonPanel.setBackground(new Color(16, 26, 67));
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));  // تكبير الخط في backButton
        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 16));  // تكبير الخط في saveButton
        sortButton = new JButton("Sort");
        sortButton.setFont(new Font("Arial", Font.PLAIN, 16));  // تكبير الخط في sortButton
        actionButtonPanel.add(backButton);
        actionButtonPanel.add(saveButton);
        actionButtonPanel.add(sortButton);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        bottomPanel.setBackground(new Color(16, 26, 67));
        bottomPanel.add(sortTypePanel);
        bottomPanel.add(actionButtonPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(resultsPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);

        loadFileButton.addActionListener(this);
        sortButton.addActionListener(this);
        saveButton.addActionListener(this);
        backButton.addActionListener(this);

        homefram.add(mainPanel);
        homefram.revalidate();
        homefram.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == loadFileButton) {
            loadFile();
        } else if (source == sortButton) {
            sortNumbers();
        } else if (source == saveButton) {
            saveToFile();
        } else if (source == backButton) {
            HideAll();
            ShowSortings();
        }
    }
    public void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                StringBuilder fileContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent.append(line).append(",");
                }
                inputField.setText(fileContent.toString().replaceAll(",$", ""));
                resultLabel.setText("Result: File loaded successfully!");
            } catch (IOException ex) {
                resultLabel.setText("Result: Error loading file.");
            }
        }
    }

    public void sortNumbers() {
        String input = inputField.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Input field is empty! Please enter numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            int[] data = Arrays.stream(input.split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();
    
            String selectedSort = (String) sortComboBox.getSelectedItem();
            
            if ("Compare Sort Times".equals(selectedSort)) {
                compareSortTimes(data);
                return;
            }
    
            boolean isAscending = ascendingRadioButton.isSelected();
            boolean isDescending = descendingRadioButton.isSelected();
    
            if (!isAscending && !isDescending) {
                JOptionPane.showMessageDialog(null, "Please select sort direction (Ascending or Descending).", "Sort Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
    
            long startTime = System.nanoTime();
    
            if ("Bubble Sort".equals(selectedSort)) {
                bubbleSort(data, isAscending);
            } else if ("Selection Sort".equals(selectedSort)) {
                selectionSort(data, isAscending);
            } else if ("Merge Sort".equals(selectedSort)) {
                mergeSort(data, isAscending);
            } else if ("Quick Sort".equals(selectedSort)) {
                quickSort(data, 0, data.length - 1, isAscending);
            } else if ("Insertion Sort".equals(selectedSort)) {
                insertionSort(data, isAscending);
            } else if ("Counting Sort".equals(selectedSort)) {
                countingSort(data, isAscending);
            } else if ("Heap Sort".equals(selectedSort)) {
                heapSort(data, isAscending);
            } else if ("Bucket Sort".equals(selectedSort)) {
                bucketSort(data, isAscending);
            } else if ("Radix Sort".equals(selectedSort)) {
                radixSort(data, isAscending);
            } else if ("Shell Sort".equals(selectedSort)) {
                shellSort(data, isAscending);
            }
    
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
    
            StringBuilder sortedList = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                sortedList.append(data[i]);
                if (i < data.length - 1) {
                    sortedList.append(", ");
                }
            }
    
            sortedListLabel.setText("Sorted List: " + sortedList.toString());
            timeElapsedLabel.setText("Time Elapsed: " + timeElapsed + " nanoseconds");
            resultLabel.setText("Result: Sorting completed.");
    
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter numbers separated by commas.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void compareSortTimes(int[] originalData) {
        String[] sortAlgorithms = {"Bubble Sort", "Selection Sort", "Insertion Sort", "Merge Sort", "Quick Sort",
                                    "Counting Sort", "Heap Sort", "Bucket Sort", "Radix Sort", "Shell Sort"};
    
        StringBuilder result = new StringBuilder();
        StringBuilder timeDetails = new StringBuilder("Sort Times (nanoseconds):\n\n");
    
        int[] finalSortedList = null; // لحفظ القائمة النهائية المرتبة
        for (String algorithm : sortAlgorithms) {
            int[] data = Arrays.copyOf(originalData, originalData.length); // نسخة من البيانات الأصلية
            long startTime = System.nanoTime();
    
            // تنفيذ خوارزمية الفرز
            switch (algorithm) {
                case "Bubble Sort":
                    bubbleSort(data, true);
                    break;
                case "Selection Sort":
                    selectionSort(data, true);
                    break;
                case "Insertion Sort":
                    insertionSort(data, true);
                    break;
                case "Merge Sort":
                    mergeSort(data, true);
                    break;
                case "Quick Sort":
                    quickSort(data, 0, data.length - 1, true);
                    break;
                case "Counting Sort":
                    countingSort(data, true);
                    break;
                case "Heap Sort":
                    heapSort(data, true);
                    break;
                case "Bucket Sort":
                    bucketSort(data, true);
                    break;
                case "Radix Sort":
                    radixSort(data, true);
                    break;
                case "Shell Sort":
                    shellSort(data, true);
                    break;
            }
    
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
    
            // حفظ أول قائمة مرتبة فقط
            if (finalSortedList == null) {
                finalSortedList = Arrays.copyOf(data, data.length);
            }
    
            // إضافة وقت التنفيذ إلى التفاصيل
            timeDetails.append(algorithm).append(": ").append(timeElapsed).append(" ns\n").append("-----------------------------------------------------\n");
        }
    
        // بناء القائمة المرتبة كسلسلة نصية
        StringBuilder sortedList = new StringBuilder();
        for (int i = 0; i < finalSortedList.length; i++) {
            sortedList.append(finalSortedList[i]);
            if (i < finalSortedList.length - 1) {
                sortedList.append(", ");
            }
        }
    
        // إضافة القائمة المرتبة النهائية إلى الرسالة
        result.append("Sorted List:\n")
              .append(sortedList.toString())
              .append("\n")
              .append(timeDetails.toString());
    
        // عرض النتائج في نافذة واحدة
        JOptionPane.showMessageDialog(null, result.toString(), "Comparison Result", JOptionPane.INFORMATION_MESSAGE);
    }
    

    public void bubbleSort(int[] arr, boolean ascending) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if ((ascending && arr[j] > arr[j + 1]) || (!ascending && arr[j] < arr[j + 1])) {
                    // Swap arr[j] and arr[j + 1]
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public void selectionSort(int[] arr, boolean ascending) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int idx = i;
            for (int j = i + 1; j < n; j++) {
                if ((ascending && arr[j] < arr[idx]) || (!ascending && arr[j] > arr[idx])) {
                    idx = j;
                }
            }
            // Swap arr[i] and arr[idx]
            int temp = arr[i];
            arr[i] = arr[idx];
            arr[idx] = temp;
        }
    }

    public void mergeSort(int[] arr, boolean ascending) {
        if (arr.length < 2) {
            return;
        }
        int mid = arr.length / 2;
        int[] left = Arrays.copyOfRange(arr, 0, mid);
        int[] right = Arrays.copyOfRange(arr, mid, arr.length);

        mergeSort(left, ascending);
        mergeSort(right, ascending);
        merge(arr, left, right, ascending);
    }

    public void merge(int[] arr, int[] left, int[] right, boolean ascending) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if ((ascending && left[i] < right[j]) || (!ascending && left[i] > right[j])) {
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
            }
        }
        while (i < left.length) {
            arr[k++] = left[i++];
        }
        while (j < right.length) {
            arr[k++] = right[j++];
        }
    }

    public void quickSort(int[] arr, int low, int high, boolean ascending) {
        if (low < high) {
            int pi = partition(arr, low, high, ascending);
            quickSort(arr, low, pi - 1, ascending);
            quickSort(arr, pi + 1, high, ascending);
        }
    }

    public void insertionSort(int[] arr, boolean ascending) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && ((ascending && arr[j] > key) || (!ascending && arr[j] < key))) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    public void countingSort(int[] arr, boolean ascending) {
        int max = Arrays.stream(arr).max().getAsInt();
        int min = Arrays.stream(arr).min().getAsInt();
        int range = max - min + 1;
        int[] count = new int[range];
        int[] output = new int[arr.length];

        for (int i : arr) {
            count[i - min]++;
        }

        if (ascending) {
            for (int i = 1; i < count.length; i++) {
                count[i] += count[i - 1];
            }
        } else {
            for (int i = count.length - 2; i >= 0; i--) {
                count[i] += count[i + 1];
            }
        }

        for (int i = arr.length - 1; i >= 0; i--) {
            output[count[arr[i] - min] - 1] = arr[i];
            count[arr[i] - min]--;
        }

        System.arraycopy(output, 0, arr, 0, arr.length);
    }

    public void heapSort(int[] arr, boolean ascending) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i, ascending);
        }

        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0, ascending);
        }
    }

    private void heapify(int[] arr, int n, int i, boolean ascending) {
        int largestOrSmallest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && ((ascending && arr[left] > arr[largestOrSmallest]) || (!ascending && arr[left] < arr[largestOrSmallest]))) {
            largestOrSmallest = left;
        }

        if (right < n && ((ascending && arr[right] > arr[largestOrSmallest]) || (!ascending && arr[right] < arr[largestOrSmallest]))) {
            largestOrSmallest = right;
        }

        if (largestOrSmallest != i) {
            int swap = arr[i];
            arr[i] = arr[largestOrSmallest];
            arr[largestOrSmallest] = swap;

            heapify(arr, n, largestOrSmallest, ascending);
        }
    }

    public void bucketSort(int[] arr, boolean ascending) {
        if (arr.length <= 0) {
            return;
        }

        int max = Arrays.stream(arr).max().getAsInt();
        int min = Arrays.stream(arr).min().getAsInt();
        int bucketCount = (max - min) / arr.length + 1;

        ArrayList<Integer>[] buckets = new ArrayList[bucketCount];
        for (int i = 0; i < bucketCount; i++) {
            buckets[i] = new ArrayList<>();
        }

        for (int i : arr) {
            buckets[(i - min) / arr.length].add(i);
        }

        int index = 0;
        for (ArrayList<Integer> bucket : buckets) {
            bucket.sort((ascending ? Integer::compare : Comparator.reverseOrder()));
            for (int num : bucket) {
                arr[index++] = num;
            }
        }
    }

    public void radixSort(int[] arr, boolean ascending) {
        int max = Arrays.stream(arr).max().getAsInt();
        int exp = 1;

        while (max / exp > 0) {
            countingSortByDigit(arr, exp, ascending);
            exp *= 10;
        }
    }

    private void countingSortByDigit(int[] arr, int exp, boolean ascending) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];

        for (int i : arr) {
            count[(i / exp) % 10]++;
        }

        if (ascending) {
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }
        } else {
            for (int i = 8; i >= 0; i--) {
                count[i] += count[i + 1];
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }

        System.arraycopy(output, 0, arr, 0, n);
    }

    public void shellSort(int[] arr, boolean ascending) {
        int n = arr.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j;
                for (j = i; j >= gap && ((ascending && arr[j - gap] > temp) || (!ascending && arr[j - gap] < temp)); j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = temp;
            }
        }
    }

    public int partition(int[] arr, int low, int high, boolean ascending) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if ((ascending && arr[j] < pivot) || (!ascending && arr[j] > pivot)) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    public void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(inputField.getText());
                resultLabel.setText("Result: File saved successfully.");
            } catch (IOException ex) {
                resultLabel.setText("Result: Error saving file.");
            }
        }
    }
}
