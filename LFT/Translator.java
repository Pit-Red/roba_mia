import java.io.*;
import java.util.List;

//import javax.swing.text.html.HTML.Tag;

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
    }

    public void statlist(int line){
        stat(line);
        match(';');
        if(look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ
             || look.tag == Tag.WHILE || look.tag == Tag.COND){
            line = code.newLabel();
            code.emitLabel(line);
            statlist(line);
        }
    }


    public void stat(int line) {
        switch(look.tag) {
            case Tag.ASSIGN:
                match(look.tag);
                expr();
                match(Tag.TO);
                idlist('i');
                break;
            case Tag.PRINT:
                match(look.tag);
                match('[');
                idlist('o');
                match(']');
                code.emit(OpCode.invokestatic, 1);
                break;
            case Tag.READ:
                match(look.tag);
                match('[');
	            idlist('i');
                match(']');
                code.emit(OpCode.invokestatic, 0);
                break;
            case Tag.WHILE:
                match(look.tag);
                match('(');
                //bexpr();
                match(')');
                stat(line);
                break;
            case Tag.COND:
                match(look.tag);
                match('[');
                //optlist();
                match(']');
                stat_(line);
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


    private void idlist(char operation) {
        switch(look.tag){
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr==-1) {
                    id_addr = count;
                    st.insert(((Word)look).lexeme,count++);
                }
                code.emit(OpCode.ldc, id_addr);
                match(Tag.ID);
                idlistp();
                break;
            case Tag.NUM:
                
                break;
        }
    }

    private void idlistp(){
        if(look.tag == ','){
            match(',');
            int id_addr = st.lookupAddress(((Word)look).lexeme);
            if (id_addr==-1) {
                id_addr = count;
                st.insert(((Word)look).lexeme,count++);
            }
            code.emit(OpCode.ldc, id_addr);
            match(Tag.ID);
            idlistp();
        }
    }

    private void expr() {
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
                code.emit(OpCode.idiv);
                break;
            case Tag.NUM:
                code.emit(OpCode.ldc, NumberTok.valore);
                match(Tag.NUM);
                break;
            case Tag.ID:
                code.emit(OpCode.iload, st.lookupAddress(((Word)look).lexeme));
                break;
        }
    }

    private void exprlist(){
        expr();
        exprlistp();
    }

    private void exprlistp(){
        if(look.tag == ','){
            match(',');
            expr();
            exprlistp();
        }
    }
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/home/pit/Desktop/prog/java/LFT/prova.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator trans = new Translator(lex, br);
            trans.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
    
}
