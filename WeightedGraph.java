import java.util.*;

/**
 * Implementacja grafu ważonego z ograniczeniami:
 * - Maksymalnie M wierzchołków
 * - Wagi krawędzi z przedziału [0, K]
 */
public class WeightedGraph {

    // Stałe definiujące ograniczenia grafu
    public static final int M = 1000; // Maksymalna liczba wierzchołków
    public static final int K = 100; // Maksymalna waga krawędzi

    private final int maxVertices;
    private final int maxWeight;
    private final List<List<Edge>> adjacencyList;
    private final Set<Integer> vertices;
    private int edgeCount;

    /**
     * Konstruktor z domyślnymi ograniczeniami
     */
    public WeightedGraph() {
        this(M, K);
    }

    /**
     * Konstruktor z niestandardowymi ograniczeniami
     * 
     * @param maxVertices maksymalna liczba wierzchołków
     * @param maxWeight   maksymalna waga krawędzi
     */
    public WeightedGraph(int maxVertices, int maxWeight) {
        if (maxVertices <= 0 || maxWeight < 0) {
            throw new IllegalArgumentException("Nieprawidłowe ograniczenia grafu");
        }

        this.maxVertices = maxVertices;
        this.maxWeight = maxWeight;
        this.adjacencyList = new ArrayList<>(maxVertices);
        this.vertices = new HashSet<>();
        this.edgeCount = 0;

        // Inicjalizuj listy sąsiedztwa
        for (int i = 0; i < maxVertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    /**
     * Klasa reprezentująca krawędź grafu
     */
    public static class Edge {
        private final int destination;
        private final int weight;

        public Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }

        public int getDestination() {
            return destination;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            Edge edge = (Edge) obj;
            return destination == edge.destination && weight == edge.weight;
        }

        @Override
        public int hashCode() {
            return Objects.hash(destination, weight);
        }

        @Override
        public String toString() {
            return String.format("→%d(w:%d)", destination, weight);
        }
    }

    /**
     * Dodaje wierzchołek do grafu
     * 
     * @param vertex numer wierzchołka [0, M-1]
     * @throws IllegalArgumentException jeśli wierzchołek jest poza zakresem
     */
    public void addVertex(int vertex) {
        validateVertex(vertex);
        vertices.add(vertex);
    }

    /**
     * Dodaje krawędź skierowaną do grafu
     * 
     * @param source      wierzchołek początkowy
     * @param destination wierzchołek końcowy
     * @param weight      waga krawędzi [0, K]
     * @throws IllegalArgumentException jeśli parametry są nieprawidłowe
     */
    public void addEdge(int source, int destination, int weight) {
        validateVertex(source);
        validateVertex(destination);
        validateWeight(weight);

        // Dodaj wierzchołki jeśli nie istnieją
        addVertex(source);
        addVertex(destination);

        // Sprawdź czy krawędź już istnieje
        if (!hasEdge(source, destination)) {
            adjacencyList.get(source).add(new Edge(destination, weight));
            edgeCount++;
        } else {
            // Aktualizuj wagę istniejącej krawędzi
            updateEdgeWeight(source, destination, weight);
        }
    }

    /**
     * Dodaje krawędź nieskierowaną (dwukierunkową)
     * 
     * @param vertex1 pierwszy wierzchołek
     * @param vertex2 drugi wierzchołek
     * @param weight  waga krawędzi
     */
    public void addUndirectedEdge(int vertex1, int vertex2, int weight) {
        addEdge(vertex1, vertex2, weight);
        if (vertex1 != vertex2) { // Unikaj podwójnych pętli
            addEdge(vertex2, vertex1, weight);
        }
    }

    /**
     * Sprawdza czy istnieje krawędź między wierzchołkami
     * 
     * @param source      wierzchołek początkowy
     * @param destination wierzchołek końcowy
     * @return true jeśli krawędź istnieje
     */
    public boolean hasEdge(int source, int destination) {
        validateVertex(source);
        validateVertex(destination);

        return adjacencyList.get(source).stream()
                .anyMatch(edge -> edge.getDestination() == destination);
    }

    /**
     * Zwraca wagę krawędzi między wierzchołkami
     * 
     * @param source      wierzchołek początkowy
     * @param destination wierzchołek końcowy
     * @return waga krawędzi lub -1 jeśli nie istnieje
     */
    public int getEdgeWeight(int source, int destination) {
        validateVertex(source);
        validateVertex(destination);

        return adjacencyList.get(source).stream()
                .filter(edge -> edge.getDestination() == destination)
                .mapToInt(Edge::getWeight)
                .findFirst()
                .orElse(-1);
    }

    /**
     * Aktualizuje wagę istniejącej krawędzi
     * 
     * @param source      wierzchołek początkowy
     * @param destination wierzchołek końcowy
     * @param newWeight   nowa waga
     */
    public void updateEdgeWeight(int source, int destination, int newWeight) {
        validateVertex(source);
        validateVertex(destination);
        validateWeight(newWeight);

        List<Edge> edges = adjacencyList.get(source);
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getDestination() == destination) {
                edges.set(i, new Edge(destination, newWeight));
                return;
            }
        }
        throw new IllegalArgumentException("Krawędź nie istnieje");
    }

    /**
     * Usuwa krawędź z grafu
     * 
     * @param source      wierzchołek początkowy
     * @param destination wierzchołek końcowy
     * @return true jeśli krawędź została usunięta
     */
    public boolean removeEdge(int source, int destination) {
        validateVertex(source);
        validateVertex(destination);

        List<Edge> edges = adjacencyList.get(source);
        boolean removed = edges.removeIf(edge -> edge.getDestination() == destination);
        if (removed) {
            edgeCount--;
        }
        return removed;
    }

    /**
     * Zwraca listę sąsiadów wierzchołka
     * 
     * @param vertex wierzchołek
     * @return lista krawędzi wychodzących
     */
    public List<Edge> getNeighbors(int vertex) {
        validateVertex(vertex);
        return new ArrayList<>(adjacencyList.get(vertex));
    }

    /**
     * Zwraca wszystkie wierzchołki grafu
     * 
     * @return zbiór wierzchołków
     */
    public Set<Integer> getVertices() {
        return new HashSet<>(vertices);
    }

    /**
     * Zwraca liczbę wierzchołków
     * 
     * @return liczba wierzchołków
     */
    public int getVertexCount() {
        return vertices.size();
    }

    /**
     * Zwraca liczbę krawędzi
     * 
     * @return liczba krawędzi
     */
    public int getEdgeCount() {
        return edgeCount;
    }

    /**
     * Sprawdza czy graf jest pusty
     * 
     * @return true jeśli nie ma wierzchołków
     */
    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    /**
     * Czyści graf ze wszystkich wierzchołków i krawędzi
     */
    public void clear() {
        vertices.clear();
        edgeCount = 0;
        for (List<Edge> edges : adjacencyList) {
            edges.clear();
        }
    }

    /**
     * Zwraca reprezentację grafu jako macierz sąsiedztwa
     * 
     * @return macierz wag (-1 oznacza brak krawędzi)
     */
    public int[][] getAdjacencyMatrix() {
        int maxVertex = vertices.stream().mapToInt(Integer::intValue).max().orElse(-1);
        if (maxVertex == -1)
            return new int[0][0];

        int[][] matrix = new int[maxVertex + 1][maxVertex + 1];

        // Inicjalizuj -1 (brak krawędzi)
        for (int i = 0; i <= maxVertex; i++) {
            Arrays.fill(matrix[i], -1);
            matrix[i][i] = 0; // Pętla ma wagę 0 lub nie istnieje
        }

        // Wypełnij wagami
        for (int vertex : vertices) {
            for (Edge edge : adjacencyList.get(vertex)) {
                matrix[vertex][edge.getDestination()] = edge.getWeight();
            }
        }

        return matrix;
    }

    /**
     * Sprawdza poprawność numeru wierzchołka
     */
    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= maxVertices) {
            throw new IllegalArgumentException(
                    String.format("Wierzchołek %d musi być z zakresu [0, %d]", vertex, maxVertices - 1));
        }
    }

    /**
     * Sprawdza poprawność wagi krawędzi
     */
    private void validateWeight(int weight) {
        if (weight < 0 || weight > maxWeight) {
            throw new IllegalArgumentException(
                    String.format("Waga %d musi być z zakresu [0, %d]", weight, maxWeight));
        }
    }

    /**
     * Klasa reprezentująca element w kolejce priorytetowej dla algorytmu Dijkstry
     */
    public static class DijkstraNode implements Comparable<DijkstraNode>, HasValue {
        private final int vertex;
        private final int distance;

        public DijkstraNode(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        public int getVertex() {
            return vertex;
        }

        public int getDistance() {
            return distance;
        }

        @Override
        public int wartość() {
            return distance;
        }

        @Override
        public int compareTo(DijkstraNode other) {
            return Integer.compare(this.distance, other.distance);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            DijkstraNode that = (DijkstraNode) obj;
            return vertex == that.vertex && distance == that.distance;
        }

        @Override
        public int hashCode() {
            return Objects.hash(vertex, distance);
        }
    }

    /**
     * Klasa reprezentująca wynik algorytmu Dijkstry
     */
    public static class DijkstraResult {
        private final int source;
        private final Map<Integer, Integer> distances;
        private final Map<Integer, Integer> predecessors;

        public DijkstraResult(int source, Map<Integer, Integer> distances, Map<Integer, Integer> predecessors) {
            this.source = source;
            this.distances = new HashMap<>(distances);
            this.predecessors = new HashMap<>(predecessors);
        }

        /**
         * Zwraca najkrótszą odległość do wierzchołka
         * 
         * @param vertex wierzchołek docelowy
         * @return odległość lub Integer.MAX_VALUE jeśli nieosiągalny
         */
        public int getDistance(int vertex) {
            return distances.getOrDefault(vertex, Integer.MAX_VALUE);
        }

        /**
         * Sprawdza czy wierzchołek jest osiągalny
         * 
         * @param vertex wierzchołek
         * @return true jeśli osiągalny
         */
        public boolean isReachable(int vertex) {
            return distances.containsKey(vertex) && distances.get(vertex) != Integer.MAX_VALUE;
        }

        /**
         * Zwraca najkrótszą ścieżkę do wierzchołka
         * 
         * @param vertex wierzchołek docelowy
         * @return lista wierzchołków na ścieżce (lub pusta jeśli nieosiągalny)
         */
        public List<Integer> getPath(int vertex) {
            if (!isReachable(vertex)) {
                return new ArrayList<>();
            }

            List<Integer> path = new ArrayList<>();
            int current = vertex;

            while (current != -1) {
                path.add(0, current);
                current = predecessors.getOrDefault(current, -1);
            }

            return path;
        }

        /**
         * Zwraca wszystkie osiągalne wierzchołki z odległościami
         * 
         * @return mapa wierzchołek -> odległość
         */
        public Map<Integer, Integer> getAllDistances() {
            return new HashMap<>(distances);
        }

        public int getSource() {
            return source;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Dijkstra z wierzchołka %d:\n", source));

            List<Integer> sortedVertices = new ArrayList<>(distances.keySet());
            Collections.sort(sortedVertices);

            for (int vertex : sortedVertices) {
                int dist = distances.get(vertex);
                if (dist == Integer.MAX_VALUE) {
                    sb.append(String.format("  %d: nieosiągalny\n", vertex));
                } else {
                    List<Integer> path = getPath(vertex);
                    sb.append(String.format("  %d: odległość=%d, ścieżka=%s\n",
                            vertex, dist, path));
                }
            }

            return sb.toString();
        }
    }

    /**
     * Implementacja algorytmu Dijkstry z użyciem kolejki priorytetowej
     * Znajduje najkrótsze ścieżki z wierzchołka źródłowego do wszystkich innych
     * 
     * @param source            wierzchołek źródłowy
     * @param priorityQueueType typ kolejki priorytetowej (1=SortedStack, 2=BST,
     *                          3=Bucket)
     * @return wynik algorytmu Dijkstry
     * @throws IllegalArgumentException jeśli wierzchołek źródłowy nie istnieje
     */
    public DijkstraResult dijkstra(int source, int priorityQueueType) {
        validateVertex(source);

        if (!vertices.contains(source)) {
            throw new IllegalArgumentException("Wierzchołek źródłowy nie istnieje w grafie");
        }

        // Inicjalizacja struktur danych
        Map<Integer, Integer> distances = new HashMap<>();
        Map<Integer, Integer> predecessors = new HashMap<>();
        Set<Integer> visited = new HashSet<>();

        // Utworz kolejkę priorytetową
        PriorityQueue<DijkstraNode> pq;
        switch (priorityQueueType) {
            case 1:
                pq = new SortedStackPriorityQueue<>();
                break;
            case 2:
                pq = new BSTPriorityQueue<>();
                break;
            case 3:
                pq = new BucketPriorityQueue<>();
                break;
            default:
                throw new IllegalArgumentException("Nieprawidłowy typ kolejki priorytetowej");
        }

        // Inicjalizuj odległości
        for (int vertex : vertices) {
            distances.put(vertex, Integer.MAX_VALUE);
            predecessors.put(vertex, -1);
        }
        distances.put(source, 0);

        // Dodaj wierzchołek źródłowy do kolejki
        pq.insert(new DijkstraNode(source, 0));

        // Główna pętla algorytmu
        while (!pq.isEmpty()) {
            DijkstraNode current = pq.extractMin();
            int currentVertex = current.getVertex();

            // Pomiń jeśli już odwiedzony
            if (visited.contains(currentVertex)) {
                continue;
            }

            visited.add(currentVertex);

            // Sprawdź wszystkich sąsiadów
            for (Edge edge : adjacencyList.get(currentVertex)) {
                int neighbor = edge.getDestination();
                int weight = edge.getWeight();

                // Relaksacja krawędzi
                int newDistance = distances.get(currentVertex) + weight;

                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    predecessors.put(neighbor, currentVertex);

                    // Dodaj do kolejki tylko jeśli nie był jeszcze odwiedzony
                    if (!visited.contains(neighbor)) {
                        pq.insert(new DijkstraNode(neighbor, newDistance));
                    }
                }
            }
        }

        return new DijkstraResult(source, distances, predecessors);
    }

    /**
     * Wersja algorytmu Dijkstry z domyślną kolejką priorytetową (SortedStack)
     * 
     * @param source wierzchołek źródłowy
     * @return wynik algorytmu Dijkstry
     */
    public DijkstraResult dijkstra(int source) {
        return dijkstra(source, 1); // Domyślnie SortedStackPriorityQueue
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Graf pusty";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Graf (%d wierzchołków, %d krawędzi):\n",
                getVertexCount(), getEdgeCount()));

        List<Integer> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices);

        for (int vertex : sortedVertices) {
            sb.append(String.format("  %d: %s\n", vertex, adjacencyList.get(vertex)));
        }

        return sb.toString();
    }
}