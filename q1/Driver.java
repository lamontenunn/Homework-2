import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Driver {

    private static final int NEIGHBORS = 5;
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name of training data file: ");
        String trainingDataIn = scanner.next();
        System.out.print("Enter name of training output file name: ");
        String trainingDataOut = scanner.next();

        System.out.print("Enter name of classifed data file name: ");
        String classifedFileName = scanner.next();

       
        normalizeTrainingData(trainingDataIn, trainingDataOut);




        NearestNeighbor n = new NearestNeighbor();

        n.loadTrainingData(trainingDataOut);
        
    }

    private static void normalizeTrainingData(String input, String output) throws IOException {

        Scanner in = new Scanner(new File(input));
        PrintWriter out = new PrintWriter(new FileWriter(output));

       

        int numberOfRecords = in.nextInt();
        int numberOfAttributes = in.nextInt();
        int numberOfClasses = in.nextInt();

        out.println(numberOfRecords + " " + numberOfAttributes + " " + numberOfClasses);

        for (int i = 0; i < numberOfRecords; i++) {

            int[] sexMaritalClassArr = new int[9];


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
            if(sex.equals("male")) sexMaritalClassArr[0] = 1; else sexMaritalClassArr[1] = 1;

            // marital status
            String maritalStatus = in.next();
            if(maritalStatus.equals("single")) sexMaritalClassArr[2] = 1;
            else if(maritalStatus.equals("married")) sexMaritalClassArr[3] = 1;
            else if(maritalStatus.equals("divorced")) sexMaritalClassArr[4] = 1;

   
            // class
            String riskClass = in.next();
            if(riskClass.equals("low")) sexMaritalClassArr[5] = 1;
            else if(riskClass.equals("medium")) sexMaritalClassArr[6] = 1;
            else if(riskClass.equals("high")) sexMaritalClassArr[7] = 1;
            else if(riskClass.equals("undetermined")) sexMaritalClassArr[8] = 1;

            for(int num : sexMaritalClassArr) out.print(num+"");
            out.println();


        }
        in.close();
        out.close();

    }

    private static double normalizeData(double number, double min, double max) {

        // this algorithm will convert the credit score to a double
        // on the scale from 0 - 1 using Min-Max Normalization

        return (number - min) / (max - min);

    }





}