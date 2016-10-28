package assembler;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author Michael Frederick (n00725913)
 */
public class Assembler {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int lineCount;
        String opCodeList = "SICOPS.txt";
        File file;
        HashTable symbols;
        OPHashTable opcodes;

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
                    System.out.println(e.getMessage());
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
        
    public static int getLineCount(File file) throws FileNotFoundException {
        int lineCount = 0;
        Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNext()){
                lineCount++;
                //I'm not inserting the values yet
                fileScanner.nextLine();
            }
        
        return lineCount;
    }
    
    private static OPHashTable buildOPTable(int size, String opCodeListFilename) throws FileNotFoundException {
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

    private static void programAssemble(File file, HashTable symbols, OPHashTable opcodes) throws FileNotFoundException {
        int address;
        DataItem item;
        String programLine;
        Scanner programScanner = new Scanner(file);
        
        //Read First line
        programLine = programScanner.nextLine();
        if ("START".equals(programLine.substring(10, 16).trim().toUpperCase())) {
            address = Integer.parseInt(programLine.substring(19, 28).trim());
            symbols.insertData(buildCommand(programLine, opcodes));
        }
        else {
            address = 0;
        }
        while ((programLine = programScanner.nextLine()) != null || "END".equals(programLine.substring(10, 16).trim().toUpperCase())) {
            if (programLine.charAt(0) != '.') {
                item = buildCommand(programLine, opcodes);
                item.setAddress(address);
                address += item.getCommandLength();
            }
        }
        System.out.printf("Done%n");
    }
    
    private static DataItem buildCommand(String line, OPHashTable opTable) {
        String label = line.substring(0, 7).trim();
        String mneumonic = line.substring(10, 16).trim();
        String operand = line.substring(19, 28).trim();
        String comments = line.substring(31).trim();
        String error = "";
        int index;
        char operandFlag;
        boolean extended;
        DataItem item;
        
        // Validate Strings
        if (isNullOrEmpty(label)) {
            error = " No Label Found ";
        }
        
        if (isNullOrEmpty(mneumonic) || (index = opTable.searchForData(mneumonic)) == -1) {
            error += " Invalid mneomonic ";
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
    
    private static boolean isNullOrEmpty(String str) {
        boolean a = false;
        if ("".equals(str) || str == null) {
            a = true;
        }
        return a;
    }
    
}
