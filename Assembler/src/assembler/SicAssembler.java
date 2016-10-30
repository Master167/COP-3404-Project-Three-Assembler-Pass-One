/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author Michael
 */
public class SicAssembler {
        
    private static final String OUTPUTFILE = "output.txt";
    private HashTable symbols;
    private OPHashTable opcodes;
    // Build something for literals
    
    /**
     * @param args the command line arguments
     */
    public SicAssembler(String[] args) {
        int lineCount;
        String opCodeList = "SICOPS.txt";
        File file;

        if (args[0] != null || !args[0].isEmpty()) {
            file = new File(args[0]);
            if (!file.isDirectory() && file.exists()) {
                try {
                    lineCount = getLineCount(file);
                    symbols = new HashTable(lineCount);
                    lineCount = getLineCount(new File(opCodeList));
                    opcodes = buildOPTable(lineCount, opCodeList);
                    programAssemble(file, symbols, opcodes);
                }
                catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }
            else {
                System.out.println("Invalid Filename");
            }
        }
        else {
            System.out.println("No file to Assemble");
        }
    }
        
    public int getLineCount(File file) throws FileNotFoundException {
        int lineCount = 0;
        Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNext()){
                lineCount++;
                //I'm not inserting the values yet
                fileScanner.nextLine();
            }
        
        return lineCount;
    }
    
    private OPHashTable buildOPTable(int size, String opCodeListFilename) throws FileNotFoundException {
        OPHashTable table = new OPHashTable(size);
        File file = new File(opCodeListFilename);
        Scanner fileScanner = new Scanner(file);

        while (fileScanner.hasNext()) {
            StringTokenizer tokenMaker = new StringTokenizer(fileScanner.nextLine());
            if (tokenMaker.countTokens() == 4) {
                // OPCODE
                table.insertData(new OPCode(tokenMaker.nextToken(),tokenMaker.nextToken(), Integer.parseInt(tokenMaker.nextToken()), tokenMaker.nextToken()));
            }
            else if (tokenMaker.countTokens() == 2) {
                // Register Symbol
                table.insertData(new OPCode(tokenMaker.nextToken(), tokenMaker.nextToken(), -1, null));
            }
        }
        
        return table;
    }   

    private void programAssemble(File file, HashTable symbols, OPHashTable opcodes) throws FileNotFoundException {
        int address;
        DataItem item;
        String programLine;
        Scanner programScanner = new Scanner(file);
        
        // To write to file
        try {
            FileWriter fileWriter = new FileWriter(OUTPUTFILE, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter outputWriter = new PrintWriter(bufferedWriter);

            //Read First line
            programLine = programScanner.nextLine();
            if ("START".equals(programLine.substring(10, 16).trim().toUpperCase())) {
                address = Integer.parseInt(programLine.substring(19, 28).trim());
                symbols.insertData(buildCommand(programLine, opcodes));
            }
            else {
                address = 0;
            }
            while ((programLine = programScanner.nextLine()) != null && !"END".equals(programLine.substring(10, 16).trim().toUpperCase())) {
                if (programLine.charAt(0) == '.') {
                    // Comment Line
                }
                else if (programLine.charAt(0) == '.') {
                    // Literal. Deal with it
                }
                else if (false) {
                    //Watch for LTORG
                }
                else {
                    item = buildCommand(programLine, opcodes);
                    item.setAddress(address);
                    address += item.getCommandLength();
                }
            }
            System.out.printf("Done%n");
        }
        catch (IOException ex) {
            System.out.printf("%s%n", ex.getMessage());
        }
    }
    
    private DataItem buildCommand(String line, OPHashTable opTable) {
        String temp;
        String label = null;
        String mneumonic = null;
        String operand = null;
        String comments = null;
        String error = "";
        int index;
        char operandFlag;
        boolean extended;
        DataItem item;
        StringTokenizer tokenMaker = new StringTokenizer(line);
        
        while (tokenMaker.hasMoreTokens()) {
            temp = tokenMaker.nextToken();
            index = line.indexOf(temp);
            if (index == 0 && index < 7) {
                label = temp;
            }
            else if (7 <= index && index < 17) {
                mneumonic = temp;
            }
            else if (17 <= index && index < 28) {
                if (temp.charAt(0) == '#' || temp.charAt(0) == '@') {
                    // Operand has a flag
                    temp = temp.substring(1);
                    if (temp.contains(",")) {
                        index = temp.indexOf(",");
                        operand = temp.substring(0, index);
                    }
                    else {
                        operand = temp;
                    }
                }
                else {
                    operand = temp;
                }
            }
            else if (28 <= index && index < line.length()) {
                comments = temp;
            }
            else {
                error = temp + " not in extpected index range";
            }
        }
        
        // Validate Strings
        if (isNullOrEmpty(label)) {
            error = " No Label Found ";
        }
        
        if (isNullOrEmpty(mneumonic) || (index = opTable.searchForData(mneumonic)) == -1) {
            error += " Invalid mneomonic "; // Do stuff for assembler directives and reserved words
        }
        else {
            // Get opcode and calculate length, You have an index number
        }
        
        if (isNullOrEmpty(operand)) {
            error += " No Operand ";
        }

        extended = line.charAt(9) == '+';
        operandFlag = line.charAt(18);
        
        item = new DataItem(label, extended, mneumonic, operandFlag, operand, comments);
        if (!isNullOrEmpty(error)) {
            item.setError(error);
        }
        return item;
    }
    
    private boolean isNullOrEmpty(String str) {
        boolean a = false;
        if ("".equals(str) || str == null) {
            a = true;
        }
        return a;
    }
    
    /**
     * Outputs the message to the Output file
     * @param message 
     */
    private void writeToFile(String message) {
        try (   
                FileWriter fileWriter = new FileWriter(OUTPUTFILE, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter outputWriter = new PrintWriter(bufferedWriter);
            ) {
            outputWriter.printf(message);
        }
        catch (IOException ex) {
            System.out.printf("%s%n", ex.getMessage());
        }
    }
}
