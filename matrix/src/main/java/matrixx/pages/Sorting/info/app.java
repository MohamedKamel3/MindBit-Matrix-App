package matrixx.pages.Sorting.info;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class app extends JFrame {

    //jframe size
    private static final int WIDTH = 620;
    private static final int HEIGHT = 800;
    //panels
    JPanel homePanel;
    JPanel bubbleSortPanel;
    JPanel selectionSortPanel;
    JPanel quickSortPanel;
    JPanel heapSortPanel;

    //listeners to Menu item choice selected by user
    private class BubbleSortListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            homePanel.setVisible(false);
            bubbleSortPanel.setVisible(true);
            selectionSortPanel.setVisible(false);
            quickSortPanel.setVisible(false);
            heapSortPanel.setVisible(false);
        }
    }//end of BubbleSortListener inner class

    private class HomePanelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            homePanel.setVisible(true);
            bubbleSortPanel.setVisible(false);
            selectionSortPanel.setVisible(false);
            quickSortPanel.setVisible(false);
            heapSortPanel.setVisible(false);
        }
    }//end of HomePanelListener inner class

    private class SelectionSortPanelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            homePanel.setVisible(false);
            bubbleSortPanel.setVisible(false);
            selectionSortPanel.setVisible(true);
            quickSortPanel.setVisible(false);
            heapSortPanel.setVisible(false);
        }
    }//end of selectionSortPanelListener inner class

    private class QuickSortPanelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            homePanel.setVisible(false);
            bubbleSortPanel.setVisible(false);
            selectionSortPanel.setVisible(false);
            quickSortPanel.setVisible(true);
            heapSortPanel.setVisible(false);
        }
    }//end of QuickSortPanelListener inner class

    private class HeapSortPanelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            homePanel.setVisible(false);
            bubbleSortPanel.setVisible(false);
            selectionSortPanel.setVisible(false);
            quickSortPanel.setVisible(false);
            heapSortPanel.setVisible(true);
        }
    }

    public app() {
        super("Sorting Algorithms");
        setSize(WIDTH, HEIGHT);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        setLocationRelativeTo(null);

        JLabel welcomeInfo = new JLabel("WELCOME!! Choose from the menu above for desired algorithm and click sort to see the magic happen!!");
        homePanel = new JPanel();
        homePanel.setLayout(new BorderLayout());
        homePanel.add(welcomeInfo, BorderLayout.NORTH);
        homePanel.setVisible(true);
        add(homePanel);

        bubbleSortPanel = bubbleSorter.runBubbleSort();
        bubbleSortPanel.setVisible(false);
        add(bubbleSortPanel);

        //makes the selection sort panel
        selectionSortPanel = SelectionSorter.runSelectionSort();
        selectionSortPanel.setVisible(false);
        add(selectionSortPanel);

        //makes the Quick sort panel
        quickSortPanel = QuickSorter.runQuickSort();
        quickSortPanel.setVisible(false);
        add(quickSortPanel);

        //makes the Heap sort panel
        heapSortPanel = HeapSorter.runHeapSort();
        heapSortPanel.setVisible(false);
        add(heapSortPanel);

        //makes menu
        JMenu menu = new JMenu("Menu");
        //adds all menu items
        JMenuItem homeChoice = new JMenuItem("Home");
        homeChoice.addActionListener(new HomePanelListener());
        menu.add(homeChoice);
        JMenuItem bubbleChoice = new JMenuItem("Bubble Sort");
        bubbleChoice.addActionListener(new BubbleSortListener());
        menu.add(bubbleChoice);
        JMenuItem selectionChoice = new JMenuItem("Selection Sort");
        selectionChoice.addActionListener(new SelectionSortPanelListener());
        menu.add(selectionChoice);
        JMenuItem quickChoice = new JMenuItem("Quick Sort");
        quickChoice.addActionListener(new QuickSortPanelListener());
        menu.add(quickChoice);
        JMenuItem heapChoice = new JMenuItem("Heap Sort");
        heapChoice.addActionListener(new HeapSortPanelListener());
        menu.add(heapChoice);

        //creates the menu bar
        JMenuBar bar = new JMenuBar();
        bar.add(menu);
        setJMenuBar(bar);
    }

}
