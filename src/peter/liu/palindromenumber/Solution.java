package peter.liu.palindromenumber;

/**
 * Determine whether an integer is a palindrome. Do this without extra space.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public boolean isPalindrome(int x) {
		if(x<0){
			return false;
		}
		int reverse=0;
		int j=x;
		while(j!=0){
			reverse=reverse*10+j%10;
			j=j/10;
		}
		return reverse==x;
	}
}
