package peter.liu.validpalindrome;


/**
 * Given a string, determine if it is a palindrome, considering only
 * alphanumeric characters and ignoring cases.
 * 
 * For example, "A man, a plan, a canal: Panama" is a palindrome. "race a car"
 * is not a palindrome.
 * 
 * Note: Have you consider that the string might be empty? This is a good
 * question to ask during an interview.
 * 
 * For the purpose of this problem, we define empty string as valid palindrome.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
//	public boolean isPalindrome(String s) {
//		char[] array = s.toCharArray();
//		for (int i = 0, j = array.length - 1; i < j; i++, j--) {
//			while (!((array[i] >= 'A' && array[i] <= 'Z')
//					|| (array[i] >= 'a' && array[i] <= 'z') || (array[i] >= '0' && array[i] <= '9'))&&i<j) {
//				i++;
//			}
//			while (!((array[j] >= 'A' && array[j] <= 'Z')
//					|| (array[j] >= 'a' && array[j] <= 'z') || (array[j] >= '0' && array[j] <= '9'))&&i<j) {
//				j--;
//			}
//			if (Character.toLowerCase(array[i]) != Character
//					.toLowerCase(array[j])) {
//				return false;
//			}
//		}
//		return true;
//	}
	
	public boolean isPalindrome(String s) {
		
		int start=0;
		int end=s.length()-1;
		while(start<s.length()&&end>=0&&start<end) {
			while(start<s.length()&&!isaAlphanumeric(s.charAt(start))) {
				start++;
			}
			while(end>=0&& !isaAlphanumeric(s.charAt(end))) {
				end--;
			}
			if(start==s.length()||end==-1) {
				return true;
			}
			if(Character.toLowerCase(s.charAt(start))!=Character.toLowerCase(s.charAt(end))) {
				return false;
			}
			start++;
			end--;
		}
		return true;
        
    }
	
	private boolean isaAlphanumeric(char c){
		return Character.isDigit(c)||Character.isLetter(c);
	}
	
	
}
