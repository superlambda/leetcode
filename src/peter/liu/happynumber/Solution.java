package peter.liu.happynumber;

import java.util.HashSet;
import java.util.Set;

/**
 * Write an algorithm to determine if a number is "happy".
 * 
 * A happy number is a number defined by the following process: Starting with
 * any positive integer, replace the number by the sum of the squares of its
 * digits, and repeat the process until the number equals 1 (where it will
 * stay), or it loops endlessly in a cycle which does not include 1. Those
 * numbers for which this process ends in 1 are happy numbers.
 * 
 * Example: 19 is a happy number
 * 
 * 12 + 92 = 82 82 + 22 = 68 62 + 82 = 100 12 + 02 + 02 = 1
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public boolean isHappy(int n) {
		Set<Integer> resultSet=new HashSet<>();
		while(!resultSet.contains(n)){
			resultSet.add(n);
			int result=0;
			while(n>0){
				result+=(n%10)*(n%10);
				n/=10;
			}
			if(result==1){
				return true;
			}
			n=result;
		}
		return false;
	}

}
