package src.Super;
/*
 * @author Han Yeeun - s3912055
 */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClaimProcessManagerImpl implements ClaimProcessManager {
    private List<Claim> claims = new ArrayList<>();
    private final String claimsFilePath = "src/Data/Claims/ClaimList.txt";
    private final String customersDirectory = "src/Data/Customers/";
    private final String documentsDirectory = "src/Data/Claims/DocumentList.txt";

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ClaimProcessManagerImpl() {
        loadClaimsFromFile();
    }


    @Override
    public List<Claim> getAllClaims() {
        return new ArrayList<>(claims);}
    @Override
    public boolean printCustomerClaims(String customerId) throws IOException {
        Path dirPath = Paths.get(customersDirectory);
        boolean fileFound = false;

        try (Stream<Path> paths = Files.walk(dirPath)) {
            fileFound = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().contains(customerId))
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

    private void printClaimsFromFile(Path filePath) throws IOException {
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

        if (!foundClaims) {
            System.out.println("\n※ No claims found for this customer ※");
        }
    }

    @Override
    public void printAllClaims() throws IOException {
        Path path = Paths.get(claimsFilePath);
        Files.lines(path).forEach(System.out::println);
    }

    @Override
    public void printAllDocuments() throws IOException {
        Path documentListPath = Paths.get("src/Data/Claims/DocumentList.txt");
        Files.lines(documentListPath).forEach(System.out::println);
    }

    @Override
    public void addClaim(Claim claim) {
        claims.add(claim);
        saveClaimsToFile();
        updateCustomerFileWithClaim(claim);
        try {
            synchronizeClaimStatuses();
        } catch (IOException e) {
            System.err.println("\n※ Failed to synchronize claim statuses ※");
        }
    }

    @Override
    public void updateClaimProcess(String claimId) throws IOException {
        Path claimPath = Paths.get(claimsFilePath);
        List<String> claimLines = Files.readAllLines(claimPath);
        boolean claimFound = false;
        String customerID = "";
        String insuranceCardNumber = "";
        String claimDate = "";

        for (int i = 0; i < claimLines.size(); i++) {
            String[] parts = claimLines.get(i).split("\\|");
            if (parts[0].trim().equals(claimId) && (parts[7].trim().equalsIgnoreCase("NEW") || parts[7].trim().equalsIgnoreCase("PROCESSING"))) {
                claimFound = true;
                parts[7] = "DONE";
                claimLines.set(i, String.join("|", parts));
                customerID = parts[3].substring(parts[3].indexOf('(') + 1, parts[3].indexOf(')'));
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
        Path documentPath = Paths.get(documentsDirectory);
        Files.write(documentPath, Arrays.asList(documentEntry), StandardOpenOption.APPEND);

        updateCustomerFileWithStatus(claimId, "DONE");
        System.out.println("\nClaim process updated successfully !");
    }

    private void updateCustomerFileWithStatus(String claimId, String newStatus) throws IOException {
        Path customersDirectoryPath = Paths.get(customersDirectory);

        try (Stream<Path> paths = Files.walk(customersDirectoryPath)) {
            paths.filter(Files::isRegularFile).forEach(file -> {
                try {
                    List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
                    boolean claimListSectionFound = false;
                    List<String> updatedLines = new ArrayList<>();

                    for (String line : lines) {
                        if (line.trim().equals("Claim List:")) {
                            claimListSectionFound = true;
                            updatedLines.add(line);
                            continue;
                        }
                        if (claimListSectionFound && line.contains(claimId)) {
                            String updatedLine = line.replaceAll("(NEW|PROCESSING)", newStatus);
                            updatedLines.add(updatedLine);
                        } else {
                            updatedLines.add(line);
                        }
                    }

                    Files.write(file, updatedLines, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                } catch (IOException e) {
                    System.err.println("Failed to update the customer file: " + file);
                }
            });
        }
    }

    public void updateBankInfo(String claimId, String newBankName, String newBankAccount) throws IOException {
        if (newBankName.isEmpty() || newBankAccount.isEmpty()) {
            System.out.println("Bank name and account cannot be empty.");
            return;
        }

        updateClaimRecord(claimId, parts -> {
            if (!"DONE".equalsIgnoreCase(parts[7].trim())) {
                parts[6] = newBankName + " - " + parts[6].split(" - ")[1] + " - " + newBankAccount;
                return true;
            }
            return false;
        });
    }

    @Override
    public void updateClaimAmount(String claimId, double newClaimAmount) throws IOException {
        if (newClaimAmount <= 0) {
            System.out.println("\n※ Claim amount must be positive ※");
            return;
        }

        updateClaimRecord(claimId, parts -> {
            if (!"DONE".equalsIgnoreCase(parts[7].trim())) {
                parts[5] = String.format("%.2f", newClaimAmount);
                return true;
            }
            return false;
        });
    }
    @Override
    public void updateExamDate(String claimId, String newExamDateStr) throws IOException {
        // Check for non-empty input before proceeding
        if (newExamDateStr.isEmpty()) {
            System.out.println("Exam date cannot be empty.");
            return;
        }

        LocalDate newExamDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            newExamDate = LocalDate.parse(newExamDateStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use the format YYYY-MM-DD.");
            return;
        }

        updateClaimRecord(claimId, parts -> {
            // Check if exam date is after claim date
            LocalDate claimDate;
            try {
                claimDate = LocalDate.parse(parts[1].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                System.out.println("Failed to parse claim date.");
                return false;
            }

            if (newExamDate.isAfter(claimDate)) {
                System.out.println("Exam date cannot be after the claim date.");
                return false;
            }

            if (!"DONE".equalsIgnoreCase(parts[7].trim())) {
                parts[2] = newExamDateStr; // Assuming the date format in the file is "yyyy-MM-dd"
                return true;
            }
            return false;
        });
    }

    private void updateClaimRecord(String claimId, Function<String[], Boolean> updateLogic) throws IOException {
        Path claimListPath = Paths.get(claimsFilePath);
        List<String> claimLines = Files.readAllLines(claimListPath);
        boolean updated = false;

        for (int i = 0; i < claimLines.size(); i++) {
            String[] parts = claimLines.get(i).split(" \\| ");
            if (parts[0].trim().equals(claimId)) {
                if (updateLogic.apply(parts)) {
                    claimLines.set(i, String.join(" | ", parts));
                    updated = true;
                    break;
                }
            }
        }

        if (updated) {
            Files.write(claimListPath, claimLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            String customerId = claimLines.stream()
                    .filter(line -> line.startsWith(claimId))
                    .findFirst()
                    .map(line -> line.split(" \\| ")[3])
                    .map(info -> info.substring(info.indexOf('(') + 1, info.indexOf(')')))
                    .orElse("");
            if (!customerId.isEmpty()) {
                updateCustomerFile(customerId, claimId, updateLogic);
            }
        } else {
            System.out.println("\n※ Claim ID not found or already marked as done ※");
        }
    }

    private void updateCustomerFile(String customerId, String claimId, Function<String[], Boolean> updateLogic) throws IOException {
        Path customerFilePathPattern = Paths.get(customersDirectory, customerId + "_*.txt");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(customerFilePathPattern.getParent(), customerFilePathPattern.getFileName().toString())) {
            for (Path path : stream) {
                List<String> lines = Files.readAllLines(path);
                List<String> updatedLines = new ArrayList<>();
                boolean updated = false;
                for (String line : lines) {
                    if (line.contains(claimId)) {
                        String[] parts = line.split(" \\| ");
                        if (updateLogic.apply(parts)) {
                            updatedLines.add(String.join(" | ", parts));
                            updated = true;
                        } else {
                            updatedLines.add(line);
                        }
                    } else {
                        updatedLines.add(line);
                    }
                }
                if (updated) {
                    Files.write(path, updatedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                }
                break;
            }
        }
    }

    @Override
    public void deleteClaim(String fid) {
        try {
            Path claimListPath = Paths.get(claimsFilePath);
            List<String> claimLines = Files.readAllLines(claimListPath, StandardCharsets.UTF_8);
            List<String> updatedClaimLines = new ArrayList<>();
            boolean claimFound = false;
            String customerID = null;

            for (String line : claimLines) {
                if (line.startsWith(fid + " |")) {
                    claimFound = true;
                    String[] parts = line.split(" \\| ");
                    String insuredInfo = parts[3];
                    customerID = insuredInfo.substring(insuredInfo.indexOf('(') + 1, insuredInfo.indexOf(')'));
                } else {
                    updatedClaimLines.add(line);
                }
            }

            if (!claimFound) {
                System.out.println("\n※ Claim ID " + fid + " not found ※");
                return;
            }

            Files.write(claimListPath, updatedClaimLines, StandardCharsets.UTF_8);
            System.out.println("\nClaim ID " + fid + " has been successfully deleted !");

            if (customerID != null) {
                deleteClaimFromCustomerFile(customerID, fid);
            }
        } catch (IOException e) {
            System.err.println("\n※ Error while deleting claim: " + e.getMessage() + " ※");
        }
    }


    private void deleteClaimFromCustomerFile(String customerID, String claimID) throws IOException {
        Path customerDir = Paths.get(customersDirectory);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(customerDir, customerID + "_*.txt")) {
            for (Path filePath : stream) {
                List<String> customerFileLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
                List<String> updatedLines = new ArrayList<>();

                for (String line : customerFileLines) {
                    if (!line.contains(claimID)) {
                        updatedLines.add(line);
                    }
                }

                Files.write(filePath, updatedLines, StandardCharsets.UTF_8);
                break;
            }
        }
    }

    @Override
    public Claim getOneClaim(String fid) {
        for (Claim claim : claims) {
            if (claim.getfId().equals(fid)) {
                printClaimDetails(claim);
                return claim;
            }
        }System.out.println("\n※ Claim Id " + fid + " not found ※");
        return null;
    }

    private void printClaimDetails(Claim claim) {
        System.out.println("\n[ Claim Detail ]");
        System.out.println("Claim Id: " + claim.getfId());
        System.out.println("Status: " + claim.getStatus());
        System.out.println("Exam Date: " + claim.getExamDate());
        System.out.println("Claim Date: " + claim.getClaimDate());
        System.out.println("Insured Person: " + claim.getInsuredPerson() + "(" + claim.getCustomerId() + ")");
        System.out.println("Insurance Card: " + claim.getCardNumber());
        System.out.println("Claim Amount: " + claim.getClaimAmount());
        System.out.println("Bank Info: " + claim.getReceiverBankingInfo().getBank() + " - " + claim.getReceiverBankingInfo().getName() + " - " + claim.getReceiverBankingInfo().getNumber());
    }



    /*
     * Method
     * */

    // add new claim

    public void addNewClaim(Scanner scanner) {
        String personId = requestInputFromUser(scanner, "\n- Enter Insurance person ID (e.g., c1000001): ", "Invalid input. Please try again.");
        String customerName = getCustomerNameById(personId);
        if (customerName.isEmpty()) {
            System.out.println("\n※ Customer information does not exist ※");
            return;
        }

        LocalDate lastClaimDate = getMostRecentClaimDate();
        LocalDate claimDate = null;
        while (claimDate == null) {
            System.out.print("- Enter Claim date (yyyy-MM-dd): ");
            try {
                LocalDate inputDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
                if (!inputDate.isBefore(lastClaimDate) || inputDate.isEqual(lastClaimDate)) {
                    claimDate = inputDate;
                } else {
                    System.out.println("\n※ The claim date must be equal to or after the most recent claim date ※");
                }
            } catch (DateTimeParseException e) {
                System.out.println("\n※ Invalid date format ※");
            }
        }

        LocalDate examDate = null;
        while (examDate == null) {
            System.out.print("- Enter Exam date (yyyy-MM-dd): ");
            try {
                LocalDate inputDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
                if (!inputDate.isAfter(claimDate)) {
                    examDate = inputDate;
                } else {
                    System.out.println("\n※ The exam date must be equal to or before the claim date ※");
                }
            } catch (DateTimeParseException e) {
                System.out.println("\n※ Invalid date format ※");
            }}

        double claimAmount = requestDoubleFromUser(scanner, "- Enter Claim amount: ", "Invalid amount format. Please try again.");

        String insuranceCardNumber = getInsuranceCardNumberById(personId);
        if (insuranceCardNumber.isEmpty()) {
            System.out.println("\n※ Failed to find insurance card number for the customer ※");
            return;
        }
        String bankName = requestInputFromUser(scanner, "- Enter Bank name: ", "Invalid input. Please try again.");
        String bankAccountNumber = requestInputFromUser(scanner, "- Enter Bank account number: ", "Invalid input. Please try again.");

        String claimId = UniqueIdGenerator.generateClaimId();
        Claim claim = new Claim(claimId, claimDate, customerName, personId, insuranceCardNumber, examDate, claimAmount, Claim.ClaimStatus.NEW,
                new Claim.ReceiverBankingInfo(bankName, customerName, bankAccountNumber));

        updateLastClaimStatusToProcessing();
        addClaim(claim);
        System.out.println("\nClaim added successfully !");
    }

    private void updateLastClaimStatusToProcessing() {
        if (!claims.isEmpty()) {
            claims.get(claims.size() - 1).setStatus(Claim.ClaimStatus.PROCESSING);
        }
        saveClaimsToFile();
    }

    public void loadClaimsFromFile() {
        if (!Files.exists(Paths.get(claimsFilePath))) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(Paths.get(claimsFilePath), StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] parts = line.split(" \\| ");
                if (parts.length < 8) continue; // Ensure there are enough parts to parse

                String fid = parts[0].trim();
                LocalDate claimDate = LocalDate.parse(parts[1].trim(), dateFormatter);
                LocalDate examDate = LocalDate.parse(parts[2].trim(), dateFormatter);
                String insuredInfo = parts[3].trim(); // "JaneDoe(c000001)"
                String cardNumber = parts[4].trim();
                double claimAmount = Double.parseDouble(parts[5].trim());
                String[] bankInfo = parts[6].split(" - ");
                String bank = bankInfo[0].trim();
                String accountName = bankInfo.length > 1 ? bankInfo[1] : "";
                String accountNumber = bankInfo.length > 2 ? bankInfo[2] : "";
                Claim.ClaimStatus status = Claim.ClaimStatus.valueOf(parts[7].trim().toUpperCase());

                String insuredPerson = insuredInfo.substring(0, insuredInfo.indexOf('('));
                String customerId = insuredInfo.substring(insuredInfo.indexOf('(') + 1, insuredInfo.indexOf(')'));

                Claim claim = new Claim(fid, claimDate, insuredPerson, customerId, cardNumber, examDate, claimAmount, status,
                        new Claim.ReceiverBankingInfo(bank, accountName, accountNumber));
                claims.add(claim);
            }
        } catch (IOException e) {
            System.err.println("\n※ Failed to load claims from file ※");
        }
    }

    public void saveClaimsToFile() {
        List<String> claimRecords = claims.stream()
                .map(claim -> {
                    String customerInfo = claim.getInsuredPerson() + "(" + claim.getCustomerId() + ")";
                    return String.join(" | ",
                            claim.getfId(), claim.getClaimDate().format(dateFormatter), claim.getExamDate().format(dateFormatter),
                            customerInfo, claim.getCardNumber(),
                            String.valueOf(claim.getClaimAmount()),
                            claim.getReceiverBankingInfo().getBank() + " - " + customerInfo + " - " + claim.getReceiverBankingInfo().getNumber(),
                            claim.getStatus().toString());
                })
                .collect(Collectors.toList());
        try {
            Files.write(Paths.get(claimsFilePath), claimRecords, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            System.err.println("\n※ Failed to save claims to file ※");
        }
    }

    private String requestInputFromUser(Scanner scanner, String prompt, String errorMessage) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            System.out.println(errorMessage);
            System.out.print(prompt);
            input = scanner.nextLine().trim();
        }
        return input;
    }

    private double requestDoubleFromUser(Scanner scanner, String prompt, String errorMessage) {
        double amount;
        while (true) {
            System.out.print(prompt);
            try {
                amount = Double.parseDouble(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
            }
        }
        return amount;
    }

    private LocalDate getMostRecentClaimDate() {
        LocalDate mostRecentDate = LocalDate.MIN;
        try {
            mostRecentDate = Files.lines(Paths.get(claimsFilePath))
                    .map(line -> line.split(" \\| "))
                    .filter(parts -> parts.length >= 8) // Ensure there are enough parts
                    .map(parts -> {
                        try {
                            return LocalDate.parse(parts[1].trim(), dateFormatter);
                        } catch (DateTimeParseException e) {
                            return LocalDate.MIN;
                        }
                    })
                    .max(LocalDate::compareTo)
                    .orElse(LocalDate.MIN);
        } catch (IOException e) {
            System.err.println("\n※ Error checking the most recent claim date ※");
        }
        return mostRecentDate;
    }

    private String getCustomerNameById(String customerId) {
        try {
            return Files.walk(Paths.get(customersDirectory))
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

    private void updateCustomerFileWithClaim(Claim claim) {
        Path customerFilePath = findCustomerFilePath(claim.getCustomerId());
        if (customerFilePath == null) {
            System.err.println("\n※ Customer file not found for ID: " + claim.getCustomerId() + " ※");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(customerFilePath, StandardCharsets.UTF_8);
            boolean claimListSectionFound = false;
            int insertIndex = -1;

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).trim().equals("Claim List:")) {
                    claimListSectionFound = true;
                    insertIndex = i + 1;
                    continue;
                }

                if (claimListSectionFound && !lines.get(i).trim().isEmpty()) {
                    insertIndex = i + 1;
                }
            }

            if (!claimListSectionFound) {
                lines.add("Claim List:");
                insertIndex = lines.size();
            } else if (insertIndex == -1) {
                insertIndex = lines.size();
            }

            String claimRecord = formatClaimForCustomerFile(claim);

            lines.add(insertIndex, claimRecord);
            Files.write(customerFilePath, lines, StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("\n※ Failed to update customer file with new claim: " + e.getMessage() + " ※");
        }
    }
    private void synchronizeCustomerFilesWithUpdatedStatuses() {
        loadClaimsFromFile();

        claims.forEach(this::updateCustomerFileWithClaimStatus);
    }
    private void updateCustomerFileWithClaimStatus(Claim claim) {
        Path customerFilePath = findCustomerFilePath(claim.getCustomerId());
        if (customerFilePath == null) {
            System.err.println("\n※ Customer file not found for ID: " + claim.getCustomerId() + " ※");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(customerFilePath, StandardCharsets.UTF_8);
            boolean foundClaimList = false;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().equals("Claim List:")) {
                    foundClaimList = true;
                    continue;
                }
                if (foundClaimList && line.contains(claim.getfId())) {
                    String updatedClaimLine = formatClaimForCustomerFile(claim);
                    lines.set(i, updatedClaimLine);
                    break;
                }
            }

            Files.write(customerFilePath, lines, StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("\n※ Failed to update customer file with new claim status: " + e.getMessage() +" ※");
        }
    }

    private String formatClaimForCustomerFile(Claim claim) {
        // This helper method formats the claim data for insertion into the customer file.
        return String.format("%s | %s | %s | %s(%s) | %s | %.2f | %s - %s - %s | %s",
                claim.getfId(), claim.getClaimDate().format(dateFormatter), claim.getExamDate().format(dateFormatter),
                claim.getInsuredPerson(), claim.getCustomerId(), claim.getCardNumber(), claim.getClaimAmount(),
                claim.getReceiverBankingInfo().getBank(), claim.getInsuredPerson(), claim.getReceiverBankingInfo().getNumber(),
                claim.getStatus().toString());
    }

    private Path findCustomerFilePath(String customerId) {
        try {
            return Files.walk(Paths.get(customersDirectory))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().contains(customerId))
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            System.err.println("\n※ Error finding customer file for ID: " + customerId + " ※");
            return null;
        }
    }

    private String getInsuranceCardNumberById(String customerId) {
        try {
            return Files.walk(Paths.get(customersDirectory))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().startsWith(customerId + "_"))
                    .findFirst()
                    .map(path -> {
                        try {
                            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                            for (String line : lines) {
                                if (line.startsWith("Insurance card:")) {
                                    return line.split(":")[1].trim();
                                }
                            }
                        } catch (IOException e) {
                            System.err.println("\n※ Failed to read customer file ※");
                        }
                        return "";
                    })
                    .orElse("");
        } catch (IOException e) {
            System.err.println("\n※ Failed to search for customer file ※");
        }
        return "";
    }

    private void synchronizeClaimStatuses() throws IOException {
        Map<String, String> claimStatusMap = new HashMap<>();
        List<String> claimListLines = Files.readAllLines(Paths.get(claimsFilePath), StandardCharsets.UTF_8);
        for (String line : claimListLines) {
            String[] parts = line.split(" \\| ");
            if (parts.length > 7) {
                String claimId = parts[0].trim();
                String status = parts[7].trim();
                claimStatusMap.put(claimId, status);
            }
        }

        Files.walk(Paths.get(customersDirectory))
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                        List<String> updatedLines = new ArrayList<>();
                        boolean isClaimListSection = false;

                        for (String line : lines) {
                            if (line.startsWith("Claim List:")) {
                                isClaimListSection = true;
                                updatedLines.add(line);
                                continue;
                            }

                            if (isClaimListSection) {
                                String[] parts = line.split(" \\| ");
                                if (parts.length > 7) {
                                    String claimId = parts[0].trim();
                                    if (claimStatusMap.containsKey(claimId)) {
                                        parts[7] = claimStatusMap.get(claimId); // 상태 업데이트
                                        String updatedLine = String.join(" | ", parts);
                                        updatedLines.add(updatedLine);
                                        continue;
                                    }
                                }
                            }

                            updatedLines.add(line);
                        }

                        Files.write(path, updatedLines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                    } catch (IOException e) {
                        System.err.println("\n※ Error updating claim status in customer file ※");
                    }
                });
    }
}