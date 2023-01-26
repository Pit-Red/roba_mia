import java.io.*;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
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
        int expr_val;
        expr_val = expr();
        match(Tag.EOF);
        System.out.println("Riusltato = " + expr_val);
    }

    private int expr() {
        int term_val, exprp_val;
        term_val = term();
        exprp_val = exprp(term_val);
        return exprp_val;
    }

    private int exprp(int exprp_i) {
        int term_val, exprp_val;
        switch (look.tag) {
            case '+':
                match(look.tag);
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;
            case '-':
                match(look.tag);
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                break;
            default:
                exprp_val = exprp_i;  
        }
        return exprp_val;
    }

    private int term() {
        int fact_val, term_val;
        fact_val = fact();
        term_val = termp(fact_val);
        return term_val;
    }

    private int termp(int termp_i) {
        int term_val, termp_val;
        switch ((char)look.tag) {
            case '*':
                match(look.tag);
                term_val = term();
                termp_val = termp(termp_i * term_val);
                break;
            case '/':
                match(look.tag);
                term_val = term();
                termp_val = termp(termp_i / term_val);
                break;
            default:
                termp_val = termp_i;
                break;  
        }
        return termp_val;
    }

    private int fact() {
        int fact_val = 0;
        switch(look.tag){
            case '(':
                match('(');
                fact_val = expr();
                match(')');
                break;
            case Tag.NUM:
                fact_val = NumberTok.valore;
                match(Tag.NUM);
                break;
        }
        return fact_val;
    }

		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/home/pit/Scrivania/prog/LFT/es 4/provaParser"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore parser = new Valutatore(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}