package src;

import java.io.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;



import src.Super.Customer;
import src.Super.InsuranceCard;
import src.Super.UniqueIdGenerator;
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

    // Main Menu
    public void startApplication() throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to the Insurance Management System!");
            System.out.println("\n[ Main Menu ]");
            System.out.println("[1] Manage Customers");
            System.out.println("[2] Manage Claims");
            System.out.println("[3] Profiles");
            System.out.println("[4] Log Out");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    manageCustomer();
                    break;
                case 2:
                    // manage claims Logic
                    break;
                case 3:
                    Profile();
                    break;
                case 4:
                    startLoginProcess();
                    break;
                default:
                    System.out.println("\n※ Invalid choice ※");
                    break;
            }}
        scanner.close();
    }

    public static void manageCustomer() /*throws IOException*/ {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("\n[ Manage Customer ]");
            System.out.println("[1] List All Customers");
            System.out.println("[2] View Customer Details");
            System.out.println("[3] Update Customer Information");
            System.out.println("[4] Add New Customer");
            System.out.println("[5] Delete Customer");
            System.out.println("[6] Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    listAllCustomers();
                    break;
                case 2:
                    viewCustomerDetails();
                    break;
                case 3:
                    updateCustomerInfo();
                    break;
                case 4:
                    addNewCustomer();
                    break;
                case 5:
                    deleteCustomer();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("\n※ Invalid choice ※");
                    break;
            }}}

    // Main-[2] Manage Claims

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

    /**
     * Methods
     */
    // Main-[1] Manage Customer
        // [1] List All Customers
    public static void listAllCustomers() {
        Path dirPath = Paths.get("src/Data/Customers");
        try {
            List<String> customerFiles = Files.list(dirPath)
                    .map(path -> path.getFileName().toString().replace(".txt", ""))
                    .collect(Collectors.toList());

            if (customerFiles.isEmpty()) {
                System.out.println("\n※ Customer information does not exist ※");
            } else {
                System.out.println("\n [ Customer List ]");
                customerFiles.forEach(System.out::println);
                System.out.println("\nDisplays all customer list.");
            }
        } catch (IOException e) {
            System.err.println("\n※ Failed to retrieve customer list ※");
            e.printStackTrace();
        }
        manageCustomer();
    }
    // [2] View Customer Details
    public static void viewCustomerDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n- Please enter the customer ID to view details (e.g., c1000001):");
        String customerId = scanner.nextLine();
        Path dirPath = Paths.get("src/Data/Customers");
        try {
            String details = Files.list(dirPath)
                    .filter(path -> path.getFileName().toString().startsWith(customerId))
                    .findFirst()
                    .map(Main::readCustomerDetailsWithoutClaims)
                    .orElse("\n※ Customer information not found ※");

            System.out.println(details);
        } catch (IOException e) {
            System.err.println("\n※ Failed to retrieve customer details ※");
            e.printStackTrace();
        }
        manageCustomer();
    }

    private static String readCustomerDetailsWithoutClaims(Path path) {
        try {
            return Files.readAllLines(path).stream()
                    .takeWhile(line -> !line.startsWith("Claim list:")) // Read only before "Claim list:" begins
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return "\n※ There was an error reading customer information ※";
        }
    }
        // [3] Update Customer Details
        public static void updateCustomerInfo() {
            // 메소드 내부로 이동된 CUSTOMERS_DIRECTORY
            final String CUSTOMERS_DIRECTORY = "src/Data/Customers/";

            Scanner scanner = new Scanner(System.in);

            System.out.println("\n- Enter customer ID:");
            String customerId = scanner.nextLine();

            File dir = new File(CUSTOMERS_DIRECTORY);
            File[] matchingFiles = dir.listFiles((dir1, name) -> name.startsWith(customerId + "_") && name.endsWith(".txt"));

            if (matchingFiles == null || matchingFiles.length == 0) {
                System.out.println("\n※ Customer file not found ※");
                return;
            }

            File customerFile = matchingFiles[0]; // 첫 번째 일치하는 파일을 사용합니다.

            try {
                System.out.println("\nWhat information you want to update?");
                System.out.println("[1] Dependents");
                System.out.println("[2] Insurance card Expiration date");
                System.out.println("[3] Exit");
                System.out.println("Choose an option:");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        updateDependents(customerFile.toPath(), scanner);
                        break;
                    case 2:
                        updateExpirationDate(customerFile.toPath(), scanner);
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("\n※ Invalid option ※");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    private static void updateExpirationDate(Path filePath, Scanner scanner) throws IOException {
        System.out.println("- Enter new expiration date (YYYY-MM-DD):");
        String newExpirationDate = scanner.nextLine();

        List<String> lines = Files.readAllLines(filePath);
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith("Expiration date:")) {
                lines.set(i, "Expiration date: " + newExpirationDate);
                break;
            }
        }

        Files.write(filePath, lines, StandardCharsets.UTF_8);
        System.out.println("\n Expiration date updated !");
    }
    private static void updateDependents(Path filePath, Scanner scanner) throws IOException {
        System.out.println("\nEnter new dependents (comma separated). Press enter to keep existing:");
        String newDependents = scanner.nextLine();

        List<String> lines = Files.readAllLines(filePath);
        boolean foundDependents = false;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith("Dependents:")) {
                if (!newDependents.isEmpty()) {
                    lines.set(i, "Dependents: " + newDependents);
                }
                foundDependents = true;
                break;
            }
        }

        if (!foundDependents && !newDependents.isEmpty()) {
            lines.add("Dependents: " + newDependents);
        }

        Files.write(filePath, lines, StandardCharsets.UTF_8);
        System.out.println("\nDependents updated !");
    }

        // [4] Add New Customer
    public static void addNewCustomer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n- Enter Full Name(e.g., MinhVuThanh):");
        String fullName = scanner.nextLine();

        UniqueIdGenerator generator = UniqueIdGenerator.getInstance();
        String cid = generator.generateCustomerId();
        String cardNumber = generator.generateCardNumber();

        System.out.println("\n※ One card can only have one holder ※\n");

        System.out.println("- Enter Expiration Date (YYYY-MM-DD):");
        String expirationDateString = scanner.nextLine();
        LocalDate expirationDate = LocalDate.parse(expirationDateString, DateTimeFormatter.ISO_LOCAL_DATE);

        System.out.println("- Enter Policy Owner:");
        String policyOwner = scanner.nextLine();

        System.out.println("- Enter Dependents (e.g., Mom, Dad, Sister, Brother):");
        String dependentsInput = scanner.nextLine();
        List<String> dependents = new ArrayList<>();
        if (!dependentsInput.isEmpty()) {
            dependents.addAll(Arrays.asList(dependentsInput.split(",\\s*")));
        }

        boolean isPolicyHolder = true;
        Customer tempCustomer = new Customer(cid, fullName, isPolicyHolder);
        InsuranceCard insuranceCard = new InsuranceCard(cardNumber, tempCustomer, policyOwner, expirationDate);
        tempCustomer.setInsuranceCard(insuranceCard);
        tempCustomer.setDependents(dependents);
        saveCustomerDataToFile(tempCustomer, dependents, policyOwner);
    }

    private static void saveCustomerDataToFile(Customer customer, List<String> dependents, String policyOwner) {
        String fileName = customer.getId() + "_" + customer.getFullName() + ".txt";
        String dependentsFormatted = String.join(", ", dependents);
        String policyHolderStatus = customer.isPolicyHolder() ? "Yes" : "No";

        String customerData = String.format(
                "Id: %s\nFull name: %s\nDependents: %s\n\nInsurance card: %s\nExpiration date: %s\nCard holder: %s\nPolicy holder: %s\nPolicy owner: %s\n\nClaim list: %s\n",
                customer.getId(), customer.getFullName(), dependentsFormatted,
                customer.getInsuranceCard().getCardNum(), customer.getInsuranceCard().getExpirationDate().toString(),
                customer.getFullName(), policyHolderStatus, policyOwner, String.join(", ", customer.getClaims())
        );

        Path path = Paths.get("src/Data/Customers/" + fileName);
        try {
            Files.writeString(path, customerData);
            System.out.println("\nCustomer data saved successfully !");
        } catch (IOException e) {
            System.err.println("\n※ Failed to save customer data ※");
            e.printStackTrace();
        }
    }
        // [5] Delete Customer
        public static void deleteCustomer() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n- Enter the customer ID to delete:");
            String customerId = scanner.nextLine();


            Path directoryPath = Paths.get("src/Data/Customers");
            try {
                Files.list(directoryPath)
                        .filter(path -> path.getFileName().toString().startsWith(customerId + "_"))
                        .findFirst()
                        .ifPresent(path -> confirmAndDelete(path, scanner));
            } catch (IOException e) {
                System.err.println("\n※ Failed to find the customer data file ※");
                e.printStackTrace();
            }
        }
    private static void confirmAndDelete(Path path, Scanner scanner) {
        System.out.println("\nAre you sure delete " + path.getFileName() + " file ?");
        System.out.println("※ Enter 'yes' to delete the customer information or Enter 'no' to return the Customer manage menu ※\n");
        String confirmation = scanner.nextLine();

        if ("yes".equalsIgnoreCase(confirmation)) {
            try {
                Files.delete(path);
                System.out.println("\n" + path.getFileName() + " has been successfully deleted.");
            } catch (IOException e) {
                System.err.println("\n※ Failed to delete data file ※\n※ Please Try Again ※");
                e.printStackTrace();
            }
        } else if ("no".equalsIgnoreCase(confirmation)) {
            manageCustomer(); // user typing 'no' go back to manageCustomer()
        } else {
            System.out.println("\n※ Invalid input ※\n※ Returns to Customer Management Menu ※");
            manageCustomer(); //Invalid input handling
        }
    }

    // Main-[3] Profile
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
        startLoginProcess();
    }

}
