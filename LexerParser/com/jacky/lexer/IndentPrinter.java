package com.jacky.lexer;

import java.io.PrintWriter;

public class IndentPrinter {
    private int indent;
    private PrintWriter out;

    public IndentPrinter() {
        this(new PrintWriter(System.out), 0);
    }

    public IndentPrinter(PrintWriter out) {
        this(out, 0);
    }

    public IndentPrinter(PrintWriter out, int indent) {
        this.out = out;
        this.indent = indent;
    }

    public String println(Object value) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < indent; i++){
        	sb.append(" ");
        }
        sb.append(value.toString());
        return sb.toString();
    }

    public String println(String text) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < indent; i++){
        	sb.append(" ");
        }
        sb.append(text);
        return sb.toString();
    }

    public void print(String text) {
        out.println(text);
    }

    public void println() {
        out.println();
    }

    public void incrementIndent() {
        ++indent;
    }

    public void decrementIndent() {
        --indent;
    }

    public int getIndentLevel() {
        return indent;
    }

    public void setIndentLevel(int indentLevel) {
        this.indent = indentLevel;
    }

    public void flush() {
        out.flush();
    }
	public static void main(String[] args) {		
		PrintWriter pw1  = new PrintWriter(System.out, true);
		IndentPrinter ip = new IndentPrinter(pw1, 0);
		
		ip.println("hello");
		ip.incrementIndent();
		ip.incrementIndent();
		ip.println("there");
	}
}
