package src;

import java.io.*;
import java.io.IOException;
import java.util.Scanner;

import src.UserInterface.Login;

import static src.UserInterface.Login.*;

public class Main {
    public static void main(String[] args) throws IOException {
        startLoginProcess();
    }

    public static void startLoginProcess() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n[ Login Menu ]");
            System.out.println("[1] Login");
            System.out.println("[2] Register");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    register(scanner);
                    break;
                default:
                    System.out.println("\n※ Invalid choice ※\n");
            }}}

    public void startApplication() throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to the Insurance Management System!");
            System.out.println("\n[ Main Menu ]");
            System.out.println("[1] Manage Customers");
            System.out.println("[2] Manage Claims");
            System.out.println("[3] Reports");
            System.out.println("[4] Profiles");
            System.out.println("[5] Log Out");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    // manage customer Logic
                    break;
                case 2:
                    // manage claims Logic
                    break;
                case 3:
                    // manage report Logic
                    break;
                case 4:
                    Profile();
                    break;
                case 5:
                    startLoginProcess();
                    break;
                default:
                    System.out.println("\n※ Invalid choice ※");
                    break;
            }}
        scanner.close();
    }

    public void Profile() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("\n[ Profile ]");
            System.out.println("[1] List All Users");
            System.out.println("[2] View My ID");
            System.out.println("[3] View My PW");
            System.out.println("[4] Delete My Account");
            System.out.println("[5] Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    listAllUsers();
                    break;
                case 2:
                    System.out.println("\nYour ID: " + Login.getLoggedInUserId());
                    break;
                case 3:
                    System.out.println("\nYour Password: " + Login.getLoggedInUserPassword());
                    break;
                case 4:
                    deleteMyAccount();
                    break;
                case 5:
                    return; // Exit the Profile menu
                default:
                    System.out.println("\n※ Invalid choice ※");
                    break;
        }}}

    // Methods
    private void listAllUsers() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(Login.DATA_PATH));
        String line;
        System.out.println("\n[ List of All Users ]");
        while ((line = reader.readLine()) != null) {
            String[] userDetails = line.split(",");
            System.out.println("ID: " + userDetails[0]);
        }
        reader.close();
    }

    private void deleteMyAccount() throws IOException {
        File inputFile = new File(Login.DATA_PATH);
        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String lineToRemove = getLoggedInUserId() + "," + getLoggedInUserPassword();
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
        }

        System.out.println("\nAccount successfully deleted !");
        startLoginProcess(); // Assuming this method resets the application state or exits.
    }


} // class cover
