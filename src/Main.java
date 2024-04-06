package src;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.function.Function;
import java.util.stream.Stream;

import src.Super.UniqueIdGenerator;
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
                case 2:
                    viewClaimDetails(scanner);
                    break;
                case 3:
//                    addNewClaim(scanner);
                    break;
                case 4:
                    updateClaim();
                    break;
                case 5:
                    deleteClaim(scanner);
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


    // Main-[2] Manage Claim
        // [1] List All Cliam
    public static void listAllClaims() throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean exitMenu = false;

        while (!exitMenu) {
            System.out.println("\nList all claims\n" +
                    "\t[1] Claims List of One Customer\n" +
                    "\t[2] Total Claim List\n" +
                    "\t[3] List All Claim Documents\n" +
                    "\t[4] Exit\n" +
                    "Choose an option:");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("\n - Enter Customer ID:");
                    scanner.nextLine();
                    String customerId = scanner.nextLine();
                    if (!printCustomerClaims(customerId)) {
                        System.out.println("\n※ Customer ID does not exist ※");
                    }
                    break;
                case 2:
                    printAllClaims();
                    break;
                case 3:
                    printAllDocuments();                    break;
                case 4:
                    exitMenu = true;
                    break;
                default:
                    System.out.println("\n※ Invalid option ※");
            }
        }
    }
    // [1] Claims List of One Customer
    private static boolean printCustomerClaims(String customerId) throws IOException {
        Path dirPath = Paths.get("src/Data/Customers");
        boolean fileFound = false;

        try (Stream<Path> paths = Files.walk(dirPath)) {
            fileFound = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().startsWith(customerId))
                    .findFirst()
                    .map(path -> {
                        try {
                            printClaimsFromFile(path);
                            return true;
                        } catch (IOException e) {
                            System.out.println("\n※ An error occurred while reading the file ※");
                            return false;
                        }
                    }).orElse(false);
        }

        return fileFound;
    }

    private static void printClaimsFromFile(Path filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));
        String line;
        boolean claimSection = false;
        boolean foundClaims = false;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Claim List:")) {
                claimSection = true;
                continue;
            }
            if (claimSection && !line.trim().isEmpty()) {
                System.out.println(line);
                foundClaims = true;
            }
        }
        reader.close();

        if (claimSection && !foundClaims) {
            System.out.println("\n※ Claim does not exist ※");
        }
    }

    // total claim list
    private static void printAllClaims() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/Data/Claims/ClaimList.txt"));
        String line;
        boolean foundClaims = false;

        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            foundClaims = true;
        }
        reader.close();

        if (!foundClaims) {
            System.out.println("\n※ Claim does not exist ※");
        }
    }

    // document list
    private static void printAllDocuments() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/Data/Claims/DocumentList.txt"));
        String line;
        boolean foundClaims = false;

        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            foundClaims = true;
        }
        reader.close();

        if (!foundClaims) {
            System.out.println("\n※ Document does not exist ※");
        }
    }

        // [2] View Claim Details
        public static void viewClaimDetails(Scanner scanner) {
            String claimsFilePath = "src/Data/Claims/ClaimList.txt";

            try {
                System.out.print("\n- Enter Claim Id (e.g., f0000000001): ");
                String claimId = scanner.nextLine();

                Path path = Paths.get(claimsFilePath);
                boolean claimFound = false;

                try (Scanner fileScanner = new Scanner(path)) {
                    while (fileScanner.hasNextLine()) {
                        String line = fileScanner.nextLine();
                        if (line.startsWith(claimId)) {
                            // Split the line to extract claim details
                            String[] details = line.split(" \\| ");
                            System.out.println("\n[ Claim Detail ]");
                            System.out.println("Claim Id: " + details[0]);
                            System.out.println("Status: " + details[7].trim());
                            System.out.println("Exam Date: " + details[2]);
                            System.out.println("Claim Date: " + details[1]);
                            System.out.println("Insured Person: " + details[3]);
                            System.out.println("Insurance Card: " + details[4]);
                            System.out.println("Claim Amount: " + details[5]);
                            System.out.println("Bank Info: " + details[6]);

                            claimFound = true;
                            break;
                        }
                    }

                    if (!claimFound) {
                        System.out.println("\n※ Claim Id " + claimId + " not found ※");
                    }
                } catch (IOException e) {
                    System.err.println("\n※ Error reading the claims file ※");
                }

            } catch (Exception e) {
                System.err.println("\n※ Unexpected error occurred ※");
            }
        }

        // [3] Add New Claim
        /*public static void addNewClaim(Scanner scanner) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);

            String claimsFilePath = "src/Data/Claims/ClaimList.txt";
            String customersDirectory = "src/Data/Customers/";

            try {
                System.out.print("- Enter Insurance person ID (e.g., c1000001): ");
                String personId = scanner.nextLine();

                File directory = new File(customersDirectory);
                FilenameFilter filter = (dir, name) -> name.startsWith(personId + "_");
                File[] matchingFiles = directory.listFiles(filter);

                if (matchingFiles == null || matchingFiles.length == 0) {
                    throw new FileNotFoundException("\n※ Customer file not found for ID: " + personId + ".\n※ Please check the ID and try again ※");
                }
                File customerFile = matchingFiles[0];

                String customerDetails = new String(Files.readAllBytes(customerFile.toPath()));
                String[] lines = customerDetails.split("\n");
                String customerName = lines[1].split(": ")[1].trim();
                String insuranceCardNumber = lines[4].split(": ")[1].trim();

                System.out.print("- Enter Claim date (yyyy-MM-dd): ");
                String claimDateStr = scanner.nextLine();
                Date claimDate = dateFormat.parse(claimDateStr);
                if (!isValidClaimDate(claimDate, claimsFilePath, dateFormat)) {
                    throw new IllegalArgumentException("\n※ Claim date must be today or in the future, and after the most recent claim date ※");
                }

                System.out.print("- Enter Exam date (yyyy-MM-dd): ");
                String examDateStr = scanner.nextLine();
                Date examDate = dateFormat.parse(examDateStr);
                if (examDate.after(claimDate)) {
                    throw new IllegalArgumentException("\n※ Exam date cannot be after the claim date ※");
                }

                System.out.print("- Enter Claim amount: ");
                String claimAmount = scanner.nextLine();

                System.out.print("- Enter Bank name: ");
                String bankName = scanner.nextLine();

                System.out.print("- Enter Bank account: ");
                String bankAccount = scanner.nextLine();

                String claimId = UniqueIdGenerator.generateClaimId(); // Assume this method is implemented elsewhere

                // Create the new claim record with "NEW" status
                String newClaimRecord = String.format("%s | %s | %s | %s(%s) | %s | %s | %s - %s - %s | NEW",
                        claimId, claimDateStr, examDateStr, customerName, personId, insuranceCardNumber, claimAmount,
                        bankName, customerName, bankAccount);

                updateAllClaimsStatus(claimsFilePath, customersDirectory, claimDateStr, dateFormat);

                Files.write(Paths.get(claimsFilePath), (newClaimRecord + "\n").getBytes(), StandardOpenOption.APPEND);

                updateCustomerFileWithClaim(customerFile, newClaimRecord);

                System.out.println("\nClaim added successfully !");
            } catch (ParseException e) {
                System.err.println("\n※ Error parsing date: " + e.getMessage() + " ※");
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                System.err.println("※ File handling error ※");
            } catch (IllegalArgumentException e) {
                System.err.println("※ Validation error ※");
            } catch (Exception e) {
                System.err.println("※ Unexpected error occurred ※");
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
    }*/

        // [4] Update Claim
        public void updateClaim() throws IOException {
            boolean continueRunning = true;
            Scanner scanner = new Scanner(System.in);

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
                    case 1:
                        updateProcess();
                        break;
                    case 2:
                        updateBankInfo();
                        break;
                    case 3:
                        updateClaimAmount();
                        break;
                    case 4:
                        updateExamDate();
                        break;
                    case 5:
                        continueRunning = false;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            }
        }

    public void updateProcess() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n- Enter claim ID to mark as done (e.g., f0000000001): ");
        String claimId = scanner.nextLine();

        Path claimPath = Paths.get("src/Data/Claims/ClaimList.txt");
        Path documentPath = Paths.get("src/Data/Claims/DocumentList.txt");
        List<String> claimLines = Files.readAllLines(claimPath);
        boolean claimFound = false;
        String customerID = "";
        String insuranceCardNumber = "";
        String claimDate = "";

        for (int i = 0; i < claimLines.size(); i++) {
            String[] parts = claimLines.get(i).split("\\|");
            if (parts[0].trim().equals(claimId) && (parts[7].trim().equalsIgnoreCase("New") || parts[7].trim().equalsIgnoreCase("Processing"))) {
                claimFound = true;
                parts[7] = " Done"; // Update status
                claimLines.set(i, String.join("|", parts));
                customerID = parts[3].split("\\(")[1].split("\\)")[0].trim(); // Extracting CustomerID
                insuranceCardNumber = parts[4].trim();
                claimDate = parts[1].trim();
                break;
            }
        }

        if (!claimFound) {
            System.out.println("\n※ Claim ID not found or not eligible for update ※");
            return;
        }

        Files.write(claimPath, claimLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        String documentEntry = String.format("%s_%s_%s(%s).pdf", claimId, insuranceCardNumber, customerID, claimDate);
        Files.write(documentPath, Arrays.asList(documentEntry), StandardOpenOption.APPEND);

        Path customerFilePath = Paths.get("src/Data/Customers/" + customerID + "_*.txt");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(customerFilePath.getParent(), customerFilePath.getFileName().toString())) {
            for (Path path : stream) {
                List<String> customerLines = Files.readAllLines(path);
                for (int i = 0; i < customerLines.size(); i++) {
                    if (customerLines.get(i).contains(claimId)) {
                        customerLines.set(i, customerLines.get(i).replaceAll("Processing|New", "Done"));
                        Files.write(path, customerLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nClaim process updated successfully !");
    }
    public void updateClaimRecord(String claimId, Function<String[], Boolean> updateLogic) throws IOException {
        Path claimListPath = Paths.get("src/Data/Claims/ClaimList.txt");
        List<String> claimLines = Files.readAllLines(claimListPath);
        boolean updated = false;

        for (int i = 0; i < claimLines.size(); i++) {
            String[] parts = claimLines.get(i).split("\\|");
            if (parts[0].trim().equals(claimId)) {
                // Attempt to update using the provided logic
                if (updateLogic.apply(parts)) {
                    claimLines.set(i, String.join("|", parts));
                    updated = true;
                    // After updating ClaimList, also update the customer file
                    updateCustomerFile(parts[3].trim().split("\\(")[1].split("\\)")[0], claimId, parts);
                }
                break;
            }
        }

        if (updated) {
            Files.write(claimListPath, claimLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } else {
            System.out.println("\n※ Claim ID not found, or claim is already marked as 'Done' ※");
        }
    }

    private void updateCustomerFile(String customerId, String claimId, String[] updatedParts) throws IOException {
        Path customerFilePath = Paths.get("src/Data/Customers", customerId + "_*.txt");
        // Find the matching customer file
        try (var stream = Files.newDirectoryStream(customerFilePath.getParent(), customerFilePath.getFileName().toString())) {
            for (Path path : stream) {
                List<String> lines = Files.readAllLines(path);
                List<String> updatedLines = new ArrayList<>();
                for (String line : lines) {
                    if (line.contains(claimId)) {
                        String updatedLine = String.join("|", updatedParts);
                        updatedLines.add(updatedLine);
                    } else {
                        updatedLines.add(line);
                    }
                }
                Files.write(path, updatedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                break;
            }
        }
    }

    public void updateBankInfo() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n- Enter claim ID to update Bank info: ");
        String claimId = scanner.nextLine();

        System.out.print("- Enter new bank name: ");
        String newBankName = scanner.nextLine();
        System.out.print("- Enter new bank account: ");
        String newBankAccount = scanner.nextLine();

        updateClaimRecord(claimId, (parts) -> {
            if (!"Done".equalsIgnoreCase(parts[7].trim())) {
                String[] bankInfo = parts[6].trim().split(" - ");
                parts[6] = newBankName + " - " + bankInfo[1] + " - " + newBankAccount;
                return true;
            }
            return false;
        });
    }
    public void updateClaimAmount() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n- Enter claim ID to update claim amount: ");
        String claimId = scanner.nextLine();

        System.out.print("- Enter new claim amount: ");
        String newClaimAmount = scanner.nextLine();

        updateClaimRecord(claimId, (parts) -> {
            if (!"Done".equalsIgnoreCase(parts[7].trim())) {
                parts[5] = newClaimAmount;
                return true;
            }
            return false;
        });
    }
    public void updateExamDate() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n- Enter claim ID to update exam date: ");
        String claimId = scanner.nextLine();

        System.out.print("\n- Enter new exam date (YYYY-MM-DD): ");
        String newExamDate = scanner.nextLine();

        updateClaimRecord(claimId, (parts) -> {
            if (!"Done".equalsIgnoreCase(parts[7].trim()) && !(newExamDate.compareTo(parts[1].trim()) > 0)) {
                parts[2] = newExamDate;
                return true;
            }
            return false;
        });
    }
            // [5] Delete Claim
        public static void deleteClaim(Scanner scanner) {
            String claimsFilePath = "src/Data/Claims/ClaimList.txt";
            String customersDirectory = "src/Data/Customers/";

            try {
                System.out.print("\n- Enter Claim Id to delete (e.g., f0000000001): ");
                String claimId = scanner.nextLine();

                Path path = Paths.get(claimsFilePath);
                List<String> lines = Files.readAllLines(path);
                List<String> updatedLines = new ArrayList<>();

                boolean claimFound = false;
                boolean isDeletable = false;
                String customerID = null;

                for (String line : lines) {
                    if (line.startsWith(claimId + " |")) {
                        claimFound = true;
                        String[] parts = line.split(" \\| ");
                        String status = parts[7].trim();
                        if (!"done".equalsIgnoreCase(status)) {
                            isDeletable = true;
                            String insuredPerson = parts[3].trim();
                            customerID = insuredPerson.substring(insuredPerson.lastIndexOf('(') + 1, insuredPerson.lastIndexOf(')')); // Extracting the customerID
                        } else {
                            System.out.println("\n※ Claim Id " + claimId + " with status 'done' cannot be deleted ※");
                            return;
                        }
                    } else {
                        updatedLines.add(line);
                    }
                }

                if (claimFound && isDeletable) {
                    Files.write(path, updatedLines);
                    System.out.println("\nClaim Id " + claimId + " has been deleted from ClaimList.txt!");
                } else if (!claimFound) {
                    System.out.println("\n※ Claim Id " + claimId + " not found ※");
                    return;
                }

                if (customerID != null) {
                    String customerFileName = customerID + "_*.txt";
                    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(customersDirectory), customerFileName)) {
                        for (Path entry : stream) {
                            List<String> customerLines = Files.readAllLines(entry);
                            List<String> newCustomerLines = new ArrayList<>();
                            boolean claimListSection = false;

                            for (String customerLine : customerLines) {
                                if (customerLine.startsWith("Claim List:")) {
                                    claimListSection = true;
                                }
                                if (!claimListSection || !customerLine.contains(claimId)) {
                                    newCustomerLines.add(customerLine);
                                }
                            }

                            Files.write(entry, newCustomerLines);
                            System.out.println("\nClaim Id " + claimId + " has been deleted from the customer's file!");
                            break;
                        }
                    } catch (IOException | DirectoryIteratorException e) {
                        System.err.println("\n※ Error updating the customer's file ※");
                    }
                }

            } catch (IOException e) {
                System.err.println("\n※ Error reading or writing the claims file ※");
            } catch (Exception e) {
                System.err.println("\n※ Unexpected error occurred ※");
            }
        }


}
