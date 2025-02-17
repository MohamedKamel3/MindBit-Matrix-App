package matrixx.pages.solve_equations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class EquationSetupPanel extends JPanel {

    private JComboBox<Integer> mField, nField;
    private JButton startButton, backButton;

    public EquationSetupPanel(BiConsumer<Integer, Integer> startAction, ActionListener backActionListener) {
        // Set panel layout and background color
        setLayout(new GridBagLayout());
        setBackground(Color.decode("#101a43"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding for components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label and dropdown for m (number of equations)
        JLabel mLabel = new JLabel("Number of Equations (m):");
        mLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mLabel.setForeground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(mLabel, gbc);

        mField = new JComboBox<>(new Integer[]{2, 3, 4}); // Limited to 2, 3, 4 options
        mField.setFont(new Font("Arial", Font.PLAIN, 16));
        mField.setPreferredSize(new Dimension(80, 30)); // Make combo box larger for visibility
        gbc.gridx = 1;
        add(mField, gbc);

        // Label and dropdown for n (number of variables)
        JLabel nLabel = new JLabel("Number of Variables (n):");
        nLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nLabel.setForeground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(nLabel, gbc);

        nField = new JComboBox<>(new Integer[]{2, 3, 4}); // Limited to 2, 3, 4 options
        nField.setFont(new Font("Arial", Font.PLAIN, 16));
        nField.setPreferredSize(new Dimension(80, 30)); // Make combo box larger for visibility
        gbc.gridx = 1;
        add(nField, gbc);

        // Start button
        startButton = new RoundedButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setForeground(Color.decode("#ADD8E6"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(startButton, gbc);

        // Back button
        backButton = new RoundedButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setForeground(Color.decode("#ADD8E6"));
        gbc.gridy = 3;
        add(backButton, gbc);

        // ActionListener for the start button
        startButton.addActionListener(e -> {
            Integer m = (Integer) mField.getSelectedItem();
            Integer n = (Integer) nField.getSelectedItem();

            // Validate input values
            if (m == null || n == null) {
                JOptionPane.showMessageDialog(this, "Please select valid values for both equations and variables.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else if (m < n) {
                JOptionPane.showMessageDialog(this, "The number of equations must be greater than or equal to the number of variables.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                startAction.accept(m, n); // Pass m and n to the startAction
            }
        });

        // Back button ActionListener
        backButton.addActionListener(backActionListener);
    }

    // Custom-styled RoundedButton class
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
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), x, y);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            g.setColor(Color.decode("#ADD8E6"));
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        }
    }
}
