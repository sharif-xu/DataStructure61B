
/** Disjoint sets of contiguous integers that allows (a) finding whether
 *  two integers are in the same set and (b) unioning two sets together.  
 *  At any given time, for a structure partitioning the integers 1 to N, 
 *  into sets, each set is represented by a unique member of that
 *  set, called its representative.
 *  @author Ruize Xu
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        id = new int[N + 1];
        size = new int[N + 1];
        for (int i = 0; i < id.length; i++) {
            id[i] = i;
            size[i] = 1;
        }
    }

    /** Return the representative of the set currently containing V.
     *  Assumes V is contained in one of the sets.  */
    public int find(int v) {
        if (v >= id.length || v < 0) {
            throw new IllegalArgumentException("v is out of bound.");
        } else {
            while (v != id[v]) {
                v = id[v];
            }
        }
        return v;
    }

    /** Return true iff U and V are in the same set. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single set, returning its representative. */
    public int union(int u, int v) {
        if (samePartition(u, v)) {
            return find(u);
        } else {
            int uRoot = find(u);
            int vRoot = find(v);
            if (size[uRoot] >= size[vRoot]) {
                id[vRoot] = uRoot;
                size[uRoot] += size[vRoot];
                return uRoot;
            } else {
                id [uRoot] = vRoot;
                size[vRoot] += size[uRoot];
                return vRoot;
            }
        }

    }

    private int[] id;

    private int[] size;
}
