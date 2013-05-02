import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Scanner;

/**
 * Problem B: "Affine Mess." ICPC World Finals 2011.
 * 
 * The problem looks considerably more complicated than it is. We are guaranteed
 * that the rotation happens first, and that there are only 80 possible angles
 * that could have been used. Pick one of them. Now, the points may map to each
 * other in any order, so pick an order to map the points in, since there are
 * only six. Now, the problem is reduced to finding an integral scaling factor
 * and an integral translation. This amounts to solving a set of linear
 * equations with integer solutions, which is pretty simple, but requires that
 * we deal carefully with conflicts and with nonunique answers. In the end, we
 * can easily afford to exhaustively test all possibilities, so we just do so.
 * 
 * @author Louis Wasserman, Assistant Coach, UChicago "Works in Theory"
 */
public class ProbB {
  private static final AffineTransform[] ROTATIONS = new AffineTransform[80];
  static {
    int i = 0;
    for (int x = -10; x <= 10; x += 20)
      for (int y = -10; y <= 10; y++)
        ROTATIONS[i++] = AffineTransform.getRotateInstance(x, y);
    for (int y = -10; y <= 10; y += 20)
      for (int x = -9; x <= 9; x++)
        ROTATIONS[i++] = AffineTransform.getRotateInstance(x, y);
  }

  private static Point round(Point2D p) {
    return new Point((int) Math.round(p.getX()), (int) Math.round(p.getY()));
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    for (int z = 1;; z++) {
      Point[] p0 = new Point[3];
      Point[] p1 = new Point[3];
      for (int i = 0; i < 3; i++)
        p0[i] = new Point(input.nextInt(), input.nextInt());
      if (p0[0].equals(p0[1])) // only happens when all are zero
        break;
      for (int i = 0; i < 3; i++)
        p1[i] = new Point(input.nextInt(), input.nextInt());
      System.out.print("Case " + z + ": ");
      try {
        AffineTransform answer = deriveFull(p0, p1);
        System.out.println((answer == null) ? "no solution"
            : "equivalent solutions");
      } catch (NotUniqueException e) {
        System.out.println("inconsistent solutions");
      }
    }
  }

  private static class ScaleTranslate1D {
    private final int scale;
    private final int translate;

    ScaleTranslate1D(int scale, int translate) {
      this.scale = scale;
      this.translate = translate;
    }

    public int apply(int x) {
      return scale * x + translate;
    }

    public boolean equals(Object o) {
      if (o instanceof ScaleTranslate1D) {
        ScaleTranslate1D t = (ScaleTranslate1D) o;
        return scale == t.scale && translate == t.translate;
      }
      return false;
    }
  }

  // picks a rotation
  private static AffineTransform deriveFull(Point[] p0, Point[] p1)
      throws NotUniqueException {
    AffineTransform answer = null;
    for (AffineTransform rotate : ROTATIONS) {
      Point[] p0Prime = new Point[3];
      for (int i = 0; i < 3; i++)
        p0Prime[i] = round(rotate.transform(p0[i], null));
      AffineTransform scaleTranslate = derivePerm(p0Prime, p1);
      if (scaleTranslate == null)
        continue;
      AffineTransform trans = new AffineTransform(rotate);
      trans.concatenate(scaleTranslate);
      if (answer == null)
        answer = trans;
      else if (!answer.equals(trans))
        throw new NotUniqueException();
    }
    return answer;
  }

  // picks a permutation
  private static AffineTransform derivePerm(Point[] p0, Point[] p1)
      throws NotUniqueException {
    AffineTransform ans = null;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (i == j)
          continue;
        int k = 0;
        while (k == i || k == j)
          k++;

        AffineTransform tmp = derive(new Point[]{p0[i], p0[j], p0[k]}, p1);
        if (ans == null)
          ans = tmp;
        else if (ans != null && tmp != null && !ans.equals(tmp))
          throw new NotUniqueException();
      }
    }
    return ans;
  }

  // Picks a scale and a translate for both coordinates
  private static AffineTransform derive(Point[] p0, Point[] p1)
      throws NotUniqueException {
    ScaleTranslate1D xTrans = derive(xs(p0), xs(p1));
    ScaleTranslate1D yTrans = derive(ys(p0), ys(p1));
    if (xTrans != null && yTrans != null) {
      AffineTransform ans =
          AffineTransform.getScaleInstance(xTrans.scale, yTrans.scale);
      ans.translate(xTrans.translate, yTrans.translate);
      return ans;
    }
    return null;
  }

  private static int[] ys(Point[] p0) {
    return new int[]{p0[0].y, p0[1].y, p0[2].y};
  }

  private static int[] xs(Point[] p0) {
    return new int[]{p0[0].x, p0[1].x, p0[2].x};
  }

  // Picks a scaling and a translation in one dimension
  private static ScaleTranslate1D derive(int[] x0, int[] x1)
      throws NotUniqueException {
    /*
     * If we ever encounter a valid, unique answer, we return it and set unique
     * to true. If we ever return null, we always return null.
     */
    boolean unique = false;
    ScaleTranslate1D ans = null;
    for (int i = 0; i < 3; i++) {
      for (int j = i + 1; j < 3; j++) {
        try {
          ScaleTranslate1D tmp = derive(x0[i], x0[j], x1[i], x1[j]);
          if (tmp == null || (ans != null && !ans.equals(tmp))) {
            return null;
          } else {
            ans = tmp;
            unique = true;
          }
        } catch (NotUniqueException e) {
        }
      }
    }

    if (unique) {
      for (int i = 0; i < 3; i++)
        assert ans.apply(x0[i]) == x1[i];
      return ans;
    }
    throw new NotUniqueException();
  }

  @SuppressWarnings("serial") private static class NotUniqueException extends
      Exception {
  }

  // returns null if impossible
  private static ScaleTranslate1D derive(int x00, int x01, int x10, int x11)
      throws NotUniqueException {
    // a*x00 + b == x10;
    // a*x01+b== x11;
    int diff0 = x01 - x00;
    int diff1 = x11 - x10;
    ScaleTranslate1D ans;
    if (diff0 == 0 && diff1 == 0)
      throw new NotUniqueException();
    else if (diff0 == 0 || (diff1 % diff0) != 0)
      return null;
    else {
      int scale = diff1 / diff0;
      int translate = x10 - scale * x00;
      ans = new ScaleTranslate1D(scale, translate);
    }
    assert ans.apply(x00) == x10;
    assert ans.apply(x01) == x11;
    return ans;
  }
}