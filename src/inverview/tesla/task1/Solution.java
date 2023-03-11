package inverview.tesla.task1;

public class Solution {
	
	public int solution(String S) {
        // write your code in Java SE 8
		int countB=0, countN=0, countA=0;
		for(int i=0;i<S.length();i++) {
			if(S.charAt(i)=='B') {
				countB++;
			} else if(S.charAt(i)=='N') {
				countN++;
			} else if(S.charAt(i)=='A') {
				countA++;
			}
		}
		return Math.min(countB, Math.min(countN/2, countA/3));
    }

}
