package src;

import src.UserInterface.Login;

import java.io.IOException;
import java.util.Scanner;

import static src.UserInterface.Login.login;
import static src.UserInterface.Login.register;

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
                    System.out.println("\n※ Invalid choice ※\n※ Please select 1 or 2 ※\n");
            }
        }
    }

    public void startApplication() throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to the Insurance Management System!\n");
            System.out.println("\n[ Main Menu ]");
            System.out.println("[1] Manage Customers");
            System.out.println("[2] Manage Claims");
            System.out.println("[3] Reports");
            System.out.println("[4] Profiles");
            System.out.println("[5] Exit and return to Login Page");
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
                    // profile logic > 내 비번 보기, 내 아이디 보기, 탈퇴.
                    break;
                case 5:
                    startLoginProcess();
                    break;
                default:
                    System.out.println("\n※ Invalid choice ※");
                    break;
            }
        }
        scanner.close();
    }

}
