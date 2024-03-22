package program;
import java.io.*;
import java.util.*;
import java.util.Scanner;

//Program tests Bayes classifier in specific application
public class BayesTester
{
    /*************************************************************************/

    //Main method
    public static void main(String[] args) throws IOException

    {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter name of training file: ");
        String trainingFile = input.nextLine();

        System.out.print("Enter name of test file: ");
        String testFile = input.nextLine();

        System.out.print("Enter name of classified data file: ");
        String classifiedFile = input.nextLine();

        



        //preprocess files
        convertTrainingFile(trainingFile, "normalizedTrainingFile");
        convertTestFile(testFile, "normalizedTestFile"); 

        //construct bayes classifier
        Bayes classifier = new Bayes();

        //load training data
        classifier.loadTrainingData("normalizedTrainingFile");

        //compute probabilities
       classifier.computeProbability();

        //classify data
       classifier.classifyData("normalizedTestFile", classifiedFile);

        //validate classifier
       classifier.leaveOneOut();
        
        //postprocess files
       convertClassFile(classifiedFile);
    }

    /**
     * @throws IOException **************************************************************************/


     static void convertTrainingFile(String input, String output) throws IOException {
        Scanner in = new Scanner(new File(input));
        PrintWriter out = new PrintWriter(new FileWriter(output));
    
        int numberOfRecords = in.nextInt();
        int numberOfAttributes = in.nextInt();
        int numberOfClasses = in.nextInt();
    
        out.print(numberOfRecords + " " + numberOfAttributes + " " + numberOfClasses);
        out.println();
    
        for (int i = 0; i < numberOfRecords; i++) {
            int numOfLang = in.nextInt();
            out.print((numOfLang == 0 ? 1 : numOfLang == 1 ? 2 : 3) + " ");
    
            String language = in.next();
            out.print((language.equals("no") ? 1 : 2) + " ");
    
            int YoE = in.nextInt();
            out.print((YoE == 0 ? 1 : YoE == 1 ? 2 : 3) + " ");
    
            String major = in.next();
            out.print((major.equals("other") ? 1 : 2) + " ");
    
            String grade = in.next();
            out.print((grade.equals("A") ? 4 : grade.equals("B") ? 3 : grade.equals("C") ? 2 : 1) + " ");
    
            String interviewResult = in.next();
            out.println((interviewResult.equals("interview") ? 2 : 1));
        }
    
        in.close();
        out.close();
    }

    static void convertTestFile(String input, String output) throws IOException {

        Scanner in = new Scanner(new File(input));
        PrintWriter out = new PrintWriter(new FileWriter(output));
    
        int numberOfRecords = in.nextInt();
        int numberOfAttributes = in.nextInt();
    
        out.print(numberOfRecords + " " + numberOfAttributes);
        out.println();
    
        for (int i = 0; i < numberOfRecords; i++) {
            int numOfLang = in.nextInt();
            out.print((numOfLang == 0 ? 1 : numOfLang == 1 ? 2 : 3) + " ");
    
            String language = in.next();
            out.print((language.equals("no") ? 1 : 2) + " ");
    
            int YoE = in.nextInt();
            out.print((YoE == 0 ? 1 : YoE == 1 ? 2 : 3) + " ");
    
            String major = in.next();
            out.print((major.equals("other") ? 1 : 2) + " ");
    
            String grade = in.next();
            out.println((grade.equals("A") ? 4 : grade.equals("B") ? 3 : grade.equals("C") ? 2 : 1) + " ");
    
        }
    
        in.close();
        out.close();
    }


    static void convertClassFile(String classifiedFile) throws IOException {
        Scanner in = new Scanner(new File(classifiedFile));
        PrintWriter out = new PrintWriter(new FileWriter(classifiedFile));

        int numberOfRecords = in.nextInt();
        out.println(numberOfRecords);

        for (int i = 0; i < numberOfRecords; i++) {
            int classValue = in.nextInt();
            String className = classValue == 2 ? "interview" : "no";
            out.println(className);
        }

        in.close();
        out.close();
    }


}


