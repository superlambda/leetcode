package peter.liu.restoreipaddresses;

import java.util.List;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testRestoreIpAddresses() {
//		List<String> result = new Solution().restoreIpAddresses("25525511135");
		
		List<String> result = new Solution().restoreIpAddresses("010010");
		for (String r : result) {
			System.out.println(r);
		}
	}

}
