package edu.eezo.neuro;

import edu.eezo.neuro.network.LVQNetwork;
import edu.eezo.neuro.network.LinearNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Eezo on 07.12.2016.
 */
public class MainGUI extends JFrame {
    private JPanel rootPanel;
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
    private JButton buttonRun;

    public static String fileToRead = "src/main/resources/trainingData.csv";

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

        buttonRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runSystem();
            }
        });
    }

    static ButtonGroup makeButtonGroup(JRadioButton... radioButtons) {
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

    /**
     * Runs networks (main method).
     */
    private void runSystem() {
        int[] inputData = getInputDataArray();

        LinearNetwork linearNetwork = new LinearNetwork(inputData.length);
        textFieldScoringPoint.setText(linearNetwork.simulate(inputData) + "");

        LVQNetwork lvqNetwork = new LVQNetwork(inputData.length);
        labelStatus.setText(lvqNetwork.simulate(inputData) + "");
    }

    private void toggleCarAgeRadioEnableStatus(boolean isEnabled) {
        radioButtonCarAgeLess3.setEnabled(isEnabled);
        radioButtonCarAge3_7.setEnabled(isEnabled);
        radioButtonCarAgeMore7.setEnabled(isEnabled);
    }

    /**
     * Returns input data from form.
     * Selected and Enable = 1, otherwise = 0;
     *
     * @return an array of input signals
     */
    private int[] getInputDataArray() {
        int[] inputData = new int[19];

        inputData[0] = getSignalCode(radioButtonAgeLess30);
        inputData[1] = getSignalCode(radioButtonAge30_50);
        inputData[2] = getSignalCode(radioButtonAgeMore50);

        inputData[3] = getSignalCode(radioButtonEducationSecondary);
        inputData[4] = getSignalCode(radioButtonEducationSecondarySpecial);
        inputData[5] = getSignalCode(radioButtonEducationHigher);

        inputData[6] = checkBoxIsMarried.isSelected() ? 1 : 0;
        inputData[7] = checkBoxIsMarried.isSelected() ? 0 : 1;

        inputData[8] = checkBoxTookCreditBefore.isSelected() ? 1 : 0;
        inputData[9] = checkBoxTookCreditBefore.isSelected() ? 0 : 1;

        inputData[10] = getSignalCode(radioButtonSeniorityLessYear);
        inputData[11] = getSignalCode(radioButtonSeniority1_5);
        inputData[12] = getSignalCode(radioButtonSeniority5_10);
        inputData[13] = getSignalCode(radioButtonSeniorityMore10);

        inputData[14] = checkBoxHasCar.isSelected() ? 1 : 0;
        inputData[15] = checkBoxHasCar.isSelected() ? 0 : 1;

        inputData[16] = getSignalCode(radioButtonCarAgeLess3);
        inputData[17] = getSignalCode(radioButtonCarAge3_7);
        inputData[18] = getSignalCode(radioButtonCarAgeMore7);

        return inputData;
    }

    /**
     * Returns component's (toggle button's) signal code.
     * Selected and Enable = 1, otherwise = 0;
     *
     * @param toggleButton a component
     * @return <b>1</b> if component Enable and Selected, <b>0</b> - otherwise
     */
    private int getSignalCode(JToggleButton toggleButton) {
        return (toggleButton.isEnabled() && toggleButton.isSelected()) ? 1 : 0;
    }
}
