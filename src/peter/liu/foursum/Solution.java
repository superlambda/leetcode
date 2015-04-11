package peter.liu.foursum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Given an array S of n integers, are there elements a, b, c, and d in S such
 * that a + b + c + d = target? Find all unique quadruplets in the array which
 * gives the sum of target.
 * 
 * Note: Elements in a quadruplet (a,b,c,d) must be in non-descending order.
 * (ie, a ≤ b ≤ c ≤ d) The solution set must not contain duplicate quadruplets.
 * For example, given array S = {1 0 -1 0 -2 2}, and target = 0.
 * 
 * A solution set is: (-1, 0, 0, 1) (-2, -1, 1, 2) (-2, 0, 0, 2)
 * 
 * @author superlambda
 *
 */
public class Solution {

	public List<List<Integer>> fourSum(int[] num, int target) {
		Arrays.sort(num);
		List<List<Integer>> results = new ArrayList<>();
		Set<String> uniqueCheckSet = new HashSet<String>();
		for (int i = 0; i < num.length - 3; i++) {
			for (int j = i + 1; j < num.length - 2; j++) {
				if (j == i + 1 || (j > i + 1 && num[j] != num[j - 1])) {
					int sum = target - num[i] - num[j];
					int innerLow = j + 1;
					int innerHigh = num.length - 1;
					while (innerLow < innerHigh) {
						if (num[innerLow] + num[innerHigh] == sum) {
							String checkString = num[i] + "," + num[j] + ","
									+ num[innerLow] + "," + num[innerHigh];
							if (!uniqueCheckSet.contains(checkString)) {
								uniqueCheckSet.add(checkString);
								results.add(Arrays.asList(num[i], num[j],
										num[innerLow], num[innerHigh]));
							}
							while (innerLow < innerHigh
									&& num[innerLow] == num[innerLow + 1]) {
								innerLow++;
							}
							while (innerLow < innerHigh
									&& num[innerHigh] == num[innerHigh - 1]) {
								innerHigh--;
							}
							innerLow++;
							innerHigh--;
						} else if (num[innerLow] + num[innerHigh] < sum) {
							innerLow++;
						} else {
							innerHigh--;
						}
					}
				}

			}

		}
		return results;

	}
}
