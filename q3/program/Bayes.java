package program;
import java.io.*;
import java.util.*;

//Bayes classifier
public class Bayes {
    /*************************************************************************/

    // Record class (inner class)
    private class Record {
        private int[] attributes; // attributes of record
        private int className; // class of record

        // Constructor of record
        private Record(int[] attributes, int className) {
            this.attributes = attributes; // set attributes
            this.className = className; // set class
        }
    }

    /*************************************************************************/

    private int numberRecords; // number of records
    private int numberOfAttributes; // number of attributes
    private int numberClasses; // number of classes
    private ArrayList<Record> records; // list of records
    private int[] numOfEachAttributeArray; // number of attribute values
    double[][][] table; // conditional probabilities
    double[] classTable; // class probabilities

    /*************************************************************************/

    // Constructor of Bayes
    public Bayes() {
        // initial data is empty
        numberRecords = 0;
        numberOfAttributes = 0;
        numberClasses = 0;
        records = null;
        numOfEachAttributeArray = null;
        table = null;
        classTable = null;
    }

    /*************************************************************************/

    // Method loads data from training file
   // Method loads data from training file
    public void loadTrainingData(String trainingFile) throws IOException {
    Scanner inFile = new Scanner(new File(trainingFile));

    // read number of records, attributes, classes
    numberRecords = inFile.nextInt();
    numberOfAttributes = inFile.nextInt();
    numberClasses = inFile.nextInt();

    // read number of attribute values
    numOfEachAttributeArray = new int[numberOfAttributes];
    numOfEachAttributeArray[0] = 3; // experience (0.0, 0.5, 1.0)
    numOfEachAttributeArray[1] = 2; // language (0.0, 1.0)
    numOfEachAttributeArray[2] = 3; // YoE (0.0, 0.5, 1.0)
    numOfEachAttributeArray[3] = 2; // major (0.0, 1.0)
    numOfEachAttributeArray[4] = 4; // grade (0.0, 0.25, 0.75, 1.0)

    // list of records
    records = new ArrayList<Record>();

    // read each record
    for (int i = 0; i < numberRecords; i++) {
        // create attribute array
        int[] attributeArray = new int[numberOfAttributes];

        // read attributes
        int experience = inFile.nextInt();
        attributeArray[0] = experience;

        int language = inFile.nextInt();
        attributeArray[1] = language;

        int YoE = inFile.nextInt();
        attributeArray[2] = YoE;

        int major = inFile.nextInt();
        attributeArray[3] = major;

        int grade = inFile.nextInt();
        attributeArray[4] = grade;
        
        // read class
        int className = inFile.nextInt();

        // create record
        Record record = new Record(attributeArray, className);

        // add record to list of records
        records.add(record);
    }

    inFile.close();
}

    /*************************************************************************/

    // Method computes probability values necessary for classification
    public void computeProbability() {
        // compute class probabilities
        computeClassTable();

        // compute conditional probabilities
        computeTable();
    }

    /*************************************************************************/

    // Method computes class probabilities
    private void computeClassTable() {
        classTable = new double[numberClasses];

        // initialize class frequencies
        for (int i = 0; i < numberClasses; i++)
            classTable[i] = 0;

        // compute class

        // 4 3 3 4
        for (int i = 0; i < numberRecords; i++)
            classTable[records.get(i).className - 1] += 1;

        // 4/14 3/14 3/14 4/14

        // normalize class frequencies
        for (int i = 0; i < numberClasses; i++)
            classTable[i] /= numberRecords;
    }

    /*************************************************************************/

    // Method computes conditional probabilities
    private void computeTable() {
        // array to store conditional probabilites
        table = new double[numberOfAttributes][][];

        // compute conditional probabilities of each attribute
        for (int i = 0; i < numberOfAttributes; i++)
            compute(i + 1);
    }

    /*************************************************************************/

