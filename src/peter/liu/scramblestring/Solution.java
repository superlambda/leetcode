package peter.liu.scramblestring;

import java.util.Arrays;

/**
 * 
 * Given a string s1, we may represent it as a binary tree by partitioning it to two non-empty substrings recursively.

Below is one possible representation of s1 = "great":

    great
   /    \
  gr    eat
 / \    /  \
g   r  e   at
           / \
          a   t
To scramble the string, we may choose any non-leaf node and swap its two children.

For example, if we choose the node "gr" and swap its two children, it produces a scrambled string "rgeat".

    rgeat
   /    \
  rg    eat
 / \    /  \
r   g  e   at
           / \
          a   t
We say that "rgeat" is a scrambled string of "great".

Similarly, if we continue to swap the children of nodes "eat" and "at", it produces a scrambled string "rgtae".

    rgtae
   /    \
  rg    tae
 / \    /  \
r   g  ta  e
       / \
      t   a
We say that "rgtae" is a scrambled string of "great".

Given two strings s1 and s2 of the same length, determine if s2 is a scrambled string of s1.
 * @author liuyingjie
 *
 */
public class Solution {
	public boolean isScramble(String s1, String s2) {
		if (!isSameString(s1,s2)){
			return false;
		}
		return recusiveCheck(s1, s2);
    }
	private boolean recusiveCheck(String s1, String s2){
		if(s1.equals(s2)||s1.equals(new StringBuffer(s2).reverse().toString())){
			return true;
		}
		
		for(int i=1;i<s1.length();i++){
			if(isSameString(s1.substring(0,i), s2.substring(0,i))){
				if (recusiveCheck(s1.substring(0, i), s2.substring(0, i))
						&& recusiveCheck(s1.substring(i, s1.length()), s2.substring(i, s2.length()))) {
					return true;
				}
			}
			
			if(isSameString(s1.substring(0,i), s2.substring(s2.length()-i,s2.length()))){
				if (recusiveCheck(s1.substring(0, i), s2.substring(s2.length()-i,s2.length()))
						&& recusiveCheck(s1.substring(i, s1.length()), s2.substring(0, s2.length()-i))) {
					return true;
				}
			}
			
		}
		
		return false;
	}
	
	private boolean isSameString(String s1, String s2){
		char[] chars1=new char[s1.length()];
		char[] chars2=new char[s1.length()];
		for(int i=0;i<s1.length();i++){
			chars1[i]=s1.charAt(i);
			chars2[i]=s2.charAt(i);
		}
		Arrays.sort(chars1);
		Arrays.sort(chars2);
		for(int i = 0; i<chars1.length;i++){
			if(chars1[i] != chars2[i]){
				return false;
			}
		}
		return true;
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
