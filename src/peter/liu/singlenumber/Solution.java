package peter.liu.singlenumber;


/**
 * Given an array of integers, every element appears twice except for one. Find
 * that single one.
 * 
 * Note: Your algorithm should have a linear runtime complexity. Could you
 * implement it without using extra memory?
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public int singleNumber(int[] A) {
		int result = 0; 
//		Bitwise	bitwise AND	&
//		Bitwise exclusive OR	^
//		Bitwise inclusive OR	|
        for (int i : A){
        	result ^= i ; 
        }
        return result ; 
	}

}
