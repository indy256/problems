import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * Problem K: "Trash Removal." ICPC World Finals 2011.
 * 
 * This is a classic problem, and one which becomes extremely simple with the
 * Java geometry libraries. For any given polygon, the optimal orientation must
 * make one of the diagonals of the polygon vertical, where "diagonal" is
 * defined as "any line between two distinct vertices." Since n <= 100, we can
 * afford O(n^3) time without breaking a sweat.
 * 
 * Therefore, for every pair of distinct vertices, we rotate the polygon until
 * the line between those vertices is horizontal, and we compute the height of
 * the bounding box of the resulting polygon. The minimum such height is the
 * answer to the problem.
 * 
 * @author Louis Wasserman, Assistant Coach, UChicago "Works in Theory"
 */
public class ProbK {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    for (int z = 1;; z++) {
      int n = input.nextInt();
      if (n == 0)
        break;

      int[] xs = new int[n], ys = new int[n];
      for (int i = 0; i < n; i++) {
        xs[i] = input.nextInt();
        ys[i] = input.nextInt();
      }
      Polygon poly = new Polygon(xs, ys, n);

      double best = Double.MAX_VALUE;
      for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
          AffineTransform trans =
              AffineTransform.getRotateInstance(xs[i] - xs[j], ys[j] - ys[i],
                  xs[j], ys[j]);
          Shape rotated = trans.createTransformedShape(poly);
          best = Math.min(best, rotated.getBounds2D().getHeight());
        }
      }
      System.out.println("Case " + z + ": "
          + new DecimalFormat("0.00").format(best));
    }
  }
}
