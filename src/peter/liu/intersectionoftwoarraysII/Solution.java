package peter.liu.intersectionoftwoarraysII;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Solution {
	public int[] intersect(int[] nums1, int[] nums2) {
        List<Integer> list = new LinkedList<>();
        
        if(nums1==null||nums2==null||nums1.length==0||nums1.length==0){
        	return new int[0];
        }
        
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        if(nums1.length<nums2.length) {
        	addToList(nums1, nums2, list);
        }else {
        	addToList(nums2, nums1, list);
        }
        
        
        int[] result = new int[list.size()];
        int index=0;
        for(Integer element: list) {
        	result[index] = element;
        	index++;
        }
        return result;
    }
	
	private void addToList(int[] nums1, int[] nums2, List<Integer> list) {
		int index1=0;
    	int index2=0;
    	while(index1<nums1.length) {
    		while(index2<nums2.length&&nums2[index2]<nums1[index1]) {
    			index2++;
    		}
    		if(index1<nums1.length&& index2<nums2.length&&nums1[index1]==nums2[index2]) {
    			list.add(nums1[index1]);
    			index1++;
    			index2++;
    		}
    		if(index1<nums1.length&& index2<nums2.length&&nums1[index1]<nums2[index2]) {
    			index1++;
    		}
    		if(index2==nums2.length) {
    			break;
    		}
    	}
	}
	
	public int[] intersectMoreConcise(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int i=0, j=0,k=0;
        while(i<nums1.length&&j<nums2.length) {
        	if(nums1[i] < nums2[j]) {
        		i++;
        	}else if (nums1[i] > nums2[j]) {
        		j++;
        	}else {
        		nums1[k++] = nums1[i];
        		i++;
        		j++;
        	}
        }
        return Arrays.copyOfRange(nums1, 0, k);
    }

}
