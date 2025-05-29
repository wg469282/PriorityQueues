import java.util.*;

public class SortedStackPriorityQueue implements PriorityQueue {
    private List<Integer> stack;
    private List<StackPosition> positions;

    public SortedStackPriorityQueue() {
        this.stack = new ArrayList<>();
        this.positions = new ArrayList<>();
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(n)
    // Musi przeszukać całą listę aby znaleźć odpowiednie miejsce
    @Override
    public Position insert(int value) {
        if (value < 0 || value > N) {
            throw new IllegalArgumentException("Value must be in range [0, " + N + "]");
        }

        int insertIndex = 0;
        while (insertIndex < stack.size() && stack.get(insertIndex) <= value) {
            insertIndex++;
        }

        stack.add(insertIndex, value);
        StackPosition position = new StackPosition(insertIndex, value);
        positions.add(position);

        // Aktualizuj indeksy w istniejących pozycjach
        for (StackPosition pos : positions) {
            if (pos.index >= insertIndex && pos != position) {
                pos.index++;
            }
        }

        return position;
    }

    // Asymptotyczna złożoność pesymistyczna: O(1), średnia: O(1)
    // Pierwszy element jest zawsze najmniejszy
    @Override
    public int findMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return stack.get(0);
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(n)
    // Usunięcie wymaga przesunięcia wszystkich elementów
    @Override
    public int extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        int min = stack.remove(0);

        // Aktualizuj indeksy w pozycjach
        for (StackPosition pos : positions) {
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
    public void decreaseKey(Position position, int newValue) {
        if (!(position instanceof StackPosition)) {
            throw new IllegalArgumentException("Invalid position type");
        }

        StackPosition stackPos = (StackPosition) position;
        if (!stackPos.isValid() || newValue < 0 || newValue > N) {
            throw new IllegalArgumentException("Invalid operation");
        }

        if (newValue >= stackPos.getValue()) {
            throw new IllegalArgumentException("New value must be smaller");
        }

        // Usuń stary element i wstaw nowy
        stack.remove(stackPos.index);
        stackPos.valid = false;
        insert(newValue);
    }

    // Asymptotyczna złożoność pesymistyczna: O(n²), średnia: O(n²)
    // Dla każdego elementu wykonuje insert O(n)
    public static PriorityQueue buildHeap(int[] elements) {
        SortedStackPriorityQueue pq = new SortedStackPriorityQueue();
        for (int element : elements) {
            pq.insert(element);
        }
        return pq;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n*m), średnia: O(n*m)
    // gdzie n i m to rozmiary kolejek
    @Override
    public PriorityQueue merge(PriorityQueue other) {
        SortedStackPriorityQueue result = new SortedStackPriorityQueue();

        // Dodaj wszystkie elementy z tej kolejki
        for (int value : this.stack) {
            result.insert(value);
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

    private static class StackPosition implements Position {
        int index;
        int value;
        boolean valid;

        StackPosition(int index, int value) {
            this.index = index;
            this.value = value;
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
