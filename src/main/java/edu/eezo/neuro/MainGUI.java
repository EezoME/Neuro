package edu.eezo.neuro;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Eezo on 07.12.2016.
 */
public class MainGUI extends JFrame {
    private JPanel rootPanel;

    private MainGUI(){
        super("Neuron Net for Credit Score");
        setSize(450, 415);
        setLocationRelativeTo(null);
        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainGUI();
            }
        });
    }
}
