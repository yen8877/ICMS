package src.Super;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class UniqueIdGenerator {
    private static UniqueIdGenerator instance;
    private static final Path CUSTOMERS_DIR_PATH = Paths.get("src/Data/Customers");
    private static final Path CLAIMS_LIST_PATH = Paths.get("src/Data/Claims/ClaimList.txt");
    private static final Random random = new Random();

    private UniqueIdGenerator() {}

    public static synchronized UniqueIdGenerator getInstance() {
        if (instance == null) {
            instance = new UniqueIdGenerator();
        }
        return instance;
    }

    public synchronized String generateCustomerId() {
        try (Stream<Path> paths = Files.list(CUSTOMERS_DIR_PATH)) {
            // 고객 ID 최대값 찾기
            int maxId = paths.map(path -> path.getFileName().toString().split("_")[0].substring(1)) // c1000001 -> 1000001
                    .mapToInt(Integer::parseInt)
                    .max()
                    .orElse(1000000); // 기본값

            return "c" + (maxId + 1);
        } catch (IOException e) {
            throw new RuntimeException("\n※ Fail to find Customer Data ※", e);
        }
    }

    public synchronized String generateCardNumber() {
        int firstPart = random.nextInt(100000); // 0부터 99999 사이의 랜덤한 숫자
        int lastPart = random.nextInt(100000); // 동일한 범위의 랜덤한 숫자
        return String.format("%05d-%05d", firstPart, lastPart);
    }

    public static synchronized String generateClaimId() {
        final String claimsFilePath = "src/Data/Claims/ClaimList.txt"; // 클레임 파일 경로
        AtomicLong maxId = new AtomicLong(0);

        try {
            Files.lines(Paths.get(claimsFilePath))
                    .map(line -> line.split(" \\| ")[0].trim()) // "fid" 부분 추출
                    .filter(id -> id.startsWith("f")) // "f"로 시작하는 ID만 필터링
                    .map(id -> id.substring(1)) // "f"를 제거하고 숫자 부분만 추출
                    .mapToLong(Long::parseLong) // 문자열을 long 타입으로 변환
                    .max() // 최댓값 찾기
                    .ifPresent(max -> maxId.set(max)); // 최댓값이 존재하면 maxId 업데이트

            return "f" + String.format("%010d", maxId.incrementAndGet()); // 다음 ID 생성
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Claims Data: " + e.getMessage(), e);
        }
    }

}
