package com.jacky.lexer;

public class Parser {
	// Recursive descent parser that inputs a C++Lite program and
	// generates its abstract syntax. Each method corresponds to
	// a concrete syntax grammar rule, which appears as a comment
	// at the beginning of the method.

	Token token; // current token from the input stream
	Lexer lexer;

	public Parser(Lexer ts) { // Open the C++Lite source program
		lexer = ts; // as a token stream, and
		token = lexer.next(); // retrieve its first Token
	}

	/*
	 * Retrieve the next token or display syntax error
	 */
	protected String match(TokenType t) {
		String value = token.value();
		if (token.type().equals(t))
			token = lexer.next();
		else
			error(t);
		return value;
	}

	protected void error(TokenType tok) {
		System.err.println("Syntax error: expecting: " + tok + "; saw: "
				+ token);
		System.exit(1);
	}

	private void error(String tok) {
		System.err.println("Syntax error: expecting: " + tok + "; saw: "
				+ token);
		System.exit(1);
	}

	// return the Program object
	public Program program() {
		// Program --> void main ( ) '{' Declarations Statements '}'
		TokenType[] header = { TokenType.Int, TokenType.Main,
				TokenType.LeftParen, TokenType.RightParen };
		for (int i = 0; i < header.length; i++)
			// bypass "int main ( )"
			match(header[i]);
		match(TokenType.LeftBrace);// by pass left brace
		Program p = new Program(declarations(), statements()); // the main
																// statements
		match(TokenType.RightBrace); // by pass right brace
		return p;
	}

	private Declarations declarations() { // done
		// Declarations --> { Declaration }
		Declarations declarations = new Declarations();
		while (isType()) {
			declaration(declarations);
		}
		return declarations; // student exercise
	}

	// this should add each declaration into ds
	private void declaration(Declarations ds) {// done
		// Declaration --> Type Identifier { , Identifier } ;
		// student exercise
		Variable v;
		Type t;
		Declaration declaration;
		t = type(); //type
		do {
			token = lexer.next(); //identifier
			v = new Variable(match(TokenType.Identifier));
			declaration = new Declaration(v, t);
			ds.members.add(declaration);
		} while(token != Token.semicolonTok);
		token = lexer.next();//move to the next declaration
	}

	private Type type() {
		// Type --> int | bool | float | char
		Type t = null;
		// student exercise
		if (token.type() == TokenType.Int) {
			t = Type.INT;
		} else if (token.type() == TokenType.Bool) {
			t = Type.BOOL;
		} else if (token.type() == TokenType.Char) {
			t = Type.CHAR;
		} else if (token.type() == TokenType.Float) {
			t = Type.FLOAT;
		}
		return t;
	}

	protected Statement statement() {
		// Statement --> ; | Block | Assignment | IfStatement | WhileStatement
		// Statement s = new Skip();
		/**
		 * ------------------------------------- student exercise
		 * ------------------------------------------------
		 **/
		// ------- ; case ----------------
		if (token.type() == TokenType.Semicolon) { // ; --done
			token = lexer.next();
			Statement s = new Skip();
			return s;
		}
		// --------------------------- block ---------------
		else if (token.type() == TokenType.LeftBrace) { // open left brace must
														// go with right brace
														// //done
			token = lexer.next();
			Statement s = statements();
			if (token.type() != TokenType.RightBrace) {
				error("right brace missing for block");
			}
			token = lexer.next();
			return s;
		} else if (token.type() == TokenType.While) { // done
			Statement s = whileStatement();
			return s;
		} else if (token.type() == TokenType.If) { // if current token is if
			Statement s = ifStatement();
			return s;
		} else if (token.type() == TokenType.Identifier) { // done
			Statement s = assignment();
			return s;
		} else {
			assert false : "should never get here";
			return new Skip();
		}
	}

	private Block statements() { // done
		// Block --> '{' Statements '}'
		Block b = new Block();
		// student exercise
		while (token.type() == TokenType.Semicolon
				|| token.type() == TokenType.LeftBrace
				|| token.type() == TokenType.Identifier
				|| token.type() == TokenType.If
				|| token.type() == TokenType.While) {
			b.members.add(statement());
		}
		return b;
	}

	private Assignment assignment() {
		// Assignment --> Identifier = Expression;
		Variable target = new Variable(match(TokenType.Identifier));
		match(TokenType.Assign); // if it is an assignment, an assign type must
									// follow the identifier
		Expression source = expression(); // follow the assign is an expression
		match(TokenType.Semicolon); // after that is a semicolon to signal the
									// end of an assignment it. NOTE: by calling
									// this, we ensure that the end of the
									// assignment statement, we already move to
									// the next token
		Assignment a = new Assignment(target, source); // finally we construct
														// the Assigment object
		return a; // return the Assignment object
	}

	private Conditional ifStatement() {
		// IfStatement --> if ( Expression ) Statement [ else Statement ]
		Conditional c = new Conditional(null, null);
		match(TokenType.If);
		match(TokenType.LeftParen);
		Expression e = expression();
		match(TokenType.RightParen);
		Statement s = statement();
		c = new Conditional(e, s);
		return c; // student exercise
	}

	private Loop whileStatement() {
		// WhileStatement --> while ( Expression ) Statement
		Loop l = new Loop(null, null);
		match(TokenType.While);
		match(TokenType.LeftParen);
		Expression e = expression();
		match(TokenType.RightParen);
		Statement s = statement();
		l = new Loop(e, s);
		return l; // student exercise
	}

