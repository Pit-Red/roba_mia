import java.io.*;
import java.util.List;

import javax.swing.text.html.HTML.Tag;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() { 
	    look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) { 
	    throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
	    if (look.tag == t) {
	        if (look.tag != Tag.EOF) move();
	    } else error("syntax error");
    }

    public void prog() {        
        int lnext_prog = code.newLabel();
        statlist(lnext_prog);
        code.emitLabel(lnext_prog);
        match(Tag.EOF);
        try {
        	code.toJasmin();
        }
        catch(java.io.IOException e) {
        	System.out.println("IO error\n");
        };
	// ... completare ...
    }

    public void statlist(int line){
        if(look.tag == ';'){
            match(';');
            line = code.newLabel();
            //code.emitLabel(line);
            stat(line);
        }
    }

    public void stat(int line) {
        switch(look.tag) {
            case Tag.ASSIGN:
                match(look.tag);
                expr(line);
                match(Tag.TO);
                idlist(line);
                break;
            case Tag.PRINT:
                match(look.tag);
                match('[');
                idlist(line);
                code.emit(opCode.invokestatic, 1);
                match(']');
                break;
            case Tag.READ:
                match(look.tag);
                match('[');
	            idlist(line);
                match(']');
                break;
            case Tag.WHILE:
                match(look.tag);
                match('(');
                bexpr(line);
                match(')');
                stat(line);
                break;
            case Tag.COND:
                match(look.tag);
                match('[');
                optlist(line);
                match(']');
                stat_();
                break;
            case '{':
                match('{');
                statlist(line);
                match('}');
                break;
        }
     }

    private void stat_(int line){
        switch(look.tag){
            case Tag.END:
                match(look.tag);
                break;
            case Tag.ELSE:
                match(look.tag);
                stat(line);
                match(Tag.END);
                break;
        }
    }


    private void idlist(int line) {
        switch(look.tag) {
	    case Tag.ID:
        	int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr==-1) {
                    id_addr = count;
                    st.insert(((Word)look).lexeme,count++);
                }
                match(Tag.ID);
	// ... completare ...
    	}
    }

    private void expr(int line) {
        switch(look.tag) {
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                code.emit(OpCode.iadd);
                break;
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                code.emit(OpCode.imul);
                break;
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv)
                break;
            case Tag.NUM:
                code.emit(opCode.ldc, NumberTok.valore);
                match(Tag.NUM)
                break;
            case Tag.ID:
                code.emit()
                break;
	// ... completare ...
        }
    }

    private void exprlist(){

    }
}
