package edu.eezo.neuro.network;

import edu.eezo.neuro.MainGUI;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Workspace on 14.12.2016.
 * Use this class for classification client (approved, declined)
 */
public class LVQNetwork {

    class InputElement {
        public int[] x = null;
        public int klass = -1;

        public InputElement(int[] x, int klass) {
            this.x = x;
            this.klass = klass;
        }
    }

    private final int classes = 2; // Class 0 - clients with less than 130 score (cluster 1), class 1 - 130 and above (cluster 2, 3, 4)
    private final int clusters = 4;
    private int inputs = 0; // Number of inputs
    private final int trainingCycles = 1000;
    private InputElement[] inputElements = null;
    private double[][] weights = new double[clusters][];
    private final double alpha = 0.0001; // Training speed

    public LVQNetwork(int inputs) {
        this.inputs = inputs;

        train();
    }

    private void initWeights() {
        for (int i = 0; i < clusters; i++) {
            double weightsVector[] = new double[inputs];
            for (int j = 0; j < inputs; j++) {
                weightsVector[j] = Math.random() * alpha;
            }
            weights[i] = weightsVector;
        }
    }

    private double[] sqrEuclideanDistance(InputElement input) {
        double[] distanceJ = new double[clusters];

        for (int j = 0; j < clusters; j++) {
            double sum = 0.0;
            for (int i = 0; i < input.x.length; i++) {
                sum += Math.pow(input.x[i] - weights[j][i], 2);
            }
            distanceJ[j] = Math.sqrt(sum);
        }

        return distanceJ;
    }

    private void updateWeights(int winnerPos, InputElement input) {
        double newWeights[] = new double[inputs];

        for (int i = 0; i < newWeights.length; i++) {
            if (winnerPos == 0 && input.klass == 0) {
                newWeights[i] = weights[winnerPos][i] + alpha * (input.x[i] - weights[winnerPos][i]);
            } else if ((winnerPos == 1 || winnerPos == 2 || winnerPos == 3) && input.klass == 1) {
                newWeights[i] = weights[winnerPos][i] + alpha * (input.x[i] - weights[winnerPos][i]);
            } else {
                newWeights[i] = weights[winnerPos][i] - alpha * (input.x[i] - weights[winnerPos][i]);
            }
        }

        weights[winnerPos] = newWeights;
    }

    public int simulate(int[] input) {
        InputElement inputElement = new InputElement(input, -1);

        double[] distanceJ = new double[clusters];

        for (int j = 0; j < clusters; j++) {
            double sum = 0.0;
            for (int i = 0; i < inputElement.x.length; i++) {
                sum += Math.pow(inputElement.x[i] - weights[j][i], 2);
            }
            distanceJ[j] = Math.sqrt(sum);
        }

        int klass = -1;
        switch (minimumDistance(distanceJ)) {
            case 0:
                klass = 0;
                break;
            case 1:
                klass = 1;
                break;
            case 2:
                klass = 1;
                break;
            case 3:
                klass = 1;
                break;
        }

        return klass;
    }

    private int minimumDistance(double[] distances) {
        int minIndex = 0;
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] < distances[minIndex]) {
                minIndex = i;
            }
        }

        return minIndex;
    }


    private void train() {
        System.out.println("Start training LVQ...");

        initWeights();
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(MainGUI.fileToRead), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputElements = new InputElement[lines.size()];

        for (int c = 0; c < trainingCycles; c++) {
            int lineCount = 0;
            for (String line : lines) {
                String[] csvInputValues = line.split(";");
                int[] csvInputBinary = new int[inputs];
                for (int i = 0; i < csvInputValues.length - 1; i++) { // The last one is standard value
                    csvInputBinary[i] = Integer.parseInt(csvInputValues[i]);
                }

                int currentInputClass = 0;

                if (Double.parseDouble(csvInputValues[csvInputValues.length - 1]) < 100) {
                    currentInputClass = 0;
                } else {
                    currentInputClass = 1;
                }

                inputElements[lineCount] = new InputElement(csvInputBinary, currentInputClass);

                double distance[] = sqrEuclideanDistance(inputElements[lineCount]);
                int winnerCluster = minimumDistance(distance);

                updateWeights(winnerCluster, inputElements[lineCount]);

                lineCount++;
            }
        }

        System.out.println("Finished training LVQ after " + trainingCycles + " cycles.");
    }
}
