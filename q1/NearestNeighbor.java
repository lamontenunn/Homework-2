package q1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class NearestNeighbor {
    

    public NearestNeighbor() {

        numberRecords = 0;      
        numberAttributes = 0;
        numberClasses = 0;
        numberNeighbors = 0; 
        records = null;     

    }

    private int numberRecords;               //number of training records   
    private int numberAttributes;            //number of attributes   
    private int numberClasses;               //number of classes
    private int numberNeighbors;             //number of nearest neighbors
    private ArrayList<Record> records;       //list of training records





    public void loadTrainingData(String trainingFile) throws IOException
    {
         Scanner inFile = new Scanner(new File(trainingFile));

         //read number of records, attributes, classes
         numberRecords = inFile.nextInt();
         numberAttributes = inFile.nextInt();
         numberClasses = inFile.nextInt();

         //create empty list of records
         records = new ArrayList<Record>();        

         //for each record
         for (int i = 0; i < numberRecords; i++)    
         {
             //create attribute array
             double[] attributeArray = new double[numberAttributes]; 
                                     
             //read attribute values
             for (int j = 0; j < numberAttributes; j++)   
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

