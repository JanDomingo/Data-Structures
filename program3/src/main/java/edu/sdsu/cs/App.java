//Created by:
// Andrew Bostros cssc0727
// Jan Domingo cssc0749
package edu.sdsu.cs;

import edu.sdsu.cs.datastructures.DirectedGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class App {

    public static void main(String[] args) {

        DirectedGraph graph = new DirectedGraph<>();

        //-----Initiates the CSV file-----
        /** Change this parameter for different CSV files **/
        String fileName = ("layout.csv");
        File csvFile = new File(fileName);

        //-----Verifies that the required files exists-----
        try {
            //-----Reads each line in the CSV file and parses the two vertices-----
            Scanner inputStream = new Scanner(csvFile);
                String line = inputStream.nextLine();
                String start, destination;
                String[] names;

            //-----Reads each line and adds it as a vertex. If two vertices
            //detected, break and start connecting the vertices-----
            while (inputStream.hasNextLine()) {
                names = line.split(",", 2);
                start = names[0];
                destination = names[1];
                if (destination.compareTo("") != 0)
                    break;
                graph.add(start);
                line = inputStream.nextLine();
            }

            //-----Continues from previous block, if there are two values in the
            //CSV file, start connecting the vertices-----
            while (inputStream.hasNextLine()){
                names = line.split(",", 2);
                start = names[0];
                destination = names[1];
                graph.connect(start, destination);
                line = inputStream.nextLine();
            }
            inputStream.close();


            System.out.println("\n" + graph);  //Displays information about the graph

            //Displays the shortest path between two vertices:
            /**Change "Quang Tri" and "Quang Nam" for the desired
             \start and destination vertices respectively. **/
            System.out.println("\nThe shortest path from the start to destination vertex is: \n\t" +
                    graph.shortestPath("Quang Tri", "Quang Nam"));
            System.out.println("The distance between the routes were: " +
                    graph.shortestPath("Quang Tri", "Quang Nam").size());

            System.out.println("\nThank you professor Healey and all the TA's for " +
                    "your hard work and help this semester! -Jan & Andrew");
        }

        catch (FileNotFoundException e) {
            System.out.println("Error: Unable to open " + fileName + ". Verify the " +
                    "file exists, is accessible, and meets the syntax requirements");
        }


    }
}
