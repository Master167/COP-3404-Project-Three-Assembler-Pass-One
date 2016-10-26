package assembler;

/**
 * Builds and contains a DataItem table based on the input file given
 * @author Michael Frederick (n00725913)
 */
public class HashTable { 
    
    private DataItem[] data;
    private int size;
    private DataItem deletedRecord;
    
    /**
     * Generates a HashTable
     * @param size 
     */
    public HashTable(int size) {
        this.size = size;
        this.deletedRecord = new DataItem("deleted");
        this.data = new DataItem[this.getNextPrime(this.size)];
    }
    
    /**
     * Hashes the string by each character value and Horner's Polynomial
     * @param str
     * @param maxSize
     * @return int Index
     */
    private int hashFunction(String str, int maxSize) {
        int value;
        char[] charArray = str.toCharArray();
        int temp;
        value = charArray[0];
        for (int i = 1; i < charArray.length; i++) {
            temp = charArray[i];
            value = (value * 26 + temp) % maxSize;
        }
        return value;
    }
    
    /**
     * How I resolve a data collision
     * @param index
     * @param collisions
     * @return int Next Index to be checked
     */
    private int collisionResolver(int index, int collisions) {
        return ((index * index) + collisions) % this.size;
    }
    
    /**
     * Inserts the DataItem into the array, if item with the same label does not exist
     * @param item 
     */
    public void insertData(DataItem item) {
        int insertionIndex = this.hashFunction(item.getLabel(), this.size);
        int collisions = 0;
        boolean searching = true;
        
        while (searching) {
            if (this.data[insertionIndex] == null) {
                this.data[insertionIndex] = item;
                //System.out.println("Inserting '" + item.getLabel() + "' with value: " + item.getValue() + " at index: " + insertionIndex + " with " + collisions);
                searching = false;
            }
            else if (item.equals(this.data[insertionIndex])) {
                //System.out.println("ERROR: '" + item.getLabel() + "' already exists at index: " + insertionIndex);
                searching = false;
            }
            else {
                // Going to next cell
                collisions++;
                insertionIndex = this.collisionResolver(insertionIndex, collisions);
            }
        }
        
    }

    /**
     * Searches the Data array for the DataItem with the same string
     * @param key 
     */
    public void searchForData(String key) {
        int insertionIndex = this.hashFunction(key, this.size);
        int collisions = 0;
        boolean searching = true;
        
        while (searching) {
            if (this.data[insertionIndex] == null || this.data[insertionIndex].equals(deletedRecord)) {
                //System.out.println("ERROR: '" + key + "' not found");
                searching = false;
            }
            else if (this.data[insertionIndex].getLabel().equals(key)) {
                //System.out.println("'" + key + "' with value: " + this.data[insertionIndex].getValue() + " found at index: " + insertionIndex);
                searching = false;
            }
            else {
                collisions++;
                insertionIndex = this.collisionResolver(insertionIndex, collisions);
            }
        }
    }
    
    public void printTable() {
        // Do something here
    }
    
    // Method from pg. 541 of Data Structures and Algorithms in Java by Robert Lafore
    private int getNextPrime(int min){
        for(int i = min + 1; i < Integer.MAX_VALUE - 1; i++){
            if(isPrime(i)){
                return i;
            }
        }
        return -1;
    }
    
    // Method from pg. 541 of Data Structures and Algorithms in Java by Robert Lafore
    private boolean isPrime(int p){
        for(int i = 2; (i * i) <= p; i++){
            if( (p % i) == 0){
                return false;
            }
        }
        return true;
    }
    
}//end Class HashTable