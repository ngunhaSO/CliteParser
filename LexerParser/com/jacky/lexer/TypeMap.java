package com.jacky.lexer;

import java.util.*;

public class TypeMap extends HashMap<Variable, Type> {

	// TypeMap is implemented as a Java HashMap.
	// Plus a 'display' method to facilitate experimentation.

	public void display() {
		System.out.println("Final state:");
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator it = this.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<Variable,Type> pairs = (Map.Entry<Variable, Type>)it.next();
			sb.append(pairs.getKey() + ", " + pairs.getValue());
			sb.append("\n");
		}
		return sb.toString();
	}

}