    // Method computes conditional probabilities of an attribute
    private void compute(int attribute) {
        // find number of attribute values
        int numOfEachAttributeArray = this.numOfEachAttributeArray[attribute - 1];

        // create array to hold conditional probabilities
        table[attribute - 1] = new double[numberClasses][numOfEachAttributeArray];

        // initialize conditional probabilities
        for (int i = 0; i < numberClasses; i++)
            for (int j = 0; j < numOfEachAttributeArray; j++)
                table[attribute - 1][i][j] = 0;

        // compute class-attribute frequencies
        for (int k = 0; k < numberRecords; k++) {
            int i = records.get(k).className - 1;
            int j = records.get(k).attributes[attribute - 1] - 1;
            table[attribute - 1][i][j] += 1;
        }

        // compute conditional probabilities using laplace correction

        for (int i = 0; i < numberClasses; i++)
            for (int j = 0; j < numOfEachAttributeArray; j++) {

                // i == current class being considered
                // j == current attribute being considered
                double value = (table[attribute - 1][i][j] + 1) / (classTable[i] * numberRecords + numOfEachAttributeArray);

                // classTable [i] represents the probibility of the current class | i is the
                // current class (we have 4 total so were gonna loop through 4 times)
                table[attribute - 1][i][j] = value;
            }
    }

    /*************************************************************************/

    // Method computes conditional probability of a class for given attributes
    private double findProbability(int className, int[] attributes) {
        double value;
        double product = 1;

        // find product of conditional probabilities stored in table
        for (int i = 0; i < numberOfAttributes; i++) {
            value = table[i][className - 1][attributes[i] - 1];
            product = product * value;
        }

        // multiply product and class probability
        return product * classTable[className - 1];
    }

    /*************************************************************************/

    // Method classifies an attribute
    private int classify(int[] attributes) {
        double maxProbability = 0;
        int maxClass = 0;

        // for each class
        for (int i = 0; i < numberClasses; i++) {
            // find conditional probability of class given the attribute
            double probability = findProbability(i + 1, attributes);

            // choose the class with the maximum probability
            if (probability > maxProbability) {
                maxProbability = probability;
                maxClass = i;
            }
        }

        // return maximum class
        return maxClass + 1;
    }

    /*************************************************************************/

    // Method reads test records from test file and writes classified records
    // to classified file
    public void classifyData(String testFile, String classifiedFile) 
    throws IOException
    {
         Scanner inFile = new Scanner(new File(testFile));
         PrintWriter outFile = new PrintWriter(new FileWriter(classifiedFile));

         //read number of records
         int numberRecords = inFile.nextInt();

         //write number of records
         outFile.println(numberRecords);

         //for each record
         for (int i = 0; i < numberRecords; i++)
         {
             //create attribute array
             int[] attributeArray = new int[numberOfAttributes];

             //read attributes
             for (int j = 0; j < numberOfAttributes; j++)
                  attributeArray[j] = inFile.nextInt();

             //find class of attributes
             int className = classify(attributeArray);

             //write class name
             outFile.println(className);
         }

         inFile.close();
         outFile.close();
    }





    /*************************************************************************/



    public void leaveOneOut() {
        int numberErrors = 0;
    
        for (int i = 0; i < numberRecords; i++) {
            // Remove the current record from the training set
            Record removedRecord = records.remove(i);
    
            // re-train the Bayes classifier
            computeProbability();
    
            // Classify the removed record using the trained classifier
            int predictedClass = classify(removedRecord.attributes);
    
            // Compare the predicted class with the actual class
            if (predictedClass != removedRecord.className)
                numberErrors++;
    
            // Add the removed record back to the training set
            records.add(i, removedRecord);
        }
    
        // Calculate and print the error rate
        double errorRate = (double) numberErrors / numberRecords * 100;
        System.out.println("Leave-one-out error rate: " + errorRate + "%");
    }

    /*************************************************************************/
}