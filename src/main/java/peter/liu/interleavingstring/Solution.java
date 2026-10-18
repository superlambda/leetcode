package peter.liu.interleavingstring;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Given s1, s2, s3, find whether s3 is formed by the interleaving of s1 and s2.

For example,
Given:
s1 = "aabcc",
s2 = "dbbca",

When s3 = "aadbbcbcac", return true.
When s3 = "aadbbbaccc", return false.
 * @author liuyingjie
 *
 */
public class Solution {
	
	private Map<String,Boolean> memMap= new HashMap<>();
	
	public boolean isInterleave(String s1, String s2, String s3) {
        if(s1.length()+s2.length()!=s3.length()){
        	return false;
        }
        if(s1.length()==0){
        	return s2.equals(s3);
        }
        if(s2.length()==0){
        	return s1.equals(s3);
        }
        
        return canGoon(s1,s2,s3,0,0,0);
    }
	
	private boolean canGoon(String s1, String s2, String s3, int s1Index,int s2Index,int s3Index){
		
		String key=s1Index +","+s2Index;
		if(memMap.containsKey(key)){
			return memMap.get(key);
		}
		if(s1Index==s1.length()&&s2Index==s2.length()){
			return true;
		}else if (s1Index==s1.length()&&s2Index!=s2.length()){
			return s2.substring(s2Index).equals(s3.substring(s3Index));
		}else if (s2Index==s2.length()&&s1Index!=s1.length()){
			return s1.substring(s1Index).equals(s3.substring(s3Index));
		}
		
		for(int i=1;i<s1.length()-s1Index+1;i++){
			if(s1.charAt(s1Index + i-1)!=s3.charAt(s3Index + i-1)) {
				break;
			}
			for(int j=1;j<s2.length()-s2Index+1;j++){
				if(s2.charAt(s2Index + j-1)==s3.charAt(s3Index + i+j-1)){
					if(canGoon( s1,s2,s3,  s1Index+i,s2Index+j, s3Index+i+j)){
						return true;
					}
				}else{
					break;
				}
			}
		}
		
		for(int i=1;i<s2.length()-s2Index+1;i++){
			if(s2.charAt(s2Index + i-1)!=s3.charAt(s3Index + i-1)) {
				break;
			}
			for(int j=1;j<s1.length()-s1Index+1;j++){
				if(s1.charAt(s1Index + j-1)==s3.charAt(s3Index + i+j-1)){
					if(canGoon( s1,s2,s3,s1Index+j,s2Index+i, s3Index+i+j)){
						return true;
					}
				}else{
					break;
				}
			}
		}
		memMap.put(key,false);
		return false;
	}
	
}
