package src;

import java.io.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.StandardOpenOption;


import src.Super.Customer;
import src.Super.InsuranceCard;
import src.Super.UniqueIdGenerator;
import src.UserInterface.Login;
import static src.UserInterface.Login.*;
import src.Super.Claim;
import src.Super.Claim.ClaimStatus;
import src.Super.Claim.ReceiverBankingInfo;

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
                    manageClaim();
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
                    listAllCustomers();
                    break;
                case 2:
                    viewCustomerDetails();
                    break;
                case 3:
                    addNewCustomer();
                    break;
                case 4:
                    updateCustomerInfo();

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
    public  void manageClaim() throws IOException {
        Scanner scanner = new Scanner(System.in);
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
                    // List All Claims
                    break;
                case 2:
                    // View Claim Details
                    break;
                case 3:
                    addNewClaim(scanner);
                    break;
                case 4:
                    // update Claim
                    // 이거 내일 목
                    break;
                case 5:
                    // delete claim
                    // 이거 오늘 수
                    break;
                case 6:
                    return;
                default:
                    System.out.println("\n※ Invalid choice ※");
                    break;
            }}}

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
            System.out.println("\n-Enter customer ID:");
            String customerId = scanner.nextLine().trim();
            Path customerFilePath = findCustomerFileById(customerId);

            if (customerFilePath == null) {
                System.out.println("\n※ Customer file not found ※");
                return;
            }

            String customerName = extractCustomerNameFromFileName(customerFilePath.getFileName().toString());

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
                        updateDependents(customerId, customerName, customerFilePath, scanner, choice == 2);
                        break;
                    case 2:
                        updateAllDependents(customerId, customerName, customerFilePath, scanner);
                        break;
                    case 3:
                        updateExpirationDate(customerFilePath, scanner);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("\n※ Invalid option ※");
                        break;
                }
            }
        }

    private static Path findCustomerFileById(String customerId) {
        String customersDirectory = "src/Data/Customers/";
        try (Stream<Path> paths = Files.walk(Paths.get(customersDirectory))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(f -> f.getFileName().toString().startsWith(customerId + "_"))
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            System.out.println("\n※ Fail to find customer files ※\n");
            return null;
        }
    }

    private static String extractCustomerNameFromFileName(String fileName) {
        return fileName.substring(fileName.indexOf('_') + 1, fileName.lastIndexOf('.'));
    }

    private static void updateDependents(String customerId, String customerName, Path customerFilePath, Scanner scanner, boolean replace) {
        System.out.println("\n- Enter dependents' IDs (comma separated, leave blank if none):");
        String dependentsInput = scanner.nextLine().trim();
        List<String> dependentIds = Arrays.asList(dependentsInput.split(",")).stream().map(String::trim).collect(Collectors.toList());

        if (!dependentIds.isEmpty()) {
            try {
                List<String> lines = Files.readAllLines(customerFilePath);
                boolean foundDependents = false;
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).startsWith("Dependents:")) {
                        foundDependents = true;
                        String existingDependents = lines.get(i).substring("Dependents: ".length());
                        String newDependentsList = dependentIds.stream()
                                .map(id -> {
                                    Path dependentFilePath = findCustomerFileById(id);
                                    if (dependentFilePath != null) {
                                        updatePolicyHolderInDependentFile(id, customerId, customerName);
                                        return extractCustomerNameFromFileName(dependentFilePath.getFileName().toString()) + "(" + id + ")";
                                    } else {
                                        System.out.println("\n※ Customer information does not exist: " + id +" ※");
                                        return null;
                                    }
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.joining(", "));

                        if (!existingDependents.isEmpty()) {
                            newDependentsList = existingDependents + ", " + newDependentsList;
                        }
                        lines.set(i, "Dependents: " + newDependentsList);
                        break;
                    }
                }
                if (!foundDependents) {
                    String newDependents = dependentIds.stream()
                            .map(id -> extractCustomerNameFromFileName(findCustomerFileById(id).getFileName().toString()) + "(" + id + ")")
                            .collect(Collectors.joining(", "));
                    lines.add("Dependents: " + newDependents);
                }
                Files.write(customerFilePath, lines);
                System.out.println("\nDependents updated successfully !");
            } catch (IOException e) {
                System.out.println("\n※ Fail to update dependents ※\n");
            }
        } else {
            System.out.println("\n※ No dependents updated ※");
        }
    }

    private static void updatePolicyHolderInDependentFile(String dependentId, String policyHolderId, String policyHolderName) {
        Path dependentFilePath = findCustomerFileById(dependentId);
        if (dependentFilePath != null) {
            try {
                List<String> lines = Files.readAllLines(dependentFilePath);
                boolean foundPolicyHolder = false;
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).startsWith("Policy holder:")) {
                        lines.set(i, "Policy holder: " + policyHolderName + "(" + policyHolderId + ")");
                        foundPolicyHolder = true;
                        break;
                    }
                }
                if (!foundPolicyHolder) {
                    lines.add("Policy holder: " + policyHolderName + "(" + policyHolderId + ")");
                }
                Files.write(dependentFilePath, lines);
                System.out.println("Policy holder updated for dependent ID: " + dependentId);
            } catch (IOException e) {
                System.err.println("\n※ Failed to update policy holder in dependent's file: " + e.getMessage() + " ※");
            }
        } else {
            System.out.println("\n※ No file found for dependent ID: " + dependentId + " ※");
        }
    }

    // updateCustomer-[2] update all dependents
    private static void updateAllDependents(String customerId, String customerName, Path customerFilePath, Scanner scanner) {
        System.out.println("\n- Enter new dependents' IDs (comma separated, leave blank to remove all):");
        String dependentsInput = scanner.nextLine().trim();

        List<String> dependentIds = new ArrayList<>();

        if (dependentsInput.isEmpty()) {
            System.out.println("\nAre you sure delete the list of all dependents? Type 'yes' or 'no':");
            String confirmation = scanner.nextLine().trim();
            if (!confirmation.equalsIgnoreCase("yes")) {
                System.out.println("\n※ Deletion of Dependent has been cancelled ※");
                return; // No confirmation, return to manageCustomer
            }
        } else {
            dependentIds = Arrays.asList(dependentsInput.split(","))
                    .stream().map(String::trim).collect(Collectors.toList());
        }

        try {
            List<String> lines = Files.readAllLines(customerFilePath);
            List<String> updatedLines = new ArrayList<>();
            List<String> oldDependents = new ArrayList<>();
            boolean foundDependents = false;

            for (String line : lines) {
                if (line.startsWith("Dependents:")) {
                    foundDependents = true;
                    if (!line.equals("Dependents:")) {
                        oldDependents = Arrays.asList(line.substring("Dependents: ".length()).split(", "))
                                .stream().map(dep -> dep.substring(dep.indexOf("(") + 1, dep.indexOf(")")))
                                .collect(Collectors.toList());
                    }
                    String newDependentsList = dependentIds.stream()
                            .map(id -> {
                                Path dependentFilePath = findCustomerFileById(id);
                                if (dependentFilePath != null) {
                                    updatePolicyHolderInDependentFile(id, customerId, customerName);
                                    return extractCustomerNameFromFileName(dependentFilePath.getFileName().toString()) + "(" + id + ")";
                                } else {
                                    System.out.println("\n※ Customer information does not exist: " + id + " ※");
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.joining(", "));

                    updatedLines.add("Dependents: " + newDependentsList);
                } else {
                    updatedLines.add(line);
                }
            }

            if (!foundDependents && !dependentIds.isEmpty()) {
                // Add the new Dependents section if it wasn't found and we have new dependents to add
                String newDependentsList = dependentIds.stream()
                        .map(id -> extractCustomerNameFromFileName(findCustomerFileById(id).getFileName().toString()) + "(" + id + ")")
                        .collect(Collectors.joining(", "));
                updatedLines.add("Dependents: " + newDependentsList);
            }

            Files.write(customerFilePath, updatedLines);

            // Update old dependents to remove this customer as their policy holder
            for (String depId : oldDependents) {
                removePolicyHolderFromDependent(depId, customerId);
            }

            System.out.println("\nDependent information updated successfully !");
        } catch (IOException e) {
            System.out.println("\n※ Failed to update dependents information ※");
        }
    }

    private static void removePolicyHolderFromDependent(String dependentId, String customerId) {
        Path dependentFilePath = findCustomerFileById(dependentId);
        if (dependentFilePath != null) {
            try {
                List<String> lines = Files.readAllLines(dependentFilePath);
                String dependentName = extractCustomerNameFromFileName(dependentFilePath.getFileName().toString());

                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).startsWith("Policy holder:")) {
                        lines.set(i, "Policy holder: " + dependentName + "(" + dependentId + ")");
                        break;
                    }
                }
                Files.write(dependentFilePath, lines);
                System.out.println("\nPolicy holder updated to self for dependent ID: " + dependentId);
            } catch (IOException e) {
                System.err.println("\n※ Failed to update policy holder in dependent's file: " + e.getMessage() + " ※");
            }
        } else {
            System.out.println("\n※ No file found for dependent ID: " + dependentId + " ※");
        }
    }

    // updateCustomer-[3] update expiration date
    private static void updateExpirationDate(Path customerFilePath, Scanner scanner) {
        System.out.println("\n- Enter new expiration date (YYYY-MM-DD):");
        String newDate = scanner.nextLine();
        try {
            List<String> lines = Files.readAllLines(customerFilePath);
            boolean found = false;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith("Expiration date:")) {
                    lines.set(i, "Expiration date: " + newDate);
                    found = true;
                    break;
                }
            }
            if (!found) {
                lines.add("Expiration date: " + newDate);
            }
            Files.write(customerFilePath, lines);
            System.out.println("\nExpiration date updated successfully!");
        } catch (IOException e) {
            System.out.println("\n※ Fail to update the expiration date ※\n");
        }
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
                if (isPolicyHolder(inputId, CUSTOMERS_DIR_PATH) && !inputId.equals(currentCustomerId)) {
                    System.out.println("\n※ The person with ID " + inputId + " is already a policy holder ※");
                    isValidInput = false;
                    break;
                }

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

    // Main-[2] Manage Claim
        // [1] List All Cliam

        // [2] View Claim Details

        // [3] Add New Claim
        public static void addNewClaim(Scanner scanner) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);

            String claimsFilePath = "src/Data/Claims/ClaimList.txt";
            String customersDirectory = "src/Data/Customers/";

            try {
                System.out.print("Enter Insurance person ID (e.g., c1000001): ");
                String personId = scanner.nextLine();

                File directory = new File(customersDirectory);
                FilenameFilter filter = (dir, name) -> name.startsWith(personId + "_");
                File[] matchingFiles = directory.listFiles(filter);

                if (matchingFiles == null || matchingFiles.length == 0) {
                    throw new FileNotFoundException("Customer file not found for ID: " + personId + ". Please check the ID and try again.");
                }
                File customerFile = matchingFiles[0];

                String customerDetails = new String(Files.readAllBytes(customerFile.toPath()));
                String[] lines = customerDetails.split("\n");
                String customerName = lines[1].split(": ")[1].trim();
                String insuranceCardNumber = lines[4].split(": ")[1].trim();

                System.out.print("Enter Claim date (yyyy-MM-dd): ");
                String claimDateStr = scanner.nextLine();
                Date claimDate = dateFormat.parse(claimDateStr);
                if (!isValidClaimDate(claimDate, claimsFilePath, dateFormat)) {
                    throw new IllegalArgumentException("Claim date must be today or in the future, and after the most recent claim date.");
                }

                System.out.print("Enter Exam date (yyyy-MM-dd): ");
                String examDateStr = scanner.nextLine();
                Date examDate = dateFormat.parse(examDateStr);
                if (examDate.after(claimDate)) {
                    throw new IllegalArgumentException("Exam date cannot be after the claim date.");
                }

                System.out.print("Enter Claim amount: ");
                String claimAmount = scanner.nextLine();

                System.out.print("Enter Bank name: ");
                String bankName = scanner.nextLine();

                System.out.print("Enter Bank account: ");
                String bankAccount = scanner.nextLine();

                String claimId = UniqueIdGenerator.generateClaimId(); // Assume this method is implemented elsewhere

                // Create the new claim record with "NEW" status
                String newClaimRecord = String.format("%s | %s | %s | %s | %s | %s | %s - %s - %s | NEW",
                        claimId, claimDateStr, examDateStr, customerName, insuranceCardNumber, claimAmount,
                        bankName, customerName, bankAccount);

                // Update all existing claims' status based on the new claim date before appending the new claim
                updateAllClaimsStatus(claimsFilePath, customersDirectory, claimDateStr, dateFormat);

                // Append the new claim record
                Files.write(Paths.get(claimsFilePath), (newClaimRecord + "\n").getBytes(), StandardOpenOption.APPEND);

                // Also, update the specific customer's file with the new claim record
                updateCustomerFileWithClaim(customerFile, newClaimRecord);

                System.out.println("Claim added successfully.");
            } catch (ParseException e) {
                System.err.println("Error parsing date: " + e.getMessage());
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                System.err.println("File handling error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("Validation error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error occurred: " + e.getMessage());
            }
        }

    private static void updateExistingClaimsStatus(String claimsFilePath, String newClaimDateStr, SimpleDateFormat dateFormat) throws IOException, ParseException {
        Path path = Paths.get(claimsFilePath);
        List<String> lines = Files.readAllLines(path);
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            if (!line.trim().isEmpty() && line.contains("|")) {
                String[] parts = line.split(" \\| ");
                String claimDateStr = parts[1].trim();
                Date claimDate = dateFormat.parse(claimDateStr);
                Date newClaimDate = dateFormat.parse(newClaimDateStr);

                if (!claimDate.after(newClaimDate) && line.endsWith("NEW")) { // If the claim date is not after the new claim date
                    line = line.replaceFirst("NEW", "Processing");
                }
            }
            updatedLines.add(line);
        }
        Files.write(path, updatedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static void updateCustomerFileStatus(File customerFile, String newClaimDateStr, SimpleDateFormat dateFormat) throws IOException, ParseException {
        List<String> lines = Files.readAllLines(customerFile.toPath());
        boolean isClaimListSection = false;
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            if (line.startsWith("Claim List:")) {
                isClaimListSection = true;
                updatedLines.add(line);
                continue;
            }

            if (isClaimListSection && line.contains("|")) {
                String[] parts = line.split(" \\| ");
                if (parts.length > 1) {
                    String claimDateStr = parts[1].trim();
                    Date claimDate = dateFormat.parse(claimDateStr);

                    if (!claimDate.after(dateFormat.parse(newClaimDateStr)) && line.endsWith("NEW")) {
                        // Update status to "Processing" if the claim date is not after the new claim date
                        line = line.replaceFirst("NEW", "Processing");
                    }
                }
            }
            updatedLines.add(line);
        }

        Files.write(customerFile.toPath(), updatedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static boolean isValidClaimDate(Date claimDate, String claimsFilePath, SimpleDateFormat dateFormat) throws IOException, ParseException {
        List<String> claims = Files.readAllLines(Path.of(claimsFilePath));
        Date lastDate = new Date(0); // Initialize to a very old date
        for (String claim : claims) {
            String[] parts = claim.split(" \\| ");
            Date date = dateFormat.parse(parts[1].trim());
            if (date.after(lastDate)) {
                lastDate = date;
            }
        }
        // The claim date is valid if it's today or after the most recent claim date
        return !claimDate.before(lastDate) && !claimDate.before(new Date());
    }

    private static void updateAllClaimsStatus(String claimsFilePath, String customersDirectory, String newClaimDateStr, SimpleDateFormat dateFormat) throws IOException, ParseException {
        // Update the status in ClaimList.txt
        updateExistingClaimsStatus(claimsFilePath, newClaimDateStr, dateFormat);

        // Update the status in all customer files in the Customers directory
        File directory = new File(customersDirectory);
        for (File file : directory.listFiles()) {
            updateCustomerFileStatus(file, newClaimDateStr, dateFormat);
        }
    }

    private static void updateCustomerFileWithClaim(File customerFile, String newClaimRecord) throws IOException {
        // This method appends the new claim record under "Claim List:" in the customer's file
        List<String> fileContent = new ArrayList<>(Files.readAllLines(customerFile.toPath(), StandardCharsets.UTF_8));
        boolean claimListFound = false;
        for (int i = 0; i < fileContent.size(); i++) {
            if ("Claim List:".equals(fileContent.get(i).trim())) {
                claimListFound = true;
                fileContent.add(i + 1, newClaimRecord);
                break;
            }
        }
        if (!claimListFound) {
            fileContent.addAll(Arrays.asList("Claim List:", newClaimRecord));
        }
        Files.write(customerFile.toPath(), fileContent, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
    }

        // [4] Update Claim

        // [5] Delete Claim

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
