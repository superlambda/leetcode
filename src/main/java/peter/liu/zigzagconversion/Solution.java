package peter.liu.zigzagconversion;


/**
 * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number
 * of rows like this: (you may want to display this pattern in a fixed font for
 * better legibility)
 * 
 * P A H N A P L S I I G Y I R
 * 
 * And then read line by line: "PAHNAPLSIIGYIR"
 * 
 * Write the code that will take a string and make this conversion given a
 * number of rows:
 * 
 * string convert(string text, int nRows);
 * 
 * convert("PAYPALISHIRING", 3) should return "PAHNAPLSIIGYIR".
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public String convert(String s, int nRows) {
		if("".equals(s)||nRows==1){
			return s;
		}
		StringBuilder[] sbs=new StringBuilder[nRows];
		for(int i=0;i<sbs.length;i++){
			sbs[i]=new StringBuilder();
		}
		char[] chars=s.toCharArray();
		int index=0;
		boolean asc=true;
		for(int i=0;i<chars.length;i++){
			sbs[index].append(chars[i]);
			if(asc){
				index++;
				if(index==nRows){
					index-=2;
					asc=false;
				}
			}else{
				index--;
				if(index==-1){
					index+=2;
					asc=true;
				}
			}
		}
		StringBuilder result=new StringBuilder();
		for(int i=0;i<sbs.length;i++){
			result.append(sbs[i]);
		}
		return result.toString();
	}
}
