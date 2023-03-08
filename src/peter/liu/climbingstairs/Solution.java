package peter.liu.climbingstairs;

/**
 * You are climbing a stair case. It takes n steps to reach to the top.
 * 
 * Each time you can either climb 1 or 2 steps. In how many distinct ways can
 * you climb to the top?
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public int climbStairs(int n) {
		if (n == 1) {
			return 1;
		}
		if (n == 2) {
			return 2;
		}
		
		int prev1 = 2;
		int prev2 = 1;
		int current=0;
		for (int i = 3; i <= n; i++) {
			current=prev1+prev2;
			prev2=prev1;
			prev1=current;
		}
		return current;
	}

	public int climbStairsWithDP(int n) {
		if (n == 1) {
			return 1;
		}
		int[] dp = new int[n + 1];
		dp[1] = 1;
		dp[2] = 2;
		for (int i = 3; i <= n; i++) {
			dp[i] = dp[i - 1] + dp[i - 2];
		}
		return dp[n];
	}
}
