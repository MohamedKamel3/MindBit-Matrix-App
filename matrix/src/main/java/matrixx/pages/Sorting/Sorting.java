package matrixx.pages.Sorting;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import matrixx.pages.Sorting.List.ListPage;
import static matrixx.pages.Sorting.Matrix.MatrixInputPage.ShowMatrixInput;
import matrixx.pages.Sorting.info.app;
import static matrixx.pages.home_page.HomePage.ShowHomePage;
import static matrixx.pages.home_page.HomePage.homefram;
import matrixx.tools.RoundedButton;

public class Sorting implements ActionListener {

    public static JPanel ButtonsPanel = new JPanel();
    public static JLabel label = new JLabel();
    static RoundedButton ListButton;
    static RoundedButton MatrixButton;
    static RoundedButton InfoButton;
    static RoundedButton BackButton;

    public Sorting() {
        ButtonsPanel.setLayout(new GridLayout(2, 2, 0, -20));
        ButtonsPanel.setBackground(Color.decode("#101a43"));
        ButtonsPanel.setBounds(0, 100, 500, 400);

        label.setText("Sorting");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", 1, 36));
        label.setBounds(220, 30, 200, 50);

        ListButton = new RoundedButton("List", new Dimension(135, 80), new Color(173, 216, 230), 25);
        ListButton.setForeground(Color.decode("#101a43"));
        MatrixButton = new RoundedButton("Matrix", new Dimension(135, 80), new Color(173, 216, 230), 25);
        MatrixButton.setForeground(Color.decode("#101a43"));
        InfoButton = new RoundedButton("Info", new Dimension(135, 80), new Color(173, 216, 230), 25);
        InfoButton.setForeground(Color.decode("#101a43"));
        BackButton = new RoundedButton("Back", new Dimension(135, 80), new Color(173, 216, 230), 25);
        BackButton.setForeground(Color.decode("#101a43"));

        ListButton.addActionListener(this);
        MatrixButton.addActionListener(this);
        InfoButton.addActionListener(this);
        BackButton.addActionListener(this);

        ListButton.setFocusable(false);
        MatrixButton.setFocusable(false);
        InfoButton.setFocusable(false);
        BackButton.setFocusable(false);

        ButtonsPanel.add(ListButton);
        ButtonsPanel.add(MatrixButton);
        ButtonsPanel.add(InfoButton);
        ButtonsPanel.add(BackButton);

    }

    public static void ShowSortings() {
        homefram.setTitle("Sorting");
        homefram.setSize(550, 500);
        homefram.setLayout(null);

        homefram.add(label);
        homefram.add(ButtonsPanel);

        ButtonsPanel.setVisible(true);
        label.setVisible(true);

        homefram.revalidate();
        homefram.repaint();

    }

    public static void HideSorting() {
        try {
            Component[] components = homefram.getContentPane().getComponents();
            for (Component component : components) {
                component.setVisible(false);
            }
            homefram.revalidate();
            homefram.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ListButton) {
            HideSorting();
            ListPage page = new ListPage();

        } else if (e.getSource() == MatrixButton) {
            HideSorting();
            ShowMatrixInput();

        } else if (e.getSource() == InfoButton) {
            app gui = new app();
            gui.setVisible(true);

        } else if (e.getSource() == BackButton) {
            HideSorting();
            ShowHomePage();
        }
    }
}
