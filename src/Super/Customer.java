package src.Super;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String cid;
    private final String fullName;
    private InsuranceCard insuranceCard;
    private boolean isPolicyHolder;
    private Customer policyHolder;
    private final List<String> claims = new ArrayList<>();
    private final List<Customer> dependents = new ArrayList<>();

    public Customer(String cid, String fullName, boolean isPolicyHolder) {
        this.cid = cid;
        this.fullName = fullName;
        this.isPolicyHolder = isPolicyHolder;
        this.policyHolder = isPolicyHolder ? this : null;
    }

    public void addDependent(Customer dependent) {
        dependent.setPolicyHolder(this);
        this.dependents.add(dependent);
    }

    public void setPolicyHolder(Customer policyHolder) {
        this.policyHolder = policyHolder;
        this.isPolicyHolder = (policyHolder == null || policyHolder.equals(this));
    }

    public String getPolicyHolderIdentification() {
        return policyHolder != null ? policyHolder.getFullName() + "(" + policyHolder.getId() + ")" : "None";
    }

    public String getPolicyHolderId() {
        return (policyHolder != null && !policyHolder.equals(this)) ? policyHolder.getId() : null;
    }

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

    public Customer getPolicyHolder() {
        return policyHolder;
    }
}
