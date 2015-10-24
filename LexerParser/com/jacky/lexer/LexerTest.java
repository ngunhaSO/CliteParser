package com.jacky.lexer;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import javax.xml.ws.ServiceMode;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LexerTest {
	
	private Lexer lexer;
	public ExpectedException expectedException = ExpectedException.none();
	
	//set up
	public LexerTest() {
		lexer = new Lexer("C:\\Users\\JackyNguyen\\workspace_school\\LexerParser\\src\\programs\\hello.cpp");
	}
	
	@Ignore
	@Test(expected = FileNotFoundException.class)
	public void testLoadFile(){
		Lexer lexerTest = new Lexer("C:\\Users\\JackyNguyen\\workspace_school\\LexerParser\\src\\programs\\hello.cpp");
	}
	
	@Test
	public void testLoadFileFail() {
		Lexer lexerTest = new Lexer("C:\\Users\\JackyNguyen\\workspace_school\\LexerParser\\src\\programs\\hello.cpp");
		expectedException.expect(FileNotFoundException.class);
	}
	
	//test chkOpt function
	//test pass
	@Ignore
	@Test
	public void testChkOpt() {
		//Lexer lexer = new Lexer("C:\\Users\\JackyNguyen\\workspace_school\\LexerParser\\src\\programs\\hello.cpp");
		Token token = lexer.chkOpt('=', Token.assignTok, Token.eqeqTok);
		Token expected = Token.assignTok;
		assertEquals(expected, token);
	}
	
	@Test
	public void testChkOpt3(){
		Token token = lexer.chkOpt('!', Token.notTok, Token.noteqTok);
		Token expected = Token.notTok;
		assertEquals(expected, token);
	}
	
	//test fail
	@Ignore
	@Test
	public void testChkOptFail() {
		//Lexer lexer = new Lexer("C:\\Users\\JackyNguyen\\workspace_school\\LexerParser\\src\\programs\\hello.cpp");
		Token token = lexer.chkOpt('=', Token.assignTok, Token.eqeqTok);
		Token expected = Token.eqeqTok;
		assertEquals(token, expected);
	}
	
	//test pass
	@Ignore
	@Test
	public void testChkOpt2() {
		Token token = lexer.chkOpt('<', Token.ltTok, Token.lteqTok);
		Token expected = Token.ltTok;
		assertEquals(token, expected);
	}
	
	
	//test pass check digit
	@Test
	public void testIsDigit(){
		boolean isDig = lexer.isDigit('3');
		assertTrue(isDig);
	}
	
	//test fail check digit
	@Test
	public void testIsDigitFail() {
		boolean isDig = lexer.isDigit('a');
		assertTrue(isDig);
	}
}
