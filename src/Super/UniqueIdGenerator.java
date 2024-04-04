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
        AtomicLong maxId = new AtomicLong(0); // Use AtomicLong for thread safety

        try {
            Files.lines(CLAIMS_LIST_PATH)
                    .map(line -> line.split(" \\| ")[0].trim()) // Extract the ID part
                    .map(id -> id.substring(1)) // Remove the leading 'f'
                    .mapToLong(Long::parseLong)
                    .max()
                    .ifPresent(max -> maxId.set(max)); // Update the maxId if a max value is present

            return "f" + String.format("%010d", maxId.incrementAndGet()); // Increment and format the new ID
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Claims Data: " + e.getMessage(), e);
        }
    }

}
