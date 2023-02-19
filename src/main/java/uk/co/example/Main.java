package uk.co.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import java.util.Set;


/** This program reads a large file of long values and writes them to a MapDB disk based treeSet
 */


public class Main {
    public static void main(String[] args) {
        if(args.length < 2){
            System.out.println("missing input and output filename arguments");
            return;
        }
        String inFilename = args[0];
        String outFilename = args[1];


        try {
            BufferedReader reader = new BufferedReader(new FileReader(inFilename));
            String line = reader.readLine();

            DB db = DBMaker
                    .fileDB(outFilename)
                    .make();

            Set<Long> primeSet  = db
                    .treeSet("mySet")
                    .serializer(Serializer.LONG)
                    .createOrOpen();

            while (line != null) {
                String[] numberStrings = line.split(",");
                for (String numStr : numberStrings){
                    primeSet.add(Long.parseLong(numStr));
                }
                line = reader.readLine();
            }
            db.commit();
            db.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}