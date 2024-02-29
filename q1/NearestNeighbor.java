

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class NearestNeighbor {
    

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
    private ArrayList<Record> records;       //list of training records





    public void loadTrainingData(String trainingFile) throws IOException
    {
         Scanner inFile = new Scanner(new File(trainingFile));

         //read number of records, attributes, classes
         numberOfRecords = inFile.nextInt();
         numberOfAttributes = inFile.nextInt();
         numberOfClasses = inFile.nextInt();

         //create empty list of records
         records = new ArrayList<Record>();        

         //for each record
         for (int i = 0; i < numberOfRecords; i++)    
         {
             //create attribute array
             double[] attributeArray = new double[numberOfAttributes]; 
                                     
             //read attribute values
             for (int j = 0; j < numberOfAttributes; j++)   
                  attributeArray[j] = inFile.nextDouble();  
 
             //read class name
             int className = inFile.nextInt();

             //create record

// Record record = new Record(attributeArray, className);

             //add record to list of records
             
// records.add(record);
         }

         inFile.close();
    }



}

