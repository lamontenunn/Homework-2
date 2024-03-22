package program;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class NearestNeighbor {

    private static final int NUM_ATTRIBUTES = 5;

    private class Record {
        private double[] attributes;
        private int classLabel;

        private Record(double[] attributes, int classLabel) {
            this.attributes = attributes;
            this.classLabel = classLabel;
        }
    }

    private int numRecords;
    private int numAttributes;
    private int numClasses;
    private int numNeighbors;
    private ArrayList<Record> trainingSet;

    public NearestNeighbor() {
        numRecords = 0;
        numAttributes = 0;
        numClasses = 0;
        numNeighbors = 0;
        trainingSet = null;
    }

    public void loadTrainingData(String trainingFile) throws IOException {
        Scanner inFile = new Scanner(new File(trainingFile));

        numRecords = inFile.nextInt();
        numAttributes = inFile.nextInt();
        numClasses = inFile.nextInt();

        trainingSet = new ArrayList<>();
        inFile.nextLine();

        while (inFile.hasNextLine()) {
            String line = inFile.nextLine();
            String[] parts = line.split("\\s+");

            if (parts.length != NUM_ATTRIBUTES + 1) {
                throw new IllegalArgumentException("Invalid training data format.");
            }

            double[] attributes = new double[NUM_ATTRIBUTES];
            for (int i = 0; i < NUM_ATTRIBUTES; i++) {
                attributes[i] = Double.parseDouble(parts[i]);
            }

            int classLabel = Integer.parseInt(parts[NUM_ATTRIBUTES]);

            trainingSet.add(new Record(attributes, classLabel));
        }

        inFile.close();
    }

    public void setParameters(int numNeighbors) {
        this.numNeighbors = numNeighbors;
    }

    private void findNearestNeighbors(double[] distances, int[] indices) {
        for (int i = 0; i < numNeighbors; i++) {
            for (int j = i + 1; j < distances.length; j++) {
                if (distances[i] > distances[j]) {
                    double tempDistance = distances[i];
                    distances[i] = distances[j];
                    distances[j] = tempDistance;
    
                    int tempIndex = indices[i];
                    indices[i] = indices[j];
                    indices[j] = tempIndex;
                }
            }
        }
    }

    private int findMajorityClass(int[] indices) {
        int[] classCounts = new int[numClasses];

        for (int i = 0; i < numNeighbors; i++) {
            classCounts[trainingSet.get(indices[i]).classLabel]++;
        }

        int maxCount = 0;
        int majorityClass = 0;
        for (int i = 0; i < numClasses; i++) {
            if (classCounts[i] > maxCount) {
                maxCount = classCounts[i];
                majorityClass = i;
            }
        }

        return majorityClass;
    }

    private double calculateDistance(Record record1, Record record2) {
        double distance = 0;
        for (int i = 0; i < numAttributes; i++) {
            double diff = record1.attributes[i] - record2.attributes[i];
            distance += diff * diff;
        }
        return Math.sqrt(distance);
    }

    public void leaveOneOutClassification() {
        int numErrors = 0;

        for (int i = 0; i < numRecords; i++) {
            Record testRecord = trainingSet.get(i); // leave this one out
            double[] attributes = testRecord.attributes;
            int actualClass = testRecord.classLabel;

            int predictedClass = classifyExcludingRecord(attributes, i);

            if (predictedClass != actualClass) {
                numErrors++;
            }
        }

        double errorRate = (double) numErrors / numRecords * 100;
        System.out.println("Error Rate: " + errorRate + "%");
    }

    private int classifyExcludingRecord(double[] attributes, int excludeIndex) {
        
        double[] distances = new double[numRecords - 1];
        int[] indices = new int[numRecords - 1];

        int index = 0;
        for (int i = 0; i < numRecords; i++) {
            if (i == excludeIndex) {
                continue;
            }

            Record comparisonRecord = trainingSet.get(i);
            distances[index] = calculateDistance(new Record(attributes, 0), comparisonRecord);
            indices[index] = i;
            index++;
        }

        findNearestNeighbors(distances, indices);

        return findMajorityClass(indices);
    }

    public int classify(double[] attributes) {
        double[] distances = new double[numRecords];
        int[] indices = new int[numRecords];

        for (int i = 0; i < numRecords; i++) {
            Record comparisonRecord = trainingSet.get(i);
            distances[i] = calculateDistance(new Record(attributes, 0), comparisonRecord);
            indices[i] = i;
        }

        findNearestNeighbors(distances, indices);

        return findMajorityClass(indices);
    }
}