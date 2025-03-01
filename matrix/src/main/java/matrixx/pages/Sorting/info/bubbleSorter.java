package matrixx.pages.Sorting.info;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class bubbleSorter extends JPanel {

    //variables for bar graphics
    private final static int BAR_WIDTH = 30;
    private final static int BAR_HEIGHT = 400;
    private int[] list;
    //JPanel variable object to create/display and return
    private static JPanel mainPanel;

    //initializes the list of bubbleSorter object
    public bubbleSorter() {
    }

    public bubbleSorter(int[] list) {
        this.list = list;
    }

    //sets the list to this bubbleSorter object's list
    private void setItems(int[] list) {
        this.list = list;
        repaint();
    }

    //sorts the BubbleSorter object
    private void sort() {
        new SortWorker(list).execute();
    }

    /*
    * Paint component that creates the graphical bars
    * References: https://stackoverflow.com/questions/64196198/bubble-sort-animation
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //creates the bar for each element
        for (int i = 0; i < list.length; i++) {
            int x = i * BAR_WIDTH;
            int y = getHeight() - list[i];

            //makes the color red
            g.setColor(Color.RED);
            g.fillRect(x, y, BAR_WIDTH, list[i]);
            g.setColor(Color.BLUE);
            g.drawString("" + list[i], x, y);
        }
    }

    /*
    * Returns the Dimensions of the rectangle bars used for bubbleSort graph
    * References: https://stackoverflow.com/questions/64196198/bubble-sort-animation
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(list.length * BAR_WIDTH, BAR_HEIGHT + 20);
    }

    /*
    * Class that sorts the bars using the bubbleSort algorithm
    * References: https://stackoverflow.com/questions/64196198/bubble-sort-animation
     */
    private class SortWorker extends SwingWorker<Void, int[]> {

        private int[] list;

        public SortWorker(int[] unsortedItems) {
            list = Arrays.copyOf(unsortedItems, unsortedItems.length);
        }

        //bubbleSort algorithm
        @Override
        protected Void doInBackground() {
            //variables
            int n = list.length;
            int temp = 0;

            for (int i = 0; i < (n - 1); i++) {
                for (int j = 0; j < (n - i - 1); j++) {
                    if (list[j] > list[j + 1]) {
                        //swaps list elements
                        temp = list[j];
                        list[j] = list[j + 1];
                        list[j + 1] = temp;

                        //repaint(); to show graphics to user
                        publish(Arrays.copyOf(list, list.length));
                        //sleep to slow down graphics
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                        }
                    }
                }
            }

            Toolkit tk = Toolkit.getDefaultToolkit();
            tk.beep();
            return null;
        }

        @Override
        protected void process(List<int[]> processList) {
            int[] list = processList.get(processList.size() - 1);
            setItems(list);
        }

        @Override
        protected void done() {
        }
    }

    //method that generates random numbers and fills an int array
    private static int[] generateListNumbers() {
        //variables initialized
        int[] list = new int[20];

        /*creates random object and generates number from 0 to 
        BAR_HEIGHT for each element*/
        Random random = new Random();
        for (int i = 0; i < list.length; i++) {
            list[i] = random.nextInt(bubbleSorter.BAR_HEIGHT);
        }

        return list;
    }

    /*
    * method that will run the entire bubblesorting algorithm.
    * From creating the objects and panels and buttons, to
    * calling all functions to sort, generate, order and return
    * a panel with a visual bubbleSorting algorithm.
     */
    public static JPanel runBubbleSort() {
        //creates the object bubbleSort, initializes the list, and fills it with randomly generated numbers
        bubbleSorter bubbleSort = new bubbleSorter(bubbleSorter.generateListNumbers());

        //adds title to the top of the panel
        JLabel title = new JLabel("Bubble Sort");

        //makes and adds two buttons, generate and sort
        JButton generate = new JButton("Generate Data");
        generate.addActionListener((e) -> bubbleSort.setItems(bubbleSorter.generateListNumbers()));
        JButton sort = new JButton("Sort Data");
        sort.addActionListener((e) -> bubbleSort.sort());

        //adds the buttons to the panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(generate);
        bottomPanel.add(sort);

        //creates the mainPanel for the bubbleSort graphics
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(bubbleSort, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(title, BorderLayout.NORTH);

        return mainPanel;
    }
}
