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