package peter.liu.evaluatereversepolishnotation;

import java.util.Stack;

/**
 * Evaluate the value of an arithmetic expression in Reverse Polish Notation.
 * 
 * Valid operators are +, -, *, /. Each operand may be an integer or another
 * expression.
 * 
 * Some examples: ["2", "1", "+", "3", "*"] -> ((2 + 1) * 3) -> 9 ["4", "13",
 * "5", "/", "+"] -> (4 + (13 / 5)) -> 6 Subscribe to see which companies asked
 * this question
 * 
 * @author liuyingjie
 *
 */
public class Solution {
	public int evalRPN(String[] tokens) {
		Stack<String> stack= new Stack<>();
		for(int j=tokens.length-1;j>=0;j--){
			if(isOperator(tokens[j])){
				stack.push(tokens[j]);
			}else{
				if(!stack.isEmpty()&&!isOperator(tokens[j])&&!isOperator(stack.peek())){
					String b=stack.pop();
					stack.push(String.valueOf(calculate(tokens[j],b,stack.pop())));
					while(stack.size()>1){
						String a=stack.pop();
						if(!isOperator(stack.peek())){
							String newB=stack.pop();
							stack.push(String.valueOf(calculate(a,newB,stack.pop())));
						}else{
							stack.push(a);
							break;
						}
						
					}
				}else{
					stack.push(tokens[j]);
				}
			}
		}
		while(stack.size()>1){
			stack.push(String.valueOf(calculate(stack.pop(),stack.pop(),stack.pop())));
		}
		return Integer.valueOf(stack.pop());
	}

	private int calculate(String a, String b, String operator) {
		switch (operator) {
		case "+":
			return Integer.valueOf(a) + Integer.valueOf(b);
		case "-":
			return Integer.valueOf(a) - Integer.valueOf(b);
		case "*":
			return Integer.valueOf(a) * Integer.valueOf(b);
		case "/":
			int numberA=Integer.valueOf(a);
			int numberB=Integer.valueOf(b);
			if(numberB!=0){
				return  numberA/numberB ;
			}else{
				if(numberA>0){
					return Integer.MAX_VALUE;
				}
				if(numberA == 0){
					return 0;
				}
				if(numberA<0){
					return Integer.MIN_VALUE;
				}
			}
		}
		return 0;

	}

	private boolean isOperator(String token) {
		return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
	}

}
