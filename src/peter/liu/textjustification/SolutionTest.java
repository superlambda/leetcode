package peter.liu.textjustification;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testFullJustify() {
		String[] words={""};
		List<String> result=new Solution().fullJustify(words, 2);
		assertEquals(result.get(0),"  ");
		
		String[] words2={"a"};
		result=new Solution().fullJustify(words2, 1);
	    assertEquals(result.get(0),"a");
	   
		result=new Solution().fullJustify(words2, 2);
		assertEquals(result.size(),1);
	    assertEquals(result.get(0),"a ");
	    
	    
	    String[] words3={"a","b","c","d","e"};
		result=new Solution().fullJustify(words3, 3);
		
	    assertEquals(result.get(0),"a b");
		assertEquals(result.get(1),"c d");
		assertEquals(result.get(2),"e  ");
	    
		String[] words4 = {"Listen","to","many,","speak","to","a","few."};
		result=new Solution().fullJustify(words4, 6);
		
		String[] words5={"Here","is","an","example","of","text","justification."};
		result=new Solution().fullJustify(words5, 15);
		
		assertEquals(result.get(0),"Here    is   an");
		assertEquals(result.get(1),"example of text");
		assertEquals(result.get(2),"justification. ");
		
		String[] words6={"Don't","go","around","saying","the","world","owes","you","a","living;","the","world","owes","you","nothing;","it","was","here","first."};
				
		result=new Solution().fullJustify(words6, 30);
		
		assertEquals(result.get(0),"Don't  go  around  saying  the");
		assertEquals(result.get(1),"world  owes  you a living; the");
		assertEquals(result.get(2),"world owes you nothing; it was");
		assertEquals(result.get(3),"here first.                   ");		
	}

}
