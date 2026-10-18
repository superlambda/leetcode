package peter.liu.firstbadversion;

public class Solution extends VersionControl {
	
	public Solution(int firstBadVersion) {
		super(firstBadVersion);
	}
	public int firstBadVersion(int n) {
		int middle = 0;

		int start = 0;
		int end = n;
		while (start < end) {
			middle = (start + end) >>>1;
			if (isBadVersion(middle)) {
				end = middle;
			} else {
				start = middle + 1;
			}
		}

		if(start==end && isBadVersion(start)) {
			return start;
		}
		return -1;
	}

}
