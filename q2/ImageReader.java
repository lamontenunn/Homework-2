
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ImageReader {

    private class Record {
        private int className;
        private ArrayList<Integer> zerosAndOnes;
    
        private Record(ArrayList<Integer> zerosAndOnes, int className) {
            this.zerosAndOnes = zerosAndOnes;
            this.className = className;
        }
    }

    public ImageReader() {

        numberOfRecords = 0; 
        numberOfNeighbors = 0;
        numberOfClasses = 0;
        records = null;

    }
    
    private int numberOfClasses;
    private int numberOfRecords; // number of training records
    private int numberOfNeighbors; // number of nearest neighbors
    private ArrayList<Record> records;


    public void loadTrainingData(String trainingFile) throws IOException {
        Scanner inFile = new Scanner(new File(trainingFile));
        numberOfRecords = inFile.nextInt();
        numberOfClasses = inFile.nextInt();
    
        records = new ArrayList<Record>();
    
        while (inFile.hasNextLine()) {
            String line = inFile.nextLine().trim();
            if (line.isEmpty()) {
                continue; // Skip empty lines
            }
    
            int className = Integer.parseInt(line);
    
            ArrayList<Integer> zerosAndOnes = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
                String bitsLine = inFile.nextLine().trim();
                for (int j = 0; j < bitsLine.length(); j++) {
                    char bit = bitsLine.charAt(j);
                    zerosAndOnes.add(Character.getNumericValue(bit));
                }
            }
    
            records.add(new Record(zerosAndOnes, className));
        }
    
        inFile.close();
    }


    // method sets num of Neighbors
    void setParamaters(int num) {
        numberOfNeighbors = num;

    }

    /*************************************************************************/

    private void nearestNeighbor(double[] distance, int[] id) {
        int size = distance.length;
        for (int i = 0; i < numberOfNeighbors && i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (distance[i] > distance[j]) {
                    double tempDistance = distance[i];
                    distance[i] = distance[j];
                    distance[j] = tempDistance;
    
                    int tempId = id[i];
                    id[i] = id[j];
                    id[j] = tempId;
                }
            }
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
        int hammingDistance = 0;
        for (int i = 0; i < r1.zerosAndOnes.size(); i++) {
            if (r1.zerosAndOnes.get(i) != r2.zerosAndOnes.get(i)) {
                hammingDistance++;
            }
        }
        return hammingDistance;
    }

    



    int classify(ArrayList<Integer> imageData) {
        double[] distanceArray = new double[numberOfRecords];
        int[] id = new int[numberOfRecords];
    
        for (int i = 0; i < numberOfRecords; i++) {
            Record comparisonRecord = records.get(i);
            double dist = distance(new Record(imageData, 0), comparisonRecord);
            distanceArray[i] = dist;
            id[i] = i;
        }
    
        nearestNeighbor(distanceArray, id);
    
        return majority(id);
    }

    int classify(ArrayList<Integer> imageData, int excludeIndex) {
        double[] distanceArray = new double[numberOfRecords - 1];
        int[] id = new int[numberOfRecords - 1];
        int index = 0;
        for (int i = 0; i < numberOfRecords; i++) {
            if (i == excludeIndex) {
                continue; // Skip the current record being classified
            }
            Record comparisonRecord = records.get(i);
            double dist = distance(new Record(imageData, 0), comparisonRecord);
            distanceArray[index] = dist;
            id[index] = i;
            index++;
        }
        nearestNeighbor(distanceArray, id);
        return majority(id);
    }




    public void leaveOneOutValidation() {
        int errorCount = 0;
        for (int i = 0; i < numberOfRecords; i++) {
            Record testRecord = records.get(i);
            int predictedClass = classify(testRecord.zerosAndOnes, i);
            if (predictedClass != testRecord.className) {
                errorCount++;
            }
        }
        double accuracy = (double) errorCount / numberOfRecords * 100;
        System.out.println("validation error: " + accuracy+"%");
    }
    

}