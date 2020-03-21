import java.util.ArrayList;

/** A Generic heap class. Unlike Java's priority queue, this heap doesn't just
 * store Comparable objects. Instead, it can store any type of object
 * (represented by type T) and an associated priority value.
 * @author Ruize Xu*/
public class ArrayHeap<T> {

    /* DO NOT CHANGE THESE METHODS. */

    /** An ArrayList that stores the nodes in this binary heap. */
    private ArrayList<Node> contents;

    /** A constructor that initializes an empty ArrayHeap. */
    public ArrayHeap() {
        contents = new ArrayList<>();
        contents.add(null);
    }

    /** Returns the number of elements in the priority queue. */
    public int size() {
        return contents.size() - 1;
    }

    /** Returns the node at index INDEX. */
    private Node getNode(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    /** Sets the node at INDEX to N */
    private void setNode(int index, Node n) {
        // In the case that the ArrayList is not big enough
        // add null elements until it is the right size
        while (index + 1 > contents.size()) {
            contents.add(null);
        }
        contents.set(index, n);
    }

    /** Returns and removes the node located at INDEX. */
    private Node removeNode(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.remove(index);
        }
    }

    /** Swap the nodes at the two indices. */
    private void swap(int index1, int index2) {
        Node node1 = getNode(index1);
        Node node2 = getNode(index2);
        this.contents.set(index1, node2);
        this.contents.set(index2, node1);
    }

    /** Prints out the heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /** Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getNode(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getNode(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getNode(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getNode(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /** A Node class that stores items and their associated priorities. */
    public class Node {
        private T _item;
        private double _priority;

        private Node(T item, double priority) {
            this._item = item;
            this._priority = priority;
        }

        public T item() {
            return this._item;
        }

        public double priority() {
            return this._priority;
        }

        public void setPriority(double priority) {
            this._priority = priority;
        }

        @Override
        public String toString() {
            return this._item.toString() + ", " + this._priority;
        }
    }

    /* FILL IN THE METHODS BELOW. */

    /** Returns the index of the left child of the node at i. */
    private int getLeftOf(int i) {
        if (getNode(2 * i) != null) {
            return 2 * i;
        } else {
            return -1;
        }
    }

    /** Returns the index of the right child of the node at i. */
    private int getRightOf(int i) {
        if (getNode(2 * i + 1) != null) {
            return 2 * i + 1;
        } else {
            return -1;
        }
    }

    /** Returns the index of the node that is the parent of the
     *  node at i. */
    private int getParentOf(int i) {
        return i / 2;
    }

    /** Returns the index of the node with smaller priority. If one
     * node is null, then returns the index of the non-null node.
     * Precondition: at least one of the nodes is not null. */
    private int min(int index1, int index2) {
        if (index1 == -1) {
            return index2;
        } else if (index2 == -1) {
            return index1;
        }
        if (this.getNode(index1) == null) {
            return index2;
        }
        if (this.getNode(index2) == null) {
            return index1;
        }
        if (this.getNode(index1).priority() < this.getNode(index2).priority()) {
            return index1;
        } else {
            return index2;
         }
    }

    /** Returns the item with the smallest priority value, but does
     * not remove it from the heap. If multiple items have the minimum
     * priority value, returns any of them. Returns null if heap is
     * empty. */
    public T peek() {
        if (size() == 0) {
            return null;
        } else {
            return getNode(1).item();
        }
    }

    /** Bubbles up the node currently at the given index until no longer
     *  needed. */
    private void bubbleUp(int index) {
        int pNodeIndex = getParentOf(index);
        if (min(index, pNodeIndex) == index && index != 1) {
            swap(index, pNodeIndex);
            bubbleUp(pNodeIndex);
        }
    }

    /** Bubbles down the node currently at the given index until no longer
     *  needed. */
    private void bubbleDown(int index) {
        int lNodeIndex = getLeftOf(index);
        int rNodeIndex = getRightOf(index);
        int minChildIndex = min(lNodeIndex, rNodeIndex);
        if (min(index, minChildIndex) == minChildIndex) {
            swap(index, minChildIndex);
            bubbleDown(minChildIndex);
        }
    }

    /** Inserts an item with the given priority value. Assume that item is
     * not already in the heap. Same as enqueue, or offer. */
    public void insert(T item, double priority) {
        Node temp = new Node(item, priority);
        setNode(size() + 1, temp);
        bubbleUp(size());

    }

    /** Returns the element with the smallest priority value, and removes
     * it from the heap. If multiple items have the minimum priority value,
     * removes any of them. Returns null if the heap is empty. Same as
     * dequeue, or poll. */
    public T removeMin() {
        if (size() == 0) {
            return null;
        }
        swap(1, size());
        Node min = removeNode(size());
        bubbleDown(1);
        return min.item();
    }

    /** Changes the node in this heap with the given item to have the given
     * priority. You can assume the heap will not have two nodes with the
     * same item. Does nothing if the item is not in the heap. Check for
     * item equality with .equals(), not == */
    public void changePriority(T item, double priority) {
        int index = 0;
        for (int i = 1; i <= size(); i++) {
            Node temp = getNode(i);
            if (temp != null) {
                if (temp.item().equals(item)) {
                    index = i;
                    temp.setPriority(priority);
                    break;
                }
            }
        }
        int currentNodeL = getLeftOf(index);
        int currentNodeR = getRightOf(index);
        int currentNodeP = getParentOf(index);
        int minUp = min(index, currentNodeP);
        int minChild = min(currentNodeL, currentNodeR);
        int minDown = min(index, minChild);
        if (minUp == index) {
            bubbleUp(index);
        }
        if (minDown == minChild) {
            bubbleDown(index);
        }
    }
}
