
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class NearestNeighbor {

    private class Record {
        private double[] continuousAttributes; // Continuous attributes of the record
        private int[] categoricalAttributes; // Categorical attributes of the record, one-hot encoded
        private int className; // Class of the record

        // Constructor of Record
        private Record(double[] continuousAttributes, int[] categoricalAttributes, int className) {
            this.continuousAttributes = continuousAttributes;
            this.categoricalAttributes = categoricalAttributes;
            this.className = className; // Set class
        }
    }

    public NearestNeighbor() {

        numberOfRecords = 0;
        numberOfAttributes = 0;
        numberOfClasses = 0;
        numberOfNeighbors = 0;
        records = null;

    }

    private int numberOfRecords; // number of training records
    private int numberOfAttributes; // number of attributes
    private int numberOfClasses; // number of classes
    private int numberOfNeighbors; // number of nearest neighbors
    private ArrayList<Record> records; // list of training records

    public void loadTrainingData(String trainingFile) throws IOException {
        Scanner inFile = new Scanner(new File(trainingFile));

        // Read number of records, attributes, classes
        numberOfRecords = inFile.nextInt();
        numberOfAttributes = inFile.nextInt();
        numberOfClasses = inFile.nextInt();

        // Create empty list of records
        records = new ArrayList<Record>();
        inFile.nextLine();

        while (inFile.hasNextLine()) {
            String line = inFile.nextLine();
            String[] parts = line.split("\\s+");

            // first 3 attributes are continuous and already normalized
            double[] continuousAttributes = new double[3];
            for (int i = 0; i < 3; i++) {
                continuousAttributes[i] = Double.parseDouble(parts[i]);
            }

            //  the next 4 attributes are categorical (one-hot encoded)
            int[] categoricalAttributes = new int[4];
            for (int i = 0; i < 4; i++) {
                categoricalAttributes[i] = Integer.parseInt(parts[i + 3]);
            }

            // The last part is the class
            int className = Integer.parseInt(parts[parts.length - 1]);

            // Create and add the record
            records.add(new Record(continuousAttributes, categoricalAttributes, className));
        }

        inFile.close();
    }


    // method sets num of Neighbors
    void setParamaters(int num) {
        numberOfNeighbors = num;

    }

    /*************************************************************************/

    // Method finds the nearest neighbors
    private void nearestNeighbor(double[] distance, int[] id) {
        // sort distances and choose nearest neighbors
        for (int i = 0; i < numberOfNeighbors; i++)
            for (int j = i; j < numberOfRecords; j++)
                if (distance[i] > distance[j]) {
                    double tempDistance = distance[i];
                    distance[i] = distance[j];
                    distance[j] = tempDistance;

                    int tempId = id[i];
                    id[i] = id[j];
                    id[j] = tempId;
                }
    }

    /*************************************************************************/

    // Method finds the majority class of nearest neighbors
    private int majority(int[] id) {
        double[] frequency = new double[numberOfClasses];

        // class frequencies are zero initially
        for (int i = 0; i < numberOfClasses; i++)
            frequency[i] = 0;

        // each neighbor contributes 1 to its class
        for (int i = 0; i < numberOfNeighbors; i++)
            frequency[records.get(id[i]).className] += 1;

        // find majority class
        int maxIndex = 0;
        for (int i = 0; i < numberOfClasses; i++)
            if (frequency[i] > frequency[maxIndex])
                maxIndex = i;

        return maxIndex;
    }

    /*************************************************************************/

    private double distance(Record r1, Record r2) {

        // finds Eucudian distance
        double continuousDistance = 0;
        for (int i = 0; i < r1.continuousAttributes.length; i++) {
            double diff = r1.continuousAttributes[i] - r2.continuousAttributes[i];
            continuousDistance += diff * diff;
        }


        // finds hamming distance / binary distance
        int hammingDistance = 0;
        for (int i = 0; i < r1.categoricalAttributes.length; i++) {
            if (r1.categoricalAttributes[i] != r2.categoricalAttributes[i]) {
                hammingDistance++;
            }
        }

        // Combine distances 
        return Math.sqrt(continuousDistance + hammingDistance);
    }

    public void leaveOneOutClassification() {
        double error = 0;

        for (int i = 0; i < records.size(); i++) {
            // Isolate the test record
            Record testRecord = records.get(i);
            double[] testContinuousAttributes = testRecord.continuousAttributes;
            int[] testCategoricalAttributes = testRecord.categoricalAttributes;
            int actualClass = testRecord.className;

            // Classify the test record using the modified training set
            int predictedClass = classifyExclude(testContinuousAttributes, testCategoricalAttributes, i);

            // Check if the classification was incorrect
            if (predictedClass != actualClass) {
                error+=1;
            }
        }

        // Calculate and print the accuracy or error rate

        double rate = error / records.size() * 100;
        System.out.println("Error Rate: " + rate + "%");
    }

    private int classifyExclude(double[] continuousAttributes, int[] categoricalAttributes, int excludeIndex) {
        double[] distanceArray = new double[numberOfRecords];
        int[] id = new int[numberOfRecords];

        int dIndex = 0;
        for (int i = 0; i < numberOfRecords; i++) {
            // if param from leave-one-out method is the excluded index then skip
            if (i == excludeIndex)
                continue; // Skip the excluded record

            Record comparisonRecord = records.get(i);
            double dist = distance(
                    new Record(continuousAttributes, categoricalAttributes, 0), // Temporary Record for comparison
                    comparisonRecord);

            distanceArray[dIndex] = dist;
            id[dIndex] = i;
            dIndex++;
        }

        // Find nearest neighbors
        nearestNeighbor(distanceArray, id);

        // Determine majority class
        return majority(id);
    }


    int classify(double[] continuousAttributes, int[] categoricalAttributes) {
        double[] distanceArray = new double[numberOfRecords];
        int[] id = new int[numberOfRecords];

        int dIndex = 0;
        for (int i = 0; i < numberOfRecords; i++) {

            Record comparisonRecord = records.get(i);
            double dist = distance(
                    new Record(continuousAttributes, categoricalAttributes, 0), // Temporary Record for comparison
                    comparisonRecord);

            distanceArray[dIndex] = dist;
            id[dIndex] = i;
            dIndex++;
        }

        // Find nearest neighbors
        nearestNeighbor(distanceArray, id);

        // Determine majority class
        return majority(id);
    }





    

}