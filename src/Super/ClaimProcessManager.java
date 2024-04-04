package src.Super;


/**
 * @author Han Yeeun - s3912055
 */
import java.util.List;

public interface ClaimProcessManager {
    void addClaim(Claim claim);
    void updateClaim(String fid, Claim claim);
    void deleteClaim(String fid);
    Claim getOneClaim(String fid);
    List<Claim> getAllClaims();
    void saveClaimsToFile();
    void loadClaimsFromFile();
}

