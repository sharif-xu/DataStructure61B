import com.sun.xml.internal.xsom.XSWildcard;
import com.sun.xml.internal.xsom.impl.scd.Iterators;

import java.util.Arrays;
import java.util.Comparator;

/** Minimal spanning tree utility.
 *  @author Ruize Xu
 */
public class MST {

    /** Given an undirected, weighted, connected graph whose vertices are
     *  numbered 1 to V, and an array E of edges, returns an array of edges
     *  in E that form a minimal spanning tree of the input graph.
     *  Each edge in E is a three-element int array of the form (u, v, w),
     *  where 0 < u < v <= V are vertex numbers, and 0 <= w is the weight
     *  of the edge. The result is an array containing edges from E.
     *  Neither E nor the arrays in it may be modified.  There may be
     *  multiple edges between vertices.  The objects in the returned array
     *  are a subset of those in E (they do not include copies of the
     *  original edges, just the original edges themselves.) */
    public static int[][] mst(int V, int[][] E) {
        E = Arrays.copyOf(E, E.length);
        int numEdgesInResult = V - 1;
        int[][] result = new int[numEdgesInResult][];
        UnionFind uf = new UnionFind(V);
        Arrays.sort(E, EDGE_WEIGHT_COMPARATOR);
        int count = 0;
        for (int[] edge : E) {
            if (uf.find(edge[0]) != uf.find(edge[1])) {
                if (count <= numEdgesInResult) {
                    result[count] = edge;
                    count++;
                    uf.union(edge[0], edge[1]);
                }
            }
        }
        return result;
    }

    /** An ordering of edges by weight. */
    private static final Comparator<int[]> EDGE_WEIGHT_COMPARATOR =
        new Comparator<int[]>() {
            @Override
            public int compare(int[] e0, int[] e1) {
                return e0[2] - e1[2];
            }
        };

}
