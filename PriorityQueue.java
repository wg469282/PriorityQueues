public interface PriorityQueue {
    // Stała definiująca maksymalną wartość elementów w kolejce
    int N = 1000; // Łatwo zmieniana z poziomu interfejsu

    /**
     * Wstawia element do kolejki priorytetowej
     * 
     * @param value wartość do wstawienia z przedziału [0, N]
     * @return pozycja elementu w kolejce (do operacji decreaseKey)
     * @throws IllegalArgumentException jeśli value < 0 lub value > N
     */
    Position insert(int value);

    /**
     * Znajduje najmniejszy element w kolejce bez usuwania
     * 
     * @return najmniejsza wartość w kolejce
     * @throws NoSuchElementException jeśli kolejka jest pusta
     */
    int findMin();

    /**
     * Usuwa i zwraca najmniejszy element z kolejki
     * 
     * @return najmniejsza wartość w kolejce
     * @throws NoSuchElementException jeśli kolejka jest pusta
     */
    int extractMin();

    /**
     * Zmniejsza wartość elementu na określonej pozycji
     * 
     * @param position pozycja elementu zwrócona przez insert()
     * @param newValue nowa wartość (musi być mniejsza od obecnej)
     * @throws IllegalArgumentException jeśli newValue >= obecnej wartości
     *                                  lub newValue < 0 lub newValue > N
     */
    void decreaseKey(Position position, int newValue);

    /**
     * Tworzy nową kolejkę priorytetową zawierającą wszystkie elementy z tablicy
     * 
     * @param elements tablica elementów z przedziału [0, N]
     * @return nowa kolejka priorytetowa
     * @throws IllegalArgumentException jeśli którykolwiek element wykracza poza [0,
     *                                  N]
     */
    static PriorityQueue buildHeap(int[] elements) {
        // Implementacja będzie w klasach implementujących
        throw new UnsupportedOperationException("Metoda musi być zaimplementowana w klasie konkretnej");
    }

    /**
     * Łączy dwie kolejki priorytetowe w jedną
     * 
     * @param other druga kolejka do połączenia
     * @return nowa kolejka zawierająca elementy z obu kolejek
     */
    PriorityQueue merge(PriorityQueue other);

    /**
     * Sprawdza czy kolejka jest pusta
     * 
     * @return true jeśli kolejka nie zawiera elementów
     */
    boolean isEmpty();

    /**
     * Zwraca liczbę elementów w kolejce
     * 
     * @return rozmiar kolejki
     */
    int size();

    /**
     * Czyści kolejkę ze wszystkich elementów
     */
    void clear();

    /**
     * Interfejs reprezentujący pozycję elementu w kolejce
     * Używany do operacji decreaseKey
     */
    interface Position {
        /**
         * Zwraca wartość elementu na tej pozycji
         * 
         * @return wartość elementu
         */
        int getValue();

        /**
         * Sprawdza czy pozycja jest nadal ważna
         * 
         * @return true jeśli pozycja wskazuje na istniejący element
         */
        boolean isValid();
    }
}

