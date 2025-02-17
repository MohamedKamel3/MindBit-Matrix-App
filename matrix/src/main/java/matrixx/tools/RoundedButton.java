package matrixx.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class RoundedButton extends JButton implements ActionListener {

    private final String buttonText;  // Variable for the button text
    private final Color buttonColor;  // Color for the border
    private Color textColor;    // Color for the text
    private int fontSize; // Font size for the button text
    private Dimension buttonDimension;

    // Constructor to set button properties (text, dimension, color, font size, text color)
    public RoundedButton(String text, Dimension dimension, Color borderColor, int fontSize) {
        super(text); // Call JButton constructor with button text
        this.buttonText = text;
        this.setPreferredSize(dimension); // Set preferred size for the button
        this.buttonColor = borderColor;  // Border color
        this.textColor = textColor;  // Text color
        this.fontSize = fontSize;
        this.buttonDimension = dimension;
        setOpaque(false);  // Make the button transparent
        setContentAreaFilled(false); // Make the button background transparent
        setBorderPainted(false); // Remove default border
        // Add action listener to handle button clicks
        addActionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Antialiasing for smoother edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Set the color for the border
        g2d.setColor(buttonColor);
        // Draw the border of the button (without filling the background)
        g2d.drawRoundRect(90, 30, buttonDimension.width, buttonDimension.height, 50, 50); // (x, y, width, height, arcWidth, arcHeight)
        // Set the font with the specified font size
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
        // Measure the text to center it
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(buttonText);
        int textHeight = fm.getHeight();
        // Set the text color
        g2d.setColor(textColor);
        g2d.setBackground(Color.decode("#101a43"));
        // Draw the text in the center of the button
        int x = 90 + (buttonDimension.width - textWidth) / 2;  // Adjust x to center
        int y = 30 + ((buttonDimension.height - textHeight) / 2) + fm.getAscent();  // Adjust y to center
        g2d.drawString(buttonText, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle button click event here
    }
}
