import java.util.Arrays;
import java.util.Scanner;

/**
 * Problem A: "To Add or to Multiply." ICPC World Finals 2011.
 * 
 * <p>
 * Consider the result of the sequence
 * <code>c_t A,1 M,c_{t-1} A, 1 M,...,c_1 A, 1 M, c_0</code> when applied to
 * input <code>x</code>. A little thinking reveals that the result is
 * <code>x*m^t + a*(c_0 + c_1*m + ... + c_t * m^t)</code>.
 * 
 * <p>
 * We can conclude immediately that <code>(q-p)*m^t <= s-r</code>, so in
 * particular, <code>t <= log_m (s-r)</code>, so if <code>m>1</code>, then
 * <code>t</code> is at most 30. Therefore, we can afford to exhaustively
 * iterate over all values of t.
 * 
 * <p>
 * For any fixed value of <code>t</code>, suppose we've determined that we want
 * the result when applied to <code>x</code> to be <code>x*m^t + a*b</code>. We
 * write <code>b</code> in base <code>m</code>, and if it cannot fit in
 * <code>t+1</code> digits, we put the overflow in <code>c_t</code>.
 * 
 * <p>
 * The cost of this machine is <code>t + c_0 + c_1 + ... + c_t</code>.
 * Therefore, it is to our advantage to have as many zeroes in the base-m
 * representation as possible. We find lower and upper bounds on b, by
 * substituting in p and q, and we find the number between these bounds with the
 * most zeroes in its base-m representation. Clearly, the resulting sequence is
 * optimal for this fixed value of <code>t</code>.
 * 
 * <p>
 * Since we can afford to iterate over all possible values of <code>t</code>,
 * and finding the optimal sequence for any given <code>t</code> takes O(t)
 * time, the whole thing takes O(t^2) = O(log_m(s-r)^2).
 * 
 * @author Louis Wasserman, Assistant Coach, UChicago "Works in Theory"
 */
public class ProbA {
  static long a;
  static long m;

  static class Answer implements Comparable<Answer> {
    private long cost;
    private long[] bDigits;
    private int t;

    public Answer(long[] bDigits) {
      this.bDigits = bDigits;
      t = bDigits.length - 1;
      for (int i = 0, j = t; i < j; i++, j--) {
        long tmp = bDigits[i];
        bDigits[i] = bDigits[j];
        bDigits[j] = tmp;
      }
      cost = t;
      for (long d : bDigits)
        cost += d;
    }

    public long apply(long k) {
      for (long digit : bDigits)
        k = m * (k + a * digit);
      return k / m;
    }

    public int compareTo(Answer a) {
      long cmp = cost - a.cost;
      for (int j = 0; cmp == 0 && j < bDigits.length && j < a.bDigits.length; j++)
        cmp = bDigits[j] - a.bDigits[j];
      if (cmp == 0)
        return t - a.t;
      else
        return (cmp > 0) ? 1 : -1; // cmp is a long, we must return int
    }

    public String toString() {
      StringBuilder builder = new StringBuilder();
      long mults = 0;
      for (long digit : bDigits) {
        if (digit != 0) {
          if (mults != 0)
            builder.append(' ').append(mults).append('M');
          mults = 0;
          builder.append(' ').append(digit).append('A');
        }
        mults++;
      }
      if (--mults > 0)
        builder.append(' ').append(mults).append('M');
      String ans = builder.toString();
      return ans.length() > 0 ? ans.trim() : "empty";
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    for (int z = 1;; z++) {
      a = input.nextInt();
      m = input.nextInt();
      long p = input.nextInt(), q = input.nextInt(), r = input.nextInt(), s =
          input.nextInt();
      if ((a | m | p | q | r | s) == 0)
        break;
      long mt = 1;
      Answer best = null;
      for (int t = 0;; t++, mt *= m) {
        long bMin = (r + a - 1 - p * mt) / a, bMax = (s - q * mt) / a;
        if (bMax < bMin || bMax < 0 || bMin < 0 || (t > 0 && m == 1))
          break;
        Answer ans =
            new Answer(
                inBetween(digits(bMin, m, t + 1), digits(bMax, m, t + 1)));
        if (ans.apply(p) >= r && ans.apply(q) <= s
            && (best == null || ans.compareTo(best) < 0))
          best = ans;
      }
      System.out.println("Case " + z + ": "
          + ((best == null) ? "impossible" : best.toString()));
    }
  }

  /**
   * If two numbers are specified in a given base, finds the number between them
   * with the fewest nonzero digits.
   */
  private static long[] inBetween(long[] d1, long[] d2) {
    int d = d1.length;
    long[] digits = new long[d];
    for (int i = d - 1; i >= 0; i--) {
      if (d1[i] != d2[i]) {
        boolean allZero = true;
        for (int j = 0; j < i && allZero; j++)
          allZero = allZero && d1[j] == 0;
        digits[i] = d1[i] + (allZero ? 0 : 1);
        Arrays.fill(digits, 0, i, 0);
        return digits;
      }
      digits[i] = d1[i];
    }
    return digits;
  }

  /**
   * Converts a to a representation in the specified base, with at most d
   * digits.  Overflow is put into the most significant digit.
   */
  private static long[] digits(long a, long base, int d) {
    long[] digits = new long[d];
    assert a >= 0;
    for (int i = 0; i < d - 1; i++, a /= base) {
      digits[i] = a % base;
      assert digits[i] >= 0;
    }
    digits[d - 1] = a;
    assert a >= 0;
    return digits;
  }
}
