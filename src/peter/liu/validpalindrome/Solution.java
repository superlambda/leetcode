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
//		LinkedList<Character> list = new LinkedList<>();
//		LinkedList<Character> reverseList = new LinkedList<>();
//		for (char c : s.toCharArray()) {
//			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')
//					|| (c >= '0' && c <= '9')) {
//				list.addLast(c);
//				reverseList.addFirst(c);
//			}
//		}
//		Iterator<Character> ascIterator = list.iterator();
//		Iterator<Character> descIterator = reverseList.iterator();
//		while (ascIterator.hasNext()) {
//			char asc = ascIterator.next();
//			char desc = descIterator.next();
//			if (Character.toLowerCase(asc) != Character.toLowerCase(desc)) {
//				return false;
//			}
//		}
//		return true;
//
//	}
//	public boolean isPalindrome(String s) {
//		LinkedList<Character> list = new LinkedList<>();
//		for (char c : s.toCharArray()) {
//			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')
//					|| (c >= '0' && c <= '9')) {
//				list.addLast(c);
//			}
//		}
//		Character[] array = list.toArray(new Character[list.size()]);
//		for (int i = 0, j = array.length - 1; i < j; i++, j--) {
//			if (Character.toLowerCase(array[i]) != Character
//					.toLowerCase(array[j])) {
//				return false;
//			}
//		}
//		return true;
//
//	}
	
	public boolean isPalindrome(String s) {
		char[] array = s.toCharArray();
		for (int i = 0, j = array.length - 1; i < j; i++, j--) {
			while (!((array[i] >= 'A' && array[i] <= 'Z')
					|| (array[i] >= 'a' && array[i] <= 'z') || (array[i] >= '0' && array[i] <= '9'))&&i<j) {
				i++;
			}
			while (!((array[j] >= 'A' && array[j] <= 'Z')
					|| (array[j] >= 'a' && array[j] <= 'z') || (array[j] >= '0' && array[j] <= '9'))&&i<j) {
				j--;
			}
			if (Character.toLowerCase(array[i]) != Character
					.toLowerCase(array[j])) {
				return false;
			}
		}
		return true;
	}
	
	
}
