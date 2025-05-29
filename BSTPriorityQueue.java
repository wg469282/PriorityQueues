import java.util.*;

public class BSTPriorityQueue implements PriorityQueue {
    private TreeNode root;
    private int size;

    public BSTPriorityQueue() {
        this.root = null;
        this.size = 0;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(log n)
    // W najgorszym przypadku drzewo jest zdegenerowane do listy
    @Override
    public Position insert(int value) {
        if (value < 0 || value > N) {
            throw new IllegalArgumentException("Value must be in range [0, " + N + "]");
        }

        TreeNodePosition position = new TreeNodePosition(value);
        root = insertNode(root, value, position);
        size++;
        return position;
    }

    private TreeNode insertNode(TreeNode node, int value, TreeNodePosition position) {
        if (node == null) {
            TreeNode newNode = new TreeNode(value);
            position.node = newNode;
            return newNode;
        }

        if (value <= node.value) {
            node.left = insertNode(node.left, value, position);
        } else {
            node.right = insertNode(node.right, value, position);
        }
        return node;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(log n)
    // Musi znaleźć najmniejszy element (najlżejszy w lewo)
    @Override
    public int findMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return findMinNode(root).value;
    }

    private TreeNode findMinNode(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(log n)
    // Znajdź minimum i usuń go z drzewa
    @Override
    public int extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        TreeNode minNode = findMinNode(root);
        int minValue = minNode.value;
        root = deleteNode(root, minValue);
        size--;
        return minValue;
    }

    private TreeNode deleteNode(TreeNode node, int value) {
        if (node == null)
            return null;

        if (value < node.value) {
            node.left = deleteNode(node.left, value);
        } else if (value > node.value) {
            node.right = deleteNode(node.right, value);
        } else {
            if (node.left == null)
                return node.right;
            if (node.right == null)
                return node.left;

            TreeNode successor = findMinNode(node.right);
            node.value = successor.value;
            node.right = deleteNode(node.right, successor.value);
        }
        return node;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n), średnia: O(log n)
    // Usuwa stary węzeł i wstawia nowy z nową wartością
    @Override
    public void decreaseKey(Position position, int newValue) {
        if (!(position instanceof TreeNodePosition)) {
            throw new IllegalArgumentException("Invalid position type");
        }

        TreeNodePosition treePos = (TreeNodePosition) position;
        if (!treePos.isValid() || newValue < 0 || newValue > N) {
            throw new IllegalArgumentException("Invalid operation");
        }

        if (newValue >= treePos.getValue()) {
            throw new IllegalArgumentException("New value must be smaller");
        }

        root = deleteNode(root, treePos.getValue());
        treePos.valid = false;
        size--;
        insert(newValue);
    }

    // Asymptotyczna złożoność pesymistyczna: O(n²), średnia: O(n log n)
    // Dla każdego elementu wykonuje insert
    public static PriorityQueue buildHeap(int[] elements) {
        BSTPriorityQueue pq = new BSTPriorityQueue();
        for (int element : elements) {
            pq.insert(element);
        }
        return pq;
    }

    // Asymptotyczna złożoność pesymistyczna: O(n*m*log(n+m)), średnia:
    // O(n*m*log(n+m))
    @Override
    public PriorityQueue merge(PriorityQueue other) {
        BSTPriorityQueue result = new BSTPriorityQueue();

        // Dodaj wszystkie elementy z tego drzewa
        List<Integer> thisElements = new ArrayList<>();
        inorderTraversal(root, thisElements);
        for (int value : thisElements) {
            result.insert(value);
        }

        // Dodaj wszystkie elementy z drugiego drzewa
        while (!other.isEmpty()) {
            result.insert(other.extractMin());
        }

        return result;
    }

    private void inorderTraversal(TreeNode node, List<Integer> result) {
        if (node != null) {
            inorderTraversal(node.left, result);
            result.add(node.value);
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

    private static class TreeNode {
        int value;
        TreeNode left, right;

        TreeNode(int value) {
            this.value = value;
        }
    }

    private static class TreeNodePosition implements Position {
        TreeNode node;
        int value;
        boolean valid;

        TreeNodePosition(int value) {
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
