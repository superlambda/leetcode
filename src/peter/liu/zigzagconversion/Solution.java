package peter.liu.zigzagconversion;

import java.util.HashMap;
import java.util.Map;

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
		if("".equals(s)){
			return "";
		}
		Map<Integer,StringBuilder> rowMap=new HashMap<>();
		int rowIterator=0;
		for(int i=0;i<s.length();i++){
			int index=rowIterator;
			if(index==nRows){
				index=nRows%2;
				rowIterator=0;
			}else{
				rowIterator++;
			}
			StringBuilder sb=rowMap.get(index);
			if(sb==null){
				sb=new StringBuilder();
			}
			sb.append(s.charAt(i));
			rowMap.put(index, sb);
		}
		StringBuilder result=new StringBuilder();
		for(int i=0;i<nRows;i++){
			if(rowMap.get(i)!=null){
				result.append(rowMap.get(i));
			}
		}
		return result.toString();

	}
}
