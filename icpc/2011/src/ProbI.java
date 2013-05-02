import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.util.Scanner;

/**
 * Problem I: "Mummy Madness." ICPC World Finals 2011.
 * 
 * Let d(p0, p1) be the L_infinity norm. The objective is to find the point p
 * maximizing d(0, p) while satisfying d(0, p) <= d(m_i, p) for each mummy m_i.
 * 
 * While I'm sure more refined approaches to this problem exist, my approach is
 * relatively simplistic. For each mummy, I construct, as a java.awt.geom.Area,
 * the region in the plane which is closer (in the L_infinity norm) to the
 * origin than to the mummy. I intersect each of these Areas, which is
 * reasonably efficient, since each Area is bounded by O(1) line segments.
 * 
 * Once I have the resulting Area, which is actually a polygon, the maximum
 * distance between the origin and a point in the polygon is simply the maximum
 * x or y coordinate, in absolute value. This can be easily computed by getting
 * the bounding box of the Area.
 * 
 * @author Louis Wasserman, Assistant Coach, UChicago "Works in Theory"
 */
public class ProbI {
  private static final int MAX = 2000000;

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    for (int z = 1;; z++) {
      int n = input.nextInt();
      if (n < 0)
        break;
      Area answer = new Area(new Rectangle(-MAX, -MAX, 2 * MAX, 2 * MAX));
      for (int i = 0; i < n; i++) {
        Point p = new Point(input.nextInt(), input.nextInt());
        Area closer = closerToOrigin(p);
        if (!closer.contains(answer.getBounds2D()))
          answer.intersect(closer);
      }
      Rectangle2D bounds2d = answer.getBounds2D();
      int ans =
          (int) max(bounds2d.getMaxX(), -bounds2d.getMinX(),
              bounds2d.getMaxY(), -bounds2d.getMinY());
      System.out.println("Case " + z + ": "
          + ((ans == MAX) ? "never" : Integer.toString(ans)));
    }
  }

  private static double max(double... vals) {
    double max = Double.NEGATIVE_INFINITY;
    for (double d : vals)
      max = Math.max(max, d);
    return max;
  }

  /**
   * Returns the Area of points that are closer to the origin than to the
   * specified point in the L_\infty metric. Since we can't have infinite Areas,
   * we restrict it to the rectangle of points that are within MAX of the
   * origin.
   */
  private static Area closerToOrigin(Point2D p) {
    AffineTransform trans = new AffineTransform();
    if (Math.abs(p.getX()) > Math.abs(p.getY()))
      trans.concatenate(new AffineTransform(new double[]{0, 1, 1, 0}));
    if (p.getX() < 0)
      trans.scale(-1, 1);
    if (p.getY() < 0)
      trans.scale(1, -1);
    try {
      p = trans.inverseTransform(p, null);
    } catch (NoninvertibleTransformException e) {
      throw new RuntimeException(e);
    }

    Path2D path = new Path2D.Double();
    // We've guaranteed that 0 <= x <= y, with y > 0.  Now we do the casework
    // for the possibilities 0 < x = y, 0 < x < y, and 0 = x < y.
    if (p.getX() == p.getY()) {
      path.moveTo(-MAX, -MAX);
      path.lineTo(MAX, -MAX);
      path.lineTo(MAX, -p.getX());
      path.lineTo(-p.getX(), MAX);
      path.lineTo(-MAX, MAX);
      path.closePath();
    } else if (0 < p.getX() && p.getX() < p.getY()) {
      path.moveTo(-MAX, -MAX);
      path.lineTo(MAX, -MAX);
      double x1 = p.getX();
      double y1 = p.getY();
      path.lineTo(MAX, y1 - MAX);
      path.lineTo(y1 / 2, y1 / 2);
      path.lineTo(x1 - y1 / 2, y1 / 2);
      path.lineTo(x1 - MAX, MAX);
      path.lineTo(-MAX, MAX);
      path.closePath();
    } else {
      assert p.getX() == 0;
      path.moveTo(-MAX, -MAX);
      path.lineTo(MAX, -MAX);
      path.lineTo(MAX, MAX);
      double y12 = p.getY() / 2;
      path.lineTo(y12, y12);
      path.lineTo(-y12, y12);
      path.lineTo(-MAX, MAX);
      path.closePath();
    }
    path.transform(trans);
    return new Area(path);
  }
}
