package assign6;


import assign6.ast.*;
import assign6.lexer.* ;
import assign6.parser.* ;
import assign6.unparser.*;
import assign6.typechecker.*;


public class Main {
    public static void main (String[] args) {
        Lexer lexer = new Lexer();
        Parser parser = new Parser(lexer);
        TypeChecker typeCheck = new TypeChecker(parser);
        Unparser unparser = new Unparser(typeCheck);

    }
}