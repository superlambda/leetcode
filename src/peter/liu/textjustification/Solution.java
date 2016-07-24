package peter.liu.textjustification;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Given an array of words and a length L, format the text such that each line has exactly L characters and is fully (left and right) justified.

You should pack your words in a greedy approach; that is, pack as many words as you can in each line. Pad extra spaces ' ' when necessary so that each line has exactly L characters.

Extra spaces between words should be distributed as evenly as possible. If the number of spaces on a line do not divide evenly between words, the empty slots on the left will be assigned more spaces than the slots on the right.

For the last line of text, it should be left justified and no extra space is inserted between words.

For example,
words: ["This", "is", "an", "example", "of", "text", "justification."]
L: 16.

Return the formatted lines as:
[
   "This    is    an",
   "example  of text",
   "justification.  "
]
Note: Each word is guaranteed not to exceed L in length.
 * @author liuyingjie
 *
 */
public class Solution {
	public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> result = new LinkedList<>();
        List<String> line=new ArrayList<>();
        int length=0;
        for(int i=0;i<words.length;i++){
        	String word=words[i];
        	if(word.length()==maxWidth&&line.isEmpty()){
        		result.add(word);
        	}else if(length+word.length()+1<=maxWidth){
        		if(length==0){
        			if(word.length()>0){
        				line.add(word);
        				length=length+word.length();
        			}else{
        				line.add(" ");
        				length++;
        			}
        		}else{
        			line.add(" ");
        			length++;
        			if(word.length()>0){
        				line.add(word);
        				length=length+word.length();
        			}
        		}
        		
        		if(length==maxWidth){
        			length=0;
        			addToResult(result, line,maxWidth);
        		}else if(i==words.length-1){
        			appendSpace(line,length, maxWidth);
        			addToResult(result, line,maxWidth);
        		}
        		
        	}else{
        		if(line.size()<=1){
        			appendSpace(line,length, maxWidth);
				}else{
					//extend the line length to maxWidth
					while(length<maxWidth){
						for(int j=1;j<=line.size()-1;j+=2){
							line.set(j, line.get(j)+" ");
							length++;
							if(length==maxWidth){
								break;
							}
						}
					}
				}	
    			
    			//add the expended line to the result
    			length=0;
    			addToResult(result, line,maxWidth);
    			line.add(word);
    			length=word.length();	
        	}
        }
        if(!line.isEmpty()){
        	appendSpace(line,length, maxWidth);
			addToResult(result, line,maxWidth);
        }
        return result;
        
    }
	
	private void appendSpace(List<String>line,int length, int maxWidth){
		StringBuffer sb=new StringBuffer();
		while(length<maxWidth){
			sb.append(" ");
			length++;
		}
		line.add(sb.toString());
	}
	
	private void addToResult(List<String> result, List<String> line,int maxWidth) {
		StringBuffer newLine = new StringBuffer();
		for (String s : line) {
			newLine.append(s);
		}
		result.add(newLine.toString());
		
		line.clear();
	}
}
