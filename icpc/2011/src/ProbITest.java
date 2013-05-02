import java.util.Random;

import junit.framework.TestCase;

public class ProbITest extends TestCase {
  public void testOnProvidedData() {
    String testInput = "4\n-3 5\n3 4\n-6 -2\n1 -5\n1\n0 -1\n-1\n";
    ICPCRunner.assertMatches(ProbI.class, testInput, 4, "never");
  }

  public void testOnZeroMummies() {
    ICPCRunner.assertMatches(ProbI.class, "0\n-1\n", "never");
  }

  public void testOnLargeUniformData() {
    StringBuilder builder = new StringBuilder(100000);
    int n = 10000;
    builder.append(n).append('\n');
    Random random = new Random(135872);
    for (int i = 0; i < n; i++) {
      int x = random.nextInt(2000001) - 1000000;
      int y = random.nextInt(2000001) - 1000000;
      builder.append(x).append(' ').append(y).append('\n');
    }
    builder.append(-1);
    ICPCRunner.assertMatches(ProbI.class, builder.toString(), "\\d+");
  }

  public void testOnLargeGaussianData() {
    StringBuilder builder = new StringBuilder(100000);
    int n = 10000;
    builder.append(n).append('\n');
    Random random = new Random(135872);
    for (int i = 0; i < n; i++) {
      int x =
          Math.max(-1000000,
              Math.min(1000000, (int) (10000 * random.nextGaussian())));
      int y =
          Math.max(-1000000,
              Math.min(1000000, (int) (10000 * random.nextGaussian())));
      builder.append(x).append(' ').append(y).append('\n');
    }
    builder.append(-1);
    ICPCRunner.assertMatches(ProbI.class, builder.toString(), "\\d+");
  }
}
