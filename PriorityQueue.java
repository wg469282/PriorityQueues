import java.util.NoSuchElementException;

/**
 * Generyczny interfejs kolejki priorytetowej dla elementów typu T
 * 
 * @param <T> typ elementów w kolejce, musi implementować Comparable i
 *            udostępniać metodę wartość()
 */
public interface PriorityQueue<T extends Comparable<T> & HasValue> {

    // Stała definiująca maksymalną wartość elementów w kolejce
    int N = 1000; // Łatwo zmieniana z poziomu interfejsu

    /**
     * Wstawia element do kolejki priorytetowej
     *
     * @param element element do wstawienia (element.wartość() musi być z przedziału
     *                [0, N])
     * @return pozycja elementu w kolejce (do operacji decreaseKey)
     * @throws IllegalArgumentException jeśli element.wartość() < 0 lub
     *                                  element.wartość() > N
     */
    Position<T> insert(T element);

    /**
     * Znajduje najmniejszy element w kolejce bez usuwania
     *
     * @return najmniejszy element w kolejce
     * @throws NoSuchElementException jeśli kolejka jest pusta
     */
    T findMin();

    /**
     * Usuwa i zwraca najmniejszy element z kolejki
     *
     * @return najmniejszy element z kolejki
     * @throws NoSuchElementException jeśli kolejka jest pusta
     */
    T extractMin();

    /**
     * Zmniejsza wartość elementu na określonej pozycji
     *
     * @param position   pozycja elementu zwrócona przez insert()
     * @param newElement nowy element (newElement.wartość() musi być mniejsza od
     *                   obecnej)
     * @throws IllegalArgumentException jeśli newElement.wartość() >= obecnej
     *                                  wartości
     *                                  lub newElement.wartość() < 0 lub
     *                                  newElement.wartość() > N
     */
    void decreaseKey(Position<T> position, T newElement);

    /**
     * Tworzy nową kolejkę priorytetową zawierającą wszystkie elementy z tablicy
     *
     * @param elements tablica elementów (każdy element.wartość() musi być z
     *                 przedziału [0, N])
     * @return nowa kolejka priorytetowa
     * @throws IllegalArgumentException jeśli którykolwiek element.wartość()
     *                                  wykracza poza [0, N]
     */
    static <T extends Comparable<T> & HasValue> PriorityQueue<T> buildHeap(T[] elements) {
        // Implementacja będzie w klasach implementujących
        throw new UnsupportedOperationException("Metoda musi być zaimplementowana w klasie konkretnej");
    }

    /**
     * Łączy dwie kolejki priorytetowe w jedną
     *
     * @param other druga kolejka do połączenia
     * @return nowa kolejka zawierająca elementy z obu kolejek
     */
    PriorityQueue<T> merge(PriorityQueue<T> other);

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
     * 
     * @param <T> typ elementu
     */
    interface Position<T> {

        /**
         * Zwraca element na tej pozycji
         *
         * @return element
         */
        T getElement();

        /**
         * Sprawdza czy pozycja jest nadal ważna
         *
         * @return true jeśli pozycja wskazuje na istniejący element
         */
        boolean isValid();
    }
}

/**
 * Interfejs dla elementów, które mogą być przechowywane w kolejce priorytetowej
 * Wymaga implementacji metody wartość() zwracającej liczbę z przedziału [0, N]
 */
interface HasValue {
    /**
     * Zwraca wartość liczbową elementu z przedziału [0, N]
     * 
     * @return wartość liczbowa elementu
     */
    int wartość();
}