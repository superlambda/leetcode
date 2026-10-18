package peter.liu.palindromepartitioningII;

/**
 * Given a string s, partition s such that every substring of the partition is a
 * palindrome.
 * 
 * Return the minimum cuts needed for a palindrome partitioning of s.
 * 
 * For example, given s = "aab", Return 1 since the palindrome partitioning
 * ["aa","b"] could be produced using 1 cut.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public int minCut(String s) {
		int n = s.length();
		if(n<2|| isPalindrome(s)){
			return 0;
		}
		char[] charArray=s.toCharArray();
        int[] cut =new int[n+1];  // number of cuts for the first k characters
        for (int i = 0; i <= n; i++) {
        	cut[i] = i-1;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; i-j >= 0 && i+j < n && charArray[i-j]==charArray[i+j] ; j++) {// odd length palindrome
                cut[i+j+1] = Math.min(cut[i+j+1],1+cut[i-j]);
            }

            for (int j = 1; i-j+1 >= 0 && i+j < n && charArray[i-j+1] == charArray[i+j]; j++){ // even length palindrome
                cut[i+j+1] = Math.min(cut[i+j+1],1+cut[i-j+1]);
            }
        }
        return cut[n];
	}
	private boolean isPalindrome(String s){
		return s.equals(new StringBuffer(s).reverse().toString());
	}
}
