

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class NearestNeighbor {

    private class Record 
    {
        private double[] attributes;         //attributes of record      
        private int className;               //class of record

        //Constructor of Record
        private Record(double[] attributes, int className)
        {
            this.attributes = attributes;    //set attributes 
            this.className = className;      //set class
        }
    }
    

    public NearestNeighbor() {

        numberOfRecords = 0;      
        numberOfAttributes = 0;
        numberOfClasses = 0;
        numberOfNeighbors = 0; 
        records = null;     

    }

    private int numberOfRecords;               //number of training records   
    private int numberOfAttributes;            //number of attributes   
    private int numberOfClasses;               //number of classes
    private int numberOfNeighbors;             //number of nearest neighbors
    private ArrayList<Record> records;         //list of training records





    public void loadTrainingData(String trainingFile) throws IOException {
        Scanner inFile = new Scanner(new File(trainingFile));
    
        // Read number of records, attributes, classes
        numberOfRecords = inFile.nextInt();
        numberOfAttributes = inFile.nextInt();
        numberOfClasses = inFile.nextInt();
    
        // Create empty list of records
        records = new ArrayList<Record>();
    
        // For each record
        for (int i = 0; i < numberOfRecords; i++) {
            // Read the entire attribute string for a record
            String attributeString = inFile.next();
    
            // Placeholder for starting index of each attribute in the string
            int startIdx = 0;
    
            // Create attribute array
            double[] attributeArray = new double[numberOfAttributes];
    
            // Parse string into attributes
            for (int j = 0; j < numberOfAttributes && startIdx < attributeString.length(); j++) {
                int endIdx = startIdx + j + 2; // Calculate end index for the current attribute
                if (endIdx > attributeString.length()) endIdx = attributeString.length(); // Adjust if beyond string length
    
                // Convert substring to double and assign to attribute array
                attributeArray[j] = Double.parseDouble(attributeString.substring(startIdx, endIdx));
    
                // Update start index for next attribute
                startIdx = endIdx;
            }
    
            // Read class name (assuming it's still an integer at the end of each record's line)
            int className = inFile.nextInt();
    
            // Create and add record to list of records
            Record record = new Record(attributeArray, className);
            records.add(record);
        }
    
        inFile.close();
    }




    void setParamaters(int num) {
        numberOfNeighbors = num;


    }
    



/*************************************************************************/

    //Method determines the class of a set of attributes
    private int classify(double[] attributes)
    {
        
        double[] distance = new double[numberOfRecords];
        int[] id = new int[numberOfRecords];


        // need to convert the array in records first
        for(int i =0;i<numberOfRecords;i++) {
            distance[i] = distance(attributes,records.get(i).attributes);
            id[i] = i;
        }

        nearestNeighbor(distance, id);

        int className = majority(id);


        return className;

        



    }

    /*************************************************************************/

    //Method finds the nearest neighbors
    private void nearestNeighbor(double[] distance, int[] id)
    {
        //sort distances and choose nearest neighbors
        for (int i = 0; i < numberOfNeighbors; i++)
            for (int j = i; j < numberOfRecords; j++)
                if (distance[i] > distance[j])
                {
                    double tempDistance = distance[i];
                    distance[i] = distance[j];
                    distance[j] = tempDistance;

                    int tempId = id[i];
                    id[i] = id[j];
                    id[j] = tempId;
                }
    }

    /*************************************************************************/

    //Method finds the majority class of nearest neighbors
    private int majority(int[] id)
    {
        double[] frequency = new double[numberOfClasses];

        //class frequencies are zero initially
        for (int i = 0; i < numberOfClasses; i++)
            frequency[i] = 0;

        //each neighbor contributes 1 to its class
        for (int i = 0; i < numberOfNeighbors; i++)
            frequency[records.get(id[i]).className - 1] += 1;

        //find majority class
        int maxIndex = 0;                         
        for (int i = 0; i < numberOfClasses; i++)   
            if (frequency[i] > frequency[maxIndex])
               maxIndex = i;

        return maxIndex + 1;
    }

    /*************************************************************************/


    private double distance(double[] u, double[] v)
    {
        double distance = 0;         

        for (int i = 0; i < u.length; i++)
            distance = distance + (u[i] - v[i])*(u[i] - v[i]);

        distance = Math.sqrt(distance); 
 
        return distance;               
    }
    

    private int findArrDistance(int[] arr1, int[] arr2) {

        int arrDistance = 0;
        
        
        for(int i =0;i<arr1.length;i++) if(arr1[i] != arr2[i]) arrDistance+= 1;





        return arrDistance;

    }



    public void validate(String file) {


        for(int i =0;i<numberOfRecords;i++) {


            for(int j=i+1;j<numberOfRecords;j++) {

                



            }
        }


    }


}

