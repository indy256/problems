import java.util.*;

/**
 * Problem J: "Pyramids." ICPC World Finals 2011.
 * 
 * This problem can be solved with dynamic programming. Note that there are at
 * most 320 possible pyramids which could ever be produced, and p[j] is the jth
 * smallest pyramid. Let sol[i][j] be the best solution (according to the
 * Pharaoh) that uses exactly i blocks, and restricts itself to the smallest (by
 * volume) j pyramids. This array needs dimensions 10^6 by 320, which is
 * manageable. Clearly, sol[i][j] = best(p[j] + sol[i-p[j].volume][j-1],
 * sol[i][j-1]), which can be computed in constant time. Therefore, if c blocks
 * are available, the answer is sol[c][320].
 * 
 * @author Louis Wasserman, Assistant Coach, UChicago "Works in Theory"
 */
public class ProbJ {
  private static final int MILLION = 1000000;

  static class Pyramid implements Comparable<Pyramid> {
    final int height;
    final int volume;
    final int base;
    final boolean high;

    Pyramid(int height, int volume, int base, boolean high) {
      this.height = height;
      this.volume = volume;
      this.high = high;
      this.base = base;
    }

    @Override public int compareTo(Pyramid o) {
      int cmp = o.volume - volume;
      return (cmp == 0) ? base - o.base : cmp;
    }

    public String toString() {
      return base + (high ? "H" : "L");
    }
  }

  static List<Pyramid> pyramids;
  static {
    pyramids = new ArrayList<Pyramid>(320);
    int blocks = 1;
    int lowblocks = 1;
    for (int h = 2;; h++) {
      blocks += h * h;
      if (blocks >= MILLION)
        break;
      pyramids.add(new Pyramid(h, blocks, h, true));
      if (4 * blocks <= MILLION)
        pyramids.add(new Pyramid(h, 4 * blocks, 2 * h, false));
      int lowWidth = 2 * h - 1;
      lowblocks += lowWidth * lowWidth;
      if (lowblocks <= MILLION)
        pyramids.add(new Pyramid(h, lowblocks, lowWidth, false));
    }
    Collections.sort(pyramids);
    Collections.reverse(pyramids);
  }

  static class Solution {
    Pyramid pyramid;
    Solution prev;

    Solution(Pyramid pyramid, Solution prev) {
      this.pyramid = pyramid;
      this.prev = prev;
    }

    private void append(StringBuilder builder) {
      builder.append(pyramid);
      if (prev != null) {
        builder.append(' ');
        prev.append(builder);
      }
    }

    public String toString() {
      StringBuilder builder = new StringBuilder();
      append(builder);
      return builder.toString();
    }
  }

  private static List<Pyramid> pyramidsUpTo(int volume) {
    int index =
        Collections.binarySearch(pyramids, new Pyramid(2 * MILLION, volume, 0,
            true));
    return pyramids.subList(0, (index >= 0) ? index : -1 - index);
  }

  private static Solution[][] solutions;

  private static Solution get(int i, int j) {
    if (solutions[i] == null)
      fill(i);
    return solutions[i][j];
  }

  /**
   * Fills in the solutions array for solutions using exactly i cubes.
   */
  private static void fill(int i) {
    if (solutions[i] != null)
      return;
    Solution s = null;
    int index = pyramidsUpTo(i).size();
    solutions[i] = new Solution[index + 1];
    assert i >= pyramids.get(index).volume;
    for (int j = 0; j < index; j++) {
      Pyramid p = pyramids.get(j);
      int remaining = i - p.volume;
      if (remaining > 0 && j > 0 && solutions[remaining][j - 1] != null)
        solutions[i][j] = s = new Solution(p, solutions[remaining][j - 1]);
      else if (remaining == 0)
        solutions[i][j] = s = new Solution(p, null);
      else if (remaining < 0)
        break;
      else
        solutions[i][j] = s;
    }
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    for (int z = 1;; z++) {
      int c = scanner.nextInt();
      if (c == 0)
        break;
      solutions = new Solution[c + 1][];
      for (int i = 1; i <= c; i++) {
        // My laptop has problems holding the whole thing in memory,
        // but we never need to hold more than 500000 rows in memory at once.
        if (i > 500000)
          solutions[i - 500000] = null;
        fill(i);
      }
      Solution best = null;
      for (int j = 0; j < solutions[c].length; j++)
        if (get(c, j) != null)
          best = get(c, j);
      System.out.println("Case " + z + ": "
          + ((best == null) ? "impossible" : best.toString()));
    }
  }
}
