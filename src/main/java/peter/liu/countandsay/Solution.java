package peter.liu.countandsay;

/**
 * The count-and-say sequence is the sequence of integers beginning as follows:
 * 1, 11, 21, 1211, 111221, ...
 * 
 * 1 is read off as "one 1" or 11. 11 is read off as "two 1s" or 21. 21 is read
 * off as "one 2, then one 1" or 1211.
 * 
 * Given an integer n, generate the nth sequence.
 * 
 * Note: The sequence of integers will be represented as a string.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public String countAndSay(int n) {
		if (n == 1) {
			return "1";
		}
		if(n==2){
			return "11";
		}
		char[] charAr = {'1','1'};
		int nth = 2;
		while (nth < n) {
			int count = 1;
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 1; i < charAr.length; i++) {
				if (charAr[i - 1] == charAr[i]) {
					count++;
				} else {
					sBuffer.append(count).append(charAr[i - 1]);
					count = 1;
				}
				if (i == charAr.length - 1) {
					if (charAr[i] != charAr[i - 1]) {
						sBuffer.append(1).append(charAr[i]);
					}else{
						sBuffer.append(count).append(charAr[i]);
					}
				}
			}
			nth++;
			if(nth==n){
				return  sBuffer.toString();
			}
			charAr = sBuffer.toString().toCharArray();
		}
		return "";
	}
}
