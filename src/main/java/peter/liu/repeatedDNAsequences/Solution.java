package peter.liu.repeatedDNAsequences;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 
 * All DNA is composed of a series of nucleotides abbreviated as A, C, G, and T,
 * for example: "ACGAATTCCG". When studying DNA, it is sometimes useful to
 * identify repeated sequences within the DNA.
 * 
 * Write a function to find all the 10-letter-long sequences (substrings) that
 * occur more than once in a DNA molecule.
 * 
 * For example,
 * 
 * Given s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT",
 * 
 * Return: ["AAAAACCCCC", "CCCCCAAAAA"].
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public List<String> findRepeatedDnaSequences(String s) {
		List<String> result =new LinkedList<>();
		Set<String> sequenceSet= new HashSet<>();
		for(int i=0; i+10<=s.length();i++){
			String subString=s.substring(i,i+10);
			if(!sequenceSet.contains(subString)){
				sequenceSet.add(subString);
			}else{
				if(!result.contains(subString)){
					result.add(subString);
				}
			}
		}
		return result;
		
	}                                                                            
}
