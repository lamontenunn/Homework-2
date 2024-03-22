
/*
* Bayes Classifier Application
*
* This application implements a Bayes classifier for predicting job interview outcomes based on various attributes.
* It uses a training dataset to learn the probabilities and then classifies a test dataset.
*
* The application consists of two main classes:
* 1. Bayes: Represents the Bayes classifier and provides methods for training and classification.
* 2. BayesTester: Serves as the entry point of the application and handles user interaction and file I/O.
*
* The input files are assumed to be in a specific format:
* - Training file: Contains the training dataset with attributes and class labels.
* - Test file: Contains the test dataset with attributes only.
*
* The application prompts the user to enter the names of the training file, test file, and classified file.
* It then converts the input files to a normalized format, trains the Bayes 
* classifier using the training data,
* classifies the test data, and writes the results to the classified file.
*
* Additionally, the application performs leave-one-out cross-validation to evaluate the accuracy of the classifier.
*
* 
*
* Author: LaMonte Nunn
* Date: 21 March 2024
*/
package program;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class BayesTester {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the training file name and read the input
        System.out.print("Enter the training file name: ");
        String trainingFile = scanner.nextLine();

        System.out.print("Enter the test file name: ");
        String testFile = scanner.nextLine();

        System.out.print("Enter the classified file name: ");
        String classifiedFile = scanner.nextLine();

        // Convert the training file to a normalized format
        convertFile(trainingFile, "normalizedTraining", true);

        // Convert the test file to a normalized format
        convertFile(testFile, "normalizedTest", false);

        Bayes classifier = new Bayes();

        // Load the training data into the classifier
        classifier.loadTrainingData("normalizedTraining");

        // Compute the probabilities required for classification
        classifier.computeProbabilities();

        // Classify the test data and write the results to the classified file
        classifier.classifyData("normalizedTest", classifiedFile);

        // Perform leave-one-out cross-validation on the classifier
        classifier.leaveOneOutCrossValidation();

        // Convert the classified file to a human-readable format
        convertClassFile(classifiedFile, "classifedDataFile");
    }

    // Method to convert input files to a normalized format
    private static void convertFile(String inputFile, String outputFile, boolean isTrainingFile) throws IOException {
        try (Scanner scanner = new Scanner(new File(inputFile));
                PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            int numRecords = scanner.nextInt();
            writer.print(numRecords);

            if (isTrainingFile) {
                int numAttributes = scanner.nextInt();
                int numClasses = scanner.nextInt();
                writer.print(" " + numAttributes + " " + numClasses);

                for (int i = 0; i < numAttributes; i++) {
                    writer.print(" " + scanner.nextInt());
                }
            }
            writer.println();

            for (int i = 0; i < numRecords; i++) {
                int numLanguages = scanner.nextInt() + 1;
                writer.print(numLanguages + " ");

                String language = scanner.next();
                int languageNumber = convertLanguageToNumber(language);
                writer.print(languageNumber + " ");

                int YoE = scanner.nextInt() + 1;
                writer.print(YoE + " ");

                String degree = scanner.next();
                int degreeNumber = convertDegreeToNumber(degree);
                writer.print(degreeNumber + " ");

                String grade = scanner.next();
                int gradeNumber = convertGradeToNumber(grade);
                writer.print(gradeNumber);

                if (isTrainingFile) {
                    String className = scanner.next();
                    int classNumber = convertClassToNumber(className);
                    writer.print(" " + classNumber);
                }

                writer.println();
            }
        }
    }

    // method converts the classified file to a human-readable format
    private static void convertClassFile(String inputFile, String outputFile) throws IOException {
        try (Scanner scanner = new Scanner(new File(inputFile));
                PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            int numRecords = scanner.nextInt();
            writer.println(numRecords);
            for (int i = 0; i < numRecords; i++) {
                int classNumber = scanner.nextInt();
                String className = convertNumberToClass(classNumber);
                writer.println(className);
            }
        }
    }

    // convert language knowledge to a numeric value
    private static int convertLanguageToNumber(String language) {
        return language.equals("yes") || language.equals("java") ? 1 : 2;
    }

    // convert degree type to a numeric value
    private static int convertDegreeToNumber(String degree) {
        return degree.equals("cs") ? 1 : 2;
    }

    // Method convert academic grade to a numeric value
    private static int convertGradeToNumber(String grade) {
        return switch (grade) {
            case "A" -> 1;
            case "B" -> 2;
            case "C" -> 3;
            default -> 4;
        };
    }

    // convert class name to a numeric value
    private static int convertClassToNumber(String className) {
        return className.equals("interview") ? 1 : 2;
    }

    // convert numeric class value to its corresponding class name
    private static String convertNumberToClass(int number) {
        return number == 1 ? "interview" : "no";
    }
}