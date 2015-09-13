package peter.liu.constructbinarytreefrominorderandpostordertraversal;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testBuildTree() {
		int[] postorder={2,1};
		int[] inorder={2,1};
		new Solution().buildTree(inorder, postorder);
	}

}
