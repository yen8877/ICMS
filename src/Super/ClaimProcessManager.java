package src.Super;

import java.io.IOException;
import java.util.List;

public interface ClaimProcessManager {
    void addClaim(Claim claim);

    void deleteClaim(String fid);
    Claim getOneClaim(String fid);
    List<Claim> getAllClaims();
    void saveClaimsToFile();
    void loadClaimsFromFile();

    boolean printCustomerClaims(String customerId) throws IOException;
    void printAllClaims() throws IOException;
    void printAllDocuments() throws IOException;

    void updateClaimProcess(String claimId) throws IOException;
    void updateBankInfo(String claimId, String newBankName, String newBankAccount) throws IOException;
    void updateClaimAmount(String claimId, double newClaimAmount) throws IOException;
    void updateExamDate(String claimId, String newExamDate) throws IOException;
}