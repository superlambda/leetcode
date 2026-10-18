package peter.liu.triangle;

import java.util.List;

/**
 * Given a triangle, find the minimum path sum from top to bottom. Each step you may move to adjacent numbers on the row below.

For example, given the following triangle
[
     [2],
    [3,4],
   [6,5,7],
  [4,1,8,3]
]
The minimum path sum from top to bottom is 11 (i.e., 2 + 3 + 5 + 1 = 11).

Note:
Bonus point if you are able to do this using only O(n) extra space, where n is the total number of rows in the triangle.
 * @author superlambda
 *
 */
public class Solution {
	
	public int minimumTotal(List<List<Integer>> triangle) {
		int[] array = new int[triangle.size()+1];
	    for(int i=triangle.size()-1;i>=0;i--){
	    	List<Integer> list=triangle.get(i);
	        for(int j=0;j<list.size();j++){
	        	array[j] = Math.min(array[j],array[j+1])+list.get(j);
	        }
	    }
	    return array[0];
    }

}
