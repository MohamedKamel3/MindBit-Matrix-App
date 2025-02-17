package matrixx.pages.operations;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import static matrixx.pages.home_page.HomePage.homefram;

public class Panel2 {

    public static JPanel Panel2;
    public static ArrayList<JPanel> matrixPanels;

    // Variables to store matrix dimensions and selected operation
    public static ArrayList<String> rowsList = new ArrayList<>();
    public static ArrayList<String> colsList = new ArrayList<>();
    public static String selectedOperation;

    public static void ShowFrame2(int matrixCount) {
        homefram.setSize(450, 650);  // زيادة حجم الإطار لزيادة المساحة
        homefram.setTitle("Matrix operations");
        Panel2 = new JPanel();
        Panel2.setLayout(new GridBagLayout());
        Panel2.setBackground(Color.decode("#101a43"));

        matrixPanels = new ArrayList<>();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // ضبط المسافات بين المكونات
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Set up matrix panels and operations
        for (int i = 0; i < matrixCount; i++) {
            JPanel matrixPanel = new JPanel();
            matrixPanel.setBackground(Color.decode("#1E3A8A"));
            matrixPanel.setLayout(new GridBagLayout());
            matrixPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

            GridBagConstraints matrixGbc = new GridBagConstraints();
            matrixGbc.insets = new Insets(5, 5, 5, 5);

            JLabel rowsLabel = new JLabel("Number of rows");
            rowsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            rowsLabel.setForeground(Color.LIGHT_GRAY);
            JLabel colsLabel = new JLabel("Number of columns");
            colsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            colsLabel.setForeground(Color.LIGHT_GRAY);

            JComboBox<String> rowsBox = new JComboBox<>(new String[]{"2", "3", "4"});
            rowsBox.setFont(new Font("Arial", Font.PLAIN, 16));
            JComboBox<String> colsBox = new JComboBox<>(new String[]{"2", "3", "4"});
            colsBox.setFont(new Font("Arial", Font.PLAIN, 16));

            matrixGbc.gridx = 0;
            matrixGbc.gridy = 0;
            matrixPanel.add(rowsLabel, matrixGbc);
            matrixGbc.gridx = 1;
            matrixPanel.add(rowsBox, matrixGbc);
            matrixGbc.gridx = 0;
            matrixGbc.gridy = 1;
            matrixPanel.add(colsLabel, matrixGbc);
            matrixGbc.gridx = 1;
            matrixPanel.add(colsBox, matrixGbc);

            matrixPanels.add(matrixPanel);
            gbc.gridx = 0;
            gbc.gridy = i * 3;  // فصل اللوحات المصفوفة بمقدار 3 صفوف
            Panel2.add(matrixPanel, gbc);

            if (i < matrixCount - 1) {
                JPanel operationsPanel = new JPanel();
                operationsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                operationsPanel.setBackground(Color.decode("#101a43"));

                JLabel operationsLabel = new JLabel("Select the operation:");
                operationsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                operationsLabel.setForeground(Color.LIGHT_GRAY);
                JComboBox<String> operationsComboBox = new JComboBox<>(new String[]{"Addition", "Subtraction", "Multiplication"});
                operationsComboBox.setFont(new Font("Arial", Font.PLAIN, 16));

                operationsPanel.add(operationsLabel);
                operationsPanel.add(operationsComboBox);
                gbc.gridx = 0;
                gbc.gridy = i * 3 + 1;  // وضع لوحة العمليات بين المصفوفات
                Panel2.add(operationsPanel, gbc);
            }
        }

        // Back and Start panel
        JPanel backStartPanel = new JPanel();
        backStartPanel.setLayout(new GridBagLayout());
        backStartPanel.setBackground(Color.decode("#101a43"));
        GridBagConstraints backStartGbc = new GridBagConstraints();
        backStartGbc.insets = new Insets(10, 10, 10, 10);
        backStartGbc.gridx = 0;
        backStartGbc.gridwidth = 2;

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.decode("#1E3A8A"));
        backButton.setPreferredSize(new java.awt.Dimension(120, 50));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel2.setVisible(false);
                Panel1.ShowFrame1();
            }
        });

        // Start Button with custom style
        JButton startButton = new RoundedButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(Color.decode("#ADD8E6"));
        startButton.setPreferredSize(new java.awt.Dimension(120, 50));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean valid = true;
                String operation = "";

                // Clearing the lists before adding new dimensions
                rowsList.clear();
                colsList.clear();

                if (matrixPanels.size() == 2) {
                    JComboBox<String> rowsBox1 = (JComboBox<String>) matrixPanels.get(0).getComponent(1);
                    JComboBox<String> colsBox1 = (JComboBox<String>) matrixPanels.get(0).getComponent(3);
                    JComboBox<String> rowsBox2 = (JComboBox<String>) matrixPanels.get(1).getComponent(1);
                    JComboBox<String> colsBox2 = (JComboBox<String>) matrixPanels.get(1).getComponent(3);

                    String rows1 = (String) rowsBox1.getSelectedItem();
                    String cols1 = (String) colsBox1.getSelectedItem();
                    String rows2 = (String) rowsBox2.getSelectedItem();
                    String cols2 = (String) colsBox2.getSelectedItem();

                    // Add dimensions to the lists
                    rowsList.add(rows1);
                    colsList.add(cols1);
                    rowsList.add(rows2);
                    colsList.add(cols2);

                    JPanel operationsPanel = (JPanel) Panel2.getComponent(1);
                    JComboBox<String> operationsComboBox = (JComboBox<String>) operationsPanel.getComponent(1);
                    operation = (String) operationsComboBox.getSelectedItem();
                    selectedOperation = operation;

                    if (operation.equals("Addition") || operation.equals("Subtraction")) {
                        if (!rows1.equals(rows2) || !cols1.equals(cols2)) {
                            valid = false;
                            JOptionPane.showMessageDialog(Panel2, "Error: The dimensions for addition or subtraction must be the same.");
                        }
                    } else if (operation.equals("Multiplication")) {
                        if (!cols1.equals(rows2)) {
                            valid = false;
                            JOptionPane.showMessageDialog(Panel2, "Error: The number of columns of the first matrix must equal the number of rows of the second matrix for multiplication.");
                        }
                    }
                } else if (matrixPanels.size() == 3) {
                    for (int i = 0; i < matrixPanels.size() - 1; i++) {
                        JComboBox<String> rowsBox1 = (JComboBox<String>) matrixPanels.get(i).getComponent(1);
                        JComboBox<String> colsBox1 = (JComboBox<String>) matrixPanels.get(i).getComponent(3);
                        JComboBox<String> rowsBox2 = (JComboBox<String>) matrixPanels.get(i + 1).getComponent(1);
                        JComboBox<String> colsBox2 = (JComboBox<String>) matrixPanels.get(i + 1).getComponent(3);

                        String rows1 = (String) rowsBox1.getSelectedItem();
                        String cols1 = (String) colsBox1.getSelectedItem();
                        String rows2 = (String) rowsBox2.getSelectedItem();
                        String cols2 = (String) colsBox2.getSelectedItem();

                        // Add dimensions to the lists
                        rowsList.add(rows1);
                        colsList.add(cols1);

                        if (i == matrixPanels.size() - 2) { // For the last matrix
                            rowsList.add(rows2);
                            colsList.add(cols2);
                        }

                        JPanel operationsPanel = (JPanel) Panel2.getComponent(2 * i + 1);
                        JComboBox<String> operationsComboBox = (JComboBox<String>) operationsPanel.getComponent(1);
                        operation = (String) operationsComboBox.getSelectedItem();
                        selectedOperation = operation;

                        if (operation.equals("Addition") || operation.equals("Subtraction")) {
                            if (!rows1.equals(rows2) || !cols1.equals(cols2)) {
                                valid = false;
                                JOptionPane.showMessageDialog(Panel2, "Error: The dimensions for addition or subtraction must be the same between matrices " + (i + 1) + " and " + (i + 2) + ".");
                                break;
                            }
                        } else if (operation.equals("Multiplication")) {
                            if (!cols1.equals(rows2)) {
                                valid = false;
                                JOptionPane.showMessageDialog(Panel2, "Error: The number of columns of matrix " + (i + 1) + " must equal the number of rows of matrix " + (i + 2) + " for multiplication.");
                                break;
                            }
                        }
                    }
                }

                if (valid) {
                    System.out.println("Valid matrices and operations.");
                    Panel2.setVisible(false);
                    Panel3 pag = new Panel3(matrixPanels.size());
                    Panel3.ShowPanel3(); // Navigate to Panel3
                }
            }
        });

        backStartPanel.add(startButton, backStartGbc);
        backStartPanel.add(backButton, backStartGbc);

        gbc.gridx = 0;
        gbc.gridy = matrixCount * 3 + 1;  // وضع زر الرجوع و البدء بعد كل المكونات
        Panel2.add(backStartPanel, gbc);

        // Adjusted to fit all components
        try {
            // إخفاء جميع المكونات داخل الـ JFrame
            Component[] components = homefram.getContentPane().getComponents();
            for (Component component : components) {
                component.setVisible(false);
            }

            // إعادة تحديث الـ JFrame
            homefram.revalidate();

            // إعادة رسم الـ JFrame لتحديث العرض
            homefram.repaint();

        } catch (Exception e) {
            // طباعة الخطأ في حال حدوث استثناء
            e.printStackTrace();
        }
        homefram.add(Panel2);  // إضافة Panel2 إلى الإطار
        Panel2.setVisible(true);
        homefram.revalidate();  // إعادة تحديث الإطار
        homefram.repaint();

    }

    // Custom-styled RoundedButton class for Start and Back buttons
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
            g2.setColor(Color.decode("#ADD8E6"));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g);
            g2.dispose();
        }
    }
}
