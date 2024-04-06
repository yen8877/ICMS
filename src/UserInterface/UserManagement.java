package src.UserInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManagement {
    private static final String DATA_PATH = Login.DATA_PATH;

    public static void listAllUsers() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(DATA_PATH));
        String line;
        System.out.println("\n[ List of All Users ]");
        while ((line = reader.readLine()) != null) {
            String[] userDetails = line.split(",");
            System.out.println("ID: " + userDetails[0] + " - Name: " + (userDetails.length > 2 ? userDetails[2] : "N/A"));
        }
        reader.close();
    }

    public static void deleteMyAccount(String userId, String userPassword) throws IOException {
        File inputFile = new File(DATA_PATH);
        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String lineToRemove = userId + "," + userPassword;
        String currentLine;

        while ((currentLine = reader.readLine()) != null) {
            // Trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if (trimmedLine.equals(lineToRemove)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();

        if (!inputFile.delete()) {
            System.out.println("\n※ Could not delete the original file ※");
            return;
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("\n※ Could not rename the temporary file ※");
        } else {
            System.out.println("\nAccount successfully deleted !");
        }
    }
}
