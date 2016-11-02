package assembler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author Michael Frederick (n00725913)
 */
public class Assembler {
    
    private static final String OUTPUTFILE = "output.txt";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SicAssembler passOne = new SicAssembler(args);
    }
    
}
