package src.UserInterface;
/**
 * @author Han Yeeun - s3912055
 */
import java.io.*;
import java.util.Scanner;

public class Login {
    private static final String DATA_PATH = System.getProperty("user.dir") + "/src/Data/Users/Users.txt";

    public static void login(Scanner scanner) throws IOException {
        boolean loginSuccess = false;
        while (!loginSuccess) {
            System.out.println("\n[ Login ]");
            System.out.print("- Enter your ID: ");
            String id = scanner.nextLine();
            if ("back".equalsIgnoreCase(id)) return; // Go back to main menu
            System.out.print("- Enter your Password: ");
            String password = scanner.nextLine();
            if ("back".equalsIgnoreCase(password)) return; // Go back to main menu

            if (validateCredentials(id, password)) {
                System.out.println("\nLogin successful.\nRedirecting to Main.java...");
                loginSuccess = true; // Break the loop and proceed
                // Instead of calling Main.main(null), instantiate Main and call startApplication
                src.Main mainApp = new src.Main();
                mainApp.startApplication();
            } else {
                System.out.println("\n※ Login failed ※\n※ Incorrect ID or Password ※");
                System.out.println("※ Enter 'back' to return to the main menu or try again ※");
            }
        }
    }

    private static boolean validateCredentials(String id, String password) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(DATA_PATH));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] userDetails = line.split(",");
            if (userDetails[0].equals(id) && userDetails[1].equals(password)) {
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;
    }

    public static void register(Scanner scanner) throws IOException {
        boolean registrationSuccess = false;
        while (!registrationSuccess) {
            System.out.println("\n[ Register ]");
            System.out.print("- Enter a new ID (5 characters minimum): ");
            String newId = scanner.nextLine();
            if ("back".equalsIgnoreCase(newId)) return; // Go back to main menu
            System.out.print("- Enter a new Password (8 characters minimum, letters & numbers): ");
            String newPassword = scanner.nextLine();
            if ("back".equalsIgnoreCase(newPassword)) return; // Go back to main menu

            if (isIdExists(newId)) {
                System.out.println("\n※ This is a duplicate ID ※");
                System.out.println("\n※ Please enter another ID ※");
                System.out.println("※ Enter 'back' to return to the main menu or try again ※");
            } else if (!isValidRegistration(newId, newPassword)) {
                System.out.println("\n※ Please enter a valid 5-character minimum ID & 8-character minimum password (letters & numbers) ※");
                System.out.println("※ Enter 'back' to return to the main menu or try again ※");
            } else {
                BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_PATH, true));
                writer.write(newId + "," + newPassword);
                writer.newLine();
                writer.close();
                System.out.println("\nRegistration successful.\nYou can now login.\n");
                registrationSuccess = true; // Break the loop and proceed
            }
        }
    }

    private static boolean isValidRegistration(String id, String password) {
        return id.length() >= 5 && password.length() >= 8 && password.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}$");
    }

    // Check if the ID already exists
    private static boolean isIdExists(String id) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(DATA_PATH));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] userDetails = line.split(",");
            if (userDetails[0].equals(id)) {
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;
    }
}