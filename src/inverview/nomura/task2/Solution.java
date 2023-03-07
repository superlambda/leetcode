package inverview.nomura.task2;

import java.util.HashSet;
import java.util.Set;

public class Solution {
	
//	public int solution(String S) {
//        // write your code in Java SE 8
//		int count=0;
//		Set<Character> set=new HashSet<>();
//		for(int i=0; i< S.length(); i++) {
//			if(!set.contains(S.charAt(i))) {
//				set.add(S.charAt(i));
//			}else {
//				count++;
//				set.clear();
//				set.add(S.charAt(i));
//			}
//		}
//		
//		return count+1;
//    }
	
	public int solution(String S) {
        // write your code in Java SE 8
		int count=0;
		//fast cursor
        int fast=0;
        //slow cursor
		int slow=0;
		boolean findDuplicate=false;
		for(fast=1; fast< S.length(); fast++) {
			for(int i=slow;i<fast;i++) {
				if(S.charAt(i)==S.charAt(fast)) {
					findDuplicate=true;
					break;
				}
			}
			if(findDuplicate) {
				count++;
				slow=fast;
				findDuplicate=false;
			}
		}
		return count+1;
    }

}
