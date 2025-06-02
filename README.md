# Projekt: Implementacja Kolejek Priorytetowych i Algorytmu Dijkstry

## Opis projektu
Projekt zawiera implementację różnych struktur danych kolejek priorytetowych oraz algorytmu Dijkstry do znajdowania najkrótszych ścieżek w grafie ważonym. Kolejki priorytetowe są wykorzystywane zarówno do sortowania elementów, jak i w algorytmie Dijkstry. 

### Główne funkcjonalności:
1. **Kolejki priorytetowe**:
   - `SortedStackPriorityQueue` - implementacja oparta na posortowanej liście.
   - `BSTPriorityQueue` - implementacja oparta na drzewie BST.
   - BucketPriorityQueue - implementacja oparta na kubełkach (bucket).

2. **Algorytm Dijkstry**:
   - Znajdowanie najkrótszych ścieżek w grafie ważonym z możliwością wyboru implementacji kolejki priorytetowej.

3. **Testowanie**:
   - Testowanie poprawności i wydajności różnych implementacji kolejek priorytetowych.
   - Testowanie algorytmu Dijkstry na grafach tworzonych dynamicznie przez użytkownika.

---

## Struktura projektu
```
.
├── Main.java                     // Główna klasa programu
├── WeightedGraph.java            // Implementacja grafu ważonego i algorytmu Dijkstry
├── PriorityQueue.java            // Interfejs dla kolejek priorytetowych
├── SortedStackPriorityQueue.java // Implementacja kolejki priorytetowej opartej na liście
├── BSTPriorityQueue.java         // Implementacja kolejki priorytetowej opartej na drzewie BST
├── BucketPriorityQueue.java      // Implementacja kolejki priorytetowej opartej na kubełkach
```

---

## Wymagania
- **Java 8 lub nowsza**
- Kompilator `javac`

---

## Instrukcja uruchomienia

1. **Skompiluj projekt**:
   W katalogu głównym projektu uruchom:
   ```bash
   javac *.java
   ```

2. **Uruchom program**:
   ```bash
   java Main
   ```

3. **Wybierz opcję**:
   Po uruchomieniu programu użytkownik może wybrać jedną z dwóch opcji:
   - `1`: Testowanie kolejek priorytetowych.
   - `2`: Uruchomienie algorytmu Dijkstry.

---

## Przykłady użycia

### 1. Testowanie kolejek priorytetowych
- Program poprosi o podanie liczby elementów oraz ich wartości.
- Wynikiem będzie posortowana lista elementów oraz informacje o poprawności działania każdej implementacji.

### 2. Algorytm Dijkstry
- Program poprosi o:
  - Liczbę wierzchołków i krawędzi w grafie.
  - Dodanie krawędzi w formacie: `źródło cel waga`.
  - Wybór wierzchołka początkowego.
  - Wybór implementacji kolejki priorytetowej (1=SortedStack, 2=BST, 3=Bucket).
- Wynikiem będzie lista najkrótszych ścieżek z wierzchołka początkowego do wszystkich innych.

---

## Kluczowe klasy

### 1. **`PriorityQueue`**
Interfejs definiujący operacje dla kolejek priorytetowych:
- `insert(T element)`
- `findMin()`
- `extractMin()`
- `decreaseKey(Position<T> position, T newElement)`
- `merge(PriorityQueue<T> other)`

### 2. **`WeightedGraph`**
Implementacja grafu ważonego z metodami:
- `addEdge(int source, int destination, int weight)`
- `dijkstra(int source, int priorityQueueType)`

### 3. **`DijkstraResult`**
Klasa reprezentująca wynik algorytmu Dijkstry:
- `getDistance(int vertex)` - zwraca odległość do wierzchołka.
- `getPath(int vertex)` - zwraca najkrótszą ścieżkę do wierzchołka.

---

## Przykładowe dane wejściowe i wyjściowe

### Testowanie kolejek priorytetowych:
**Wejście**:
```
Podaj liczbę elementów: 5
Podaj 5 liczb z zakresu [0, 1000]:
12 45 7 89 23
```

**Wyjście**:
```
Testowanie: Sorted Stack
  Wynik: [7, 12, 23, 45, 89]
  Posortowane poprawnie: ✓ TAK
  Zachowane wszystkie elementy: ✓ TAK

Testowanie: BST
  Wynik: [7, 12, 23, 45, 89]
  Posortowane poprawnie: ✓ TAK
  Zachowane wszystkie elementy: ✓ TAK

Testowanie: Bucket Queue
  Wynik: [7, 12, 23, 45, 89]
  Posortowane poprawnie: ✓ TAK
  Zachowane wszystkie elementy: ✓ TAK
```

### Algorytm Dijkstry:
**Wejście**:
```
Podaj liczbę wierzchołków: 4
Podaj liczbę krawędzi: 5
Dodawanie krawędzi (format: źródło cel waga):
0 1 10
0 2 5
1 2 2
1 3 1
2 3 9
Podaj wierzchołek początkowy dla algorytmu Dijkstry: 0
Wybierz typ kolejki priorytetowej (1=SortedStack, 2=BST, 3=Bucket): 1
```

**Wyjście**:
```
Dijkstra z wierzchołka 0:
  0: odległość=0, ścieżka=[0]
  1: odległość=8, ścieżka=[0, 2, 1]
  2: odległość=5, ścieżka=[0, 2]
  3: odległość=9, ścieżka=[0, 2, 1, 3]
```

---

## Autorzy
- **Imię i nazwisko**: Wiktor Geraltowski
- **Data**: 2 czerwca 2025

---

