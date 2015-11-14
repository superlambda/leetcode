package peter.liu.binarysearchtreeiterator;

import org.junit.Test;

public class BSTIteratorTest {

	@Test
	public void testBSTIterator() {
		TreeNode root=new TreeNode(2);
		root.left=new TreeNode(1);
		
		BSTIterator i = new
				BSTIterator(root); 
		while (i.hasNext()){
			System.out.println(i.next());
		}			
	}
		
}