package peter.liu.candy;

/**
 * 
 * There are N children standing in a line. Each child is assigned a rating
 * value.
 * 
 * You are giving candies to these children subjected to the following
 * requirements:
 * 
 * Each child must have at least one candy. Children with a higher rating get
 * more candies than their neighbors. What is the minimum candies you must give?
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	
	public int candy(int[] ratings) {
		
		 if(ratings.length<=1){
			 return ratings.length;
		 }
			 
		 int[] candies =new int[ratings.length];
		 candies[0]=1;
		 for (int i = 1; i < ratings.length; i++) {
			 if(ratings[i]>ratings[i-1]){
				 candies[i]=candies[i-1]+1;
			 }else{
				 candies[i]=1;
			 }
		 }
		 for (int i= ratings.length-1; i>0 ; i--){
			 if(ratings[i-1]>ratings[i]){
				 candies[i-1]=Math.max(candies[i]+1,candies[i-1]);
			 }
		 }
		 int result=0;
		 for (int i = 0; i < candies.length; i++) {
			 result+=candies[i];
		 }
		 return result;
    }

}
