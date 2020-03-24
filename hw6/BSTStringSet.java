import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Ruize Xu
 */
public class BSTStringSet implements StringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        if (!this.contains(s)){
            _root = putHelp(_root, s);
        }
    }

    /** A recursive put helper functhion. */
    public Node putHelp(Node root, String s) {
        if (root == null) {
            return new Node(s);
        }
        if (s.compareTo(root.s) == 0) {
            root.s = s;
        } else if (s.compareTo(root.s) < 0) {
            root.left = putHelp(root.left, s);
        } else if (s.compareTo(root.s) > 0) {
            root.right = putHelp(root.right, s);
        }
        return root;
    }

    @Override
    public boolean contains(String s) {
        return containsHelp(_root, s);
    }

    /** A recursive contains helper functhion. */
    public boolean containsHelp(Node node, String s) {
        if (node == null){
            return false;
        }
        if (s.compareTo(node.s) == 0) {
            return true;
        } else if (s.compareTo(node.s) < 0) {
            return containsHelp(node.left, s);
        } else if (s.compareTo(node.s) > 0) {
            return containsHelp(node.right, s);
        } else {
            return false;
        }
    }

    @Override
    public List<String> asList() {
        List<String> resultList = new ArrayList<String>();
        BSTIterator iterator = new BSTIterator(_root);
        while (iterator.hasNext()) {
            resultList.add(iterator.next());
        }
        return resultList;
    }

    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    //@Override
    public Iterator<String> iterator(String low, String high) {
        Stack<Node> temp = new Stack<Node>();
        BSTIterator iterator = new BSTIterator(null);
        iteratorHelp(_root, low, high, temp);
        while (!temp.empty()) {
            Node node = temp.pop();
            node.right = null;
            node.left = null;
            iterator._toDo.push(node);
        }
        return iterator;
    }

    public void iteratorHelp(Node node, String low, String high, Stack<Node> temp) {
        if (node != null) {
            boolean greatThanLow = (low.compareTo(node.s) < 0);
            boolean lessThanHigh = (high.compareTo(node.s) > 0);
            if (greatThanLow) {
                iteratorHelp(node.left, low, high, temp);
            }
            if (greatThanLow && lessThanHigh) {
                temp.push(node);
            }
            if (lessThanHigh) {
                iteratorHelp(node.right, low, high, temp);
            }
        }
    }

    /** Root node of the tree. */
    private Node _root;
}
