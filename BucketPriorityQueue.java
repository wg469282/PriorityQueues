import java.util.*;

public class BucketPriorityQueue implements PriorityQueue {
    private List<Integer>[] buckets;
    private int size;
    private int minBucket;

    @SuppressWarnings("unchecked")
    public BucketPriorityQueue() {
        this.buckets = new List[N + 1];
        for (int i = 0; i <= N; i++) {
            buckets[i] = new ArrayList<>();
        }
        this.size = 0;
        this.minBucket = N + 1;
    }

    // Asymptotyczna złożoność pesymistyczna: O(1), średnia: O(1)
    // Bezpośredni dostęp do kubełka przez indeks
    @Override
    public Position insert(int value) {
        if (value < 0 || value > N) {
            throw new IllegalArgumentException("Value must be in range [0, " + N + "]");
        }

        buckets[value].add(value);
        size++;

        if (value < minBucket) {
            minBucket = value;
        }

        return new BucketPosition(value, buckets[value].size() - 1);
    }

    // Asymptotyczna złożoność pesymistyczna: O(N), średnia: O(1)
    // W najgorszym przypadku musi przeszukać wszystkie kubełki
    @Override
    public int findMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        updateMinBucket();
        return minBucket;
    }

    private void updateMinBucket() {
        while (minBucket <= N && buckets[minBucket].isEmpty()) {
            minBucket++;
        }
    }

    // Asymptotyczna złożoność pesymistyczna: O(N), średnia: O(1)
    // Znajdź minimum i usuń jeden element
    @Override
    public int extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        updateMinBucket();
        int minValue = minBucket;
        buckets[minBucket].remove(buckets[minBucket].size() - 1);
        size--;

        if (buckets[minBucket].isEmpty()) {
            updateMinBucket();
        }

        return minValue;
    }

    // Asymptotyczna złożoność pesymistyczna: O(1), średnia: O(1)
    // Bezpośrednie usunięcie i wstawienie w odpowiednie kubełki
    @Override
    public void decreaseKey(Position position, int newValue) {
        if (!(position instanceof BucketPosition)) {
            throw new IllegalArgumentException("Invalid position type");
        }

        BucketPosition bucketPos = (BucketPosition) position;
        if (!bucketPos.isValid() || newValue < 0 || newValue > N) {
            throw new IllegalArgumentException("Invalid operation");
        }

        if (newValue >= bucketPos.getValue()) {
            throw new IllegalArgumentException("New value must be smaller");
        }

        // Usuń ze starego kubełka
        int oldValue = bucketPos.getValue();
        buckets[oldValue].remove(bucketPos.indexInBucket);
        bucketPos.valid = false;
        size--;

        // Dodaj do nowego kubełka
        insert(newValue);

        // Aktualizuj minBucket jeśli potrzeba
        if (newValue < minBucket) {
            minBucket = newValue;
        }
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(n)
    // Każdy element dodawany w czasie stałym
    public static PriorityQueue buildHeap(int[] elements) {
        BucketPriorityQueue pq = new BucketPriorityQueue();
        for (int element : elements) {
            pq.insert(element);
        }
        return pq;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n + m), średnia: O(n + m)
    // Kopiowanie wszystkich elementów z obu struktur
    @Override
    public PriorityQueue merge(PriorityQueue other) {
        BucketPriorityQueue result = new BucketPriorityQueue();

        // Dodaj wszystkie elementy z tej kolejki
        for (int i = 0; i <= N; i++) {
            for (int value : buckets[i]) {
                result.insert(value);
            }
        }

        // Dodaj wszystkie elementy z drugiej kolejki
        while (!other.isEmpty()) {
            result.insert(other.extractMin());
        }

        return result;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i <= N; i++) {
            buckets[i].clear();
        }
        size = 0;
        minBucket = N + 1;
    }

    private static class BucketPosition implements Position {
        int value;
        int indexInBucket;
        boolean valid;

        BucketPosition(int value, int indexInBucket) {
            this.value = value;
            this.indexInBucket = indexInBucket;
            this.valid = true;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public boolean isValid() {
            return valid;
        }
    }
}
