package peter.liu.firstbadversion;

public class VersionControl {
	
	private int firstBadVersion = 0;
	
	public VersionControl(int firstBadVersion) {
		this.firstBadVersion=firstBadVersion;
	}
	boolean isBadVersion(int version) {
		if(version>=firstBadVersion) {
			return true;
		}else {
			return false;
		}
	}

}
