import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CommandParser {
    private BufferedReader bufferedReader;
    private String currentLine;
    private String nextLine;

    // Constructor to initialize the CommandParser with the source file.
    public CommandParser(File sourceFile) throws IOException {
        if (sourceFile == null) {
            throw new NullPointerException("sourceFile");
        }
        if (!sourceFile.exists()) {
            throw new FileNotFoundException(sourceFile.getAbsolutePath());
        }

        this.bufferedReader = new BufferedReader(new FileReader(sourceFile));
        this.currentLine = null;
        this.nextLine = this.readNextLine();
    }

    // Read the next line from the source file, ignoring comments and empty lines.
    private String readNextLine() throws IOException {
        String line;

        do {
            line = this.bufferedReader.readLine();

            if (line == null) {
                return null;
            }
        } while (line.trim().isEmpty() || this.isComment(line));

        int commentIndex = line.indexOf("//");
        if (commentIndex != -1) {
            line = line.substring(0, commentIndex - 1);
        }

        return line;
    }

    // Check if the line is a comment.
    private boolean isComment(String line) {
        return line.trim().startsWith("//");
    }

    // Close the bufferedReader.
    public void close() {
        try {
            this.bufferedReader.close();
        } catch (IOException e) {
            // Handle exception
        }
    }

    // Check if there are more commands to parse.
    public boolean hasMoreCommands() {
        return (this.nextLine != null);
    }

    // Advance to the next command.
    public void advance() throws IOException {
        this.currentLine = this.nextLine;
        this.nextLine = this.readNextLine();
    }

    // Return the type of the current command: A_COMMAND, C_COMMAND, or L_COMMAND.
    public Utils commandType() {
        String trimmedLine = this.currentLine.trim();

        if (trimmedLine.startsWith("(") && trimmedLine.endsWith(")")) {
            return Utils.L_COMMAND;
        } else if (trimmedLine.startsWith("@")) {
            return Utils.A_COMMAND;
        } else {
            return Utils.C_COMMAND;
        }
    }

    // Return the symbol or decimal Xxx of the current command @Xxx or (Xxx).
    public String symbol() {
        String trimmedLine = this.currentLine.trim();

        if (this.commandType().equals(Utils.L_COMMAND)) {
            return trimmedLine.substring(1, this.currentLine.length() - 1);
        } else if (this.commandType().equals(Utils.A_COMMAND)) {
            return trimmedLine.substring(1);
        } else {
            return null;
        }
    }

    // Return the dest mnemonic of the current C_COMMAND.
    public String dest() {
        String trimmedLine = this.currentLine.trim();
        int destIndex = trimmedLine.indexOf("=");

        if (destIndex == -1) {
            return null;
        } else {
            return trimmedLine.substring(0, destIndex);
        }
    }

    // Return the comp mnemonic of the current C_COMMAND.
    public String comp() {
        String trimmedLine = this.currentLine.trim();
        int destIndex = trimmedLine.indexOf("=");
        if (destIndex != -1) {
            trimmedLine = trimmedLine.substring(destIndex + 1);
        }
        int compIndex = trimmedLine.indexOf(";");

        if (compIndex == -1) {
            return trimmedLine;
        } else {
            return trimmedLine.substring(0, compIndex);
        }
    }

    // Return the jump mnemonic of the current C_COMMAND.
    public String jump() {
        String trimmedLine = this.currentLine.trim();
        int compIndex = trimmedLine.indexOf(";");

        if (compIndex == -1) {
            return null;
        } else {
            return trimmedLine.substring(compIndex + 1);
        }
    }

    @Override
    public void finalize() {
        this.close();
    }
}