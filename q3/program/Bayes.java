
package program;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bayes {
    private int numRecords; // Number of records in the training data
    private int numAttributes; // Number of attributes in each record
    private int numClasses; // Number of classes in the classification problem
    private List<Record> records; // List to store the training records
    private int[] attributeValues; // Number of possible values for each attribute
    private double[][][] conditionalProbs; // Conditional probabilities of attributes given classes
    private double[] classProbabilities; // Prior probabilities of classes

    // Inner class representing a record in the training data
    private class Record {
        private final int[] attributes; // Attribute values of the record
        private final int className; // Class label of the record

        public Record(int[] attributes, int className) {
            this.attributes = attributes;
            this.className = className;
        }
    }

    public Bayes() {
        records = new ArrayList<>();
    }

    // load training data from a file
    public void loadTrainingData(String trainingFile) throws IOException {
        try (Scanner scanner = new Scanner(new File(trainingFile))) {
            numRecords = scanner.nextInt();
            numAttributes = scanner.nextInt();
            numClasses = scanner.nextInt();

            attributeValues = new int[numAttributes];
            for (int i = 0; i < numAttributes; i++) {
                attributeValues[i] = scanner.nextInt();
            }

            for (int i = 0; i < numRecords; i++) {
                int[] attributes = new int[numAttributes];
                for (int j = 0; j < numAttributes; j++) {
                    attributes[j] = scanner.nextInt();
                }
                int className = scanner.nextInt();
                records.add(new Record(attributes, className));
            }
        }
    }

    // compute the probabilities required for classification
    public void computeProbabilities() {
        computeClassProbabilities();
        computeConditionalProbabilities();
    }

    // compute the prior probabilities of classes
    private void computeClassProbabilities() {
        classProbabilities = new double[numClasses];

        for (Record record : records) {
            classProbabilities[record.className - 1]++;
        }
        for (int i = 0; i < numClasses; i++) {
            classProbabilities[i] /= numRecords;
        }
    }

    // compute the conditional probabilities of attributes given classes
    private void computeConditionalProbabilities() {
        conditionalProbs = new double[numAttributes][numClasses][];

        for (int i = 0; i < numAttributes; i++) {
            conditionalProbs[i] = new double[numClasses][attributeValues[i]];
            for (Record record : records) {
                int classIndex = record.className - 1;
                int attributeIndex = record.attributes[i] - 1;
                conditionalProbs[i][classIndex][attributeIndex]++;
            }
            for (int j = 0; j < numClasses; j++) {
                for (int k = 0; k < attributeValues[i]; k++) {
                    conditionalProbs[i][j][k] = (conditionalProbs[i][j][k] + 1) /
                            (classProbabilities[j] * numRecords + attributeValues[i]);
                }
            }
        }
    }

    // compute the probability of a class given a set of attributes
    private double computeClassProbability(int classIndex, int[] attributes) {
        double probability = 1;
        for (int i = 0; i < numAttributes; i++) {
            probability *= conditionalProbs[i][classIndex][attributes[i] - 1];
        }
        return probability * classProbabilities[classIndex];
    }

    // classify a record based on its attributes
    private int classify(int[] attributes) {
        double maxProbability = Double.NEGATIVE_INFINITY;
        int maxClassIndex = -1;
        for (int i = 0; i < numClasses; i++) {
            double probability = computeClassProbability(i, attributes);
            if (probability > maxProbability) {
                maxProbability = probability;
                maxClassIndex = i;
            }
        }
        return maxClassIndex + 1;
    }

    // classify test data and write the results to a file
    public void classifyData(String testFile, String classifiedFile) throws IOException {
        try (Scanner scanner = new Scanner(new File(testFile));
                PrintWriter writer = new PrintWriter(new FileWriter(classifiedFile))) {
            int numTestRecords = scanner.nextInt();
            writer.println(numTestRecords);
            for (int i = 0; i < numTestRecords; i++) {
                int[] attributes = new int[numAttributes];
                for (int j = 0; j < numAttributes; j++) {
                    attributes[j] = scanner.nextInt();
                }
                int className = classify(attributes);
                writer.println(className);
            }
        }
    }

    // perform leave-one-out cross-validation
    public double leaveOneOutCrossValidation() {
        int numErrors = 0;
        for (int i = 0; i < numRecords; i++) {
            Record testRecord = records.remove(i);
            numRecords--;
            computeProbabilities();
            int predictedClass = classify(testRecord.attributes);
            records.add(i, testRecord);
            numRecords++;
            if (predictedClass != testRecord.className) {
                numErrors++;
            }
        }
        double errorRate = 100.0 * numErrors / numRecords;
        System.out.println("Validation error: " + errorRate + "%");
        return errorRate;
    }
}