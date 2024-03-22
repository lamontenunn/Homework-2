package program;
/**
 * This program recognizes images of digits using the nearest neighbor algorithm.
 * Specifically, it classifies images as either one or zero based on their 256 bits.
 * 
 * The program:
 * - Uses an appropriate number of nearest neighbors and a distance measure (Hamming distance)
 * - Leave-one-out method for validation
 * - Reads training data and test data from user-specified files
 * - Writes the classified data to a user-specified output file
 * - Displays the validation error on the screen
 *
 * The training file should contain at least twenty 16x16 bit images, labeled as ones or zeros.
 * The test file should contain at least five 16x16 bit images to be classified.
 *
 * Usage:
 * 1. Run the program
 * 2. Enter the name of the training data file when prompted
 * 3. Enter the name of the test data file when prompted
 * 4. Enter the name of the output file for the classified data when prompted
 * 5. The program will display the validation error and write the classifications to the output file
 */



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;

public class ImageReaderTester {

    private static final int NEIGHBORS = 4;

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        // Program reads training data
        // from a user specifed file
        System.out.print("Enter name of training data file: ");
        String trainingDataIn = scanner.next();

        // Program reads in test data from user specifed file
        System.out.print("Enter name of test data file name: ");
        String testData = scanner.next();

        System.out.print("Enter name of classified data output file name: ");
        String classifedOutput = scanner.next();


        ImageReader n = new ImageReader();

        n.loadTrainingData(trainingDataIn);
        n.setParamaters(NEIGHBORS);

        // Call leave one out which also displays the validation rate
        n.leaveOneOutValidation();

        classifyTestData(testData, classifedOutput, n);

    }

    private static void classifyTestData(String testDataFile, String outputFile, ImageReader classifier) throws IOException {
        Scanner in = new Scanner(new File(testDataFile));
        PrintWriter out = new PrintWriter(new FileWriter(outputFile));
    
        int numberOfTestRecords = in.nextInt();
    
        for (int i = 0; i < numberOfTestRecords; i++) {
            ArrayList<Integer> imageData = new ArrayList<>();
    
            for (int j = 0; j < 16; j++) {
                String line = in.next();
                for (int k = 0; k < line.length(); k++) {
                    imageData.add(Character.getNumericValue(line.charAt(k)));
                }
            }
    
            int predictedClass = classifier.classify(imageData);
            out.println(predictedClass);
        }
    
        in.close();
        out.close();
    }
    

}