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
    private List<String> documents = new ArrayList<>();
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

    // Method to add a document with the specified format
    public void addDocument(String documentName) {
        String formattedName = String.format("%s_%s_%s.pdf", this.fid, this.cardNumber, documentName);
        documents.add(formattedName);
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

    public List<String> getDocuments() {
        return documents;
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
