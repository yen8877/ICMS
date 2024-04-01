package src;

import java.io.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
                    login(scanner);
                    break;
                case 2:
                    register(scanner);
                    break;
                default:
                    System.out.println("\n※ Invalid choice ※");
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

            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\n※ Invalid choice ※\n");
                continue; // 다음 반복으로 넘어감
            }
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

    public static void manageCustomer(){
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

            String input = scanner.nextLine(); // 입력을 문자열로 받음
            int choice;
            try {
                choice = Integer.parseInt(input); // 문자열을 정수로 변환 시도
            } catch (NumberFormatException e) {
                System.out.println("\n※ Invalid choice ※\n"); // 변환 실패 시(정수가 아닌 입력) 메시지 출력
                continue; // 다음 반복으로 넘어감
            }
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
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter customer ID:");
            String customerId = scanner.nextLine();
            String customersDirectory = "src/Data/Customers/";
            String customerFileName = findCustomerFileById(customerId, customersDirectory);

            if (customerFileName.isEmpty()) {
                System.out.println("Customer file not found.");
                return;
            }

            String customerName = extractCustomerNameFromFileName(customerFileName);

            while (true) {
                System.out.println("\nWhat information you want to update?\n" +
                        "[1] Add New Dependents\n" +
                        "[2] Update All Dependents\n" +
                        "[3] Insurance card Expiration date\n" +
                        "[4] Exit\n" +
                        "Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline left-over

                boolean success;
                switch (choice) {
                    case 1:
                    case 2:
                        success = updateDependents(customerId, customerName, customerFileName, scanner, customersDirectory, choice == 2);
                        break;
                    case 3:
                        success = updateExpirationDate(customerFileName, scanner, customersDirectory);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("\n※ Invalid option ※");
                        success = false;
                        break;
                }

                if (!success) {
                    System.out.println("\nFailed to update customer info.\n");
                }
            }
        }

    private static String findCustomerFileById(String customerId, String customersDirectory) {
        try (Stream<Path> paths = Files.walk(Paths.get(customersDirectory))) {
            Optional<String> result = paths
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(f -> f.startsWith(customerId + "_"))
                    .findFirst();
            return result.orElse("");
        } catch (IOException e) {
            System.out.println("\n※ Fail to find customer files ※\n");
            return "";
        }
    }

    private static String extractCustomerNameFromFileName(String fileName) {
        return fileName.substring(fileName.indexOf('_') + 1, fileName.lastIndexOf('.'));
    }

    private static boolean updateDependents(String customerId, String customerName, String customerFileName, Scanner scanner, String customersDirectory, boolean replace) {
        System.out.println("\n- Enter dependents' IDs (e.g., c1000001, c1000002):");
        String[] dependentIds = scanner.nextLine().split(",\\s*");

        // Gather all dependents' names and IDs for mutual update.
        Map<String, String> allDependents = new HashMap<>();
        for (String id : dependentIds) {
            if (!id.trim().isEmpty()) {
                String dependentFileName = findCustomerFileById(id.trim(), customersDirectory);
                if (!dependentFileName.isEmpty()) {
                    String dependentName = extractCustomerNameFromFileName(dependentFileName);
                    allDependents.put(id.trim(), dependentName);
                } else {
                    System.out.println("\n※ No customer record found for ID: " + id + " ※");
                    return false;
                }
            }
        }

        // Update the current customer's file with new/updated dependents.
        if (!updateCurrentCustomerDependents(customerFileName, customersDirectory, allDependents, replace)) {
            System.out.println("\n※ Fail to update dependents for customer ID: " + customerId + " ※");
            return false;
        }

        // Update each dependent's file to include all other dependents and the current customer.
        for (String dependentId : allDependents.keySet()) {
            if (!updateDependentFiles(dependentId, allDependents, customerId, customerName, customersDirectory)) {
                System.out.println("\n※ Fail to update dependent files for ID: " + dependentId + " ※");
                return false;
            }
        }

        System.out.println("\nDependents updated successfully !");
        return true;
    }

    private static boolean updateCurrentCustomerDependents(String customerFileName, String customersDirectory, Map<String, String> allDependents, boolean replace) {
        try {
            String filePath = customersDirectory + customerFileName;
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            boolean foundDependents = false;

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith("Dependents:")) {
                    foundDependents = true;
                    String newDependentsList = allDependents.entrySet().stream()
                            .map(entry -> entry.getValue() + "(" + entry.getKey() + ")")
                            .collect(Collectors.joining(", "));
                    lines.set(i, "Dependents: " + (replace ? newDependentsList : lines.get(i).substring("Dependents: ".length()) + ", " + newDependentsList));
                    break;
                }
            }

            if (!foundDependents) {
                lines.add("Dependents: " + allDependents.entrySet().stream()
                        .map(entry -> entry.getValue() + "(" + entry.getKey() + ")")
                        .collect(Collectors.joining(", ")));
            }

            Files.write(Paths.get(filePath), lines);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean updateDependentFiles(String dependentId, Map<String, String> allDependents, String customerId, String customerName, String customersDirectory) {
        try {
            String dependentFileName = findCustomerFileById(dependentId, customersDirectory);
            if (dependentFileName.isEmpty()) return false;

            String filePath = customersDirectory + dependentFileName;
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            boolean foundDependents = false;

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith("Dependents:")) {
                    foundDependents = true;
                    String newDependentsList = allDependents.entrySet().stream()
                            .filter(entry -> !entry.getKey().equals(dependentId)) // Exclude the current dependent
                            .map(entry -> entry.getValue() + "(" + entry.getKey() + ")")
                            .collect(Collectors.joining(", "));
                    if (!newDependentsList.isEmpty()) newDependentsList += ", ";
                    lines.set(i, "Dependents: " + newDependentsList + customerName + "(" + customerId + ")");
                    break;
                }
            }

            if (!foundDependents) {
                lines.add("Dependents: " + allDependents.entrySet().stream()
                        .filter(entry -> !entry.getKey().equals(dependentId)) // Exclude the current dependent
                        .map(entry -> entry.getValue() + "(" + entry.getKey() + ")")
                        .collect(Collectors.joining(", ")) + ", " + customerName + "(" + customerId + ")");
            }

            Files.write(Paths.get(filePath), lines);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean updateExpirationDate(String fileName, Scanner scanner, String customersDirectory) {
        String filePath = customersDirectory + fileName;
        System.out.println("\n- Enter new expiration date (YYYY-MM-DD):");
        String newDate = scanner.nextLine();

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith("Expiration date:")) {
                    lines.set(i, "Expiration date: " + newDate);
                    break;
                }
            }
            Files.write(Paths.get(filePath), lines);
            System.out.println("\nExpiration date updated successfully !");
        } catch (IOException e) {
            System.out.println("\n※ Fail to update the expiration date ※\n");
        }
        return false;
    }

        // [4] Add New Customer
        private static void addNewCustomer() {
            Scanner scanner = new Scanner(System.in);
            UniqueIdGenerator idGenerator = UniqueIdGenerator.getInstance();
            Path CUSTOMERS_DIR_PATH = Paths.get("src/Data/Customers");

            String fullName = "";
            while (fullName.isEmpty()) {
                System.out.println("\n- Enter customer's full name:");
                fullName = scanner.nextLine().trim();
                if (fullName.isEmpty()) {
                    System.out.println("\n※ Please enter the customer's name ※");
                }
            }

            String customerId = idGenerator.generateCustomerId();
            String insuranceCardNumber = idGenerator.generateCardNumber();

            String policyHolder = handlePolicyHolderInput(scanner, CUSTOMERS_DIR_PATH, customerId, fullName);
            String policyHolderId = "";
            if (!policyHolder.equals(fullName + "(" + customerId + ")")) {
                policyHolderId = policyHolder.substring(policyHolder.indexOf("(") + 1, policyHolder.indexOf(")"));
            }

            List<String> dependentsIds = new ArrayList<>();
            if (policyHolderId.isEmpty()) {
                dependentsIds = handleDependentsInput(scanner, CUSTOMERS_DIR_PATH, customerId);
            }

            String expirationDateStr = "";
            while (expirationDateStr.isEmpty()) {
                System.out.println("- Enter expiration date (YYYY-MM-DD):");
                expirationDateStr = scanner.nextLine().trim();
                if (expirationDateStr.isEmpty()) {
                    System.out.println("\n※ Please enter the expiration date ※");
                }
            }
            LocalDate expirationDate = LocalDate.parse(expirationDateStr);

            String policyOwner = "";
            while (policyOwner.isEmpty()) {
                System.out.println("- Enter policy owner:");
                policyOwner = scanner.nextLine().trim();
                if (policyOwner.isEmpty()) {
                    System.out.println("\n※ Please enter the policy owner ※");
                }
            }

            saveCustomerToFile(customerId, fullName, policyHolder, dependentsIds, insuranceCardNumber, expirationDate.toString(), policyOwner, CUSTOMERS_DIR_PATH);
        }


    private static String handlePolicyHolderInput(Scanner scanner, Path CUSTOMERS_DIR_PATH, String customerId, String fullName) {
        String policyHolderId, policyHolder;
        while (true) {
            System.out.println("- Enter policy holder's ID (If this customer is the policy holder, leave blank):");
            policyHolderId = scanner.nextLine().trim();
            if (policyHolderId.isEmpty()) {
                policyHolder = fullName + "(" + customerId + ")";
                break;
            } else {
                policyHolder = getCustomerNameById(policyHolderId, CUSTOMERS_DIR_PATH);
                if (!policyHolder.isEmpty()) {
                    policyHolder += "(" + policyHolderId + ")";
                    break;
                } else {
                    System.out.println("\n※ Customer's information cannot be found. Please try again ※");
                }
            }
        }
        return policyHolder;
    }

    private static List<String> handleDependentsInput(Scanner scanner, Path CUSTOMERS_DIR_PATH, String currentCustomerId) {
        List<String> dependentsIds = new ArrayList<>();
        while (true) {
            System.out.println("- Enter dependents' IDs (comma separated, leave blank if none):");
            String dependentsInput = scanner.nextLine().trim();
            if (dependentsInput.isEmpty()) {
                break; // 입력이 없으면 반복 종료
            }

            String[] inputIds = dependentsInput.split(",");
            List<String> validatedDependentsIds = new ArrayList<>();
            boolean isValidInput = true;

            for (String inputId : inputIds) {
                inputId = inputId.trim();
                // 이미 Policy Holder인지 확인
                if (isPolicyHolder(inputId, CUSTOMERS_DIR_PATH) && !inputId.equals(currentCustomerId)) {
                    System.out.println("\n※ The person with ID " + inputId + " is already a policy holder ※");
                    isValidInput = false;
                    break;
                }

                // Dependent의 이름 가져오기
                String dependentName = getCustomerNameById(inputId, CUSTOMERS_DIR_PATH);
                if (!dependentName.isEmpty()) {
                    validatedDependentsIds.add(dependentName + "(" + inputId + ")");
                } else {
                    System.out.println("\n※ Customer with ID " + inputId + " not found ※");
                    isValidInput = false;
                    break;
                }
            }

            if (isValidInput) {
                dependentsIds.addAll(validatedDependentsIds);
                break; // 모든 검증을 통과하면 while 루프 종료
            }
        }
        return dependentsIds;
    }

    private static String getCustomerNameById(String customerId, Path CUSTOMERS_DIR_PATH) {
        try {
            return Files.walk(CUSTOMERS_DIR_PATH)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().startsWith(customerId + "_"))
                    .findFirst()
                    .map(path -> path.getFileName().toString().split("_")[1].replace(".txt", ""))
                    .orElse("");
        } catch (IOException e) {
            System.err.println("\n※ Fail to search for customer ID: " + customerId + " ※");
            return "";
        }
    }

    private static boolean isPolicyHolder(String customerId, Path CUSTOMERS_DIR_PATH) {
        try {
            return Files.walk(CUSTOMERS_DIR_PATH)
                    .filter(Files::isRegularFile)
                    .anyMatch(path -> {
                        try {
                            List<String> lines = Files.readAllLines(path);
                            return lines.stream().anyMatch(line -> line.contains("Policy holder: " + customerId));
                        } catch (IOException e) {
                            return false;
                        }
                    });
        } catch (IOException e) {
            return false;
        }
    }


    private static void saveCustomerToFile(String customerId, String fullName, String policyHolder, List<String> dependentsIds, String insuranceCardNumber, String expirationDate, String policyOwner, Path CUSTOMERS_DIR_PATH) {
        Path filePath = CUSTOMERS_DIR_PATH.resolve(customerId + "_" + fullName + ".txt");
        List<String> lines = new ArrayList<>();
        lines.add("Id: " + customerId);
        lines.add("Full name: " + fullName);
        lines.add("Policy holder: " + (policyHolder.isEmpty() ? fullName + "(" + customerId + ")" : policyHolder));
        lines.add("Dependents: " + String.join(", ", dependentsIds));
        lines.add("Insurance card: " + insuranceCardNumber);
        lines.add("Expiration date: " + expirationDate);
        lines.add("Card holder: " + fullName + "(" + customerId + ")");
        lines.add("Policy owner: " + policyOwner);
        lines.add("\nClaim List: "); // Initially empty

        try {
            Files.write(filePath, lines);
            System.out.println("\nCustomer added successfull !");

            // Now, update the policy holder's file to add this new customer as a dependent.
            if (!policyHolder.isEmpty()) {
                String policyHolderId = policyHolder.substring(policyHolder.indexOf("(") + 1, policyHolder.indexOf(")"));
                if (!policyHolderId.equals(customerId)) { // Check to ensure the policy holder is not the customer itself
                    updateCustomerFileForPolicyHolderAndDependent(policyHolderId, customerId, fullName, CUSTOMERS_DIR_PATH);
                }
            }
        } catch (Exception e) {
            System.err.println("\n※ Failed to save customer data: " + e.getMessage() +" ※");
        }
    }

    private static void updateCustomerFileForPolicyHolderAndDependent(String policyHolderId, String dependentId, String dependentName, Path CUSTOMERS_DIR_PATH) {
        try {
            Files.walk(CUSTOMERS_DIR_PATH)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().startsWith(policyHolderId + "_"))
                    .findFirst()
                    .ifPresent(path -> {
                        try {
                            List<String> lines = Files.readAllLines(path);
                            boolean foundDependents = false;
                            String newDependentInfo = dependentName + "(" + dependentId + ")";

                            for (int i = 0; i < lines.size(); i++) {
                                if (lines.get(i).startsWith("Dependents:")) {
                                    foundDependents = true;
                                    if (lines.get(i).equals("Dependents:")) {
                                        // If "Dependents:" line is empty, add the new dependent info directly.
                                        lines.set(i, lines.get(i) + " " + newDependentInfo);
                                    } else {
                                        // If there are already dependents listed, append the new one.
                                        lines.set(i, lines.get(i) + ", " + newDependentInfo);
                                    }
                                    break;
                                }
                            }

                            if (!foundDependents) {
                                // If "Dependents:" section does not exist, add it at the end.
                                lines.add("Dependents: " + newDependentInfo);
                            }

                            Files.write(path, lines);
                        } catch (IOException e) {
                            System.err.println("\n※ Failed to update the policy holder's file for new dependent: " + e.getMessage() +" ※");
                        }
                    });
        } catch (IOException e) {
            System.err.println("\n※ Fail to update dependent information: " + e.getMessage() + " ※");
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
