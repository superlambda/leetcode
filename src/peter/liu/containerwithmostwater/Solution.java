package peter.liu.containerwithmostwater;

/**
 * Given n non-negative integers a1, a2, ..., an, where each represents a point
 * at coordinate (i, ai). n vertical lines are drawn such that the two endpoints
 * of line i is at (i, ai) and (i, 0). Find two lines, which together with
 * x-axis forms a container, such that the container contains the most water.
 * 
 * Note: You may not slant the container.
 * 
 * @author pcnsh197
 *
 */

public class Solution {
	/**
	 * Here is the proof. Proved by contradiction:
	 * 
	 * Suppose the returned result is not the optimal solution. Then there must
	 * exist an optimal solution, say a container with aol and aor (left and
	 * right respectively), such that it has a greater volume than the one we
	 * got. Since our algorithm stops only if the two pointers meet. So, we must
	 * have visited one of them but not the other. WLOG, let's say we visited
	 * aol but not aor. When a pointer stops at a_ol, it won't move until
	 * 
	 * The other pointer also points to aol. In this case, iteration ends. But
	 * the other pointer must have visited aor on its way from right end to aol.
	 * Contradiction to our assumption that we didn't visit aor.
	 * 
	 * The other pointer arrives at a value, say arr, that is greater than aol
	 * before it reaches aor. In this case, we does move aol. But notice that
	 * the volume of aol and arr is already greater than aol and aor (as it is
	 * wider and higher), which means that aol and aor is not the optimal
	 * solution -- Contradiction!
	 * 
	 * Both cases arrive at a contradiction.
	 * 
	 * @param height
	 * @return
	 */
	public int maxArea(int[] height) {
		int maxArea = 0;
		int i = 0, j = height.length - 1;
		while (i < j) {
			maxArea = Math.max(maxArea,
					(j - i) * Math.min(height[i], height[j]));
			if (height[i] > height[j]) {
				j--;
			} else if (height[i] <= height[j]) {
				i++;
			}
		}
		return maxArea;
	}
}
