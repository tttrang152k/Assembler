import java.util.Hashtable;

public class SymbolTable {
    private static final int DATA_STARTING_ADDRESS = 16;
    private static final int DATA_ENDING_ADDRESS = 16384;
    private static final int PROGRAM_STARTING_ADDRESS = 0;
    private static final int PROGRAM_ENDING_ADDRESS = 32767;

    private Hashtable<String, Integer> symbolMap;
    private int programAddress;
    private int dataAddress;

    public SymbolTable() {
        this.initializeSymbolTable();
        this.programAddress = PROGRAM_STARTING_ADDRESS;
        this.dataAddress = DATA_STARTING_ADDRESS;
    }

    private void initializeSymbolTable() {
        this.symbolMap = new Hashtable<String, Integer>();
        this.symbolMap.put("SP", Integer.valueOf(0));
        this.symbolMap.put("LCL", Integer.valueOf(1));
        this.symbolMap.put("ARG", Integer.valueOf(2));
        this.symbolMap.put("THIS", Integer.valueOf(3));
        this.symbolMap.put("THAT", Integer.valueOf(4));
        this.symbolMap.put("R0", Integer.valueOf(0));
        this.symbolMap.put("R1", Integer.valueOf(1));
        this.symbolMap.put("R2", Integer.valueOf(2));
        this.symbolMap.put("R3", Integer.valueOf(3));
        this.symbolMap.put("R4", Integer.valueOf(4));
        this.symbolMap.put("R5", Integer.valueOf(5));
        this.symbolMap.put("R6", Integer.valueOf(6));
        this.symbolMap.put("R7", Integer.valueOf(7));
        this.symbolMap.put("R8", Integer.valueOf(8));
        this.symbolMap.put("R9", Integer.valueOf(9));
        this.symbolMap.put("R10", Integer.valueOf(10));
        this.symbolMap.put("R11", Integer.valueOf(11));
        this.symbolMap.put("R12", Integer.valueOf(12));
        this.symbolMap.put("R13", Integer.valueOf(13));
        this.symbolMap.put("R14", Integer.valueOf(14));
        this.symbolMap.put("R15", Integer.valueOf(15));
        this.symbolMap.put("SCREEN", Integer.valueOf(16384));
        this.symbolMap.put("KBD", Integer.valueOf(24576));
    }

    // Adds the pair (symbol, address) to the table.
    public void addEntry(String symbol, int address) {
        this.symbolMap.put(symbol, Integer.valueOf(address));
    }

    // flag to check if current symbol is found in current table
    // if not must represent as new variable
    public boolean contains(String symbol) {
        return this.symbolMap.containsKey(symbol);
    }

    // Returns the address associated with the symbol
    public int getAddress(String symbol) {
        return this.symbolMap.get(symbol);
    }

    public void incrementProgramAddress() {
        this.programAddress++;
        if (this.programAddress > PROGRAM_ENDING_ADDRESS) {
            throw new RuntimeException();
        }
    }

    public void incrementDataAddress() {
        this.dataAddress++;
        if (this.dataAddress > DATA_ENDING_ADDRESS) {
            throw new RuntimeException();
        }
    }

    public int getProgramAddress() {
        return this.programAddress;
    }

    public int getDataAddress() {
        return this.dataAddress;
    }
}
