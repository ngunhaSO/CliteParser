package com.jacky.lexer;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class TestAndOr {

	@Test
	public void testPass() {
		TestMe me = new TestMe();
		assertEquals("yea", me.testOrPass());
	}
	
	@Ignore
	@Test
	public void testFail() {
		TestMe me = new TestMe();
		assertEquals("yea", me.testOrFail());
	}

}
