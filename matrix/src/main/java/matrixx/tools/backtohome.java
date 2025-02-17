package matrixx.tools;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import static matrixx.constants.PbuttonColor;

public class backtohome implements ActionListener {

    public static JButton backtohome;

    public static JButton backtohomee() {
        backtohome = new JButton("Home");
        backtohome.setFont(new Font("Arial", Font.BOLD, 20));
        backtohome.setForeground(PbuttonColor);
        backtohome.setContentAreaFilled(false);
        backtohome.setOpaque(false);
        backtohome.setBorderPainted(false);

        return backtohome;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

}
