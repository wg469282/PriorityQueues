import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class PriorityQueueExceptionTest {

    private List<PriorityQueue<IntElement>> implementations;

    @BeforeEach
    void setUp() {
        implementations = Arrays.asList(
                new SortedStackPriorityQueue<>(),
                new BSTPriorityQueue<>(),
                new BucketPriorityQueue<>());
    }

    @Test
    @DisplayName("Insert element poza zakresem wartości")
    void testInsertOutOfRangeValue() {
        IntElement tooLarge = new IntElement(PriorityQueue.N + 1);
        IntElement tooSmall = new IntElement(-1);

        for (PriorityQueue<IntElement> pq : implementations) {
            assertThrows(IllegalArgumentException.class, () -> pq.insert(tooLarge));
            assertThrows(IllegalArgumentException.class, () -> pq.insert(tooSmall));
        }
    }

    @Test
    @DisplayName("FindMin na pustej kolejce")
    void testFindMinOnEmptyQueue() {
        for (PriorityQueue<IntElement> pq : implementations) {
            assertThrows(NoSuchElementException.class, pq::findMin);
        }
    }

    @Test
    @DisplayName("ExtractMin na pustej kolejce")
    void testExtractMinOnEmptyQueue() {
        for (PriorityQueue<IntElement> pq : implementations) {
            assertThrows(NoSuchElementException.class, pq::extractMin);
        }
    }

    @Test
    @DisplayName("DecreaseKey z nieprawidłową pozycją")
    void testDecreaseKeyInvalidPosition() {
        for (PriorityQueue<IntElement> pq : implementations) {
            IntElement element = new IntElement(10);
            pq.insert(element);

            // Próba użycia pozycji z innej implementacji
            PriorityQueue<IntElement> otherPq = new BSTPriorityQueue<>();
            Position<IntElement> wrongPosition = otherPq.insert(new IntElement(5));

            assertThrows(IllegalArgumentException.class,
                    () -> pq.decreaseKey(wrongPosition, new IntElement(3)));
        }
    }

    @Test
    @DisplayName("DecreaseKey z większą wartością")
    void testDecreaseKeyWithLargerValue() {
        for (PriorityQueue<IntElement> pq : implementations) {
            IntElement element = new IntElement(10);
            Position<IntElement> pos = pq.insert(element);

            IntElement largerElement = new IntElement(15);
            assertThrows(IllegalArgumentException.class,
                    () -> pq.decreaseKey(pos, largerElement));
        }
    }

    @Test
    @DisplayName("DecreaseKey z równą wartością")
    void testDecreaseKeyWithEqualValue() {
        for (PriorityQueue<IntElement> pq : implementations) {
            IntElement element = new IntElement(10);
            Position<IntElement> pos = pq.insert(element);

            IntElement equalElement = new IntElement(10);
            assertThrows(IllegalArgumentException.class,
                    () -> pq.decreaseKey(pos, equalElement));
        }
    }

    @Test
    @DisplayName("Użycie nieważnej pozycji po extract")
    void testInvalidPositionAfterExtract() {
        for (PriorityQueue<IntElement> pq : implementations) {
            IntElement element = new IntElement(5);
            Position<IntElement> pos = pq.insert(element);

            pq.extractMin(); // To powinno unieważnić pozycję

            assertThrows(IllegalArgumentException.class,
                    () -> pq.decreaseKey(pos, new IntElement(3)));
        }
    }
}
