package matrixx.pages.solve_equations;

import java.awt.BorderLayout;
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
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class EquationResultPanel extends JPanel {
    private JButton backButton, homeButton;

    public EquationResultPanel(double[] results, JTextField[][] coefficientFields, Consumer<String> navigateBack, Consumer<String> navigateHome) {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#101a43"));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.decode("#101a43"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label for Equations Section
        JLabel equationsLabel = new JLabel("Equations");
        equationsLabel.setFont(new Font("Arial", Font.BOLD, 22));
        equationsLabel.setForeground(Color.decode("#FFD700"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(equationsLabel, gbc);

        // Determine number of variables based on columns in coefficientFields
        int numVariables = coefficientFields[0].length - 1; // Last column is the constant

        // Display each equation
        for (int i = 0; i < coefficientFields.length; i++) {
            StringBuilder equationText = new StringBuilder("Equation " + (i + 1) + ": ");
            for (int j = 0; j < numVariables; j++) { // Limit to the actual number of variables
                String coefficient = coefficientFields[i][j].getText();
                String variableName = (j == 3) ? "V" : String.valueOf((char) ('X' + j)); // Use "V" for the fourth variable
                equationText.append(coefficient).append(variableName);
                if (j < numVariables - 1) {
                    equationText.append(" + ");
                }
            }
            equationText.append(" = ").append(coefficientFields[i][coefficientFields[i].length - 1].getText());

            JLabel equationLabel = new JLabel(equationText.toString());
            equationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            equationLabel.setForeground(Color.LIGHT_GRAY);
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            gbc.gridwidth = 2;
            mainPanel.add(equationLabel, gbc);
        }

        // Label for Results Section
        JLabel resultsLabel = new JLabel("Results");
        resultsLabel.setFont(new Font("Arial", Font.BOLD, 22));
        resultsLabel.setForeground(Color.decode("#FFD700"));
        gbc.gridx = 0;
        gbc.gridy = coefficientFields.length + 1;
        gbc.gridwidth = 2;
        mainPanel.add(resultsLabel, gbc);

        // Display each result for the variables
        boolean hasNaN = false;
        for (int i = 0; i < numVariables; i++) { // Limit results to numVariables
            String variableName = (i == 3) ? "V" : String.valueOf((char) ('X' + i)); // Use "V" for the fourth variable
            String resultText = Double.isNaN(results[i]) ? "No Solution" : String.format("%.2f", results[i]);
            if (Double.isNaN(results[i])) {
                hasNaN = true;
            }

            JLabel resultLabel = new JLabel("Variable " + variableName + " = " + resultText);
            resultLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            resultLabel.setForeground(Color.LIGHT_GRAY);
            gbc.gridx = 0;
            gbc.gridy = coefficientFields.length + i + 2;
            gbc.gridwidth = 2;
            mainPanel.add(resultLabel, gbc);
        }

        if (hasNaN) {
            JOptionPane.showMessageDialog(this, "The system of equations may not have a unique solution.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Scroll pane for main panel to handle large matrices
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.decode("#101a43"));

        // Back button
        backButton = new RoundedButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> navigateBack.accept("input"));
        buttonPanel.add(backButton);

        // Home button
        homeButton = new RoundedButton("Home");
        homeButton.setFont(new Font("Arial", Font.BOLD, 20));
        homeButton.setForeground(Color.WHITE);
        homeButton.addActionListener(e -> navigateHome.accept("home"));
        buttonPanel.add(homeButton);

        // Add button panel
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Custom-styled RoundedButton inner class
    private static class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setPreferredSize(new Dimension(100, 40)); // Set preferred size to avoid overlapping
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.decode("#4682B4"));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.setColor(getForeground());
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), x, y);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            g.setColor(Color.decode("#FFD700"));
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        }
    }
}
