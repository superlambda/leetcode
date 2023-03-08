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
		int[] minmumStock = new int[prices.length];
		int maxProfit = 0;
		minmumStock[0] = prices[0];
		for (int i = 1; i < prices.length; i++) {
			minmumStock[i] = Math.min(minmumStock[i - 1], prices[i]);
			if (maxProfit < prices[i] - minmumStock[i - 1]) {
				maxProfit = prices[i] - minmumStock[i - 1];
			}
		}
		return maxProfit;
	}
	
	
	public int maxProfitWithoutArray(int[] prices) {
		
		int minmumStock = Integer.MAX_VALUE;
		int maxProfit = 0;
		
		for(int i=0; i<prices.length;i++) {
			if(prices[i]<minmumStock) {
				minmumStock =prices[i];
			}
			if(prices[i] - minmumStock>maxProfit) {
				maxProfit = prices[i] - minmumStock;
			}
		}
		
		return maxProfit;
	}

}
