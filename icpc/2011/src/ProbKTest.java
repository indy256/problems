import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class ProbKTest extends TestCase {
  public void testOnProvidedData() {
    String testInput = "3\n0 0\n3 0\n0 4\n4\n0 10\n10 0\n20 10\n10 20\n0\n";
    ICPCRunner.assertMatches(ProbK.class, testInput, "2" + Pattern.quote(".")
        + "(?:39|4[01])", "14" + Pattern.quote(".") + "1[4-6]");
  }

  public void testOnLargeData() {
    int cases = 5;
    StringBuilder builder = new StringBuilder(cases * 100 * 10);
    for (int z = 0; z < cases; z++) {
      int n = 100;
      Point[] points = new Point[n];
      Random random = new Random(13248902);
      for (int i = 0; i < n; i++) {
        int x = random.nextInt(2001) - 1000;
        int y = random.nextInt(1000);
        points[i] = new Point(x, y);
      }
      Arrays.sort(points, new Comparator<Point>() {
        @Override public int compare(Point o1, Point o2) {
          return Line2D.relativeCCW(0, 0, o1.getX(), o1.getY(), o2.getX(),
              o2.getY());
        }
      });
      builder.append(n).append('\n');
      for (int i = 0; i < n; i++) {
        builder.append(points[i].x).append(' ').append(points[i].y)
          .append('\n');
      }
    }
    builder.append("0\n");
    String pat = "\\d+\\.\\d+";
    Object[] pats = new String[cases];
    Arrays.fill(pats, pat);
    ICPCRunner.assertMatches(ProbK.class, builder.toString(), pats);
  }
}
