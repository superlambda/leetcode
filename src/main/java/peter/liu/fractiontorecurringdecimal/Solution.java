package peter.liu.fractiontorecurringdecimal;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Given two integers representing the numerator and denominator of a fraction,
 * return the fraction in string format.
 * 
 * If the fractional part is repeating, enclose the repeating part in
 * parentheses.
 * 
 * For example,
 * 
 * Given numerator = 1, denominator = 2, return "0.5". Given numerator = 2,
 * denominator = 1, return "2". Given numerator = 2, denominator = 3, return
 * "0.(6)". Credits: Special thanks to @Shangrila for adding this problem and
 * creating all test cases.
 * 
 * Subscribe to see which companies asked this question
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public String fractionToDecimal(int numerator, int denominator) {
		return calculate(numerator, denominator);
	}
	public String calculate(long numerator, long denominator) {
		boolean negative=numerator*denominator<0;
		numerator=Math.abs(numerator);
		denominator=Math.abs(denominator);
		if(numerator==0){
			return "0";
		}
		StringBuffer result = new StringBuffer();
		if (numerator / denominator > 0) {
			result.append(numerator / denominator);
		}
		numerator %= denominator;
		if(numerator==0){
			if(negative){
				return "-"+result.toString();
			}else{
				return result.toString();
			}
		}else{
			if(result.length()>0){
				numerator*=10;
				result.append(".");
			}
		}
		
		List<String> fractionalPartList = new LinkedList<>();
		Map<String, Long> keyMap = new HashMap<>();
		
		while (numerator != 0 && !keyMap.containsKey(numerator + "," + denominator)) {
			fractionalPartList.add(numerator + "," + denominator);
			keyMap.put(numerator + "," + denominator, numerator / denominator);
			numerator %= denominator;
			numerator *= 10;
		}
		String startKey = numerator + "," + denominator;
		for (String key : fractionalPartList) {
			if (result.length() == 0) {
				result.append("0.");
			} else {
				if (numerator == 0) {
					result.append(keyMap.get(key));
				} else {
					if (!key.equals(startKey)) {
						result.append(keyMap.get(key));
					} else {
						result.append("(").append(keyMap.get(key));
					}
				}

			}
		}
		if (numerator != 0) {
			result.append(")");
		}
		if(negative){
			return "-"+result.toString();
		}else{
			return result.toString();
		}
		
	}
}
