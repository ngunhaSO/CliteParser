package com.jacky.lexer;

public class TestMe {

	public String testOrPass() {
		int a = 1;
		int b = 2;
		int c = -2147483648;
		if((a == 1) || (foreverLoop() == c))
			return "yea";
		
		return "abc";
	}
	
	public String testOrFail() {
		int a = 1;
		int b = 2;
		int c = -2147483648;
		if((a == 1) | (foreverLoop() == c))
			return "yea";
		
		return "abc";
	}
	
	public static int foreverLoop() {
		int a = 0;
		while(a >= 0){
			a++;
			a--;
		}
		
		return a;
		
	}
	public static void main(String[] args) {
		System.out.println(foreverLoop());
	}
}
