package peter.liu.trappingrainwater;

/**
 * 
 * Given n non-negative integers representing an elevation map where the width
 * of each bar is 1, compute how much water it is able to trap after raining.
 * 
 * For example, Given [0,1,0,2,1,0,1,3,2,1,2,1], return 6.
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public int trap(int[] height) {
		int i=0;
		int sum=0;
		while(i<height.length){
			if(height[i]==0){
				i++;
				continue;
			}
			int fPointer=i+1;
			int tempSum=0;
			int tempHighest=0;
			int tempHighestIndex= 0;
			while(fPointer<height.length&&height[fPointer]<height[i]){
				tempSum+=height[i]-height[fPointer];
				if(height[fPointer]>=tempHighest){
					tempHighest=height[fPointer];
					tempHighestIndex=fPointer;
				}
				fPointer++;
			}
			
			if(tempSum>0&&fPointer<height.length){
				sum= sum+ tempSum;
				i=fPointer;
			}else{
				boolean tempHighestFound=tempHighestIndex>0;
				for(int j=i+1;j<tempHighestIndex;j++){
					sum+=tempHighest-height[j];
				}
				if(tempHighestFound){
					i=tempHighestIndex;
				}else{
					i+=1;
				}
				
			}
		}
		return sum;

	}
}
