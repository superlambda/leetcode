package peter.liu.reverseinteger;

/**
 * Reverse digits of an integer.
 * 
 * Example1: x = 123, return 321 Example2: x = -123, return -321
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public int reverse(int x) {
		long reverse=0;
		int left=x;
		while(left!=0){
			reverse=reverse*10+left%10;
			if(reverse>Integer.MAX_VALUE||reverse<Integer.MIN_VALUE){
				return 0;
			}
			left=left/10;
		}
		return (int)reverse;
	}
}
