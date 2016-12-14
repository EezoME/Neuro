package edu.eezo.neuro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Eezo on 07.12.2016.
 */
public class MainGUI extends JFrame {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JRadioButton radioButtonAgeLess30;
    private JRadioButton radioButtonAge30_50;
    private JRadioButton radioButtonAgeMore50;
    private JCheckBox checkBoxIsMarried;
    private JTextField textFieldScoringPoint;
    private JTextField textFieldCreditSum;
    private JRadioButton radioButtonEducationSecondary;
    private JRadioButton radioButtonEducationSecondarySpecial;
    private JRadioButton radioButtonEducationHigher;
    private JRadioButton radioButtonSeniorityLessYear;
    private JRadioButton radioButtonSeniority1_5;
    private JRadioButton radioButtonSeniority5_10;
    private JRadioButton radioButtonSeniorityMore10;
    private JRadioButton radioButtonCarAgeLess3;
    private JRadioButton radioButtonCarAge3_7;
    private JRadioButton radioButtonCarAgeMore7;
    private JCheckBox checkBoxTookCreditBefore;
    private JCheckBox checkBoxHasCar;
    private JLabel labelStatus;

    private MainGUI() {
        super("Neuron Net for Credit Score");
        setSize(530, 450);
        setLocationRelativeTo(null);
        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        makeButtonGroup(radioButtonAgeLess30, radioButtonAge30_50, radioButtonAgeMore50);
        makeButtonGroup(radioButtonEducationSecondary, radioButtonEducationSecondarySpecial, radioButtonEducationHigher);
        makeButtonGroup(radioButtonSeniorityLessYear, radioButtonSeniority1_5, radioButtonSeniority5_10, radioButtonSeniorityMore10);
        makeButtonGroup(radioButtonCarAgeLess3, radioButtonCarAge3_7, radioButtonCarAgeMore7);
        toggleCarAgeRadioEnableStatus(false);

        checkBoxHasCar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleCarAgeRadioEnableStatus(checkBoxHasCar.isSelected());
            }
        });
    }

    public static ButtonGroup makeButtonGroup(JRadioButton... radioButtons) {
        if (radioButtons == null || radioButtons.length == 0) {
            return null;
        }

        ButtonGroup buttonGroup = new ButtonGroup();

        for (JRadioButton radioButton : radioButtons) {
            buttonGroup.add(radioButton);
        }

        buttonGroup.setSelected(radioButtons[0].getModel(), true);

        return buttonGroup;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainGUI();
            }
        });
    }

    private void toggleCarAgeRadioEnableStatus(boolean isEnabled) {
        radioButtonCarAgeLess3.setEnabled(isEnabled);
        radioButtonCarAge3_7.setEnabled(isEnabled);
        radioButtonCarAgeMore7.setEnabled(isEnabled);
    }
}
