import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Assembler {
    private File assemblyCode;
    private BufferedWriter machineCode;
    private Encoder encoder;
    private SymbolTable symbolTable;

    // Constructor to initialize the Assembler with source and target files.
    public Assembler(File source, File target) throws IOException {
        this.assemblyCode = source;

        // Create buffered writer.
        FileWriter fw = new FileWriter(target);
        this.machineCode = new BufferedWriter(fw);

        // Initialize assembler components.
        this.encoder = new Encoder();
        this.symbolTable = new SymbolTable();
    }

    // Translate assembly file to machine language.
    public void translate() throws IOException {
        this.recordLabelAddress();
        this.parseAndWriteInstructions();
    }

    // Record label addresses in the symbol table.
    private void recordLabelAddress() throws IOException {
        CommandParser parser = new CommandParser(this.assemblyCode);
        while (parser.hasMoreCommands()) {
            parser.advance();

            Utils commandType = parser.commandType();

            if (commandType.equals(Utils.L_COMMAND)) {
                String symbol = parser.symbol();
                int address = this.symbolTable.getProgramAddress();
                this.symbolTable.addEntry(symbol, address);
            } else {
                this.symbolTable.incrementProgramAddress();
            }
        }
        parser.close();
    }

    // Parse source assembly file and write machine code.
    private void parseAndWriteInstructions() throws IOException {
        CommandParser parser = new CommandParser(this.assemblyCode);
        while (parser.hasMoreCommands()) {
            parser.advance();

            Utils commandType = parser.commandType();
            String instruction = null;

            if (commandType.equals(Utils.A_COMMAND)) { // parsing A-COMMAND
                String symbol = parser.symbol();
                Character firstCharacter = symbol.charAt(0);
                boolean isSymbol = (!Character.isDigit(firstCharacter));

                String address = null;
                if (isSymbol) {
                    boolean symbolExists = this.symbolTable.contains(symbol);

                    // Record address if symbol does not exist
                    // to set new variable
                    if (!symbolExists) {
                        int dataAddress = this.symbolTable.getDataAddress();
                        this.symbolTable.addEntry(symbol, dataAddress);
                        this.symbolTable.incrementDataAddress();
                    }

                    // Get address of symbol.
                    address = Integer.toString(
                            this.symbolTable.getAddress(symbol));
                } else {
                    address = symbol;
                }

                instruction = this.formatAInstruction(address);
            } else if (commandType.equals(Utils.C_COMMAND)) { // parsing C-COMMAND
                String comp = parser.comp();
                String dest = parser.dest();
                String jump = parser.jump();
                instruction = this.formatCInstruction(comp, dest, jump);
            }

            if (!commandType.equals(Utils.L_COMMAND)) { // writing all the binary instruction to hack file
                this.machineCode.write(instruction);
                // directly write "\n" to each line of the instruction
                // without setting newLine() to avoid the Unix style format mismatching
                // in the end result ()
                this.machineCode.write("\n");
            }
        }

        parser.close();
        this.machineCode.flush();
        this.machineCode.close();
    }

    // Machine-format an A-Instruction.
    private String formatAInstruction(String address) {
        return "0" + this.encoder.formatNumberAsBinary(address);
    }

    // Machine-format a C-Instruction.
    private String formatCInstruction(String comp, String dest, String jump) {
        return "111" + this.encoder.comp(comp)
                + this.encoder.dest(dest)
                + this.encoder.jump(jump);
    }
}
