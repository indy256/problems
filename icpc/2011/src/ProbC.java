import java.awt.Point;
import java.math.BigInteger;
import java.util.*;

/**
 * Problem C: "Ancient Messages." ICPC World Finals 2011.
 * 
 * The key observation is that each hieroglyph is uniquely determined by the
 * number of connected components of white pixels inside it. Therefore, each
 * connected component of black pixels corresponds to a hieroglyph, which is
 * uniquely determined by the number of connected components of white pixels
 * adjacent to the black component.
 * 
 * @author Louis Wasserman, Assistant Coach, UChicago "Works in Theory"
 */
public class ProbC {
  private static class Partition {
    private Partition parent;

    public Partition() {
      this.parent = this;
    }

    public void union(Partition p) {
      Partition x = find();
      Partition px = p.find();
      x.parent = px;
    }

    private Partition find() {
      return (parent != this) ? parent = parent.find() : this;
    }

    @Override public int hashCode() {
      return System.identityHashCode(find());
    }

    @Override public boolean equals(Object obj) {
      return obj instanceof Partition && find() == ((Partition) obj).find();
    }
  }

  static boolean[][] black;
  static int h;
  static int w;

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    for (int z = 1;; z++) {
      h = scanner.nextInt();
      w = scanner.nextInt();
      if ((w | h) == 0)
        break;
      black = new boolean[h][w];
      Partition[][] partition = new Partition[h][w];
      for (int i = 0; i < h; i++) {
        BigInteger l = scanner.nextBigInteger(16);
        for (int j = 0; j < w; j++) {
          black[i][j] = l.testBit(j);
          partition[i][j] = new Partition();
        }
      }
      for (int i = 0; i < h; i++)
        for (int j = 0; j < w; j++)
          for (Point p : adj(i, j))
            if (black[i][j] == black[p.x][p.y])
              partition[i][j].union(partition[p.x][p.y]);
      Map<Partition, Set<Partition>> adj =
          new HashMap<Partition, Set<Partition>>();
      for (int i = 0; i < h; i++)
        for (int j = 0; j < w; j++)
          for (Point p : adj(i, j))
            tryAdj(new Point(i, j), p, partition, adj);
      char[] output = new char[adj.size()];
      int i = 0;
      for (Set<Partition> adjSet : adj.values())
        output[i++] = "WAKJSD".charAt(adjSet.size() - 1);
      Arrays.sort(output);
      System.out.println("Case " + z + ": " + String.valueOf(output));
    }
  }

  // Returns a collection of points adjacent to (i, j).
  private static Collection<Point> adj(int i, int j) {
    List<Point> list = new ArrayList<Point>(4);
    for (int i2 = i - 1; i2 <= i + 1; i += 2)
      if (i2 >= 0 && i2 < h)
        list.add(new Point(i2, j));
    for (int j2 = j - 1; j2 <= j + 1; j += 2)
      if (j2 >= 0 && j2 < h)
        list.add(new Point(i, j2));
    return list;
  }

  // adj maps black components to adjacent white components.
  private static void tryAdj(Point a, Point b, Partition[][] partition,
      Map<Partition, Set<Partition>> adj) {
    if (black[a.x][a.y] && !black[b.x][b.y]) {
      Partition aP = partition[a.x][a.y];
      Set<Partition> aPAdj = adj.get(aP);
      if (aPAdj == null)
        adj.put(aP, aPAdj = new HashSet<Partition>());
      aPAdj.add(partition[b.x][b.y]);
    }
  }
}
