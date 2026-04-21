import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class TopKHeap<T extends Comparable<T>> {
    private BinaryMinHeap<T> topK; // Holds the top k items
    private BinaryMaxHeap<T> rest; // Holds all items other than the top k
    private final int k; // The value of k
    private Map<T, MyPriorityQueue<T>> itemToHeap; // Keeps track of which heap contains each item.

    // Creates a topKHeap for the given choice of k.
    public TopKHeap(int k) {
        topK = new BinaryMinHeap<>();
        rest = new BinaryMaxHeap<>();
        this.k = k;
        itemToHeap = new HashMap<>();
    }

    // Returns a list containing exactly the
    // largest k items. The list is not necessarily
    // sorted. If the size is less than or equal to
    // k then the list will contain all items.
    // The running time of this method should be O(k).
    public List<T> topK() {
        return topK.toList();
    }

    // Add the given item into the data structure.
    // The running time of this method should be O(log(n)+log(k)).
    public void insert(T item) {
        if (topK.size() < k) {
            topK.insert(item);
            itemToHeap.put(item, topK);
        } else if (!topK.isEmpty() && item.compareTo(topK.peek()) > 0) {
            demote();

            topK.insert(item);
            itemToHeap.put(item, topK);
        } else {

            rest.insert(item);
            itemToHeap.put(item, rest);
        }
    }

    // Indicates whether the given item is among the
    // top k items. Should return false if the item
    // is not present in the data structure at all.
    // The running time of this method should be O(1).
    // We have provided a suggested implementation,
    // but you're welcome to do something different!
    public boolean isTopK(T item) {
        return itemToHeap.get(item) == topK;
    }

    // To be used whenever an item's priority has changed.
    // The input is a reference to the items whose priority
    // has changed. This operation will then rearrange
    // the items in the data structure to ensure it
    // operates correctly.
    // Throws an IllegalArgumentException if the item is
    // not a member of the heap.
    // The running time of this method should be O(log(n)+log(k)).
    public void updatePriority(T item) {
        if (!itemToHeap.containsKey(item)) {
            throw new IllegalArgumentException("Item was not found and thus can not update priority");
        }

        MyPriorityQueue<T> heap = itemToHeap.get(item);
        heap.updatePriority(item);

        if (!topK.isEmpty() && !rest.isEmpty() && topK.peek().compareTo(rest.peek()) < 0) {

            demote();
            promote();
        }
    }

    // Removes the given item from the data structure
    // throws an IllegalArgumentException if the item
    // is not present.
    // The running time of this method should be O(log(n)+log(k)).
    public void remove(T item) {
        if (!itemToHeap.containsKey(item)) {
            throw new IllegalArgumentException("Item not present!");
        }

        MyPriorityQueue<T> heap = itemToHeap.get(item);
        heap.remove(item);
        itemToHeap.remove(item);

        if (heap == topK && !rest.isEmpty()) {
            promote();
        }
    }

    /*
     * My own helper function for the process of promoting the
     * root of rest Heap to root of topK heap
     */
    private void promote() {
        T promoted = rest.extract();
        itemToHeap.remove(promoted);
        topK.insert(promoted);
        itemToHeap.put(promoted, topK);
    }

    /*
     * My own helper function for the process of demoting the
     * root of topK Heap to root of rest heap
     */
    private void demote() {
        T demoted = topK.extract();
        itemToHeap.remove(demoted);
        rest.insert(demoted);
        itemToHeap.put(demoted, rest);
    }
}
