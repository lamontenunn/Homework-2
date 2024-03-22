/**
 * KmeansTester.java
 *
 * This program groups the customers of a bank into clusters using the k-means algorithm.
 * The customers are clustered based on age, income, and credit score.
 * The program determines an appropriate number of clusters u
 * performs data conversion and normalization.
 *
 * Input:  User-specified input file containing customer records
 * Output: User-specified output file containing records and their cluster labels in a grouped format
 *
 * The program performs the following steps:
 * 1. Prompts the user for the input and output file names.
 * 2. Iterates from 1 to 10 clusters and calculates the SSE (Sum of Squared Errors) for each iteration.
 * 3. Plots the SSE values against the number of clusters to help determine the appropriate number of clusters.
 * 4. Prompts the user for the desired number of clusters 
 * 5. Loads the customer records from the input file.
 * 6. Sets the parameters for k-means clustering (number of clusters, iterations, and random seed).
 * 7. Performs k-means clustering on the customer records.
 * 8. Writes the clustered records and their cluster labels to the output file in a grouped format.
 *
 * @author LaMonte Nunn
 * @version Nunn
 */


import java.io.*;
import java.util.*;

public class KmeansTester {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the input file name: ");
        String inputFile = scanner.nextLine();

        System.out.print("Enter the output file name: ");
        String outputFile = scanner.nextLine();

        int maxClusters = 10;
        double[] sseValues = new double[maxClusters];

        for (int numClusters = 1; numClusters <= maxClusters; numClusters++) {
            Kmeans k = new Kmeans();
            k.load(inputFile);
            k.setParameters(numClusters, 10000, 58947);
            k.cluster();
            sseValues[numClusters - 1] = k.calculateSSE();
        }

        plotGraph(sseValues);

        System.out.print("Enter the desired number of clusters: ");
        int desiredClusters = scanner.nextInt();

        Kmeans k = new Kmeans();
        k.load(inputFile);
        k.setParameters(desiredClusters, 10000, 58947);
        k.cluster();
        k.display(outputFile);

        scanner.close();
    }

    private static void plotGraph(double[] sseValues) {

        for (int i = 0; i < sseValues.length; i++) {
            System.out.println("clusters: " + (i + 1) + ", SSE: " + sseValues[i]);
        }
    }
}