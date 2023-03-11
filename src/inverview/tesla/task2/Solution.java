package inverview.tesla.task2;

public class Solution {

	public int solution(String S) {
		int countOfAUntilNow = 0;
		int i = 0;
		int maxNumberOfA = 0;
		while (i < S.length()) {
			if (S.charAt(i) == 'a') {
				if (countOfAUntilNow == 2) {
					return -1;
				} else {
					countOfAUntilNow++;
				}
			} else {
				maxNumberOfA = maxNumberOfA + 2 - countOfAUntilNow;
				countOfAUntilNow = 0;

			}
			i++;
			if(i==S.length()&&S.charAt(i-1)!='a') {
				maxNumberOfA +=2;
			}
		}
		return maxNumberOfA;
	}

}
