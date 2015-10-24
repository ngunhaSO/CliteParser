package com.jacky.lexer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ParserTest {

	Lexer lexer;
	Parser parser;

	@Before
	public void setUp() throws Exception {
		lexer = new Lexer(
				"C:\\Users\\JackyNguyen\\workspace_school\\LexerParser\\src\\programs\\test3.cpp");
		parser = new Parser(lexer);
		TokenType[] header = { TokenType.Int, TokenType.Main,
				TokenType.LeftParen, TokenType.RightParen };
		for (int i = 0; i < header.length; i++)// bypass "int main ( )"
			parser.match(header[i]);
		parser.match(TokenType.LeftBrace);
	}
	
	//test for file named: test2.cpp
	@Ignore
	@Test
	public void test_match() {
		String actual = parser.match(TokenType.Identifier);
		String expected = "a";
		assertEquals(expected, actual);
	}

	//test for file named test2.cpp
	@Ignore
	@Test
	public void test_match_fail() {
		String actual = parser.match(TokenType.IntLiteral);
		String expected = "Identifier";
		assertEquals(expected, actual);
	}
	
	//test for file named test3.cpp
	//this should fail too
	@Test
	public void test_match_fail2() {
		Token token = parser.token;
		//token = lexer.next();
		if(token.type() != TokenType.If) {
			parser.error(token.type());
		}
	}
	
	@Ignore
	@Test
	public void test_statementSemiColon() {
		Token t = parser.token;
		assertEquals(TokenType.Semicolon, t.type());
		Statement s = parser.statement();
		assertTrue(s == null);
	}
	
	@Ignore
	@Test
	public void test_statementIf() {
		Token t = parser.token; //getting semicolon
		t = lexer.next(); //move to next token
		assertEquals(TokenType.If, t.type()); //should be if 
		Statement s = parser.statement();	
	}

}
