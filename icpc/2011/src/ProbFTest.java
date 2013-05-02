import java.util.Random;

import junit.framework.TestCase;

public class ProbFTest extends TestCase {
  public void testOnSingleMachineInput() {
    ICPCRunner.assertMatches(ProbF.class, "1 1 10\n1 1 1 1\n0 0 0", 10);
  }

  public void testOnProvidedData() {
    ICPCRunner
      .assertMatches(
          ProbF.class,
          "6 10 20\n6 12 1 3\n1 9 1 2\n3 2 1 2\n8 20 5 4\n4 11 7 4\n2 10 9 1\n0 0 0",
          44);
  }

  public void testOnLargeData() {
    Random rand = new Random(1290310);
    StringBuilder builder = new StringBuilder();
    int n = 10000;
    int ten9 = 1000000000;
    int c = rand.nextInt(ten9 + 1);
    int d = ten9;
    append(builder, n, c, d);
    for (int i = 0; i < n; i++) {
      int price = rand.nextInt(ten9 - 2) + 2;
      int resale = rand.nextInt(price - 1) + 1;
      append(builder, rand.nextInt(d) + 1, price, resale,
          rand.nextInt(ten9) + 1);
    }
    append(builder, 0, 0, 0);
    ICPCRunner.assertMatches(ProbF.class, builder.toString(), "\\d+");
  }

  static StringBuilder append(StringBuilder builder, int... ints) {
    for (int i = 0; i < ints.length; i++) {
      if (i > 0)
        builder.append(' ');
      builder.append(ints[i]);
    }
    return builder.append('\n');
  }
}
