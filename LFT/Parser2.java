import java.io.*;   

public class Parser2 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser2(Lexer l, BufferedReader br) {
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

    void prog(){
        statlist();
        match(Tag.EOF);
    }

    void statlist(){
        stat();
        statlistp();
    }

    void statlistp(){
        if(look.tag == ';'){
            match(';');
            stat();
            statlistp();
        }
    }

    void stat(){
        switch(look.tag){
            case Tag.ASSIGN:
                match(look.tag);
                expr();
                match(Tag.TO);
                idlist();
                break;
            case Tag.PRINT:
                match(look.tag);
                match('[');
                exprlist();
                match(']');
                break;
            case Tag.READ:
                match(look.tag);
                match('[');
                idlist();
                match(']');
                break;
            case Tag.WHILE:
                match(look.tag);
                match('(');
                bexpr();
                match(')');
                stat();
                break;
            case Tag.COND:
                match(look.tag);
                match('[');
                optlist();
                match(']');
                stat_();
                break;
            case '{':
                match('{');
                statlist();
                match('}');
                break;
        }
    }

    void stat_(){
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

    void idlist(){
        match(Tag.ID);
        idlistp();
    }

    void idlistp(){
        if(look.tag == ','){
            match(',');
            match(Tag.ID);
            idlistp();
        }
    }

    void optlist(){
        optitem();
        optlistp();
    }

    void optlistp(){
        if(look.tag == Tag.OPTION){
            optitem();
            optlistp();
        }
    }

    void optitem(){
        match(Tag.OPTION);
        match('(');
        bexpr();
        match(')');
        match(Tag.DO);
        stat();
    }

    void bexpr(){
        match(Tag.RELOP);
        expr();
        expr();
    }

    void expr(){
        switch(look.tag){
            case '+':
                match(look.tag);
                match('(');
                exprlist();
                match(')');
                break;
            case '-':
                match(look.tag);
                expr();
                expr();
                break;
            case '*':
                match(look.tag);
                match('(');
                exprlist();
                match(')');
                break;
            case '/':
                match(look.tag);
                expr();
                expr();
                break;
            case Tag.NUM:
                match(look.tag);
                break;
            case Tag.ID:
                match(look.tag);
                break;
        }
    }

    void exprlist(){
        expr();
        exprlistp();
    }

    void exprlistp(){
        if(look.tag == ','){
            match(',');
            expr();
            exprlistp();
        }
    }


		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/home/pit/Scrivania/prog/LFT/es 3.2/provaParser"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser2 parser = new Parser2(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}