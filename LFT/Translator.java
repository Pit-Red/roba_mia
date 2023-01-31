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
        System.out.print(look.tag + "," + t);
	    if (look.tag == t) {
	        if (look.tag != Tag.EOF) move();
	    } else error("syntax error");
    }

    public void prog() {        
        int lnext_prog = code.newLabel();
        statlist();
        code.emitLabel(lnext_prog);
        match(Tag.EOF);
        try {
        	code.toJasmin();
        }
        catch(java.io.IOException e) {
        	System.out.println("IO error\n");
        };
    }

    public void statlist(){
        if(look.tag == Tag.WHILE)
            stat();
        else{
            stat();
            match(';');
        }
        if(look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ
             || look.tag == Tag.WHILE || look.tag == Tag.COND){
            code.emitLabel(code.newLabel());
            statlist();
        }
    }


    public void stat() {
        switch(look.tag) {
            case Tag.ASSIGN:
                match(look.tag);
                expr();
                match(Tag.TO);
                idlist();
                break;
            case Tag.PRINT:
                match(look.tag);
                match('[');
                exprlist("print");
                match(']');
                break;
            case Tag.READ:
                match(look.tag);
                code.emit(OpCode.invokestatic, 0);
                match('[');
	            idlist();
                match(']');
                break;
            case Tag.WHILE:
                int label1 = code.newLabel();
                int label2 = code.newLabel();
                int label3 = code.newLabel();
                match(look.tag);
                code.emitLabel(label1);
                match('(');
                bexpr(label2);
                match(')');
                code.emit(OpCode.GOto,label3);
                code.emitLabel(label2);
                stat();
                code.emit(OpCode.GOto,label1);
                code.emitLabel(label3);
                break;
            case Tag.COND:
                int exitlabel = code.newLabel();
                match(look.tag);
                match('[');
                optlist(exitlabel);
                match(']');
                stat_();
                code.emitLabel(exitlabel);
                break;
            case '{':
                match('{');
                statlist();
                match('}');
                break;
        }
     }

    private void stat_(){
        switch(look.tag){
            case Tag.END:
                match(look.tag);
                break;
            case Tag.ELSE:
                match(look.tag);
                stat();
                match(Tag.END);
                break;
        }
    }

    private void optlist(int exitlabel){
        optitem(exitlabel);
        optlistp(exitlabel);
    }

    private void optitem(int exitlabel){
        int label1 = code.newLabel();
        int label2 = code.newLabel();
        match(Tag.OPTION);
        match('(');
        bexpr(label1);
        match(')');
        code.emit(OpCode.GOto, label2);
        match(Tag.DO);
        code.emitLabel(label1);
        stat();
        code.emit(OpCode.GOto, exitlabel);
        code.emitLabel(label2);
    }

    private void optlistp(int exitlabel){
        if(look.tag == Tag.OPTION){
            optitem(exitlabel);
            optlistp(exitlabel);
        }
    }


    private void idlist() {
        int id_addr = st.lookupAddress(((Word)look).lexeme);
        if (id_addr==-1) {
            id_addr = count;
            st.insert(((Word)look).lexeme,count++);
        }
        code.emit(OpCode.istore, id_addr);
        match(Tag.ID);
        idlistp();
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
                exprlist("add");
                match(')');
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
                exprlist("mul");
                match(')');
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
                match(Tag.ID);
                break;
        }
    }

    private void exprlist(String s){
        expr();
        if(s.equals("print")){
            code.emit(OpCode.invokestatic, 1);
        }
        if(look.tag == ',')
            exprlistp(s);
    }

    private void exprlistp(String s){
        match(',');
        expr();
        if(s.equals("print"))
            code.emit(OpCode.invokestatic, 1);
        if(look.tag == ',')
            exprlistp(s);
        if(s.equals("add")){
            code.emit(OpCode.iadd);
        }
        if(s.equals("mul")){
            code.emit(OpCode.imul);
        }
    }

    private void bexpr(int nlabel){
        switch(((Word)look).lexeme){
            case ">":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpgt, nlabel);
                break;
            case "<":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmplt, nlabel);
                break;
            case "==":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpeq, nlabel);
                break;
            case "<=":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmple, nlabel);
                break;
            case "<>":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpne, nlabel);
                break;
            case ">=":
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpge, nlabel);
                break;
        }
    }


    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/home/pit/Scrivania/prog/LFT/es 5.1/code2.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator trans = new Translator(lex, br);
            trans.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
    
}
