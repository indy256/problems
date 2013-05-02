import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ICPCTestSuite extends TestCase {
  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ProbATest.class);
    suite.addTestSuite(ProbBTest.class);
    suite.addTestSuite(ProbETest.class);
    suite.addTestSuite(ProbFTest.class);
    suite.addTestSuite(ProbHTest.class);
    suite.addTestSuite(ProbITest.class);
    suite.addTestSuite(ProbJTest.class);
    suite.addTestSuite(ProbKTest.class);
    return suite;
  }
}
