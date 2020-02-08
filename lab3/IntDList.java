/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        // FIXME: Implement this method and return correct value
        int count = 1;
        DNode current_node = this._front;
        if (this._front == null && this._back == null)
            return 0;
        if (this._front == this._back)
            return 1;
        else
            while (current_node._next!=null) {
                current_node = current_node._next;
                count++;
            }
        return count;
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        // FIXME: Implement this method and return correct value
        if (i > 0) {
            DNode node = this._front;
            while (i > 0) {
                node = node._next;
                i -= 1;
            }
            return node._val;
        }
        else if (i < 0) {
            DNode node = this._back;
            while (i < -1) {
                node = node._prev;
                i += 1;
            }
            return node._val;
        }
        return (this._front._val);
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        // FIXME: Implement this method
        DNode node = new DNode(d);
        if (this._front == null && this._back == null) {
            this._front = node;
            this._back = node;
        }
        else {
            node._next = this._front;
            this._front._prev = node;
            this._front = node;
        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        // FIXME: Implement this `method`
        DNode node = new DNode(d);
        if (this._front == null && this._back == null) {
            this._front = node;
            this._back = node;
        }
        else {
            node._prev = this._back;
            this._back._next=node;
            this._back=node;
        }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        // FIXME: Implement this method
        DNode node = new DNode(d);
        if (this.size() == 0) {
            insertFront(d);
        }
        if (index == 0) {
            insertFront(d);
        }
        else if (index == -1) {
            insertBack(d);
        }
        else if (index > 0) {
            node = this._front;
            while (index > 1 && node._next != null) {
                node = node._next;
                index -= 1;
            }
            if (node._next != null) {
                node._prev._next = node;
                node._prev = node;
                node._next._prev = node;
                node._next = node._next._next;
            }
            else {
                insertBack(d);
            }
        }
        else if (index < -1) {
            node = this._back;
            while (index > -1 && node._prev != null) {
                node = node._prev;
                index += 1;
            }
            if (node._prev != null) {
                node._next._prev = node;
                node._next = node;
                node._prev._next = node;
                node._prev = node._prev._prev;
            }
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        // FIXME: Implement this method and return correct value
        int value = this._front._val;
        if (this.size() > 1) {
            this._front = this._front._next;
            this._front._prev._next = null;
            this._front._prev = null;
        }
        else {
            this._front = this._back = null;
        }
        return value;
    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        // FIXME: Implement this method and return correct value
        int value = this._back._val;
        if (this.size() > 1) {
            this._back = this._back._prev;
            this._back._next._prev = null;
            this._back._next = null;
        }
        else {
            this._front = this._back = null;
        }
        return value;
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        // FIXME: Implement this method and return correct value
        int value = 0;
        if (this.size() > 0) {
            if (index == 0 || index < -this.size()) {
                value = deleteFront();
            }
            else if (index == -1 || index >= this.size()) {
                value = deleteBack();
            }
            else if (index > 0) {
                DNode d = this._front;
                while (index > 0 && d._next != null) {
                    d = d._next;
                    index -= 1;
                }
                value = d._val;
                if (d._next != null) {
                    d._prev._next = d._next;
                    d._next._prev = d._prev;
                    d._prev = d._next = null;
                }
                else {
                    value = deleteBack();
                }
            }
            else if (index < -1) {
                DNode d = this._back;
                while (index < -1 && d._prev != null) {
                    d = d._prev;
                    index += 1;
                }
                value = d._val;
                if (d._prev != null) {
                    d._prev._next = d._next;
                    d._next._prev = d._prev;
                    d._prev = d._next = null;
                }
                else {
                    value = deleteFront();
                }
            }
        }
        else {
            this._front = this._back = null;
        }
        return value;
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        // FIXME: Implement this method to return correct value
        DNode node = this._front;
        String str = "";
        if (this.size() == 0) {
            return null;
        }
        while (node._next != null) {
            str += node._val;
        }
        return str;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}