	// takes the current token (the one that follows the operator =
	// and parses the ensuing stream of tokens
	// rules: Expression -> Term{AddOpTerm} ===> will not work if i have: if(a <
	// 2 || a > 3)
	private Expression expression() {
		// Expression --> Conjunction { || Conjunction }
		Expression e = conjunction();
		while (isOrOp()) {
		//	System.out.println("there is an || in bettween ");
			Operator op = new Operator(match(token.type()));
		//	System.out.println("test op: " + op.toString());
			Expression term2 = conjunction();
			e = new Binary(op, e, term2);
		}
		return e;
	}

	private Expression conjunction() {
		// Conjunction --> Equality { && Equality }
		Expression e = equality();
		while (isAndOp()) {
			Operator op = new Operator(match(token.type()));
			Expression term2 = equality();
			e = new Binary(op, e, term2);
		}
		return e;
	}

	private Expression equality() {
		// Equality --> Relation [ EquOp Relation ]
		Expression e = relation();
		while (isEqualityOp()) { // == or !=
			Operator op = new Operator(match(token.type()));
			Expression term2 = term();
			e = new Binary(op, e, term2);
		}
		return e;
	}

	private Expression relation() {
		// Relation --> Addition [RelOp Addition]
		Expression e = addition();
		while (isRelationalOp()) { // <, <=, >, >=
			Operator op = new Operator(match(token.type()));
			Expression term2 = term();
			e = new Binary(op, e, term2);
		}
		return e;
	}

	private Expression addition() {
		// Addition --> Term { AddOp Term }
		Expression e = term();
		while (isAddOp()) {
			Operator op = new Operator(match(token.type()));
			Expression term2 = term();
			e = new Binary(op, e, term2);
		}
		return e;
	}

	private Expression term() {
		// Term --> Factor { MultiplyOp Factor }
		Expression e = factor();
		while (isMultiplyOp()) {
			Operator op = new Operator(match(token.type()));
			Expression term2 = factor();
			e = new Binary(op, e, term2);
		}
		return e;
	}

	private Expression factor() {
		// Factor --> [ UnaryOp ] Primary
		if (isUnaryOp()) {
			Operator op = new Operator(match(token.type()));
			Expression term = primary();
			return new Unary(op, term);
		} else
			return primary();
	}

	private Expression primary() {
		// Primary --> Identifier | Literal | ( Expression )
		// | Type ( Expression )
		Expression e = null;
		if (token.type().equals(TokenType.Identifier)) {
			e = new Variable(match(TokenType.Identifier));
		} else if (isLiteral()) {
			e = literal();
		} else if (token.type().equals(TokenType.LeftParen)) { // if a primary
																// that has (,
																// ignore it
			token = lexer.next(); // move to next one
			e = expression(); // get expression
			match(TokenType.RightParen); // ignore )
		} else if (isType()) {
			Operator op = new Operator(match(token.type()));
			match(TokenType.LeftParen);
			Expression term = expression();
			match(TokenType.RightParen);
			e = new Unary(op, term);
		} else
			error("Identifier | Literal | ( | Type");
		return e;
	}

	/*
	 * Compare the current token.type() with TokenType.IntLiteral follow the
	 * match() method in the Parser for comparing tokens and extracting values
	 * And return token.value
	 */
	// student exercise
	private Value literal() {
		if (token.type() == TokenType.IntLiteral) {
			IntValue i = new IntValue(Integer.parseInt(token.value()));
			token = lexer.next(); // must move to the next one
			return i;
		} else if (token.type() == TokenType.FloatLiteral) {
			FloatValue f = new FloatValue(Float.parseFloat(token.value()));
			token = lexer.next();
			return f;
		} else if (token.type() == TokenType.CharLiteral) {
			CharValue c = new CharValue((token.value()).charAt(0));
			token = lexer.next();
			return c;
		} else if (token.type() == TokenType.False) {
			BoolValue b = new BoolValue(Boolean.valueOf(token.value()));
			token = lexer.next();
			return b;
		} else if (token.type() == TokenType.True) {
			BoolValue b = new BoolValue(Boolean.valueOf(token.value()));
			token = lexer.next();
			return b;
		} else {
			error("should not get here");
			return null;
		}
	}

	private boolean isOrOp() {
		return token.type().equals(TokenType.Or);
	}

	private boolean isAndOp() {
		return token.type().equals(TokenType.And);
	}

	private boolean isAddOp() {
		return token.type().equals(TokenType.Plus)
				|| token.type().equals(TokenType.Minus);
	}

	private boolean isMultiplyOp() {
		return token.type().equals(TokenType.Multiply)
				|| token.type().equals(TokenType.Divide);
	}

	private boolean isUnaryOp() {
		return token.type().equals(TokenType.Not)
				|| token.type().equals(TokenType.Minus);
	}

	private boolean isEqualityOp() {
		return token.type().equals(TokenType.Equals)
				|| token.type().equals(TokenType.NotEqual);
	}

	private boolean isRelationalOp() {
		return token.type().equals(TokenType.Less)
				|| token.type().equals(TokenType.LessEqual)
				|| token.type().equals(TokenType.Greater)
				|| token.type().equals(TokenType.GreaterEqual);
	}

	private boolean isType() {
		return token.type().equals(TokenType.Int)
				|| token.type().equals(TokenType.Bool)
				|| token.type().equals(TokenType.Float)
				|| token.type().equals(TokenType.Char);
	}

	private boolean isLiteral() {
		return token.type().equals(TokenType.IntLiteral) || isBooleanLiteral()
				|| token.type().equals(TokenType.FloatLiteral)
				|| token.type().equals(TokenType.CharLiteral);
	}

	private boolean isBooleanLiteral() {
		return token.type().equals(TokenType.True)
				|| token.type().equals(TokenType.False);
	}

	public static void main(String args[]) {
		Parser parser = new Parser(new Lexer(args[0]));
		Program prog = parser.program();
		prog.display(0); // display abstract syntax tree
	} // main

} // Parser
