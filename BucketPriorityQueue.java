import java.util.*;

public class BucketPriorityQueue<T extends Comparable<T> & HasValue> implements PriorityQueue<T> {
    private List<T>[] buckets;
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
    public Position<T> insert(T element) {
        if (element.wartość() < 0 || element.wartość() > N) {
            throw new IllegalArgumentException("Element value must be in range [0, " + N + "]");
        }

        int bucketIndex = element.wartość();
        buckets[bucketIndex].add(element);
        size++;

        if (bucketIndex < minBucket) {
            minBucket = bucketIndex;
        }

        return new BucketPosition<>(element, buckets[bucketIndex].size() - 1);
    }

    // Asymptotyczna złożoność pesymistyczna: O(N), średnia: O(1)
    // W najgorszym przypadku musi przeszukać wszystkie kubełki
    @Override
    public T findMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        updateMinBucket();
        // Znajdź najmniejszy element w minBucket
        T minElement = buckets[minBucket].get(0);
        for (T element : buckets[minBucket]) {
            if (element.compareTo(minElement) < 0) {
                minElement = element;
            }
        }
        return minElement;
    }

    private void updateMinBucket() {
        while (minBucket <= N && buckets[minBucket].isEmpty()) {
            minBucket++;
        }
    }

    // Asymptotyczna złożoność pesymistyczna: O(N + k), średnia: O(1)
    // gdzie k to liczba elementów w kubełku minBucket
    @Override
    public T extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        updateMinBucket();

        // Znajdź najmniejszy element w minBucket
        T minElement = buckets[minBucket].get(0);
        int minIndex = 0;
        for (int i = 1; i < buckets[minBucket].size(); i++) {
            T element = buckets[minBucket].get(i);
            if (element.compareTo(minElement) < 0) {
                minElement = element;
                minIndex = i;
            }
        }

        buckets[minBucket].remove(minIndex);
        size--;

        if (buckets[minBucket].isEmpty()) {
            updateMinBucket();
        }

        return minElement;
    }

    // Asymptotyczna złożoność pesymistyczna: O(1), średnia: O(1)
    // Bezpośrednie usunięcie i wstawienie w odpowiednie kubełki
    @Override
    public void decreaseKey(Position<T> position, T newElement) {
        if (!(position instanceof BucketPosition)) {
            throw new IllegalArgumentException("Invalid position type");
        }

        @SuppressWarnings("unchecked")
        BucketPosition<T> bucketPos = (BucketPosition<T>) position;

        if (!bucketPos.isValid() || newElement.wartość() < 0 || newElement.wartość() > N) {
            throw new IllegalArgumentException("Invalid operation");
        }

        if (newElement.wartość() >= bucketPos.getElement().wartość()) {
            throw new IllegalArgumentException("New element value must be smaller");
        }

        // Usuń ze starego kubełka
        T oldElement = bucketPos.getElement();
        int oldBucketIndex = oldElement.wartość();
        buckets[oldBucketIndex].remove(bucketPos.indexInBucket);
        bucketPos.valid = false;
        size--;

        // Dodaj do nowego kubełka
        insert(newElement);

        // Aktualizuj minBucket jeśli potrzeba
        int newBucketIndex = newElement.wartość();
        if (newBucketIndex < minBucket) {
            minBucket = newBucketIndex;
        }
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(n)
    // Każdy element dodawany w czasie stałym
    public static <T extends Comparable<T> & HasValue> PriorityQueue<T> buildHeap(T[] elements) {
        BucketPriorityQueue<T> pq = new BucketPriorityQueue<>();
        for (T element : elements) {
            pq.insert(element);
        }
        return pq;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n + m), średnia: O(n + m)
    // Kopiowanie wszystkich elementów z obu struktur
    @Override
    public PriorityQueue<T> merge(PriorityQueue<T> other) {
        BucketPriorityQueue<T> result = new BucketPriorityQueue<>();

        // Dodaj wszystkie elementy z tej kolejki
        for (int i = 0; i <= N; i++) {
            for (T element : buckets[i]) {
                result.insert(element);
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

    private static class BucketPosition<T> implements Position<T> {
        T element;
        int indexInBucket;
        boolean valid;

        BucketPosition(T element, int indexInBucket) {
            this.element = element;
            this.indexInBucket = indexInBucket;
            this.valid = true;
        }

        @Override
        public T getElement() {
            return element;
        }

        @Override
        public boolean isValid() {
            return valid;
        }
    }
}