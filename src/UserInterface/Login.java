package src.UserInterface;
/**
 * @author Han Yeeun - s3912055
 */

import java.io.*;
import java.util.Scanner;

public class Login {
    public static final String DATA_PATH = System.getProperty("user.dir") + "/src/Data/Users/Users.txt";
    private static String loggedInUserId = null;
    private static String loggedInUserPassword = null;

    // Getter & Setter

    public static String getLoggedInUserId() {
        return loggedInUserId;
    }

    public static String getLoggedInUserPassword() {
        return loggedInUserPassword;
    }

    // METHODS
    public static void login(Scanner scanner) throws IOException {
        boolean loginSuccess = false;
        while (!loginSuccess) {
            System.out.println("\n[ Login ]");
            System.out.print("- Enter your ID: ");
            String id = scanner.nextLine();
            if ("back".equalsIgnoreCase(id)) return;
            System.out.print("- Enter your Password: ");
            String password = scanner.nextLine();
            if ("back".equalsIgnoreCase(password)) return;

            User user = validateCredentials(id, password);
            if (user != null) {
                System.out.println("\nLogin successful.\nRedirecting to Main.java...");
                loginSuccess = true;
                src.Main mainApp = new src.Main();
                mainApp.startApplication();
            } else {
                System.out.println("\n※ Login failed ※\n※ Incorrect ID or Password ※");
                System.out.println("※ Enter 'back' to return to the main menu or try again ※");
            }
        }
    }

    private static User validateCredentials(String id, String password) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(DATA_PATH));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] userDetails = line.split(",");
            if (userDetails[0].equals(id) && userDetails[1].equals(password)) {
                reader.close();
                return new User(id, password, userDetails.length > 2 ? userDetails[2] : "");
            }
        }
        reader.close();
        return null;
    }

    public static void register(Scanner scanner) throws IOException {
        boolean registrationSuccess = false;
        while (!registrationSuccess) {
            System.out.println("\n[ Register ]");
            System.out.print("- Enter a new ID (5 characters minimum): ");
            String newId = scanner.nextLine();
            if ("back".equalsIgnoreCase(newId)) return;
            System.out.print("- Enter a new Password (8 characters minimum, letters & numbers): ");
            String newPassword = scanner.nextLine();
            if ("back".equalsIgnoreCase(newPassword)) return;
            System.out.print("- Enter your Name: ");
            String name = scanner.nextLine(); // Add name input

            if (isIdExists(newId)) {
                System.out.println("\n※ This is a duplicate ID ※");
            } else if (!isValidRegistration(newId, newPassword)) {
                System.out.println("\n※ Please enter a valid ID & password ※");
            } else {
                // Here, we create a User object and use it to save user data
                User newUser = new User(newId, newPassword, name);
                saveUser(newUser); // Assuming you implement this method to save user details
                System.out.println("\nRegistration successful.\nYou can now login.\n");
                registrationSuccess = true;
            }
        }
    }

    private static void saveUser(User user) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_PATH, true));
        // Assuming the user data format is ID,Password,Name
        writer.write(user.getUserID() + "," + user.getUserPassword() + "," + user.getUserName());
        writer.newLine();
        writer.close();
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