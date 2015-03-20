package peter.liu.threesum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given an array S of n integers, are there elements a, b, c in S such that a +
 * b + c = 0? Find all unique triplets in the array which gives the sum of zero.
 * 
 * Note:
 * 
 * Elements in a triplet (a,b,c) must be in non-descending order. (ie, a ≤ b ≤
 * c) The solution set must not contain duplicate triplets.
 * 
 * For example, given array S = {-1 0 1 2 -1 -4},
 * 
 * A solution set is: (-1, 0, 1) (-1, -1, 2)
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public List<List<Integer>> threeSum(int[] num) {
		Arrays.sort(num);
		List<List<Integer>> results = new ArrayList<>();
		for(int i=0;i<num.length-2;i++){
			if(i==0||(i>0&&num[i]!=num[i-1])){
				int low=i+1;
				int high=num.length-1;
				int sum=0-num[i];
				while(low<high){
					if(num[low]+num[high]==sum){
						results.add(Arrays.asList(num[i],num[low],num[high]));
						while(low<high&&num[low]==num[low+1]){
							low++;
						}
						while(low<high&&num[high]==num[high-1]){
							high--;
						}
						low++;
						high--;
					}else if(num[low]+num[high]<sum){
						low++;
					}else{
						high--;
					}
				}
			}
		}
		return results;
		
	}
	// public List<List<Integer>> threeSum(int[] num) {
	// List<Integer> negativeList = new ArrayList<>();
	// List<Integer> positiveList = new ArrayList<>();
	// Set<Integer> firstSet = new TreeSet<>();
	// Set<Integer> secondSet = new TreeSet<>();
	// for (int i = 0; i < num.length; i++) {
	// if (!firstSet.contains(num[i])) {
	// firstSet.add(num[i]);
	// if (num[i] < 0) {
	// negativeList.add(num[i]);
	// } else {
	// positiveList.add(num[i]);
	// }
	// } else if (!secondSet.contains(num[i])) {
	// secondSet.add(num[i]);
	// if (num[i] < 0) {
	// negativeList.add(num[i]);
	// } else {
	// positiveList.add(num[i]);
	// }
	// }else if(num[i]==0){
	// negativeList.add(num[i]);
	// }
	// }
	// if(negativeList.isEmpty()||positiveList.isEmpty()){
	// return new ArrayList<>();
	// }
	// Integer[] negativeArray = negativeList.toArray(new Integer[negativeList
	// .size()]);
	// Integer[] positiveArray = positiveList.toArray(new Integer[positiveList
	// .size()]);
	// Arrays.sort(negativeArray);
	// Arrays.sort(positiveArray);
	//
	// List<List<Integer>> results = new ArrayList<>();
	// Set<String> uniqueSet = new TreeSet<>();
	// for (int i = 0; i < negativeArray.length; i++) {
	// if (i > 0 && negativeArray[i] == negativeArray[i - 1]) {
	// continue;
	// }
	// for (int j = positiveArray.length - 1; j > 0; j--) {
	// if (positiveArray[j] * 2 + negativeArray[i] < 0) {
	// break;
	// }
	// for (int k = j - 1; k >= 0; k--) {
	// int sum=negativeArray[i] + positiveArray[j] + positiveArray[k];
	// if ( sum== 0) {
	// StringBuffer sb = new StringBuffer();
	// sb.append(negativeArray[i]).append(",")
	// .append(positiveArray[k]).append(",")
	// .append(positiveArray[j]);
	// String result = sb.toString();
	// if (!uniqueSet.contains(result)) {
	// uniqueSet.add(result);
	// List<Integer> found = new ArrayList<>();
	// found.add(negativeArray[i]);
	// found.add(positiveArray[k]);
	// found.add(positiveArray[j]);
	// results.add(found);
	// } else {
	// break;
	// }
	// } else if (sum < 0) {
	// break;
	// }
	// }
	// }
	// }
	//
	// for (int i = 0; i < positiveArray.length; i++) {
	// if (i > 0 && positiveArray[i] == positiveArray[i - 1]) {
	// continue;
	// }
	// for (int j = negativeArray.length - 1; j > 0; j--) {
	// if (negativeArray[j] * 2 + positiveArray[i] < 0) {
	// break;
	// }
	// for (int k = j - 1; k >= 0; k--) {
	// int sum=positiveArray[i] + negativeArray[j] + negativeArray[k];
	// if ( sum== 0) {
	// StringBuffer sb = new StringBuffer();
	// sb.append(negativeArray[k]).append(",")
	// .append(negativeArray[j]).append(",")
	// .append(positiveArray[i]);
	//
	// String result = sb.toString();
	// if (!uniqueSet.contains(result)) {
	// uniqueSet.add(result);
	// List<Integer> found = new ArrayList<>();
	// found.add(negativeArray[k]);
	// found.add(negativeArray[j]);
	// found.add(positiveArray[i]);
	// results.add(found);
	// } else {
	// break;
	// }
	// } else if (sum < 0) {
	// break;
	// }
	// }
	// }
	// }
	// return results;
	//
	// }
}
