//Created by Andrew Bostros CSSC0727
//and Jan Domingo CSSC0749
package edu.sdsu.cs;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import  java.util.*;


public class App {

    public static void main(String[] args) throws IOException {

        String currentDirectory = System.getProperty("user.dir");

        if(args.length == 2 && args[1]!=null) {
            File test1=new File(args[1]);
            if(test1.isDirectory()) {
                currentDirectory=args[1];
            }
            else {
                System.out.println("Incorrect directory");
            }
        }
        if(args.length==0) {
            currentDirectory = System.getProperty("user.dir");
        }

        ArrayList<String> masterList = new ArrayList<String>();
        ArrayList<File> subFolders = new ArrayList<File>();


        readFile(currentDirectory, subFolders, masterList);
        for (String master : masterList){
            tokenAl(master);
        }
        analyzeFile(masterList);

    }


    //Recursively retrieves paths to every .java & .txt files
    //by checking the folder and subfolders
    private static void readFile(String directory, ArrayList<File> subFolder,
                                 ArrayList<String> masterList) {

        ArrayList<File> checkFile = new ArrayList<File>();

        File directoryLocation = new File(directory);
        File[] directoryFiles = directoryLocation.listFiles();


        for (File fileName : directoryFiles) {

            //Add files to a master list of .txt and .java file pathways
            if (!fileName.isDirectory()) {
                if (fileName.toString().endsWith(".txt") ||
                        fileName.toString().endsWith(".java")) {
                    checkFile.add(fileName);
                    masterList.add(fileName.toString());
                }
            }

            //Checks subfolders if not a file,
            // then adds .txt and .java files in subfolders to masterlist
            if (fileName.isDirectory()) {
                subFolder.add(fileName);
                ArrayList<String> subFolderPath = new ArrayList<String>();
                String subFolderFiles = fileName.getAbsolutePath().toString();
                if (subFolderFiles.toString().endsWith(".txt") ||
                        subFolderFiles.toString().endsWith(".java")) {
                    masterList.add(subFolderFiles.toString());
                }

                subFolderPath.add(subFolderFiles);

                readFile(subFolderFiles, subFolder, masterList);

            }
        }
    }


