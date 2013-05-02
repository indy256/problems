import junit.framework.TestCase;

public class ProbBTest extends TestCase {
  private static final String EQUIV = "equivalent solutions";
  private static final String INCONST = "inconsistent solutions";
  private static final String NONE = "no solution";

  public void testOnProvidedData() {
    String testInput =
        "3 0 4 0 1 4\n-2 -4 -1 3 3 -4\n0 1 1 1 2 1\n1 2 2 2 3 2\n"
            + "1 0 2 0 3 0\n3 3 1 1 2 2\n1 0 2 0 3 0\n3 2 1 1 2 2\n"
            + "2 3 0 6 1 2\n2 3 0 6 1 2\n0 0 0 0 0 0\n";
    ICPCRunner.assertMatches(ProbB.class, testInput, EQUIV, INCONST, NONE,
        INCONST, EQUIV);
  }
}
