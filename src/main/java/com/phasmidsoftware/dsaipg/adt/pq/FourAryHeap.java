package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.Comparator;


public class FourAryHeap<K> extends PriorityQueue<K> {
    private final int arity = 4;

     public FourAryHeap(int n, boolean max, Comparator<K> comparator,boolean floyd) {
        super(n, max, comparator,floyd);
    }

    public FourAryHeap(int n, Comparator<K> comparator) {
        super(n, comparator);
    }

    protected int firstChild(int k) {
        return arity * k + (first == 1 ? -2 : 1);
    }

    protected int parent(int k) {
        return (k - (first == 1 ? 2 : 1)) / arity + first;
    }

   
    public static void main(String[] args) {
        Comparator<Integer> comparator = Comparator.naturalOrder();
        FourAryHeap<Integer> fourAryHeap = new FourAryHeap<>(10, true, comparator, true);
        System.out.println("Inserting elements into 4-ary heap...");
        fourAryHeap.give(10);
        fourAryHeap.give(20);
        fourAryHeap.give(5);
        fourAryHeap.give(15);
        fourAryHeap.give(30);
        fourAryHeap.give(25);

        System.out.println("Extracting elements from 4-ary heap:");
      while (!fourAryHeap.isEmpty()) {
    try {
        System.out.println("Extracted: " + fourAryHeap.take());
    } catch (PQException e) {
        System.err.println("Error while extracting: " + e.getMessage());
    }
}

    }
}
