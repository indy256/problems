import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class ProbETest extends TestCase {
  public void testOnProvidedData() {
    String testInput = "4 4 5 3\n1 1\n1 2\n3 3\n4 4\n2 4\n1\n2\n4\n0 0 0 0\n";
    String expectedOutput = "Case 1:\n3 (3,4)\n4 (2,2)\n5 (3,1)\n";
    assertEquals(expectedOutput, ICPCRunner.runOnInput(ProbE.class, testInput));
  }

  public void testOnLargeData() {
    int dx = 1000;
    int dy = 1000;
    boolean[][] coffee = new boolean[dx][dy];
    int n = 50000;
    for (boolean[] row : coffee)
      Arrays.fill(row, false);
    Random rand = new Random(12257982);
    for (int i = 0; i < n; i++) {
      int x;
      int y;
      do {
        x = rand.nextInt(dx);
        y = rand.nextInt(dy);
      } while (coffee[x][y]);
      coffee[x][y] = true;
    }
    StringBuilder builder = new StringBuilder(n * 11);
    int q = 3;
    builder.append(dx).append(' ').append(dy).append(' ').append(n).append(' ')
      .append(q).append('\n');
    int lowestCoffeeX = -1;
    int lowestCoffeeY = -1;
    for (int j = 0; j < dy; j++) {
      for (int i = 0; i < dx; i++) {
        if (coffee[i][j]) {
          builder.append(i + 1).append(' ').append(j + 1).append('\n');
          if (lowestCoffeeX == -1) {
            lowestCoffeeX = i + 1;
            lowestCoffeeY = j + 1;
          }
        }
      }
    }
    int[] queries = new int[q];
    queries[0] = 5000;
    queries[1] = 0;
    queries[2] = 1000;
    for (int qu : queries)
      builder.append(qu).append('\n');
    builder.append("0 0 0 0\n");
    String output = ICPCRunner.runOnInput(ProbE.class, builder.toString());
    String pattern =
        "Case 1:\n" + pattern(n, 1, 1)
            + pattern(1, lowestCoffeeX, lowestCoffeeY)
            + pattern("\\d+", "\\d+", "\\d+");
    assertTrue(output + "vs.\n" + pattern, Pattern.matches(pattern, output));
  }

  private static String pattern(Object count, Object x, Object y) {
    return count + " \\(" + x + "," + y + "\\)\n";
  }
}
