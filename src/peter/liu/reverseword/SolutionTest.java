package peter.liu.reverseword;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * @author pcnsh197
 *
 */
public class SolutionTest {

	@Test
	public void testReverseWords() {
		String s1 = "the sky is blue";
		String s2 =" 1";
		String s3 ="1 ";
		String s4 =" ";
		String s5 = "the    sky   is               blue";
		assertEquals("blue is sky the",new Solution().reverseWords(s1));
		assertEquals("1",new Solution().reverseWords(s2));
		assertEquals("1",new Solution().reverseWords(s3));
		assertEquals("",new Solution().reverseWords(s4));
		assertEquals("blue is sky the",new Solution().reverseWords(s5));
	}

}
