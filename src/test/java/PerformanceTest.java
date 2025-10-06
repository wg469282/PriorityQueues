import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class PerformanceTest {

    private static final int SMALL_DATASET = 100;
    private static final int MEDIUM_DATASET = 1000;
    private static final int LARGE_DATASET = 5000;

    @Test
    @DisplayName("Benchmark czasu insert dla różnych implementacji")
    void benchmarkInsertPerformance() {
        Map<String, PriorityQueue<IntElement>> implementations = Map.of(
                "SortedStack", new SortedStackPriorityQueue<>(),
                "BST", new BSTPriorityQueue<>(),
                "Bucket", new BucketPriorityQueue<>());

        List<IntElement> elements = TestUtils.createRandomElements(MEDIUM_DATASET, PriorityQueue.N);

        for (Map.Entry<String, PriorityQueue<IntElement>> entry : implementations.entrySet()) {
            PriorityQueue<IntElement> pq = entry.getValue();
            String name = entry.getKey();

            long startTime = System.nanoTime();
            for (IntElement element : elements) {
                pq.insert(element);
            }
            long endTime = System.nanoTime();

            double duration = (endTime - startTime) / 1_000_000.0; // ms
            System.out.printf("%s insert (%d elements): %.2f ms%n",
                    name, MEDIUM_DATASET, duration);

            assertEquals(MEDIUM_DATASET, pq.size());
        }
    }

    @Test
    @DisplayName("Test z najgorszym przypadkiem dla BST")
    void testBSTWorstCase() {
        BSTPriorityQueue<IntElement> bst = new BSTPriorityQueue<>();

        // Wstaw elementy w porządku rosnącym - tworzy zdegenerowane drzewo
        List<IntElement> sortedElements = TestUtils.createSortedElements(SMALL_DATASET, true);

        long startTime = System.nanoTime();
        for (IntElement element : sortedElements) {
            bst.insert(element);
        }
        long endTime = System.nanoTime();

        double duration = (endTime - startTime) / 1_000_000.0;
        System.out.printf("BST worst case insert: %.2f ms%n", duration);

        // Nadal powinno działać poprawnie, tylko wolniej
        assertEquals(new IntElement(0), bst.findMin());
    }

    @Test
    @DisplayName("Test BucketQueue z koncentracją w kilku kubełkach")
    void testBucketQueueConcentration() {
        BucketPriorityQueue<IntElement> bucket = new BucketPriorityQueue<>();

        // Skoncentruj wszystkie elementy w kilku kubełkach
        Random random = new Random(42);
        List<IntElement> elements = new ArrayList<>();
        for (int i = 0; i < MEDIUM_DATASET; i++) {
            int value = random.nextInt(10); // Tylko wartości 0-9
            elements.add(new IntElement(value));
        }

        for (IntElement element : elements) {
            bucket.insert(element);
        }

        // Test extractMin - powinien działać efektywnie nawet z koncentracją
        long startTime = System.nanoTime();
        List<Integer> extracted = new ArrayList<>();
        while (!bucket.isEmpty()) {
            extracted.add(bucket.extractMin().wartość());
        }
        long endTime = System.nanoTime();

        double duration = (endTime - startTime) / 1_000_000.0;
        System.out.printf("BucketQueue concentrated extraction: %.2f ms%n", duration);

        // Sprawdź czy nadal posortowane
        for (int i = 1; i < extracted.size(); i++) {
            assertTrue(extracted.get(i) >= extracted.get(i - 1));
        }
    }

    @Test
    @DisplayName("Memory usage approximation")
    void testMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();

        for (String implName : Arrays.asList("SortedStack", "BST", "Bucket")) {
            runtime.gc(); // Sugestia garbage collection
            long beforeMemory = runtime.totalMemory() - runtime.freeMemory();

            PriorityQueue<IntElement> pq = switch (implName) {
                case "SortedStack" -> new SortedStackPriorityQueue<>();
                case "BST" -> new BSTPriorityQueue<>();
                case "Bucket" -> new BucketPriorityQueue<>();
                default -> throw new IllegalArgumentException();
            };

            List<IntElement> elements = TestUtils.createRandomElements(LARGE_DATASET, PriorityQueue.N);
            for (IntElement element : elements) {
                pq.insert(element);
            }

            runtime.gc();
            long afterMemory = runtime.totalMemory() - runtime.freeMemory();
            long usedMemory = afterMemory - beforeMemory;

            System.out.printf("%s memory usage (%d elements): %d KB%n",
                    implName, LARGE_DATASET, usedMemory / 1024);
        }
    }
}
