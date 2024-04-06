package src;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.function.Function;

import src.Super.Claim;
import src.Super.ClaimProcessManager;
import src.Super.ClaimProcessManagerImpl;
import src.UserInterface.Login;
import src.Super.CustomerManager;
import src.UserInterface.UserManagement;

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

            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\n※ Invalid choice ※\n");
                continue;
            }
            switch (choice) {
                case 1:
                    Login.login(scanner);
                    break;
                case 2:
                    Login.register(scanner);
                    break;
                default:
                    System.out.println("\n※ Invalid choice ※");
            }
        }
    }

    // Main Menu
    public void startApplication() throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome to the Insurance Management System!");
            System.out.println("\n[ Main Menu ]");
            System.out.println("[1] Manage Customers");
            System.out.println("[2] Manage Claims");
            System.out.println("[3] Profiles");
            System.out.println("[4] Log Out");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\n※ Invalid choice ※\n");
                continue;
            }
            switch (choice) {
                case 1:
                    manageCustomer();
                    break;
                case 2:
                    manageClaim();
                    break;
                case 3:
                    Profile();
                    break;
                case 4:
                    startLoginProcess();
                    return; // 주의: 로그아웃 시 메인 메뉴를 종료해야 합니다.
                default:
                    System.out.println("\n※ Invalid choice ※");
                    break;
            }
        }
    }
    public static void manageCustomer(){
        Scanner scanner = new Scanner(System.in);
        CustomerManager customerManager = new CustomerManager(scanner);

        while(true) {
            System.out.println("\n[ Manage Customer ]");
            System.out.println("[1] List All Customers");
            System.out.println("[2] View Customer Details");
            System.out.println("[3] Add New Customer");
            System.out.println("[4] Update Customer Information");
            System.out.println("[5] Delete Customer");
            System.out.println("[6] Exit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\n※ Invalid choice ※\n");
                continue;
            }
            switch (choice) {
                case 1:
                    customerManager.listAllCustomers();
                    break;
                case 2:
                    customerManager.viewCustomerDetails();
                    break;
                case 3:
                    customerManager.addNewCustomer();
                    break;
                case 4:
                    updateCustomerInfo();
                    break;
                case 5:
                    customerManager.deleteCustomer();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("\n※ Invalid choice ※");
                    break;
            }}}

    // [1]-[3] Update Customer
    public static void updateCustomerInfo() {
        Scanner scanner = new Scanner(System.in);
        CustomerManager customerManager = new CustomerManager(scanner);

        System.out.println("\n-Enter customer ID:");
        String customerId = scanner.nextLine().trim();
        Path customerFilePath = customerManager.findCustomerFileById(customerId);

        if (customerFilePath == null) {
            System.out.println("\n※ Customer file not found ※");
            return;
        }

        String customerName = customerManager.extractCustomerNameFromFileName(customerFilePath.getFileName().toString());

        while (true) {
            System.out.println("\nWhat information you want to update?\n" +
                    "[1] Add New Dependents\n" +
                    "[2] Update All Dependents\n" +
                    "[3] Insurance card Expiration date\n" +
                    "[4] Exit\n" +
                    "Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (choice) {
                case 1:
                    customerManager.updateDependents(customerId);
                    break;
                case 2:
                    customerManager.updateAllDependents(customerId);
                    break;
                case 3:
                    customerManager.updateExpirationDate(customerFilePath);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("\n※ Invalid option ※");
                    break;
            }
        }
    }

    // Main-[2] Manage Claims
    public  void manageClaim() throws IOException {
        Scanner scanner = new Scanner(System.in);
        ClaimProcessManagerImpl claimManager = new ClaimProcessManagerImpl();

        while(true) {
            System.out.println("\n[ Manage Claim ]");
            System.out.println("[1] List All Claims");
            System.out.println("[2] View Claim Details");
            System.out.println("[3] Add New Claim");
            System.out.println("[4] Update Claim");
            System.out.println("[5] Delete Claim");
            System.out.println("[6] Exit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\n※ Invalid choice ※\n");
                continue;
            }
            switch (choice) {
                case 1:
                    listAllClaims();
                    break;
                case 2: // View Claim Detail
                    System.out.print("\n- Enter Claim Id (e.g., f0000000001): ");
                    String claimId = scanner.nextLine();
                    Claim claim = claimManager.getOneClaim(claimId);
                    if (claim != null) {
                    } else {
                        System.out.println("No claim found with ID: " + claimId);
                    }
                    break;
                case 3:
                    claimManager.addNewClaim(scanner);
                    break;
                case 4:
                    updateClaim();
                    break;
                case 5: // Delete Claim
                    System.out.print("\n- Enter Claim Id to delete (e.g., f0000000001): ");
                    String claimIdToDelete = scanner.nextLine();
                    claimManager.deleteClaim(claimIdToDelete);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("\n※ Invalid choice ※");
                    break;
            }}}

    // [2]-[1] List All Cliam
    public static void listAllClaims() throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean exitMenu = false;
        ClaimProcessManager claimManager = new ClaimProcessManagerImpl();

        while (!exitMenu) {
            System.out.println("\nList all claims\n" +
                    "\t[1] Claims List of One Customer\n" +
                    "\t[2] Total Claim List\n" +
                    "\t[3] List All Claim Documents\n" +
                    "\t[4] Exit\n" +
                    "Choose an option:");

            String optionStr = scanner.nextLine();
            int option;

            try {
                option = Integer.parseInt(optionStr);
            } catch (NumberFormatException e) {
                System.out.println("\n※ Invalid choice ※");
                continue;
            }

            switch (option) {
                case 1:
                    System.out.print("\n- Enter Customer ID: ");
                    String customerId = scanner.nextLine();
                    if (!claimManager.printCustomerClaims(customerId)) {
                        System.out.println("\n※ Customer ID does not exist ※");
                    }
                    break;
                case 2:
                    claimManager.printAllClaims();
                    break;
                case 3:
                    claimManager.printAllDocuments();
                    break;
                case 4:
                    exitMenu = true;
                    break;
                default:
                    System.out.println("\n※ Invalid option ※");
                    break;
            }
        }
        scanner.close();
    }

    // Main-[3] Profile
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

            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\n※ Invalid choice ※\n");
                continue;
            }
            switch (choice) {
                case 1:
                    UserManagement.listAllUsers();
                    break;
                case 2:
                    System.out.println("\nYour ID: " + Login.getLoggedInUserId());
                    break;
                case 3:
                    System.out.println("\nYour Password: " + Login.getLoggedInUserPassword());
                    break;
                case 4:
                    UserManagement.deleteMyAccount(Login.getLoggedInUserId(),Login.getLoggedInUserPassword());
                    break;
                case 5:
                    return; // Exit the Profile menu
                default:
                    System.out.println("\n※ Invalid choice ※");
                    break;
        }}}

        // [4] Update Claim
        public void updateClaim() throws IOException {
            boolean continueRunning = true;
            Scanner scanner = new Scanner(System.in);
            ClaimProcessManagerImpl claimManager = new ClaimProcessManagerImpl();

            while (continueRunning) {
                System.out.println("\n[ Update Claim ]");
                System.out.println("[1] Update process");
                System.out.println("[2] Update bank info");
                System.out.println("[3] Update claim amount");
                System.out.println("[4] Update exam date");
                System.out.println("[5] Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1: {
                        System.out.print("\n- Enter claim ID (e.g., f0000000001): ");
                        String claimIdProcess = scanner.nextLine();
                        claimManager.updateClaimProcess(claimIdProcess);
                        break;
                    }
                    case 2: {
                        System.out.print("\n- Enter claim ID for bank info update: ");
                        String claimIdBankInfo = scanner.nextLine();
                        System.out.print("- Enter new bank name: ");
                        String newBankName = scanner.nextLine();
                        System.out.print("- Enter new bank account number: ");
                        String newBankAccount = scanner.nextLine();
                        claimManager.updateBankInfo(claimIdBankInfo, newBankName, newBankAccount);
                        break;
                    }
                    case 3: {
                        System.out.print("\n- Enter claim ID for claim amount update: ");
                        String claimIdAmount = scanner.nextLine();
                        System.out.print("- Enter new claim amount: ");
                        double newClaimAmount = scanner.nextDouble();
                        scanner.nextLine();
                        claimManager.updateClaimAmount(claimIdAmount, newClaimAmount);
                        break;
                    }
                    case 4: {
                        System.out.print("\n- Enter claim ID for exam date update: ");
                        String claimIdDate = scanner.nextLine();
                        System.out.print("- Enter new exam date (YYYY-MM-DD): ");
                        String newExamDate = scanner.nextLine();
                        claimManager.updateExamDate(claimIdDate, newExamDate);
                        break;
                    }
                    case 5:
                        continueRunning = false;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            }
        }

}
