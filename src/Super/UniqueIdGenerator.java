package src.Super;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

public class UniqueIdGenerator {
    private static UniqueIdGenerator instance;
    private static final Path CUSTOMERS_DIR_PATH = Paths.get("src/Data/Customers");
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
            throw new RuntimeException("고객 목록을 불러오는데 실패했습니다.", e);
        }
    }

    public synchronized String generateCardNumber() {
        int firstPart = random.nextInt(100000); // 0부터 99999 사이의 랜덤한 숫자
        int lastPart = random.nextInt(100000); // 동일한 범위의 랜덤한 숫자
        return String.format("%05d-%05d", firstPart, lastPart);
    }
}
