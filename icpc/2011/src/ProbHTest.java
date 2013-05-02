import java.util.*;

import junit.framework.TestCase;

public class ProbHTest extends TestCase {
  public void testOnProvidedData() {
    String testInput =
        "9\n1 3\n4 1\n3 5\n1 2\n2 6\n1 5\n6 3\n1 6\n3 2\n6\n"
            + "1 2\n1 3\n2 4\n2 5\n3 6\n3 7\n0\n";
    ICPCRunner.assertMatches(ProbH.class, testInput, "2 4", "4 1");
  }

  public void testOnLargeData() {
    int cases = 1;
    StringBuilder builder = new StringBuilder(20 * 50000 * 20);
    for (int z = 0; z < cases; z++) {
      Map<Integer, Set<Integer>> graph = new HashMap<Integer, Set<Integer>>();
      int n = 1000;
      int m = 50000;
      for (int i = 1; i <= 1000; i++) {
        graph.put(i, new HashSet<Integer>());
      }
      Random rand = new Random(35987);
      for (int j = 0; j < m; j++) {
        while (true) {
          int a = rand.nextInt(n) + 1;
          int b = rand.nextInt(n) + 1;
          if (a != b && !graph.get(a).contains(b)) {
            graph.get(a).add(b);
            graph.get(b).add(a);
            break;
          }
        }
      }

      Set<Integer> component = dfs(graph, 1, new HashSet<Integer>(n));
      m = 0;
      for (int i : component) {
        for (int j : graph.get(i)) {
          if (i < j) {
            m++;
          }
        }
      }
      builder.append(m).append('\n');
      for (int i : component) {
        for (int j : graph.get(i)) {
          if (i < j) {
            builder.append(i).append(' ').append(j).append('\n');
          }
        }
      }
    }
    builder.append("0\n");
    String pat = "\\d+ \\d+";
    Object[] pats = new Object[cases];
    Arrays.fill(pats, pat);
    ICPCRunner.assertMatches(ProbH.class, builder.toString(), pats);

  }

  private static <V> Set<V> dfs(Map<V, Set<V>> g, V v, Set<V> component) {
    if (component.add(v))
      for (V w : g.get(v))
        dfs(g, w, component);
    return component;
  }
}
