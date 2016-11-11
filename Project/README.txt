Project Three COP 3404
Author: Michael Frederick (n00725913)

Purpose: Create and implement various features of the SIC Pass One assembler. Pass 1 should construct the symbol table, the addresses associated with each instruction, addresses of each label. Any errors in the file output file.

Input file format:
Col 1-8 label optional
Col 9 blank
Col 10 + optional
Col 11-17 mneumonic
Col 18 blank
Col 19 #, @, = ... optional
Col 20-29 (operand) label, register, ',',X optional ...
Col 30-31 blank
Col 32-80 comments optional

To Execute:
1. Compile the code using "javac *.java" or using the GNU command "make"
2. Use the command "java Assembler [input filename]"
    -After execution "output.txt" will be generated with the results of the
     execution.

Source Files:
SicAssembler.java
HashTable.java
OPCode.java
DataItem.java
Assembler.java
OPHashTable.java
makefile
(An Input file)

Makefile will Generate:
SicAssembler.class
HashTable.class
OPCode.class
DataItem.class
Assembler.class
OPHashTable.class

Output Files:
output.txt