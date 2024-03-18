

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ImageReaderTester {

    private static final int NEIGHBORS = 8;

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        // Program reads training data
        // from a user specifed file
        System.out.print("Enter name of training data file: ");
        String trainingDataIn = scanner.next();

        // Program reads in test data from user specifed file
        System.out.print("Enter name of test data file name: ");
        String testData = scanner.next();

        // Then
        System.out.print("Enter name of classified data output file name: ");
        String classifedOutput = scanner.next();

        normalizeTrainingData(trainingDataIn, "normalizedTrainingData");
        normalizeTestData(testData, "normalizedTestData");

        ImageReader n = new ImageReader();

        n.loadTrainingData("normalizedTrainingData");
        n.setParamaters(NEIGHBORS);

        // Call leave one out which also displays the validation rate
        n.leaveOneOutClassification();

        classifyTestData("normalizedTestData", classifedOutput, n);

    }

    private static void normalizeTrainingData(String input, String output) throws IOException {

        Scanner in = new Scanner(new File(input));
        PrintWriter out = new PrintWriter(new FileWriter(output));

        int numberOfRecords = in.nextInt();
        int numberOfAttributes = in.nextInt();
        int numberOfClasses = in.nextInt();

        out.println(numberOfRecords + " " + numberOfAttributes + " " + numberOfClasses);

        for (int i = 0; i < numberOfRecords; i++) {

            int risk = 0;
            int[] sexMaritalClassArr = new int[4];

            // credit score
            int creditScore = in.nextInt();
            double creditNumber = normalizeData(creditScore, 500, 900);
            out.print(creditNumber + " ");

            // income
            int income = in.nextInt();
            double incomeNumber = normalizeData(income, 30, 90);
            out.print(incomeNumber + " ");

            // age
            int age = in.nextInt();
            double ageNumber = normalizeData(age, 30, 80);
            out.print(ageNumber + " ");

            // sex

            String sex = in.next();
            if (sex.equals("male"))
                sexMaritalClassArr[0] = 0;
            else
                sexMaritalClassArr[0] = 1;

            // marital status
            String maritalStatus = in.next();
            if (maritalStatus.equals("single"))
                sexMaritalClassArr[1] = 1;
            else if (maritalStatus.equals("married"))
                sexMaritalClassArr[2] = 1;
            else if (maritalStatus.equals("divorced"))
                sexMaritalClassArr[3] = 1;

            // class
            String riskClass = in.next();
            if (riskClass.equals("low"))
                risk = 0;
            else if (riskClass.equals("medium"))
                risk = 1;
            else if (riskClass.equals("high"))
                risk = 2;
            else if (riskClass.equals("undetermined"))
                risk = 3;

            for (int num : sexMaritalClassArr)
                out.print(num + " ");

            out.print(risk);
            out.println();

        }
        in.close();
        out.close();

    }

    private static void normalizeTestData(String input, String out) throws IOException {

        PrintWriter output = new PrintWriter(new FileWriter(out));

        Scanner in = new Scanner(new File(input));

        int numberOfRecords = in.nextInt();

        output.print(numberOfRecords);
        output.println();

        for (int i = 0; i < numberOfRecords; i++) {
            int[] sexMaritalClassArr = new int[4];


            // credit score
            int creditScore = in.nextInt();
            double creditNumber = normalizeData(creditScore, 500, 900);
            output.print(creditNumber + " ");

            // income
            int income = in.nextInt();
            double incomeNumber = normalizeData(income, 30, 90);
            output.print(incomeNumber + " ");

            // age
            int age = in.nextInt();
            double ageNumber = normalizeData(age, 30, 80);
            output.print(ageNumber + " ");

            // sex

            String sex = in.next();
            if (sex.equals("male"))
                sexMaritalClassArr[0] = 0;
            else
                sexMaritalClassArr[0] = 1;

            // marital status
            String maritalStatus = in.next();
            if (maritalStatus.equals("single"))
                sexMaritalClassArr[1] = 1;
            else if (maritalStatus.equals("married"))
                sexMaritalClassArr[2] = 1;
            else if (maritalStatus.equals("divorced"))
                sexMaritalClassArr[3] = 1;

            
                
                for (int num : sexMaritalClassArr)
                output.print(num + " ");
                
                output.println();

               

        }

        output.close();
        in.close();

    }

    private static double normalizeData(double number, double min, double max) {

        // this algorithm will convert the credit score to a double
        // on the scale from 0 - 1 using Min-Max Normalization

        return (number - min) / (max - min);

    }

    private static void classifyTestData(String testDataFile, String outputFile, ImageReader classifier) throws IOException {
        Scanner in = new Scanner(new File("normalizedTestData"));
        PrintWriter out = new PrintWriter(new FileWriter(outputFile));
    

        // reads in num of records 
        int numberOfTestRecords = in.nextInt();
        in.nextLine(); 
    
        for (int i = 0; i < numberOfTestRecords; i++) {
            if (!in.hasNextLine()) break; 
    

            String line = in.nextLine();
            // creates string array
            String[] parts = line.split("\\s+");
    
            // Parse continuous and categorical attributes from the line
            double[] continuousAttributes = new double[3];
            for (int j = 0; j < 3; j++) {
                continuousAttributes[j] = Double.parseDouble(parts[j]);
            }
    
            int[] categoricalAttributes = new int[4];
            for (int j = 0; j < 4; j++) {
                categoricalAttributes[j] = Integer.parseInt(parts[j + 3]);
            }
    
            // Classify the record
            int predictedClass = classifier.classify(continuousAttributes, categoricalAttributes);
    
            // Write the predicted class to the output file
            // Here you can format the output as needed; this example just writes the predicted class index
            out.println(predictedClass);
        }
    
        in.close();
        out.close();
    }
    

}