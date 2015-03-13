package peter.liu.reversebits;


/**
 * Reverse bits of a given 32 bits unsigned integer.
 * 
 * For example, given input 43261596 (represented in binary as
 * 00000010100101000001111010011100), return 964176192 (represented in binary as
 * 00111001011110000010100101000000).
 * 
 * Follow up: If this function is called many times, how would you optimize it?
 * 
 * Related problem: Reverse Integer
 * 
 * Credits: Special thanks to @ts for adding this problem and creating all test
 * cases.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	// you need treat n as an unsigned value
	public int reverseBits(int n) {
		char[] charAr=Integer.toBinaryString(n).toCharArray();
		StringBuffer s=new StringBuffer();
		for(int i=charAr.length-1;i>=0;i--){
			s.append(charAr[i]);
		}
		int lengthToAdd=32-charAr.length;
		while(lengthToAdd>0){
			s.append("0");
			lengthToAdd--;
		}
		return (int)Long.parseLong(s.toString(),2);
	}

}
