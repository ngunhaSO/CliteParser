package com.jacky.lexer;

// StaticTypeCheck.java

import java.util.*;

// Static type checking for Clite is defined by the functions 
// V and the auxiliary functions typing and typeOf.  These
// functions use the classes in the Abstract Syntax of Clite.

public class StaticTypeCheck {

	public static TypeMap typing(Declarations d) {
		TypeMap map = new TypeMap();
		for (Declaration di : d.members) {
			map.put(di.v, di.t);
		}
		return map;
	}

	public static void check(boolean test, String msg) {
		if (test)
			return;
		System.err.println(msg);
		System.exit(1);
	}

	public static void V(Declarations d) {
		for (int i = 0; i < d.members.size() - 1; i++)
			for (int j = i + 1; j < d.members.size(); j++) {
				Declaration di = d.members.get(i);
				Declaration dj = d.members.get(j);
				check(!(di.v.equals(dj.v)), "duplicate declaration: " + dj.v);
			}
	}

	public static void V(Program p) {
		V(p.decpart);
		V(p.body, typing(p.decpart));
	}

	public static Type typeOf(Expression e, TypeMap tm) {
		if (e instanceof Value)
			return ((Value) e).type;
		if (e instanceof Variable) {
			Variable v = (Variable) e;
			check(tm.containsKey(v), "undefined variable: " + v);
			return (Type) tm.get(v);
		}
		if (e instanceof Binary) {
			Binary b = (Binary) e;
			if (b.op.ArithmeticOp())
				if (typeOf(b.term1, tm) == Type.FLOAT)
					return (Type.FLOAT);
				else
					return (Type.INT);
			if (b.op.RelationalOp() || b.op.BooleanOp())
				return (Type.BOOL);
		}
		if (e instanceof Unary) {
			Unary u = (Unary) e;
			if (u.op.NotOp())
				return (Type.BOOL);
			else if (u.op.NegateOp())
				return typeOf(u.term, tm);
			else if (u.op.intOp())
				return (Type.INT);
			else if (u.op.floatOp())
				return (Type.FLOAT);
			else if (u.op.charOp())
				return (Type.CHAR);
		}
		throw new IllegalArgumentException(
				"should never reach here for typeOf method");
	}

	public static void V(Expression e, TypeMap tm) {
		if (e instanceof Value)
			return;
		if (e instanceof Variable) {
			Variable v = (Variable) e;
			check(tm.containsKey(v), "undeclared variable: " + v);
			return;
		}
		if (e instanceof Binary) {
			Binary b = (Binary) e;
			Type typ1 = typeOf(b.term1, tm);
			Type typ2 = typeOf(b.term2, tm);
			V(b.term1, tm);
			V(b.term2, tm);
			if (b.op.ArithmeticOp())
				check(typ1 == typ2 && (typ1 == Type.INT || typ1 == Type.FLOAT),
						"type error for " + b.op);
			else if (b.op.RelationalOp())
				check(typ1 == typ2, "type error for " + b.op);
			else if (b.op.BooleanOp())
				check(typ1 == Type.BOOL && typ2 == Type.BOOL, b.op
						+ ": non-bool operand");
			else
				throw new IllegalArgumentException(
						"should never reach here: V binary");
			return;
		}
		// student exercise
		if (e instanceof Unary) { //done
			Unary u = (Unary) e;
			Type typ1 = typeOf(u.term, tm);
			V(u.term, tm);
			if (u.op.NotOp()) {
				check(typ1 == Type.BOOL, u.op + ": non-bool operand");
			} else if (u.op.NegateOp()) {
				check(typ1 == Type.INT || typ1 == Type.FLOAT, u.op + ": not match its parent");
			}else if(u.op.floatOp() || u.op.charOp()){
				check(typ1 == Type.INT, u.op + ": not match");
			}else if(u.op.intOp()){
				check(typ1 == Type.FLOAT || typ1 == Type.CHAR, u.op + ": not match its parent");
			} else
				throw new IllegalArgumentException(
						"should never reach here: V unary");
			return;
		}
		throw new IllegalArgumentException(
				"should never reach here: V(Expression, typemap");
	}

	public static void V(Statement s, TypeMap tm) {
		if (s == null)
			throw new IllegalArgumentException("AST error: null statement");
		if (s instanceof Skip)
			return;
		if (s instanceof Assignment) {
			Assignment a = (Assignment) s;
			check(tm.containsKey(a.target), " undefined target in assignment: "
					+ a.target); // check that the variable is declared
			V(a.source, tm); // the source expression is valid
			Type ttype = (Type) tm.get(a.target);// get the target type
			Type srctype = typeOf(a.source, tm); // get the source type
			if (ttype != srctype) { // if the source and the target type don't
									// match!!!
				if (ttype == Type.FLOAT) // if the target type is float
					check(srctype == Type.INT, "mixed mode assignment to "
							+ a.target); // the source must be int
				else if (ttype == Type.INT) // if the target type is int
					check(srctype == Type.CHAR, "mixed mode assignment to "
							+ a.target);// the source must be char
				else
					// otherwise it is a failed!
					check(false, "mixed mode assignment to " + a.target);
			}
			return;
		}
		if (s instanceof Conditional) {
			Conditional c = (Conditional) s;
			V(c.test, tm);
			V(c.thenbranch, tm);
			V(c.elsebranch, tm);
			return;
		}
		if (s instanceof Loop) {
			Loop l = (Loop) s;
			V(l.test, tm);
			V(l.body, tm);
			return;
		}
		if (s instanceof Block) {
			Block b = (Block) s;
			for (Statement st : b.members) {
				V(st, tm);
			}
			return;
		}
		throw new IllegalArgumentException(
				"should never reach here: V(statement, typemap");
	}

	public static void main(String args[]) {
		Parser parser = new Parser(new Lexer(args[0]));
		Program prog = parser.program();
		prog.display(0);
		System.out.println("\nBegin type checking...");
		System.out.println("Type map:");
		TypeMap map = typing(prog.decpart);
		map.display(); // student exercise
		V(prog);
	} // main

} // class StaticTypeCheck

