import java.util.Arrays;
import java.util.Random;

import junit.framework.TestCase;

public class ProbJTest extends TestCase {
  public void testOnProvidedData() {
    String testInput = "29\n28\n0\n";
    ICPCRunner.assertMatches(ProbJ.class, testInput, "3H 3L 2H", "impossible");
  }

  public void testOnLargeData() {
    StringBuilder builder = new StringBuilder();
    Random random = new Random(1243913);
    int cases = 1;
    for (int i = 0; i < cases; i++) {
      builder.append(random.nextInt(1000000) + 1).append('\n');
    }
    builder.append("0\n");
    String pyr = "\\d+[LH]";
    String pat = "impossible|" + pyr + "(?: " + pyr + ")*+";
    Object[] pats = new Object[cases];
    Arrays.fill(pats, pat);
    ICPCRunner.assertMatches(ProbJ.class, builder.toString(), pats);
  }
}
