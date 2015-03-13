package peter.liu.numberof1bits;

/**
 * 
 * Write a function that takes an unsigned integer and returns the number of ’1'
 * bits it has (also known as the Hamming weight).
 * 
 * For example, the 32-bit integer ’11' has binary representation
 * 00000000000000000000000000001011, so the function should return 3.
 * 
 * Credits: Special thanks to @ts for adding this problem and creating all test
 * cases.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	// you need to treat n as an unsigned value
	public int hammingWeight(int n) {
		int count=0;
		char[] charAr=Integer.toBinaryString(n).toCharArray();
		for(int i=0;i<charAr.length;i++){
			if(charAr[i]=='1'){
				count++;
			}
		}
		return count;
	}
}
