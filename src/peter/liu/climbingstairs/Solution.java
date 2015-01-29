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
		}else {
			int first=1;
			int second=1;
			for(int i=2;i<=n;i++){
				int temp=second;
				second=second+first;
				first=temp;
			}
			return second;
		}
	}
}
