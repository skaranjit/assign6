package assign6;

import assign6.lexer.* ;
import assign6.parser.* ;
import assign6.unparser.*;


public class Main {
    public static void main (String[] args) {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(lexer);
        Unparser unparser = new Unparser(parser);
        
    }
}

