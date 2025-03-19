import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ConvertToUnix {

    public static void main(String[] args) {

        // Ensure the input file path is provided
        if (args.length != 1) {
            System.out.println("Please provide the file path as the argument.");
            System.exit(1);
        }

        // Get the input file
        File inputFile = new File(args[0].trim());

        // Check if the file exists
        if (!inputFile.exists()) {
            System.out.println("The specified file does not exist.");
            System.exit(2);
        }

        try (BufferedReader reader = Files.newBufferedReader(inputFile.toPath(), StandardCharsets.UTF_8);
                BufferedWriter writer = Files.newBufferedWriter(inputFile.toPath(), StandardCharsets.UTF_8)) {

            // Read the content and overwrite the file with Unix-style line endings
            String line;
            while ((line = reader.readLine()) != null) {
                // Write the line with Unix-style line endings (only LF)
                writer.write(line);
                writer.newLine(); // Ensures only LF is written as the line ending
            }

            System.out.println("File successfully converted to Unix style!");

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            System.exit(3);
        }
    }
}
