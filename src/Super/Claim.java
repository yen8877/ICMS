package src.Super;
/**
 * @author Han Yeeun - s3912055
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Claim {
    private String fid; // Format: f-numbers; 10 numbers
    private LocalDate claimDate;
    private String insuredPerson; // Assuming this refers to the Customer's ID for simplicity
    private String cardNumber;
    private LocalDate examDate;
    private List<String> documentName = new ArrayList<>();
    private double claimAmount;
    private ClaimStatus status;
    private ReceiverBankingInfo receiverBankingInfo;

    public Claim(String fid, LocalDate claimDate, String insuredPerson, String cardNumber, LocalDate examDate, double claimAmount, ClaimStatus status, ReceiverBankingInfo receiverBankingInfo) {
        this.fid = fid;
        this.claimDate = claimDate;
        this.insuredPerson = insuredPerson;
        this.cardNumber = cardNumber;
        this.examDate = examDate;
        this.claimAmount = claimAmount;
        this.status = status;
        this.receiverBankingInfo = receiverBankingInfo;
    }

    public void addDocument(String documentName) {
        if(this.status == ClaimStatus.DONE) {
            String formattedName = String.format("%s_%s_%s.pdf", this.fid, this.cardNumber, documentName);
            this.documentName.add(formattedName);
        }
    }

    // Getters and Setters
    public String getfId() {
        return fid;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public String getInsuredPerson() {
        return insuredPerson;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public List<String> getDocumentName() {
        return documentName;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public ReceiverBankingInfo getReceiverBankingInfo() {
        return receiverBankingInfo;
    }

    // Enum for Claim Status
    public enum ClaimStatus {
        NEW, PROCESSING, DONE
    }

    // ReceiverBankingInfo inner class
    public void setDocumentNames(List<String> documentNames) {
        this.documentName = documentNames;
    }
    public static class ReceiverBankingInfo {
        private String bank;
        private String name;
        private String number;

        public ReceiverBankingInfo(String bank, String name, String number) {
            this.bank = bank;
            this.name = name;
            this.number = number;
        }

        // Getters and Setters
        public String getBank() {
            return bank;
        }

        public String getName() {
            return name;
        }

        public String getNumber() {
            return number;
        }
    }
}
