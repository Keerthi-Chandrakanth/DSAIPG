    package com.phasmidsoftware.dsaipg.adt.pq;

    import java.util.ArrayList;
    import java.util.Comparator;
    import java.util.HashMap;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Map;
    import java.util.NoSuchElementException;
    import java.util.Set;

    public class FibonacciHeap<K> extends PriorityQueue<K>{
        private Node<K> minNode;
        private int size;
        private final Set<Node<K>> roots;
        private final Comparator<K> comparator;
        
        public FibonacciHeap(int capacity, Comparator<K> comparator) {
            super(capacity, comparator);
            this.comparator = comparator; 
            this.minNode = null;
            this.size = 0;
            this.roots = new HashSet<>();
        }
        public void insert(K key) {
            Node<K> newNode = new Node<>(key);
            if (minNode == null || comparator.compare(key, minNode.key) < 0) {
                minNode = newNode;
            }
            roots.add(newNode);
            size++;
        }
        public K extractMin() {
            if (minNode == null) throw new NoSuchElementException("Heap is empty");
            K minValue = minNode.key;
            if (minNode.child != null) {
                List<Node<K>> children = new ArrayList<>();
                Node<K> child = minNode.child;
            do {
            children.add(child);
            child = child.right;
            } while (child != minNode.child);
            for (Node<K> c : children) {
                    roots.add(c);
                    c.parent = null;
                }
            }
            roots.remove(minNode);
            if (roots.isEmpty()) {
                minNode = null;
            } else {
                minNode = roots.iterator().next();
                consolidate();
            }
            size--;
            return minValue;
        }
        private void consolidate() {
            Map<Integer, Node<K>> degreeTable = new HashMap<>();
            List<Node<K>> toVisit = new ArrayList<>(roots);
        for (Node<K> node : toVisit) {
                int degree = node.degree;
                while (degreeTable.containsKey(degree)) {
                    Node<K> other = degreeTable.get(degree);
                    if (comparator.compare(other.key, node.key) < 0) {
                        Node<K> temp = node;
                        node = other;
                        other = temp;
                    }
                    link(other, node);
                    degreeTable.remove(degree);
                    degree++;
                }
                degreeTable.put(degree, node);
            }
            minNode = null;
            roots.clear();
            for (Node<K> node : degreeTable.values()) {
                roots.add(node);
                if (minNode == null || comparator.compare(node.key, minNode.key) < 0) {
                    minNode = node;
                }
            }
        

        }

        @Override
    public K take() throws PQException {
        if (isEmpty()) throw new PQException("Priority queue is empty");
    return extractMin();
    }
    @Override
    public void give(K key) {
        insert(key);
    }
    private void link(Node<K> child, Node<K> parent) {
        if (roots.contains(child)) {
            roots.remove(child);
        }
        child.left.right = child.right;
        child.right.left = child.left;
        child.parent = parent;
            if (parent.child == null) {
                parent.child = child;
                child.right = child;
                child.left = child;
            } else {
                Node<K> sibling = parent.child;
                child.right = sibling.right;
                child.left = sibling;
                sibling.right.left = child;
                sibling.right = child;
            }
        parent.degree++;
            child.marked = false;
        
        }

    private static class Node<T> {
            T key;
            Node<T> left, right, child, parent;
            int degree;
            boolean marked;

            Node(T key) {
                this.key = key;
                this.left = this;
                this.right = this;
            }
    }

        public boolean isEmpty() {
            return size == 0;
        }

        public static void main(String[] args) {
            Comparator<Integer> comparator = Comparator.naturalOrder();
            FibonacciHeap<Integer> fibHeap = new FibonacciHeap<>(10, comparator);

            fibHeap.insert(10);
            fibHeap.insert(20);
            fibHeap.insert(5);
            fibHeap.insert(15);
            fibHeap.insert(30);
            fibHeap.insert(25);
    }
    }
