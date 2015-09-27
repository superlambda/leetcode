package peter.liu.besttimetobuyandsellstock;

/**
 * Say you have an array for which the ith element is the price of a given stock
 * on day i.
 * 
 * If you were only permitted to complete at most one transaction (ie, buy one
 * and sell one share of the stock), design an algorithm to find the maximum
 * profit.
 * 
 * @author superlambda
 *
 */
public class Solution {
	public int maxProfit(int[] prices) {
		if(prices==null||prices.length==0){
			return 0;
		}
		int[] minimumStock = new int[prices.length];
		int maxmumProfit = 0;
		
		minimumStock[0] = prices[0];
		for (int i = 1; i < prices.length; i++) {
			minimumStock[i] = Math.min(minimumStock[i - 1], prices[i]);
			int profit=prices[i] - minimumStock[i - 1];
			if (maxmumProfit < profit) {
				maxmumProfit = profit;
			}
		}
		return maxmumProfit;
	}
}
