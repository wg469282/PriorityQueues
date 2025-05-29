import java.util.*;

public class Main {

    public static int[] heapSort(int[] numbers, int pqType) {
        PriorityQueue pq;
        switch (pqType) {
            case 1:
                pq = new SortedStackPriorityQueue();
                break;
            case 2:
                pq = new BSTPriorityQueue();
                break;
            case 3:
                pq = new BucketPriorityQueue();
                break;
            default:
                throw new IllegalArgumentException("Nieprawidłowy typ kolejki");
        }

        // Wstaw wszystkie elementy do kolejki priorytetowej
        for (int number : numbers) {
            if (number < 0 || number > PriorityQueue.N) {
                throw new IllegalArgumentException(
                        "Liczba " + number + " jest poza zakresem [0, " + PriorityQueue.N + "]");
            }
            pq.insert(number);
        }

        // Wyciągnij elementy w posortowanej kolejności
        int[] sorted = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            sorted[i] = pq.extractMin();
        }

        return sorted;
    }

    /**
     * Sprawdza czy tablica jest posortowana rosnąco
     */
    public static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sprawdza czy dwie tablice zawierają te same elementy (niezależnie od
     * kolejności)
     */
    public static boolean sameElements(int[] array1, int[] array2) {
        if (array1.length != array2.length) {
            return false;
        }

        int[] sorted1 = array1.clone();
        int[] sorted2 = array2.clone();
        Arrays.sort(sorted1);
        Arrays.sort(sorted2);

        return Arrays.equals(sorted1, sorted2);
    }

    /**
     * Testuje wszystkie implementacje kolejek priorytetowych
     */
    public static void testAllImplementations(int[] originalData) {
        String[] implementationNames = {
                "Sorted Stack",
                "BST",
                "Bucket Queue"
        };

        System.out.println("Dane wejściowe: " + Arrays.toString(originalData));
        System.out.println("Liczba elementów: " + originalData.length);
        System.out.println();

        boolean allCorrect = true;
        int[][] results = new int[3][];

        // Testuj każdą implementację
        for (int i = 1; i <= 3; i++) {
            System.out.printf("Testowanie: %s\n", implementationNames[i - 1]);

            try {
                // Sortuj używając i-tej implementacji
                int[] sortedResult = heapSort(originalData.clone(), i);
                results[i - 1] = sortedResult;

                // Sprawdź poprawność
                boolean isCorrectlySorted = isSorted(sortedResult);
                boolean hasSameElements = sameElements(originalData, sortedResult);
                boolean isCorrect = isCorrectlySorted && hasSameElements;

                System.out.printf("  Wynik: %s\n", Arrays.toString(sortedResult));
                System.out.printf("  Posortowane poprawnie: %s\n", isCorrectlySorted ? "✓ TAK" : "✗ NIE");
                System.out.printf("  Zachowane wszystkie elementy: %s\n", hasSameElements ? "✓ TAK" : "✗ NIE");

                if (!isCorrect) {
                    allCorrect = false;
                }

            } catch (Exception e) {
                System.out.printf("  ✗ Błąd: %s\n", e.getMessage());
                allCorrect = false;
            }

            System.out.println();
        }

        // Sprawdź czy wszystkie implementacje dają ten sam wynik
        System.out.println("=".repeat(50));
        if (allCorrect && results[0] != null && results[1] != null && results[2] != null) {
            boolean allSame = Arrays.equals(results[0], results[1]) &&
                    Arrays.equals(results[1], results[2]);

            System.out.printf("Wszystkie implementacje dają identyczny wynik: %s\n",
                    allSame ? "✓ TAK" : "✗ NIE");

            if (allSame) {
                System.out.println("✓ Wszystkie implementacje działają poprawnie!");
            } else {
                System.out.println("✗ Implementacje dają różne wyniki!");
                System.out.println("Sorted Stack: " + Arrays.toString(results[0]));
                System.out.println("BST:          " + Arrays.toString(results[1]));
                System.out.println("Bucket Queue: " + Arrays.toString(results[2]));
            }
        } else {
            System.out.println("✗ Nie wszystkie implementacje działają poprawnie!");
        }
    }

    /**
     * Główna funkcja programu
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("SPRAWDZANIE POPRAWNOŚCI KOLEJEK PRIORYTETOWYCH");
            System.out.println("=".repeat(50));
            System.out.printf("Zakres dozwolonych wartości: [0, %d]\n", PriorityQueue.N);
            System.out.println();

            System.out.print("Podaj liczbę elementów: ");
            int n = scanner.nextInt();

            if (n <= 0) {
                System.out.println("Liczba elementów musi być większa od 0");
                return;
            }

            int[] numbers = new int[n];
            System.out.printf("Podaj %d liczb z zakresu [0, %d]:\n", n, PriorityQueue.N);

            for (int i = 0; i < n; i++) {
                numbers[i] = scanner.nextInt();
            }

            System.out.println();

            // Testuj wszystkie implementacje
            testAllImplementations(numbers);

        } catch (Exception e) {
            System.err.println("Błąd: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
