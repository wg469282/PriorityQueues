import java.util.*;

public class SortedStackPriorityQueue<T extends Comparable<T> & HasValue> implements PriorityQueue<T> {
    private List<T> stack;
    private List<StackPosition<T>> positions;

    public SortedStackPriorityQueue() {
        this.stack = new ArrayList<>();
        this.positions = new ArrayList<>();
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(n)
    // Musi przeszukać całą listę aby znaleźć odpowiednie miejsce
    @Override
    public Position<T> insert(T element) {
        if (element.wartość() < 0 || element.wartość() > N) {
            throw new IllegalArgumentException("Element value must be in range [0, " + N + "]");
        }

        int insertIndex = 0;
        // Sortujemy według wartości z metody wartość()
        while (insertIndex < stack.size() && stack.get(insertIndex).wartość() <= element.wartość()) {
            insertIndex++;
        }

        stack.add(insertIndex, element);
        StackPosition<T> position = new StackPosition<>(insertIndex, element);
        positions.add(position);

        // Aktualizuj indeksy w istniejących pozycjach
        for (StackPosition<T> pos : positions) {
            if (pos.index >= insertIndex && pos != position) {
                pos.index++;
            }
        }

        return position;
    }

    // Asymptotyczna złożoność pesymistyczna: O(1), średnia: O(1)
    // Pierwszy element jest zawsze najmniejszy
    @Override
    public T findMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return stack.get(0);
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(n)
    // Usunięcie wymaga przesunięcia wszystkich elementów
    @Override
    public T extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        T min = stack.remove(0);

        // Aktualizuj indeksy w pozycjach
        for (StackPosition<T> pos : positions) {
            if (pos.index > 0) {
                pos.index--;
            } else if (pos.index == 0) {
                pos.valid = false;
            }
        }

        return min;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(n)
    // Może wymagać przesunięcia elementu w posortowanej strukturze
    @Override
    public void decreaseKey(Position<T> position, T newElement) {
        if (!(position instanceof StackPosition)) {
            throw new IllegalArgumentException("Invalid position type");
        }

        @SuppressWarnings("unchecked")
        StackPosition<T> stackPos = (StackPosition<T>) position;

        if (!stackPos.isValid() || newElement.wartość() < 0 || newElement.wartość() > N) {
            throw new IllegalArgumentException("Invalid operation");
        }

        if (newElement.wartość() >= stackPos.getElement().wartość()) {
            throw new IllegalArgumentException("New element value must be smaller than current");
        }

        // Usuń stary element i wstaw nowy
        stack.remove(stackPos.index);
        stackPos.valid = false;
        insert(newElement);
    }

    // Asymptotyczna złożoność pesymistyczna: O(n²), średnia: O(n²)
    // Dla każdego elementu wykonuje insert O(n)
    public static <T extends Comparable<T> & HasValue> PriorityQueue<T> buildHeap(T[] elements) {
        SortedStackPriorityQueue<T> pq = new SortedStackPriorityQueue<>();
        for (T element : elements) {
            pq.insert(element);
        }
        return pq;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n*m), średnia: O(n*m)
    // gdzie n i m to rozmiary kolejek
    @Override
    public PriorityQueue<T> merge(PriorityQueue<T> other) {
        SortedStackPriorityQueue<T> result = new SortedStackPriorityQueue<>();

        // Dodaj wszystkie elementy z tej kolejki
        for (T element : this.stack) {
            result.insert(element);
        }

        // Dodaj wszystkie elementy z drugiej kolejki
        while (!other.isEmpty()) {
            result.insert(other.extractMin());
        }

        return result;
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public void clear() {
        stack.clear();
        positions.clear();
    }

    private static class StackPosition<T> implements Position<T> {
        int index;
        T element;
        boolean valid;

        StackPosition(int index, T element) {
            this.index = index;
            this.element = element;
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