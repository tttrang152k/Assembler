import java.io.*;
import java.nio.file.*;

public class Prog {

    public static void main(String[] args) {
        // Ensure a valid command-line argument is provided
        if (args.length != 1 || args[0].trim().isEmpty()) {
            System.err.println("Usage: java Prog <inputFile.asm>");
            System.exit(1);
        }

        // Get the source ASM file
        File sourceAsmFile = new File(args[0].trim());

        // Validate file existence
        if (!sourceAsmFile.exists() || !sourceAsmFile.isFile()) {
            System.err.println("Error: The specified file does not exist or is not a valid file.");
            System.exit(2);
        }

        // Generate output file path with ".hack" extension
        File outputFile = getOutputFile(sourceAsmFile);

        // Process file translation
        translateFile(sourceAsmFile, outputFile);
    }

    /**
     * Generates the output file path with a ".hack" extension.
     */
    private static File getOutputFile(File sourceFile) {
        String outputFileName = sourceFile.getName().replaceFirst("\\.\\w+$", "") + ".hack";
        return new File(sourceFile.getParent(), outputFileName);
    }

    /**
     * Translates the given ASM file into a HACK file.
     */
    private static void translateFile(File sourceAsmFile, File outputFile) {
        try {
            // Overwrite existing file
            Files.deleteIfExists(outputFile.toPath());
            Files.createFile(outputFile.toPath());

            // Translate assembly code into binary
            Assembler assembler = new Assembler(sourceAsmFile, outputFile);
            assembler.translate();

            // Get output file name for better tracking purposes
            System.out.println("Translation successful! Output: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
            System.exit(3);
        }
    }
}
