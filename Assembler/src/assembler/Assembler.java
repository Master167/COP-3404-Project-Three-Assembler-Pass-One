package assembler;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
        int location;
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
                    opcodes = new OPHashTable(lineCount);
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
    
}
