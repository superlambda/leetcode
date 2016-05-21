package peter.liu.insertinterval;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Solution {
	public List<Interval> insert(List<Interval> intervals, Interval newInterval) {
		if(intervals.size()==0){
			intervals.add(newInterval);
			return intervals;
		}
		
		boolean add=false;
		for(int i=0;i<intervals.size();i++){
			if(intervals.get(i).end>newInterval.end){
				intervals.add(i, newInterval);
				add = true;
				break;
			}
		}
		if(!add){
			intervals.add(newInterval);
		}
		
		Interval toCheck=null;
		
		Stack<Interval> result=new Stack<>();
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