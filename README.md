# Priority Queues Implementation & Dijkstra's Algorithm

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![JUnit](https://img.shields.io/badge/JUnit-5.10+-green.svg)](https://junit.org/junit5/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> **Advanced data structures project featuring three different priority queue implementations with comprehensive testing suite and Dijkstra's algorithm integration.**

---

## 🚀 Project Overview

This project demonstrates advanced understanding of **data structures**, **algorithms**, and **software engineering best practices** through the implementation of multiple priority queue variants and their application in graph algorithms.

### 🎯 Key Features

- **🏗️ Three Priority Queue Implementations**:
  - `SortedStackPriorityQueue` - Array-based sorted implementation
  - `BSTPriorityQueue` - Binary Search Tree implementation  
  - `BucketPriorityQueue` - Bucket-based implementation with O(1) operations

- **📊 Algorithm Integration**:
  - **Dijkstra's shortest path algorithm** with selectable priority queue backends
  - Performance comparison between different data structure approaches
  - Real-world graph problem solving capabilities

- **🧪 Comprehensive Testing**:
  - **42+ unit tests** with JUnit 5
  - **Performance benchmarks** and complexity analysis
  - **Code coverage** reporting (>90% target)
  - **Integration tests** with Dijkstra's algorithm

---

## 📁 Project Structure

```
PriorityQueues/
├── src/
│ ├── main/java/ # Production code
│ │ ├── BSTPriorityQueue.java # BST-based priority queue
│ │ ├── BucketPriorityQueue.java # Bucket-based priority queue
│ │ ├── SortedStackPriorityQueue.java # Array-based priority queue
│ │ ├── PriorityQueue.java # Core interface definition
│ │ ├── WeightedGraph.java # Graph & Dijkstra implementation
│ │ └── Main.java # Interactive demo application
│ └── test/java/ # Test suite
│ ├── PriorityQueueBasicTest.java
│ ├── PriorityQueueExceptionTest.java
│ ├── DecreaseKeyTest.java
│ ├── MergeTest.java
│ ├── PerformanceTest.java
│ ├── DijkstraIntegrationTest.java
│ ├── BSTPriorityQueueSpecializedTest.java
│ ├── BucketPriorityQueueSpecializedTest.java
│ ├── SortedStackSpecializedTest.java
│ ├── PriorityQueueTestSuite.java
│ └── TestUtils.java
├── pom.xml # Maven configuration
├── build.sh # Build automation script
├── run.sh # Application runner script
└── README.md # This file
```

---

## ⚡ Quick Start

### Prerequisites

- **Java 17+** ([Download OpenJDK](https://openjdk.java.net/))
- **Maven 3.6+** ([Installation Guide](https://maven.apache.org/install.html))
- **Git** (for cloning)

### 🛠️ Installation & Setup

```bash
# Clone the repository
git clone https://github.com/WiktorGeralt/PriorityQueues.git
cd PriorityQueues

# Make scripts executable
chmod +x *.sh

# Build and test (automated)
./build.sh

# Run the interactive demo
./run.sh
```
##🏃‍♂️ Manual Maven Commands
```bash
# Compile the project
mvn clean compile

# Run all tests
mvn test

# Generate code coverage report
mvn jacoco:report

# Run the main application
mvn exec:java -Dexec.mainClass="Main"

# Package as JAR
mvn package
```
##🧪 Testing Suite
###Test Categories
| Test Category            | Description                                   | Test Count          |
|---------------------------|-----------------------------------------------|----------------------|
| **Basic Functionality**   | Core operations (insert, extract, find)       | 12 tests             |
| **Exception Handling**    | Edge cases and error conditions               | 8 tests              |
| **DecreaseKey Operations**| Priority modification testing                 | 6 tests              |
| **Merge Operations**      | Queue combination functionality               | 4 tests              |
| **Performance Benchmarks**| Comparative analysis between implementations  | 8 tests              |
| **Integration Tests**     | Dijkstra algorithm with different queues      | 4 tests              |

##🎮 Interactive Demo Usage1. Priority Queue Testing Mode

Test and compare all three implementations:

```bash
$ java Main
Priority Queues & Dijkstra's Algorithm Demo
==========================================

Choose option:
1. Test Priority Queues
2. Run Dijkstra's Algorithm
> 1

Enter number of elements: 5
Enter 5 numbers in range [0, 1000]:
12 45 7 89 23

Testing: SortedStackPriorityQueue
  Result: [7, 12, 23, 45, 89]
  Correctly sorted: ✓ YES
  All elements preserved: ✓ YES
  Time: 0.43ms

Testing: BSTPriorityQueue  
  Result: [7, 12, 23, 45, 89]
  Correctly sorted: ✓ YES
  All elements preserved: ✓ YES
  Time: 0.28ms

Testing: BucketPriorityQueue
  Result: [7, 12, 23, 45, 89]
  Correctly sorted: ✓ YES  
  All elements preserved: ✓ YES
  Time: 0.15ms
  ```
  ### 2. Dijkstra's Algorithm Mode
Find shortest paths in weighted graphs:

```bash
$ java Main
> 2

Enter number of vertices: 5
Enter number of edges: 7
Add edges (format: source destination weight):
0 1 4
0 2 2
1 2 1  
1 3 5
2 3 8
2 4 10
3 4 2

Enter starting vertex: 0
Choose priority queue (1=SortedStack, 2=BST, 3=Bucket): 3

Dijkstra from vertex 0 (using BucketPriorityQueue):
  Vertex 0: distance=0, path=
  Vertex 1: distance=3, path=[0, 2, 1]  
  Vertex 2: distance=2, path=[0, 2]
  Vertex 3: distance=8, path=[0, 2, 1, 3]
  Vertex 4: distance=10, path=[0, 2, 1, 3, 4]

Execution time: 1.23ms
```
## 🏗️ Architecture & Design
### Core Interface Design
```java
public interface PriorityQueue<T extends Comparable<T> & HasValue> {
    Position<T> insert(T element);           // O(log n) average
    T findMin();                            // O(1)
    T extractMin();                         // O(log n) 
    void decreaseKey(Position<T> pos, T newElement);  // O(log n)
    PriorityQueue<T> merge(PriorityQueue<T> other);   // Implementation specific
    boolean isEmpty();                      // O(1)
    int size();                            // O(1)
    void clear();                          // O(1)
}
```

### Implementation comparison
| Implementation        | Insert                     | Extract Min | Find Min | Decrease Key | Space   | Best Use Case                              |
|------------------------|----------------------------|--------------|-----------|---------------|---------|---------------------------------------------|
| **SortedStack**        | O(n)                       | O(1)         | O(1)      | O(n)          | O(n)    | Small datasets, frequent min access         |
| **BSTPriorityQueue**   | O(log n) avg,<br>O(n) worst| O(log n)     | O(log n)  | O(log n)      | O(n)    | General purpose, balanced workloads         |
| **BucketPriorityQueue**| O(1)                       | O(K)         | O(K)      | O(1)          | O(K+n)  | Limited range, high performance             |

Where K is the range of possible values (0 to N=1000)

