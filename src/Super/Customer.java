package src.Super;
/**
 * @author Han Yeeun - s3912055
 */

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String cid; // Format: c-numbers; 7 numbers
    private String fullName;
    private String cPassword;
    private InsuranceCard insuranceCard;
    private boolean isPolicyHolder;
    private List<String> claims = new ArrayList<>();
    private List<Customer> dependents = new ArrayList<>();

    public Customer(String cid, String fullName, String cPassword, InsuranceCard insuranceCard, boolean isPolicyHolder){
        this.cid = cid;
        this.fullName = fullName;
        this.cPassword = cPassword;
        this.insuranceCard = insuranceCard;
        this.isPolicyHolder = isPolicyHolder;
    }

    // Method to add a claim
    public void addClaim(String claim) {
        claims.add(claim);
    }

    // Method to add a dependent, only valid for policy holders
    public void addDependent(Customer dependent) {
        if (isPolicyHolder) {
            dependents.add(dependent);
        } else {
            System.out.println("Only policy holders can have dependents.");
        }
    }

    // Getters and Setters
    // Omitting setters for simplicity, they can be added as needed

    public String getId() {
        return cid;
    }

    public String getFullName() {
        return fullName;
    }

    public String getcPassword() {
        return cPassword;
    }

    public InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }

    public List<String> getClaims() {
        return claims;
    }

    public boolean isPolicyHolder() {
        return isPolicyHolder;
    }

    public List<Customer> getDependents() {
        return dependents;
    }
}
