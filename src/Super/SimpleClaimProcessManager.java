package src.Super;
/**
 * @author Han Yeeun - s3912055
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleClaimProcessManager implements ClaimProcessManager {
    private List<Claim> claims = new ArrayList<>();

    @Override
    public void addClaim(Claim claim) {
        claims.add(claim);
    }

    @Override
    public void updateClaim(String fid, Claim updatedClaim) {
        claims = claims.stream()
                .map(claim -> claim.getfId().equals(fid) ? updatedClaim : claim)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteClaim(String fid) {
        claims.removeIf(claim -> claim.getfId().equals(fid));
    }

    @Override
    public Claim getClaim(String fid) {
        return claims.stream()
                .filter(claim -> claim.getfId().equals(fid))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Claim> getAllClaims() {
        // Example sorting by claim date
        return claims.stream()
                .sorted(Comparator.comparing(Claim::getClaimDate))
                .collect(Collectors.toList());
    }

    @Override
    public void saveClaimsToFile(String fileName) {
        // Pseudo code for saving claims to a file, should be implemented with actual file handling
    }
}
