import java.util.Arrays;
import java.util.Scanner;

/**
 * Problem F: "Machine Works." ICPC World Finals 2011.
 * 
 * This problem is solvable with dynamic programming, but takes some thought to
 * solve correctly.
 * 
 * First, we only own at most one machine at once. Without loss of generality,
 * we always own exactly one machine: we start out with zero money, and machine
 * that generates zero profit but has a resale value of C. On the last day, we
 * buy a free machine with zero resale value and zero profit.
 * 
 * Suppose we bought machine M_i on day D_i, after which we had X dollars. We
 * have not sold it since. Then we can determine, in constant time, the money we
 * will have on day t if we sell M_i.
 * 
 * The recurrence, then, is as follows. Let the machines be sorted in ascending
 * order of available day. We let dp[i] be the money we have on day D_i, after
 * buying machine M_i. Then
 * <code>dp[i] = max {dp[j] + R_j + G_j*(D_i - D_j - 1) | j <= i} - P_i</code>.
 * The final answer, then, is the money we have after buying the dummy machine
 * on day D+1.
 * 
 * In total, this algorithm takes O(N^2), which is easily manageable.
 * 
 * @author Louis Wasserman, Assistant Coach, UChicago "Works in Theory"
 */
public class ProbF {
  static final class Machine implements Comparable<Machine> {
    private final int availDay;
    private final long dailyProfit;
    private final long price;
    private final long resalePrice;

    Machine(int availDay, long price, long resalePrice, long dailyProfit) {
      this.availDay = availDay;
      this.dailyProfit = dailyProfit;
      this.price = price;
      this.resalePrice = resalePrice;
    }

    @Override public int compareTo(Machine o) {
      return availDay - o.availDay;
    }

    public long valueWhenSold(int d) {
      return dailyProfit * (d - availDay - 1) + resalePrice;
    }
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    for (int z = 1;; z++) {
      int n = input.nextInt(), c = input.nextInt(), d = input.nextInt();
      if ((n | c | d) == 0)
        break;
      n += 2;
      Machine[] machines = new Machine[n];
      machines[0] = new Machine(0, 0, c, 0);
      machines[1] = new Machine(d + 1, 0, 0, 0);
      for (int i = 2; i < n; i++)
        machines[i] =
            new Machine(input.nextInt(), input.nextInt(), input.nextInt(),
                input.nextInt());
      Arrays.sort(machines);
      long[] dp = new long[n];
      dp[0] = 0;
      for (int i = 1; i < n; i++) {
        int day = machines[i].availDay;
        long bestValue = Integer.MIN_VALUE;
        for (int j = 0; j < i; j++)
          bestValue =
              Math.max(bestValue, dp[j] + machines[j].valueWhenSold(day));
        bestValue -= machines[i].price;
        if (bestValue < 0)
          bestValue = Integer.MIN_VALUE;
        dp[i] = bestValue;
      }
      System.out.println("Case " + z + ": " + dp[n - 1]);
    }
  }
}
