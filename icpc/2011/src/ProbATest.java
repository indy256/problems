import java.util.Arrays;
import java.util.Random;

import junit.framework.TestCase;

public class ProbATest extends TestCase {
  public void testOnProvidedData() {
    String testInput =
        "1 2 2 3 10 20\n1 3 2 3 22 33\n3 2 2 3 4 5\n5 3 2 3 2 3\n0 0 0 0 0 0";
    ICPCRunner.assertMatches(ProbA.class, testInput, "1A 2M", "1M 2A 1M",
        "impossible", "empty");
  }

  public void testOnLargeData() {
    Random rand = new Random(2340893);
    StringBuilder builder = new StringBuilder();
    int cases = 1;
    for (int z = 0; z < cases; z++) {
      int ten9 = 1000000000;
      int a = rand.nextInt(10000) + 1;
      int m = rand.nextInt(10000) + 1;
      int p = rand.nextInt(1000) + 1;
      int q = rand.nextInt(1000) + 1;
      if (p > q) {
        int tmp = p;
        p = q;
        q = tmp;
      }
      int r = rand.nextInt(ten9) + 1;
      int s = rand.nextInt(ten9) + 1;
      if (r > s) {
        int tmp = r;
        r = s;
        s = tmp;
      }
      builder
        .append(a + " " + m + " " + p + " " + q + " " + r + " " + s + "\n");
    }
    builder.append("0 0 0 0 0 0\n");
    Object[] patterns = new Object[cases];
    String op = "[1-9]\\d*+[AM]";
    String pattern = "impossible|empty|" + op + "(?: " + op + ")*+";
    Arrays.fill(patterns, pattern);
    ICPCRunner.assertMatches(ProbA.class, builder.toString(), patterns);
  }
}
