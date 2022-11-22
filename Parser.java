import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
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

    public void start() {
	    if(look.tag == '(')
            match(look.tag);
        expr();
        match(Tag.EOF);
    }

    private void expr() {
        if(look.tag == '(')
            match(look.tag);
        term();//
        exprp();
    }

    private void exprp() {
        switch ((char)look.tag) {
            case '+':
                match(look.tag);
                term();
                exprp();
                break;
            case '-':
                match(look.tag);
                term();
                exprp();
                break;  
        }
    }

    private void term() {
        fact();//
        termp();
    }

    private void termp() {
        switch ((char)look.tag) {
            case '*':
                match(look.tag);
                term();
                termp();
                break;
            case '/':
                match(look.tag);
                term();
                termp();
                break;  
        }
    }

    private void fact() {
        if(look.tag == 256){
            match(256);
            return;
        }
        else
            expr();//
        error("fact error");
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/home/pit/Desktop/prog/Lft/es 3/provaParser"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}