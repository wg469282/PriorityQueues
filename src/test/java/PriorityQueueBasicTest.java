import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class PriorityQueueBasicTest {
    
    private List<PriorityQueue<IntElement>> implementations;
    
    @BeforeEach
    void setUp() {
        implementations = Arrays.asList(
            new SortedStackPriorityQueue<>(),
            new BSTPriorityQueue<>(),
            new BucketPriorityQueue<>()
        );
    }
    
    @Test
    @DisplayName("Nowa kolejka powinna być pusta")
    void testNewQueueIsEmpty() {
        for (PriorityQueue<IntElement> pq : implementations) {
            assertTrue(pq.isEmpty());
            assertEquals(0, pq.size());
        }
    }
    
    @Test
    @DisplayName("Insert pojedynczego elementu")
    void testInsertSingleElement() {
        IntElement element = new IntElement(42);
        
        for (PriorityQueue<IntElement> pq : implementations) {
            Position<IntElement> pos = pq.insert(element);
            
            assertFalse(pq.isEmpty());
            assertEquals(1, pq.size());
            assertNotNull(pos);
            assertTrue(pos.isValid());
            assertEquals(element, pos.getElement());
            assertEquals(element, pq.findMin());
        }
    }
    
    @Test
    @DisplayName("Insert i extract wielu elementów")
    void testInsertAndExtractMultipleElements() {
        IntElement[] elements = TestUtils.createIntElements(15, 3, 8, 1, 12, 5);
        
        for (PriorityQueue<IntElement> pq : implementations) {
            // Insert wszystkich elementów
            for (IntElement element : elements) {
                pq.insert(element);
            }
            
            assertEquals(elements.length, pq.size());
            
            // Extract w kolejności priorytetowej
            List<Integer> extractedValues = new ArrayList<>();
            while (!pq.isEmpty()) {
                extractedValues.add(pq.extractMin().wartość());
            }
            
            // Sprawdź czy wyciągnięte w porządku rosnącym
            List<Integer> expected = Arrays.asList(1, 3, 5, 8, 12, 15);
            assertEquals(expected, extractedValues);
        }
    }
    
    @Test
    @DisplayName("FindMin nie usuwa elementu")
    void testFindMinDoesNotRemove() {
        IntElement[] elements = TestUtils.createIntElements(10, 5, 15, 2);
        
        for (PriorityQueue<IntElement> pq : implementations) {
            for (IntElement element : elements) {
                pq.insert(element);
            }
            
            int originalSize = pq.size();
            IntElement min = pq.findMin();
            
            assertEquals(originalSize, pq.size());
            assertEquals(new IntElement(2), min);
            assertEquals(min, pq.findMin()); // Powinno zwracać ten sam element
        }
    }
    
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 50, 100})
    @DisplayName("Test z różnymi rozmiarami danych")
    void testDifferentDataSizes(int size) {
        List<IntElement> elements = TestUtils.createRandomElements(size, PriorityQueue.N);
        
        for (PriorityQueue<IntElement> pq : implementations) {
            elements.forEach(pq::insert);
            
            assertEquals(size, pq.size());
            assertTrue(TestUtils.isMinHeapProperty(pq));
        }
    }
    
    @Test
    @DisplayName("Clear opróżnia kolejkę")
    void testClear() {
        IntElement[] elements = TestUtils.createIntElements(1, 2, 3, 4, 5);
        
        for (PriorityQueue<IntElement> pq : implementations) {
            for (IntElement element : elements) {
                pq.insert(element);
            }
            
            assertFalse(pq.isEmpty());
            
            pq.clear();
            
            assertTrue(pq.isEmpty());
            assertEquals(0, pq.size());
        }
    }
}
