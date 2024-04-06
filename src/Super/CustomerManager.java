package src.Super;
/*
 * @author Han Yeeun - s3912055
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomerManager {
    private static final Path CUSTOMERS_DIR_PATH = Paths.get("src/Data/Customers");
    private final Scanner scanner;
    private final UniqueIdGenerator idGenerator;

    public CustomerManager(Scanner scanner) {
        this.scanner = scanner;
        this.idGenerator = UniqueIdGenerator.getInstance();
    }

    // [1] List All Customers
    public void listAllCustomers() {
        try {
            List<String> customerFiles = Files.list(CUSTOMERS_DIR_PATH)
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
    }

    // [2] View Customer Details
    public void viewCustomerDetails() {
        System.out.println("\n- Please enter the customer ID to view details (e.g., c1000001):");
        String customerId = scanner.nextLine();
        try {
            String details = Files.list(CUSTOMERS_DIR_PATH)
                    .filter(path -> path.getFileName().toString().startsWith(customerId + "_"))
                    .findFirst()
                    .map(this::readCustomerDetailsWithoutClaims)
                    .orElse("\n※ Customer information not found ※");

            System.out.println(details);
        } catch (IOException e) {
            System.err.println("\n※ Failed to retrieve customer details ※");
            e.printStackTrace();
        }
    }

    private String readCustomerDetailsWithoutClaims(Path path) {
        try {
            return Files.readAllLines(path).stream()
                    .takeWhile(line -> !line.startsWith("Claim List:"))
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return "\n※ There was an error reading customer information ※";
        }
    }

    // [3] Add New Customer
    public void addNewCustomer() {
        String fullName = requestInput("\n- Enter customer's full name:", false);
        String customerId = idGenerator.generateCustomerId();
        String insuranceCardNumber = idGenerator.generateCardNumber();

        Customer policyHolder = handlePolicyHolderInput();
        List<Customer> dependents = handleDependentsInput();

        LocalDate expirationDate = requestExpirationDate();
        String policyOwner = requestInput("- Enter policy owner:", false);

        Customer customer = new Customer(customerId, fullName, true);
        if (policyHolder != null) {
            customer.setPolicyHolder(policyHolder);
        } else {
            policyHolder = customer;
        }
        customer.setPolicyHolder(policyHolder);

        InsuranceCard insuranceCard = new InsuranceCard(insuranceCardNumber, customer, policyOwner, expirationDate);
        customer.setInsuranceCard(insuranceCard);

        saveCustomerToFile(customer);

        if (policyHolder != null) {
            for (Customer dependent : dependents) {
                updateDependentPolicyHolder(dependent, policyHolder);
                updateCustomerFileForPolicyHolderAndDependent(dependent, policyHolder);
            }
        }}

    private String requestInput(String prompt, boolean allowEmpty) {
        System.out.println(prompt);
        String input = scanner.nextLine().trim();
        while (!allowEmpty && input.isEmpty()) {
            System.out.println("\n※ Input required ※");
            input = scanner.nextLine().trim();
        }
        return input;
    }

    private LocalDate requestExpirationDate() {
        LocalDate expirationDate = null;
        while (expirationDate == null) {
            try {
                String expirationDateStr = requestInput("- Enter expiration date (YYYY-MM-DD):", false);
                expirationDate = LocalDate.parse(expirationDateStr);
            } catch (Exception e) {
                System.out.println("\n※ Please enter the expiration date in the correct format (YYYY-MM-DD) ※");
            }
        }
        return expirationDate;
    }

    private Customer handlePolicyHolderInput() {
        String policyHolderId = requestInput("- Enter policy holder's ID (If this customer is the policy holder, leave blank):", true);
        if (policyHolderId.isEmpty()) {
            return null;
        } else {
            String policyHolderName = getCustomerNameById(policyHolderId);
            if (!policyHolderName.isEmpty()) {
                return new Customer(policyHolderId, policyHolderName, true);
            } else {
                System.out.println("\n※ Customer information does not exist, please try again ※");
                return handlePolicyHolderInput();
            }
        }
    }

    private List<Customer> handleDependentsInput() {
        List<Customer> dependents = new ArrayList<>();
        String dependentsInput = requestInput("- Enter dependents' IDs (comma separated, leave blank if none):", true);
        if (!dependentsInput.isEmpty()) {
            for (String inputId : dependentsInput.split(",")) {
                inputId = inputId.trim();
                if (!hasDependents(inputId)) {
                    String dependentName = getCustomerNameById(inputId);
                    if (!dependentName.isEmpty()) {
                        dependents.add(new Customer(inputId, dependentName, false));
                    } else {
                        System.out.println(inputId + "\n※ Customer information does not exist, please try again ※");
                        return handleDependentsInput();
                    }
                } else {
                    System.out.println("\n※ "+inputId + "already has other dependents and cannot be someone else's dependents ※");
                    return handleDependentsInput();
                }
            }
        }
        return dependents;
    }

    private boolean hasDependents(String customerId) {
        Path customerFilePath = CUSTOMERS_DIR_PATH.resolve(customerId + "_*.txt");
        try {
            return Files.walk(CUSTOMERS_DIR_PATH)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.startsWith(customerFilePath))
                    .anyMatch(path -> {
                        try {
                            List<String> lines = Files.readAllLines(path);
                            return lines.stream().anyMatch(line -> line.startsWith("Dependents:") && !line.endsWith("None"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return false;
                    });
        } catch (IOException e) {
            System.err.println("\n※ Error checking for dependents ※");
            e.printStackTrace();
            return false;
        }
    }

    private String getCustomerNameById(String customerId) {
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
    private void saveCustomerToFile(Customer customer) {
        Path filePath = CUSTOMERS_DIR_PATH.resolve(customer.getId() + "_" + customer.getFullName() + ".txt");
        List<String> lines = new ArrayList<>();
        lines.add("Id: " + customer.getId());
        lines.add("Full name: " + customer.getFullName());

        String policyHolderInfo = (customer.getPolicyHolder() == null || customer.getPolicyHolder().equals(customer)) ?
                customer.getFullName() + "(" + customer.getId() + ")" :
                customer.getPolicyHolder().getFullName() + "(" + customer.getPolicyHolder().getId() + ")";
        lines.add("Policy holder: " + policyHolderInfo);

        String dependentsInfo = customer.getDependents().stream()
                .map(dep -> dep.getFullName() + "(" + dep.getId() + ")")
                .collect(Collectors.joining(", "));
        lines.add("Dependents: " + (dependentsInfo.isEmpty() ? "None" : dependentsInfo));

        lines.add("Insurance card: " + customer.getInsuranceCard().getCardNum());
        lines.add("Expiration date: " + customer.getInsuranceCard().getExpirationDate());
        lines.add("Card holder: " + customer.getFullName() + "(" + customer.getId() + ")" );
        lines.add("Policy owner: " + customer.getInsuranceCard().getPolicyOwner());
        lines.add("\nClaim List: ");

        try {
            Files.write(filePath, lines);
            System.out.println("\nCustomer added successfully!");

            for (Customer dependent : customer.getDependents()) {
                updateDependentPolicyHolder(dependent, customer);
            }
        } catch (IOException e) {
            System.err.println("\n※ Failed to save customer data: " + e.getMessage() + " ※");
        }
    }

    private void updateDependentPolicyHolder(Customer dependent, Customer policyHolder) {
        Path filePath = CUSTOMERS_DIR_PATH.resolve(dependent.getId() + "_" + dependent.getFullName() + ".txt");
        try {
            List<String> lines = Files.readAllLines(filePath);
            boolean foundPolicyHolder = false;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith("Policy holder:")) {
                    lines.set(i, "Policy holder: " + policyHolder.getFullName() + "(" + policyHolder.getId() + ")");
                    foundPolicyHolder = true;
                    break;
                }
            }
            if (!foundPolicyHolder) {
                lines.add("Policy holder: " + policyHolder.getFullName() + "(" + policyHolder.getId() + ")");
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.err.println("\n※ Error updating dependent's policy holder: " + e.getMessage() + " ※");
        }
    }


    private void updateCustomerFileForPolicyHolderAndDependent(Customer dependent, Customer policyHolder) {
        Path filePath = CUSTOMERS_DIR_PATH.resolve(policyHolder.getId() + "_" + policyHolder.getFullName() + ".txt");
        try {
            List<String> lines = Files.readAllLines(filePath);
            String dependentInfo = lines.stream()
                    .filter(line -> line.startsWith("Dependents:"))
                    .findFirst()
                    .orElse("Dependents: None");

            if (dependentInfo.equals("Dependents: None")) {
                dependentInfo = "Dependents: " + dependent.getFullName() + "(" + dependent.getId() + ")";
            } else {
                dependentInfo += ", " + dependent.getFullName() + "(" + dependent.getId() + ")";
            }

            if (lines.stream().anyMatch(line -> line.startsWith("Dependents:"))) {
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).startsWith("Dependents:")) {
                        lines.set(i, dependentInfo);
                        break;
                    }
                }
            } else {
                lines.add(dependentInfo);
            }

            Files.write(filePath, lines);
        } catch (IOException e) {
            System.err.println("\n※ Failed to update policy holder's file for new dependent ※");
        }
    }

    // [4] Update Customer Details
    public Path findCustomerFileById(String customerId) {
        try (Stream<Path> paths = Files.walk(CUSTOMERS_DIR_PATH)) {
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

    public String extractCustomerNameFromFileName(String fileName) {
        Pattern pattern = Pattern.compile("_(.*?)\\.");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            System.out.println("Invalid file name format: " + fileName);
            return "Unknown";
        }
    }

    // [4]-[1] Update New Dependent
    private void updatePolicyHolderInDependentFile(String dependentId, String policyHolderId, String policyHolderName) {
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
                System.out.println("\n Policy holder updated for dependent ID: " + dependentId);
            } catch (IOException e) {
                System.err.println("\n※ Failed to update policy holder in dependent's file: " + e.getMessage() + " ※");
            }
        } else {
            System.out.println("\n※ No file found for dependent ID: " + dependentId + " ※");
        }
    }

    public void updateDependents(String customerId) {
        Path customerFilePath = findCustomerFileById(customerId);
        if (customerFilePath == null) {
            System.out.println("\n※ Customer file not found ※");
            return;
        }
        String customerName = extractCustomerNameFromFileName(customerFilePath.getFileName().toString());

        System.out.println("\n- Enter dependents' IDs (comma separated, leave blank if none):");
        String dependentsInput = scanner.nextLine().trim();
        List<String> newDependentIds = Arrays.stream(dependentsInput.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        try {
            List<String> lines = Files.readAllLines(customerFilePath);
            String existingDependents = lines.stream()
                    .filter(line -> line.startsWith("Dependents:"))
                    .findFirst()
                    .orElse("Dependents: ");

            String existingDependentsList = existingDependents.substring("Dependents: ".length()).trim();
            List<String> currentDependents = existingDependentsList.isEmpty() || existingDependentsList.equals("None")
                    ? new ArrayList<>()
                    : new ArrayList<>(Arrays.asList(existingDependentsList.split(", ")));

            for (String newDepId : newDependentIds) {
                Path dependentFilePath = findCustomerFileById(newDepId);
                if (dependentFilePath != null) {
                    String depName = extractCustomerNameFromFileName(dependentFilePath.getFileName().toString());
                    updatePolicyHolderInDependentFile(newDepId, customerId, customerName);
                    currentDependents.add(depName + "(" + newDepId + ")");
                } else {
                    System.out.println("\n※ Customer information does not exist: " + newDepId + " ※");
                }
            }

            String updatedDependents = String.join(", ", currentDependents);
            boolean foundDependents = false;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith("Dependents:")) {
                    lines.set(i, "Dependents: " + (updatedDependents.isEmpty() ? "None" : updatedDependents));
                    foundDependents = true;
                    break;
                }
            }
            if (!foundDependents) {
                lines.add("Dependents: " + (updatedDependents.isEmpty() ? "None" : updatedDependents));
            }

            Files.write(customerFilePath, lines);
            System.out.println("Dependents updated successfully!");
        } catch (IOException e) {
            System.err.println("\n※ Failed to update dependents ※");
        }
    }

    // [4]-[2] Update All Dependent
    public void updateAllDependents(String customerId) {
        Path customerFilePath = findCustomerFileById(customerId);
        if (customerFilePath == null) {
            System.out.println("\n※ Customer file not found ※");
            return;
        }
        String customerName = extractCustomerNameFromFileName(customerFilePath.getFileName().toString());

        System.out.println("\n- Enter new dependents' IDs (comma separated, leave blank to remove all):");
        String dependentsInput = scanner.nextLine().trim();
        List<String> newDependentIds = Arrays.asList(dependentsInput.split(","))
                .stream().map(String::trim).collect(Collectors.toList());

        try {
            List<String> lines = Files.readAllLines(customerFilePath);
            List<String> currentDependentIds = getCurrentDependents(lines);

            updateDependentsLineInCustomerFile(lines, newDependentIds, customerFilePath);

            List<String> removedDependentIds = new ArrayList<>(currentDependentIds);
            removedDependentIds.removeAll(newDependentIds);
            for (String depId : removedDependentIds) {
                removePolicyHolderFromDependent(depId);
            }

            for (String newDepId : newDependentIds) {
                if (!currentDependentIds.contains(newDepId)) {
                    updatePolicyHolderInDependentFile(newDepId, customerId, customerName);
                }
            }

            System.out.println("\nDependent information updated successfully!");
        } catch (IOException e) {
            System.err.println("\n※ Failed to update dependents information ※");
        }
    }

    private List<String> getCurrentDependents(List<String> lines) {
        for (String line : lines) {
            if (line.startsWith("Dependents:")) {
                String[] ids = line.substring("Dependents: ".length()).split(", ");
                return Arrays.stream(ids)
                        .filter(id -> !id.equals("None"))
                        .map(id -> id.substring(id.indexOf('(') + 1, id.indexOf(')')))
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    private void updateDependentsLineInCustomerFile(List<String> lines, List<String> newDependentIds, Path customerFilePath) throws IOException {
        boolean foundDependentsLine = false;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith("Dependents:")) {
                foundDependentsLine = true;
                String newDependentsList = "None";
                if (!newDependentIds.isEmpty()) {
                    newDependentsList = newDependentIds.stream()
                            .map(id -> {
                                Path dependentFilePath = findCustomerFileById(id);
                                if (dependentFilePath != null) {
                                    String fileName = dependentFilePath.getFileName().toString();
                                    String depCustomerName = extractCustomerNameFromFileName(fileName);
                                    return depCustomerName + "(" + id + ")";
                                } else {
                                    System.out.println("\n※ Customer information does not exist: " + id + " ※");
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull) // null 값 필터링
                            .collect(Collectors.joining(", "));
                }
                lines.set(i, "Dependents: " + newDependentsList);
                break;
            }
        }
        if (!foundDependentsLine) {
            lines.add("Dependents: " + (newDependentIds.isEmpty() ? "None" : String.join(", ", newDependentIds)));
        }

        Files.write(customerFilePath, lines);
    }

    private void removePolicyHolderFromDependent(String dependentId) {
        Path dependentFilePath = findCustomerFileById(dependentId);
        if (dependentFilePath != null) {
            try {
                List<String> lines = Files.readAllLines(dependentFilePath);
                boolean foundPolicyHolder = false;
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).startsWith("Policy holder:")) {
                        String dependentName = extractCustomerNameFromFileName(dependentFilePath.getFileName().toString());
                        lines.set(i, "Policy holder: " + dependentName + "(" + dependentId + ")");
                        foundPolicyHolder = true;
                        break;
                    }
                }
                if (!foundPolicyHolder) {
                    String dependentName = extractCustomerNameFromFileName(dependentFilePath.getFileName().toString());
                    lines.add("Policy holder: " + dependentName + "(" + dependentId + ")");
                }
                Files.write(dependentFilePath, lines);
                System.out.println("Policy holder self-updated for dependent ID: " + dependentId);
            } catch (IOException e) {
                System.err.println("Failed to update policy holder to self in dependent's file: " + e.getMessage());
            }
        } else {
            System.out.println("No file found for dependent ID: " + dependentId);
        }
    }


    // [4]-[3] Update Expiration Date
    private boolean isValidDate(String date) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        return Pattern.matches(regex, date);
    }

    public void updateExpirationDate(Path customerFilePath) {
        boolean validDate = false;
        String newDate = "";

        while (!validDate) {
            System.out.println("\n- Enter new expiration date (YYYY-MM-DD):");
            newDate = scanner.nextLine();
            if (isValidDate(newDate)) {
                validDate = true;
            } else {
                System.out.println("\n※ Invalid Input ※");
            }
        }

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
            System.out.println("\n※ Failed to update the expiration date ※\n");
        }
    }

    // [5] Delete Customer
    public void deleteCustomer() {
        System.out.println("\n- Enter the customer ID to delete:");
        String customerId = scanner.nextLine();

        try {
            Files.list(CUSTOMERS_DIR_PATH)
                    .filter(path -> path.getFileName().toString().startsWith(customerId + "_"))
                    .findFirst()
                    .ifPresent(this::confirmAndDelete);
        } catch (IOException e) {
            System.err.println("\n※ Failed to find the customer data file ※");
            e.printStackTrace();
        }
    }

    private void confirmAndDelete(Path path) {
        System.out.println("\nAre you sure you want to delete " + path.getFileName() + " file ?");
        System.out.println("※ Enter 'yes' to delete the customer information or Enter 'no' to return to the Customer manage menu ※\n");
        String confirmation = scanner.nextLine();

        if ("yes".equalsIgnoreCase(confirmation)) {
            try {
                Files.delete(path);
                System.out.println("\n" + path.getFileName() + " has been successfully deleted !");
            } catch (IOException e) {
                System.err.println("\n※ Failed to delete data file ※\n※ Please Try Again ※");
                e.printStackTrace();
            }
        } else if ("no".equalsIgnoreCase(confirmation)) {

        } else {
            System.out.println("\n※ Invalid input ※\n※ Returns to Customer Management Menu ※");
        }
    }}

