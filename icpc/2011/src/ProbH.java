import java.util.*;

/**
 * Problem H: "Mining Your Own Business." ICPC World Finals 2011.
 * 
 * An articulation vertex of a connected graph is a vertex whose deletion
 * disconnects the graph. Clearly, if any (non-backtracking) path between
 * vertices u and v goes through an articulation vertex, all paths between u and
 * v go through that vertex.
 * 
 * Therefore, if we delete all articulation vertices from a connected graph G,
 * and u and v are now in different connected components, then there is a single
 * articulation vertex whose removal from G disconnects u from v; and if u and v
 * are still in the same connected component, there is no single vertex in G
 * whose removal disconnects u from v.
 * 
 * Therefore, the problem reduces to identifying the articulation vertices,
 * removing them, and identifying the connected components of the resulting
 * graph. Each component needs one and only one escape shaft, which can be put
 * on any of its vertices. Therefore, the number of escape shafts needed is the
 * number of components, and the number of possible ways to assign those escape
 * shafts is the product of the sizes of the components.
 * 
 * <a href="http://en.wikipedia.org/wiki/Articulation_vertex">Wikipedia</a>
 * provides a linear-time algorithm to identify articulation vertices, which
 * requires a single depth-first search.
 * 
 * @author Louis Wasserman, Assistant Coach, UChicago "Works in Theory"
 */
public class ProbH {
  static List<Integer> biCompSizes; // list of discovered bicon component sizes
  static int[] depth;
  static int[] low;
  static List<List<Integer>> g; // adjacency lists

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    for (int z = 1;; z++) {
      int n = input.nextInt();
      if (n == 0)
        break;
      g = new ArrayList<List<Integer>>(n + 2);
      for (int i = 0; i < n + 2; i++)
        g.add(new ArrayList<Integer>());
      for (int i = 0; i < n; i++) {
        int s = input.nextInt(), t = input.nextInt();
        g.get(s).add(t);
        g.get(t).add(s);
      }
      depth = new int[n + 2];
      Arrays.fill(depth, -1);
      low = new int[n + 2];
      biCompSizes = new ArrayList<Integer>();
      dfs(1, 0);
      long poss = 1;
      for (int s : biCompSizes)
        poss *= s;
      System.out.println("Case " + z + ": " + biCompSizes.size() + " " + poss);
    }
  }

  private static void addIf(List<Integer> l, int i) {
    if (i > 0)
      l.add(i);
  }

  /**
   * Returns the number of descendants of v that are in the same biconnected
   * component as v. If v is an articulation vertex, returns 0.
   */
  private static int dfs(int v, int d) {
    depth[v] = d;
    low[v] = d;
    int maxLow = 0;
    List<Integer> descs = new ArrayList<Integer>(g.get(v).size());
    for (int w : g.get(v)) {
      if (depth[w] >= 0) {
        low[v] = Math.min(low[v], depth[w]);
      } else {
        addIf(descs, dfs(w, d + 1));
        low[v] = Math.min(low[v], low[w]);
        maxLow = Math.max(maxLow, low[w]);
      }
    }
    if (maxLow >= d && !descs.isEmpty()) {
      biCompSizes.addAll(descs);
      return 0;
    }
    int desc = 1;
    for (int ds : descs)
      desc += ds;
    return desc;
  }
}
