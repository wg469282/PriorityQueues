import java.util.*;

public class BSTPriorityQueue<T extends Comparable<T> & HasValue> implements PriorityQueue<T> {
    private TreeNode<T> root;
    private int size;

    public BSTPriorityQueue() {
        this.root = null;
        this.size = 0;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(log n)
    // W najgorszym przypadku drzewo jest zdegenerowane do listy
    @Override
    public Position<T> insert(T element) {
        if (element.wartość() < 0 || element.wartość() > N) {
            throw new IllegalArgumentException("Element value must be in range [0, " + N + "]");
        }

        TreeNodePosition<T> position = new TreeNodePosition<>(element);
        root = insertNode(root, element, position);
        size++;
        return position;
    }

    private TreeNode<T> insertNode(TreeNode<T> node, T element, TreeNodePosition<T> position) {
        if (node == null) {
            TreeNode<T> newNode = new TreeNode<>(element);
            position.node = newNode;
            return newNode;
        }

        if (element.compareTo(node.element) <= 0) {
            node.left = insertNode(node.left, element, position);
        } else {
            node.right = insertNode(node.right, element, position);
        }
        return node;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(log n)
    // Musi znaleźć najmniejszy element (najlżejszy w lewo)
    @Override
    public T findMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return findMinNode(root).element;
    }

    private TreeNode<T> findMinNode(TreeNode<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(log n)
    // Znajdź minimum i usuń go z drzewa
    @Override
    public T extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        TreeNode<T> minNode = findMinNode(root);
        T minElement = minNode.element;
        root = deleteNode(root, minElement);
        size--;
        return minElement;
    }

    private TreeNode<T> deleteNode(TreeNode<T> node, T element) {
        if (node == null)
            return null;

        int comparison = element.compareTo(node.element);
        if (comparison < 0) {
            node.left = deleteNode(node.left, element);
        } else if (comparison > 0) {
            node.right = deleteNode(node.right, element);
        } else {
            if (node.left == null)
                return node.right;
            if (node.right == null)
                return node.left;

            TreeNode<T> successor = findMinNode(node.right);
            node.element = successor.element;
            node.right = deleteNode(node.right, successor.element);
        }
        return node;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(log n)
    // Usuwa stary węzeł i wstawia nowy z nową wartością
    @Override
    public void decreaseKey(Position<T> position, T newElement) {
        if (!(position instanceof TreeNodePosition)) {
            throw new IllegalArgumentException("Invalid position type");
        }

        @SuppressWarnings("unchecked")
        TreeNodePosition<T> treePos = (TreeNodePosition<T>) position;

        if (!treePos.isValid() || newElement.wartość() < 0 || newElement.wartość() > N) {
            throw new IllegalArgumentException("Invalid operation");
        }

        if (newElement.wartość() >= treePos.getElement().wartość()) {
            throw new IllegalArgumentException("New element value must be smaller");
        }

        T oldElement = treePos.getElement();
        root = deleteNode(root, oldElement);
        treePos.valid = false;
        size--;
        insert(newElement);
    }

    // Asymptotyczna złożoność pesymistyczna: O(n²), średnia: O(n log n)
    // Dla każdego elementu wykonuje insert
    public static <T extends Comparable<T> & HasValue> PriorityQueue<T> buildHeap(T[] elements) {
        BSTPriorityQueue<T> pq = new BSTPriorityQueue<>();
        for (T element : elements) {
            pq.insert(element);
        }
        return pq;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n*m*log(n+m)), średnia:
    // O(n*m*log(n+m))
    @Override
    public PriorityQueue<T> merge(PriorityQueue<T> other) {
        BSTPriorityQueue<T> result = new BSTPriorityQueue<>();

        // Dodaj wszystkie elementy z tego drzewa
        List<T> thisElements = new ArrayList<>();
        inorderTraversal(root, thisElements);
        for (T element : thisElements) {
            result.insert(element);
        }

        // Dodaj wszystkie elementy z drugiego drzewa
        while (!other.isEmpty()) {
            result.insert(other.extractMin());
        }

        return result;
    }

    private void inorderTraversal(TreeNode<T> node, List<T> result) {
        if (node != null) {
            inorderTraversal(node.left, result);
            result.add(node.element);
            inorderTraversal(node.right, result);
        }
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
        root = null;
        size = 0;
    }

    private static class TreeNode<T> {
        T element;
        TreeNode<T> left, right;

        TreeNode(T element) {
            this.element = element;
        }
    }

    private static class TreeNodePosition<T> implements Position<T> {
        TreeNode<T> node;
        T element;
        boolean valid;

        TreeNodePosition(T element) {
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