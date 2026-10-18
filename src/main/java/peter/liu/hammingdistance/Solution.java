package peter.liu.hammingdistance;

public class Solution {
	public int hammingDistanceMyself(int x, int y) {
		int distance = 0;
		int remainderX = 0;
		int remainderY = 0;
		while (x != 0 || y != 0) {
			remainderX = x % 2;
			x = x >>> 1;
			remainderY = y % 2;
			y = y >>> 1;
			distance += remainderX ^ remainderY;
		}
		return distance;

	}
	
	public int hammingDistance(int x, int y) {
		int distance = 0;
		x=x^y;
		while (x!=0) {
			distance += x & 1;
			x = x>>>1;
		}
		return distance;

	}
}
