import java.util.*;
import java.util.stream.IntStream;

public class TestUtils {
    
    public static IntElement[] createIntElements(int... values) {
        return Arrays.stream(values)
                .mapToObj(IntElement::new)
                .toArray(IntElement[]::new);
    }
    
    public static List<IntElement> createRandomElements(int count, int maxValue) {
        Random random = new Random(42); // Seed dla powtarzalności
        return IntStream.range(0, count)
                .map(i -> random.nextInt(maxValue + 1))
                .mapToObj(IntElement::new)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    public static List<IntElement> createSortedElements(int count, boolean ascending) {
        List<IntElement> elements = IntStream.range(0, count)
                .mapToObj(IntElement::new)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        if (!ascending) Collections.reverse(elements);
        return elements;
    }
    
    public static boolean isMinHeapProperty(PriorityQueue<IntElement> pq) {
        List<IntElement> extracted = new ArrayList<>();
        PriorityQueue<IntElement> copy = null; // Implementuj kopiowanie jeśli potrzebne
        
        while (!pq.isEmpty()) {
            extracted.add(pq.extractMin());
        }
        
        for (int i = 1; i < extracted.size(); i++) {
            if (extracted.get(i).wartość() < extracted.get(i-1).wartość()) {
                return false;
            }
        }
        return true;
    }
}
