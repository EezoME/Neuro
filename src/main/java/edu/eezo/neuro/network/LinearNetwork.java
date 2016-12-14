package edu.eezo.neuro.network;

import edu.eezo.neuro.MainGUI;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Workspace on 14.12.2016.
 * Use this class for approximating client score
 */
public class LinearNetwork {

    private int inputs = 0; // Number of inputs
    private double[] weights = null;
    private final double alpha = 0.1; // Training speed
    private final double maxError = 0.0001;


    public LinearNetwork(int inputs) {
        this.inputs = inputs;
        this.weights = new double[inputs];

        train();
    }

    public double simulate(int[] input) {
        if (weights.length != input.length) {
            System.err.println("Incorrect input size!");
            return 0.0;
        }

        double sum = 0.0;
        for (int i = 0; i < input.length; i++) {
            sum += input[i] * weights[i];
        }

        return sum;
    }

    private void adjustWeights(int[] input, double standardOutput) {
        double[] adjustedWeights = new double[inputs];
        for (int i = 0; i < weights.length; i++) {
            adjustedWeights[i] = weights[i] - alpha * (simulate(input) - standardOutput) * input[i];
        }

        this.weights = adjustedWeights;
    }

    private double error(int[] input, double standardOutput) {
        return Math.abs(standardOutput - simulate(input));
    }

    private void train() {
        System.out.println("Start training Linear Network...");
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(MainGUI.fileToRead), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        double totalErrorValue = Double.MAX_VALUE;

        while (totalErrorValue > maxError) {
            double tempError = 0.0;
            for (String line : lines) {
                String[] csvInputValues = line.split(";");
                int[] csvInputBinary = new int[inputs];
                for (int i = 0; i < csvInputValues.length - 1; i++) { // The last one is standard value
                    csvInputBinary[i] = Integer.parseInt(csvInputValues[i]);
                }
                adjustWeights(csvInputBinary, Double.parseDouble(csvInputValues[csvInputValues.length - 1]));
                tempError += error(csvInputBinary, Double.parseDouble(csvInputValues[csvInputValues.length - 1]));
            }
            totalErrorValue = tempError;
        }

        System.out.println("Finish training Linear Network with error: " + totalErrorValue + "");
    }
}
