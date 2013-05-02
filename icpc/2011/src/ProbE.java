import java.util.Arrays;
import java.util.Scanner;

/**
 * Problem E: "Coffee Central." ICPC World Finals 2011.
 * 
 * This problem becomes trivial once you can find the number of coffee shops
 * within Manhattan distance d of a point (x0, y0) in constant time. These
 * points satisfy the equations <code> -d <= (x - x0) + (y - y0) <= d</code> and
 * <code> -d <= (x - x0) - (y - y0) <= d</code>.
 * 
 * In particular, we transform <code>(x, y)</code> to
 * <code>(x + y, x - y)</code>. Letting <code>i = x + y</code> and
 * <code>j = x - y</code>, the points <code>(i, j)</code> within distance
 * <code>d</code> of <code>(i0, j0)</code> satisfy
 * <code>i0 - d <= i <= i0 + d</code> and <code>j0 - d <= j <= j0 + d</code>.
 * Therefore, if for every <code>(i0, j0)</code> we compute the number of coffee
 * shops with <code>i <= i0 && j <= j0</code>, we can compute the number of
 * coffee shops within distance <code>d</code> of any particular point with
 * inclusion-exclusion in constant time.
 * 
 * @author Louis Wasserman, Assistant Coach, UChicago "Works in Theory"
 */
public class ProbE {
  static int[][] cumulative;
  static int width;

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    for (int z = 1;; z++) {
      int dx = input.nextInt(), dy = input.nextInt(), n = input.nextInt(), q =
          input.nextInt();
      if ((dx | dy | n | q) == 0)
        break;
      System.out.println("Case " + z + ":");

      width = 2 * (dx + dy);
      cumulative = new int[width + 1][2 * width + 1];
      for (int[] row : cumulative)
        Arrays.fill(row, 0);

      for (int i = 0; i < n; i++) {
        int x = input.nextInt(), y = input.nextInt();
        set(x + y, x - y, 1);
      }

      for (int i = 0; i <= width; i++)
        for (int j = -width; j <= width; j++)
          set(i, j,
              get(i - 1, j) + get(i, j - 1) - get(i - 1, j - 1) + get(i, j));

      for (int k = 0; k < q; k++) {
        int bestCount = -1, bestX = -1, bestY = -1;
        int d = Math.min(input.nextInt(), dx + dy);
        for (int y = 1; y <= dy; y++) {
          for (int x = 1; x <= dx; x++) {
            int count = getWithinDist(x, y, d);
            if (count > bestCount) {
              bestCount = count;
              bestX = x;
              bestY = y;
            }
          }
        }
        System.out.println(bestCount + " (" + bestX + "," + bestY + ")");
      }
    }
  }

  static int get(int xpy, int xmy) {
    xmy += width;
    return (xpy >= 0 && xmy >= 0 && xpy <= width && xmy <= 2 * width)
        ? cumulative[xpy][xmy] : 0;
  }

  static int getWithinDist(int x, int y, int d) {
    int upperI = x + y + d, upperJ = x - y + d;
    int lowerI = x + y - d - 1, lowerJ = x - y - d - 1;
    return get(upperI, upperJ) - get(lowerI, upperJ) - get(upperI, lowerJ)
        + get(lowerI, lowerJ);
  }

  static void set(int xpy, int xmy, int val) {
    cumulative[xpy][xmy + width] = val;
  }
}
