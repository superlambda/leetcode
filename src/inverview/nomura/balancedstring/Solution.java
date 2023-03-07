package inverview.nomura.balancedstring;

import java.util.Stack;

public class Solution {
	
	public int solution(String s) {
		int shortest=Integer.MAX_VALUE;
		Stack<String> fragmentStack = new Stack<>();
		fragmentStack.add(s);
		while(!fragmentStack.isEmpty()) {
			String popString=fragmentStack.pop();
			boolean isBalanced = true;
			for(int i=0; i< popString.length()-1;i++) {
				char reverseCase;
				if(Character.isUpperCase(popString.charAt(i))) {
					reverseCase = Character.toLowerCase(popString.charAt(i));
				} else {
					reverseCase = Character.toUpperCase(popString.charAt(i));
				}
				
				if(!popString.contains(String.valueOf(reverseCase))) {
					if(i>1) {
						String leftString = popString.substring(0, i);
						fragmentStack.add(leftString);
					}
					if (i < popString.length()-2) {
						String rightString = popString.substring(i+1);
						fragmentStack.add(rightString);
					}
					isBalanced = false;
					break;
				}
			}
			if(isBalanced && popString.length()<shortest) {
				shortest = popString.length();
			}
		}
		return shortest == Integer.MAX_VALUE? -1:shortest;
			
	}

}
