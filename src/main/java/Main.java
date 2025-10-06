import java.util.*;

public class Main {

    // Pomocnicza klasa wrappująca int aby implementować HasValue
    static class IntElement implements Comparable<IntElement>, HasValue {
        private final int value;

        public IntElement(int value) {
            this.value = value;
        }

        @Override
        public int wartość() {
            return value;
        }

        @Override
        public int compareTo(IntElement other) {
            return Integer.compare(this.value, other.value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            IntElement that = (IntElement) obj;
            return value == that.value;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(value);
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public int getValue() {
            return value;
        }
    }

    public static int[] heapSort(int[] numbers, int pqType) {
        PriorityQueue<IntElement> pq;
        switch (pqType) {
            case 1:
                pq = new SortedStackPriorityQueue<>();
                break;
            case 2:
                pq = new BSTPriorityQueue<>();
                break; // Dodano brakujące break
            case 3:
                pq = new BucketPriorityQueue<>();
                break; // Dodano brakujące break
            default:
                throw new IllegalArgumentException("Nieprawidłowy typ kolejki");
        }

        // Wstaw wszystkie elementy do kolejki priorytetowej
        for (int number : numbers) {
            if (number < 0 || number > PriorityQueue.N) {
                throw new IllegalArgumentException(
                        "Liczba " + number + " jest poza zakresem [0, " + PriorityQueue.N + "]");
            }
            pq.insert(new IntElement(number));
        }

        // Wyciągnij elementy w posortowanej kolejności
        int[] sorted = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            sorted[i] = pq.extractMin().getValue();
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
        int workingImplementations = 0;

        // Testuj każdą implementację
        for (int i = 1; i <= 3; i++) {
            System.out.printf("Testowanie: %s\n", implementationNames[i - 1]);

            try {
                // Sortuj używając i-tej implementacji
                int[] sortedResult = heapSort(originalData.clone(), i);
                results[i - 1] = sortedResult;
                workingImplementations++;

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

            } catch (UnsupportedOperationException e) {
                System.out.printf("  ⚠ Nie zaimplementowane: %s\n", e.getMessage());
            } catch (Exception e) {
                System.out.printf("  ✗ Błąd: %s\n", e.getMessage());
                allCorrect = false;
            }

            System.out.println();
        }

        // Sprawdź czy wszystkie działające implementacje dają ten sam wynik
        System.out.println("=".repeat(50));
        if (workingImplementations > 1) {
            List<int[]> workingResults = new ArrayList<>();
            for (int[] result : results) {
                if (result != null) {
                    workingResults.add(result);
                }
            }

            boolean allSame = true;
            for (int i = 1; i < workingResults.size(); i++) {
                if (!Arrays.equals(workingResults.get(0), workingResults.get(i))) {
                    allSame = false;
                    break;
                }
            }

            System.out.printf("Wszystkie działające implementacje dają identyczny wynik: %s\n",
                    allSame ? "✓ TAK" : "✗ NIE");

            if (allSame && allCorrect) {
                System.out.println("✓ Wszystkie działające implementacje działają poprawnie!");
            } else {
                System.out.println("✗ Znaleziono problemy z implementacjami!");
            }
        } else if (workingImplementations == 1) {
            if (allCorrect) {
                System.out.println("✓ Jedyna działająca implementacja (Sorted Stack) działa poprawnie!");
            } else {
                System.out.println("✗ Implementacja Sorted Stack ma błędy!");
            }
        } else {
            System.out.println("✗ Żadna implementacja nie działa!");
        }
    }

    public static void runDijkstra() {
        Scanner scanner = new Scanner(System.in);

        try {
            // Tworzenie grafu
            WeightedGraph graph = new WeightedGraph();

            System.out.println("Podaj liczbę wierzchołków:");
            int vertexCount = scanner.nextInt();

            System.out.println("Podaj liczbę krawędzi:");
            int edgeCount = scanner.nextInt();

            System.out.println("Dodawanie krawędzi (format: źródło cel waga):");
            for (int i = 0; i < edgeCount; i++) {
                int source = scanner.nextInt();
                int destination = scanner.nextInt();
                int weight = scanner.nextInt();
                graph.addEdge(source, destination, weight);
            }

            System.out.println("Podaj wierzchołek początkowy dla algorytmu Dijkstry:");
            int source = scanner.nextInt();

            System.out.println("Wybierz typ kolejki priorytetowej (1=SortedStack, 2=BST, 3=Bucket):");
            int priorityQueueType = scanner.nextInt();

            // Uruchomienie algorytmu Dijkstry
            WeightedGraph.DijkstraResult result = graph.dijkstra(source, priorityQueueType);

            // Wyświetlenie wyników
            System.out.println(result);

        } catch (Exception e) {
            System.err.println("Błąd: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    /**
     * Główna funkcja programu
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Wybierz opcję:");
            System.out.println("1. Testowanie kolejek priorytetowych");
            System.out.println("2. Algorytm Dijkstry");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Testowanie kolejek priorytetowych
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
                    testAllImplementations(numbers);
                    break;

                case 2:
                    // Algorytm Dijkstry
                    runDijkstra();
                    break;

                default:
                    System.out.println("Nieprawidłowy wybór.");
            }
        } catch (Exception e) {
            System.err.println("Błąd: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}