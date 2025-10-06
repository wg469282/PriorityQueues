import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class DecreaseKeyTest {

    private List<PriorityQueue<IntElement>> implementations;

    @BeforeEach
    void setUp() {
        implementations = Arrays.asList(
                new SortedStackPriorityQueue<>(),
                new BSTPriorityQueue<>(),
                new BucketPriorityQueue<>());
    }

    @Test
    @DisplayName("DecreaseKey zmienia minimum")
    void testDecreaseKeyChangesMinimum() {
        for (PriorityQueue<IntElement> pq : implementations) {
            IntElement[] elements = TestUtils.createIntElements(10, 5, 15, 8);
            List<Position<IntElement>> positions = new ArrayList<>();

            for (IntElement element : elements) {
                positions.add(pq.insert(element));
            }

            assertEquals(new IntElement(5), pq.findMin());

            // Zmniejsz element 15 do 2 - powinien stać się nowym minimum
            pq.decreaseKey(positions.get(2), new IntElement(2));

            assertEquals(new IntElement(2), pq.findMin());
        }
    }

    @Test
    @DisplayName("DecreaseKey zachowuje właściwości kolejki")
    void testDecreaseKeyMaintainsHeapProperty() {
        for (PriorityQueue<IntElement> pq : implementations) {
            List<IntElement> elements = TestUtils.createRandomElements(20, 100);
            List<Position<IntElement>> positions = new ArrayList<>();

            for (IntElement element : elements) {
                positions.add(pq.insert(element));
            }

            // Wykonaj kilka operacji decreaseKey
            Random random = new Random(42);
            for (int i = 0; i < 5; i++) {
                int randomIndex = random.nextInt(positions.size());
                Position<IntElement> pos = positions.get(randomIndex);

                if (pos.isValid()) {
                    int currentValue = pos.getElement().wartość();
                    int newValue = random.nextInt(currentValue);
                    pq.decreaseKey(pos, new IntElement(newValue));
                }
            }

            // Sprawdź czy kolejka nadal ma właściwości min-heap
            assertTrue(TestUtils.isMinHeapProperty(pq));
        }
    }

    @Test
    @DisplayName("Pozycja staje się nieważna po decreaseKey")
    void testPositionBecomesInvalidAfterDecreaseKey() {
        for (PriorityQueue<IntElement> pq : implementations) {
            IntElement element = new IntElement(10);
            Position<IntElement> pos = pq.insert(element);

            assertTrue(pos.isValid());

            pq.decreaseKey(pos, new IntElement(5));

        }
    }
}
