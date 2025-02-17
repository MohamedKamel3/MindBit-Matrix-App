package matrixx.pages.operations;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import static matrixx.pages.home_page.HomePage.homefram;

public class Frame4Panel extends JPanel implements ActionListener {

    public static JButton homeButton;
    public static JLabel Frame4Label;

    public Frame4Panel() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#101a43"));
        Frame4Label = new JLabel("Result Matrix", JLabel.CENTER);

        // add(new JLabel("Final Matrix", JLabel.CENTER), BorderLayout.NORTH);
        homeButton = new JButton("Home");
        homeButton.setFont(new Font("Arial", Font.BOLD, 20));
        homeButton.addActionListener(this);
        add(homeButton, BorderLayout.SOUTH);
    }

    public static void showFrame4() {
        homefram.setSize(400, 300);
        homefram.setLayout(new GridBagLayout());
        homefram.setTitle("Matrix Calculator");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        homefram.add(new Frame4Panel(), gbc);

        homeButton.setVisible(true);
        Frame4Label.setVisible(true);
        homefram.add(homeButton);
        homefram.add(Frame4Label);

    }

    public static void HideFrame4() {
        homeButton.setVisible(false);
        Frame4Label.setVisible(false);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == homeButton) {

        }
    }
}
