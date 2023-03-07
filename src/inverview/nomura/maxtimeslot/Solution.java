package inverview.nomura.maxtimeslot;

public class Solution {
	
	public int solution(String timeSlot) {
		String weekDay = "";
		int maxSlot = 0;
		String[] slots = timeSlot.split("\n");
		
		for(int i=0; i<slots.length; i++) {
			int start = Integer.valueOf(slots[i].substring(4,6)) * 60 + Integer.valueOf(slots[i].substring(7,9)); 
			int end = Integer.valueOf(slots[i].substring(10,12)) * 60 + Integer.valueOf(slots[i].substring(13));
			if(end - start > maxSlot) {
				weekDay = slots[i].substring(0,3);
				maxSlot = end - start;
			}
			
		}
		return maxSlot;
	}
}
