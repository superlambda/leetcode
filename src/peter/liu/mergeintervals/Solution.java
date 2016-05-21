package peter.liu.mergeintervals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

/**
 * Given a collection of intervals, merge all overlapping intervals.
 * 
 * For example, Given [1,3],[2,6],[8,10],[15,18], 
 * return [1,6],[8,10],[15,18].
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public List<Interval> merge(List<Interval> intervals) {
		Interval toCheck=null;
		Stack<Interval> result=new Stack<>();
		Collections.sort(intervals, new ComparatorInterval());
		for(Interval inter:intervals){
			if(toCheck==null){
				toCheck=inter;
			}else{
				if(toCheck.end<inter.start){
					result.add(toCheck);
					toCheck=inter;
				}else if(toCheck.start<=inter.start&&toCheck.end<inter.end){
					toCheck.end=inter.end;
					while(!result.isEmpty()&&result.peek().end>=toCheck.start){
						Interval pop=result.pop();
						toCheck.start=toCheck.start<pop.start? toCheck.start:pop.start;				
					}
					
				}else if(toCheck.start>inter.start&&toCheck.end<=inter.end){
					toCheck=inter;
					while(!result.isEmpty()&&result.peek().end>=toCheck.start){
						Interval pop=result.pop();
						toCheck.start=toCheck.start<pop.start? toCheck.start:pop.start;				
					}
				}
			}
		}
		if(toCheck!=null){
			result.add(toCheck);
		}
		return new ArrayList<Interval>(result);
	}
	
	private class ComparatorInterval implements Comparator<Interval> {
		public int compare(Interval arg0, Interval arg1) {
			int flag = arg0.end - arg1.end;
			if (flag == 0) {
				flag = arg0.start - arg1.start;
			} 
			if(flag==0){
				return flag;
			}
			if(flag<0){
				return -1;
			}
			return 1;
		}
	}
	
}




/* Definition for an interval. */
class Interval {
	int start;
	int end;

	Interval() {
		start = 0;
		end = 0;
	}

	Interval(int s, int e) {
		start = s;
		end = e;
	}
}