    private static void tokenAl(String masterfile) {
// yourString.split("\\s+");
        ArrayList<String> tokens = new ArrayList<String>();
        ArrayList<Integer> tokencounter = new ArrayList<Integer>();
        Scanner sc = null;
        ArrayList<String> toptenlist = new ArrayList<String>();
        String currtop = "";
        Integer transfer;
        Integer increment;
        ArrayList<String> tokeninsen = new ArrayList<String>();
        ArrayList<Integer> counterinsen = new ArrayList<Integer>();
        Integer nocasetransfer;
        Integer nocaseincrement;
        String nocase = "";
        int alltok = 0;
        File file1 = new File(masterfile);
        try {

            sc = new Scanner(file1);
        } catch (FileNotFoundException o) {
            o.printStackTrace();
        }

        while (sc.hasNext()) {

            File file2 = new File(masterfile);
            ;
            String reader = "";
            reader = sc.next();
            reader = reader.replace(" ", "");
            nocase = reader.toUpperCase();
            ++alltok;

            if (tokens.contains(reader)) {
                transfer = tokens.indexOf(reader);
                increment = tokencounter.get(transfer);
                increment = increment + 1;
                tokencounter.set(transfer, increment);
            }
            if(!tokens.contains(reader)){
                tokens.add(reader);
                tokencounter.add(1);
            }
        }
        PrintWriter pw = null;

        try {
            int partfile = masterfile.lastIndexOf("/");
            String partname = masterfile.substring(0, partfile);
            int statsfile = masterfile.lastIndexOf("/");
            String statsname =
                    masterfile.substring(statsfile, masterfile.length());
            statsname = statsname + ".stats";
            File file = new File(partname, statsname);


            System.out.println(masterfile);

            FileWriter fw = new FileWriter(file, false);
            pw = new PrintWriter(fw);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
                tokens.clear();
                tokencounter.clear();
                alltok = 0;
            }
        }
    }

    //This method will calculate the stats for the output
    private static void analyzeFile(ArrayList<String> masterList)
            throws IOException {
        PrintWriter pw=null;

        //ArrayList<String> outputFileArray = new ArrayList<String>();
        //ArrayList lines will create an array list
        // with the read lines of files inside masterList
        //This method takes the contents from master list
        // (the paths of .txt and .java files) and
        //reads the lines inside the file in a String ArrayList called 'lines'.
        for (int i = 0; i < masterList.size(); i++) {
            ArrayList<Integer> fileLineLengthsArray = new ArrayList<Integer>();
            ArrayList<String> tokenList = new ArrayList<String>();
            ArrayList<Integer> allTokenCountArray = new ArrayList<Integer>();
            ArrayList<Integer> tokenCountArray = new ArrayList<Integer>();
            ArrayList<String> mostFrequentlyOccuringToken
                    = new ArrayList<String>();
            ArrayList<Integer> mostFrequentlyOccuringTokenCICount
                    = new ArrayList<Integer>();
            ArrayList<String> uniqueTokensCI
                    = new ArrayList<String>(); //CI is Case-Insensitve
            ArrayList<String> uniqueTokensCS
                    = new ArrayList<String>(); //CS is Case-Sensitive
            ArrayList<String> tokenListCI = new ArrayList<String>();


            int lineLengthsArraySum = 0;
            int averageLineLength = 0;
            int uniqueTokensCount = 0;
            int uniqueTokensCSCount = 0;


            Path filePath = Paths.get(masterList.get(i));
            List<String> lines =
                    Files.readAllLines(filePath, Charset.defaultCharset());

            int partfile = masterList.get(i).lastIndexOf("/");
            String partname = masterList.get(i).substring(0, partfile);
            int statsfile = masterList.get(i).lastIndexOf("/");
            String statsname =
                    masterList.get(i).substring
                            (statsfile, masterList.get(i).length());
            statsname = statsname + ".stats";
            File file = new File(partname, statsname);
            FileWriter fw = new FileWriter(file, true);
            pw = new PrintWriter(fw);

            //This for loop iterates through the amount of lines in the txt file
            for (int j = 0; j < lines.size(); j++) {
                StringTokenizer tokenizer = new StringTokenizer(lines.get(j));
                int lineTokenCount = tokenizer.countTokens();
                allTokenCountArray.add(lineTokenCount);
                while (tokenizer.hasMoreElements()) {
                    tokenList.add(tokenizer.nextToken());
                }


                //Gets the number of unique space-delineated
                // tokens (case-insensitve) along with
                //count of most frequently occuring token (case-insensitve)
                //CI stands for case-insensitve
                for (String token : tokenList) {
                    token.toLowerCase();
                    if (!(uniqueTokensCI.contains(token.toLowerCase()))) {
                        uniqueTokensCI.add(token.toLowerCase());
                        uniqueTokensCount++;
                    }
                }

                for (String token : tokenList) {
                    if (!(uniqueTokensCS.contains(token))) {
                        uniqueTokensCS.add(token);
                        uniqueTokensCSCount++;
                    }
                }


                int lineLength = lines.get(j).length();
                int lineLengthNoSpaces = lineLength;


                String line = lines.get(j);

                //This for loop iterates through the amount of characters
                // in each line
                //Reduce length by 1 if string contains a
                // space or quotation marks, then  saves it into
                // 'lineLengthNoSpaces' then adds value to fileLineLengthsArray
                for (int k = 0; k < line.length(); k++) {

                    if (line.charAt(k) == ' ') {
                        lineLengthNoSpaces--;

                    }
                }
                //Calculates the average line length
                fileLineLengthsArray.add(lineLengthNoSpaces);
                lineLengthsArraySum += lineLengthNoSpaces;
                averageLineLength =
                        lineLengthsArraySum / fileLineLengthsArray.size();
            }

            //Creates a case-insensitive token list to calculate the
            // frequency of case-insensitive words
            for (String token : tokenList) {
                token.toLowerCase();
                tokenListCI.add(token.toLowerCase());
            }

            //Adds count value of each unique token to an arraylist
            for (int m = 0; m < uniqueTokensCI.size(); m++) {

                mostFrequentlyOccuringTokenCICount.add(Collections.
                        frequency(tokenListCI, uniqueTokensCI.get(m)));
                //Adds value of each unique tokens (case-insensitive)
                // to an arraylist with respective index
            }

            //Sorts the tokens by frequency by using BubbleSort
            int uniqueTokensCISize = uniqueTokensCI.size();
            for (int m = 1; m < uniqueTokensCISize; m++) {
                for (int n = 0; n < uniqueTokensCISize - m; n++) {
                    int currentTokenCount = Collections.frequency((tokenListCI),
                            uniqueTokensCI.get(n));
                    int nextTokenCount = Collections.frequency((tokenListCI),
                            uniqueTokensCI.get(n + 1));
                    if (currentTokenCount < nextTokenCount) {
                        Collections.swap(uniqueTokensCI,n,(n + 1));
                        Collections.swap
                                (mostFrequentlyOccuringTokenCICount,n,(n + 1));
                    }
                }
            }

            //Outputs the most frequently occuring token along with its count
            boolean stopper = false;
            for (int n = 0; n < uniqueTokensCI.size() - 1; n++) {
                int element = mostFrequentlyOccuringTokenCICount.get(n);
                int highestFrequency = mostFrequentlyOccuringTokenCICount.get(n);
                int nextHighestFrequency =
                        mostFrequentlyOccuringTokenCICount.get(n + 1);
                if (highestFrequency == nextHighestFrequency) {
                    mostFrequentlyOccuringToken.add(uniqueTokensCI.get(n));
                    mostFrequentlyOccuringToken.add((uniqueTokensCI.get(n + 1)));
                    stopper = true;
                } else if ((highestFrequency > nextHighestFrequency)
                        && (stopper == false)) {
                    mostFrequentlyOccuringToken.add(uniqueTokensCI.get(0));
                    break;
                }
            }

            //Outputs the longest line length --> NEED TO FIGURE OUT HOW TO OUTPUT THIS INTO .STATS FILE
            //Delete this comment after .stats file created

            Collections.sort(fileLineLengthsArray);
            Collections.reverse(fileLineLengthsArray);
            pw.println("Longest line length in this file: " + fileLineLengthsArray.get(0));
            pw.println("Average line length in this file: " + averageLineLength);
            pw.println("Number of unique space-delineated tokens (case-senstive): " + uniqueTokensCSCount);
            pw.println("Number of unique space-delineated tokens (case-insensitve): " + uniqueTokensCount);
            pw.println("Number of all space-delineated tokens: " + tokenList.size());
            pw.println("Most frequently occurring token(s): " + mostFrequentlyOccuringToken);
            pw.println("Count of most frequently occurring token: " + mostFrequentlyOccuringTokenCICount.get(0));
            pw.println(" ");
            pw.println("10 most frequent tokens: ");
            if (uniqueTokensCI.size() < 10) {
                for (int p = 0; p < uniqueTokensCI.size(); p++) {
                    pw.println("Token: \"" + uniqueTokensCI.get(p) + "\" Count: " + mostFrequentlyOccuringTokenCICount.get(p));
                }
            } else {
                for (int p = 0; p < 10; p++) {
                    pw.println("Token: \"" + uniqueTokensCI.get(p) + "\" Count: " + mostFrequentlyOccuringTokenCICount.get(p));
                }
            }

            pw.println(" ");
            pw.println("10 least frequent tokens: ");
            if (uniqueTokensCI.size() < 10) {
                for (int q = 0; q < uniqueTokensCI.size(); q++) {
                    int lastIndex = uniqueTokensCI.size() - 1 - q;
                    String uniqueTokensCIToken = uniqueTokensCI.get(lastIndex);
                    pw.println("Token: \"" + uniqueTokensCIToken + "\" Count: " + mostFrequentlyOccuringTokenCICount.get(lastIndex));
                }
            }
            else {
                for (int q = 0; q < 10; q++) {
                    int lastIndex = uniqueTokensCI.size() - 1 - q;
                    String uniqueTokensCIToken = uniqueTokensCI.get(lastIndex);
                    pw.println("Token: \"" + uniqueTokensCIToken + "\" Count: " + mostFrequentlyOccuringTokenCICount.get(lastIndex));
                }
            }
            pw.close();
        }}}

