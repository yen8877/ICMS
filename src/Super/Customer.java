package src.Super;
/**
 * @author Han Yeeun - s3912055
 */

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String cid; // Format: c-numbers; 7 numbers
    private String fullName;
    private InsuranceCard insuranceCard;
    private boolean isPolicyHolder;
    private List<String> claims = new ArrayList<>();
    private List<Customer> dependents = new ArrayList<>();

    public Customer(String cid, String fullName, boolean isPolicyHolder){
        this.cid = cid;
        this.fullName = fullName;
        this.isPolicyHolder = isPolicyHolder;
    }

    // Method
    public void addClaim(String claim) {
        claims.add(claim);
    }

    public void addDependent(Customer dependent) {
        if (isPolicyHolder) {
            dependents.add(dependent);
        } else {
            System.out.println("Only policy holders can have dependents.");
        }
    }

    public void setDependents(List<String> dependentsNames) {
        this.dependents.clear();
        for (String name : dependentsNames) {
            Customer dependent = new Customer("", name, false);
            this.dependents.add(dependent);
        }
    }

    // Getters and Setters
    public String getId() {
        return cid;
    }

    public String getFullName() {
        return fullName;
    }

    public InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }
    public void setInsuranceCard(InsuranceCard insuranceCard) {
        this.insuranceCard = insuranceCard;
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
