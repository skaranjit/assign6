package assign6.parser;

import assign6.ast.*;
import assign6.visitor.*;
import assign6.lexer.*;
import java.io.*;

public class Parser extends ASTVisitor
{
    public CompilationUnit cu = null;
    public Lexer lexer = null ;
    public Token look = null;
    public int level;
    String indent = "...";



    public Env top = null;

    public Parser (Lexer lexer)
    {
        this.lexer = lexer;
        cu = new CompilationUnit();
        move();
        visit(cu);
    }

    public Parser ()
    {
        cu = new CompilationUnit();
        move();
        visit(cu);
        level =0;
    }

    void move()
    {
        try
        {
            look = lexer.scan();
        }
        catch(IOException e)
        {

        }
    }


    void error(String s){
        println("Line " + lexer.line + ": " + s);
        exit(1);
    }


    void match(int t){
        try{
            if (look.tag == t) {
                println("matched: "+look.toString());
                move();

            }
            else if (look.tag == Tag.EOF)
                error("Syntax error: \";\" or \"}\" expected");
            else
                error("Syntax error: \"" + (char)t + "\" expected");
        }
        catch (Error e){

        }
    }


    void print(String s){
        System.out.print(s);
    }


    void println(String s){
        System.out.println(s);
    }


    void exit(int n){
        System.exit(n);
    }

    public void visit (CompilationUnit n)
    {
        n.block = new BlockStatementNode();
        n.block.accept(this);
    }


    public void visit (BlockStatementNode n)
    {
        match('{');
        n.sTable = top; // new code
        top = new Env(top); // new code
        n.decls = new Declarations();
        n.decls.accept(this);
        n.stmts = new Statements();
        n.stmts.accept(this);
        match('}');
        top = n.sTable; // new code
    }

    public void visit(Declarations n)
    {
        if (look.tag == Tag.BASIC)
        {
            n.decl = new DeclarationNode();
            n.decl.accept(this);
            n.decls = new Declarations();
            n.decls.accept(this);
        }
    }


    public void visit(DeclarationNode n)
    {
        n.type = new TypeNode();
        n.type.accept(this);
        n.id = new IdentifierNode();
        n.id.type = n.type.basic; // new code
        n.id.accept(this);
        top.put(n.id.w, n.id); // new code
        IdentifierNode tmp = (IdentifierNode)top.get(n.id.w); // new code
        println("&&&&&& tmp.type: " + tmp.type); // new code
        println("&&&&&& tmp.w: " + tmp.w); // new code

        match(';');
    }


    public void visit(TypeNode n)
    {
        if (look.toString().equals("int"))
            n.basic = Type.Int;
        else if (look.toString().equals("float"))
            n.basic = Type.Float;
        match(Tag.BASIC);
        if (look.tag == '[')
        {
            n.array = new ArrayTypeNode();
            n.array.accept(this);
        }
    }


    public void visit (ArrayTypeNode n)
    {
        match('[');
        ExprNode rhs_assign = null;

        if (look.tag == Tag.ID)
        {
            rhs_assign = new IdentifierNode();
            ((IdentifierNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.NUM)
        {
            rhs_assign = new NumNode();
            ((NumNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.REAL)
        {
            rhs_assign = new RealNode();
            ((RealNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.TRUE || look.tag == Tag.FALSE)
        {
            rhs_assign = new BooleanNode();
            ((BooleanNode)rhs_assign).accept(this);
        }
        else if (look.tag == '(')
        {
            rhs_assign = new ParenNode();
            ((ParenNode)rhs_assign).accept(this);
        }
        else {
            n.size = (BinExprNode) parseBinExprNode(rhs_assign, 0);
            n.size.type = Type.Int;
        }
        //match(Tag.NUM);
        match(']');
        if (look.tag == '[')
        {
            n.type = new ArrayTypeNode();
            n.type.accept(this);
        }
    }


    public void visit (Statements n)
    {
        if (look.tag != '}' && look.tag != Tag.EOF) // new line of code
        {
            switch (look.tag)
            {
	    	case Tag.BASIC:
			n.decl = new DeclarationNode();
            		n.decl.accept(this);
			n.stmts = new Statements();
			n.stmts.accept(this);
			break;
                case Tag.ID:
                    n.stmt = new AssignmentNode();
                    (n.stmt).accept(this);
                    n.stmts = new Statements();
                    n.stmts.accept(this);
                    break;
                case Tag.IF:
                    n.stmt = new ConditionalNode();
                    (n.stmt).accept(this);
                    n.stmts = new Statements();
                    n.stmts.accept(this);
                    break;
                case Tag.WHILE:
                    n.stmt = new WhileNode();
                    (n.stmt).accept(this);
                    n.stmts = new Statements();
                    n.stmts.accept(this);
                    break;
                case Tag.DO:
                    n.stmt = new DoWhileNode();
                    (n.stmt).accept(this);
                    n.stmts = new Statements();
                    n.stmts.accept(this);
                    break;
                case Tag.BREAK:
                    n.stmt = new BreakNode();
                    (n.stmt).accept(this);
                    n.stmts = new Statements();
                    n.stmts.accept(this);
                    break;
                case '{':
                    n.stmt = new BlockStatementNode();
                    (n.stmt).accept(this);
                     n.stmts = new Statements();
                     n.stmts.accept(this);
                    break;
            }
        }
    }


    public void visit(AssignmentNode n)
    {
        n.left = new IdentifierNode();
        n.left.accept(this);

        if (top.get(n.left.w)==null){
            error("Variable Must be Declared");
        }else {
            n.left.type = top.get(n.left.w).type;
        }

        IdentifierNode id = (IdentifierNode)top.get(((IdentifierNode)n.left).w); // new code
        println("In Parser, AssignmentNode's left type: " + id.type); // new code

        //((IdentifierNode)n.left).type = id.type; // new code

        //if (look.tag == '['){ // new code
        //    n.left = parseArrayAccessNode((IdentifierNode)n.left); // new code
        //} // new code

        match('=');
        ExprNode rhs_assign = null;
        if (look.tag == Tag.ID)
        {
            rhs_assign = new IdentifierNode();
            ((IdentifierNode)rhs_assign).accept(this);
            println(top.get(((IdentifierNode)rhs_assign).w).type.toString());
            rhs_assign.type = top.get(((IdentifierNode)rhs_assign).w).type;
            if (top.get(((IdentifierNode)rhs_assign).w)==null){
                error("Variable Must be Declared");
            }else {
                n.right = rhs_assign;
                n.right.type = rhs_assign.type;
            }
        }
        else if (look.tag == Tag.NUM)
        {
            rhs_assign = new NumNode();
            ((NumNode)rhs_assign).accept(this);
            n.right.type = Type.Int;
        }
        else if (look.tag == Tag.REAL)
        {
            rhs_assign = new RealNode();
            ((RealNode)rhs_assign).accept(this);
            n.right.type = Type.Float;
        }
        else if (look.tag == Tag.TRUE || look.tag == Tag.FALSE)
        {
            rhs_assign = new BooleanNode();
            ((BooleanNode)rhs_assign).accept(this);
            n.right.type = Type.Bool;
        }
        else if (look.tag == '(')
        {
            rhs_assign = new ParenNode();
            ((ParenNode)rhs_assign).accept(this);
            n.right.type = ((ParenNode)rhs_assign).type;
        }
        if (look.tag == ';')
        {
	    
            n.right = rhs_assign;
        }
        else {
            n.right = (BinExprNode) parseBinExprNode(rhs_assign, 0);

        }

        match(';');
    }

    public void visit(BinExprNode n)
    {

    }

    int getPrecedence(int op)
    {
        switch (op)
        {
            case '*': case '/': case '%':  return 12; // multiplicative
            case '+': case '-':            return 11; // additive
            case '<': case '>':
            case Tag.LE: case Tag.GE:      return 9;  // relational
            case Tag.EQ: case Tag.NE:      return 8;  // equality
            case Tag.OR:                   return 3;
            case Tag.AND:                  return 4;

            default:
                return -1;
        }
    }

    ExprNode parseBinExprNode(ExprNode lhs, int precedence)
    {
        while (getPrecedence(look.tag) >= precedence)
        {
            Token token_op = look;
            int op = getPrecedence(look.tag);
            move();
            ExprNode rhs = null;
            if (look.tag == Tag.ID)
            {
                rhs = new IdentifierNode();
                ((IdentifierNode)rhs).accept(this);
            }
            else if (look.tag == Tag.NUM)
            {
                rhs = new NumNode();
                ((NumNode)rhs).accept(this);
            }
            else if (look.tag == Tag.REAL)
            {
                rhs = new RealNode();
                ((RealNode)rhs).accept(this);
            }
            else if (look.tag == Tag.TRUE || look.tag == Tag.FALSE)
            {
                rhs = new BooleanNode();
                ((BooleanNode)rhs).accept(this);
            }
            else if (look.tag == '(')
            {
                rhs = new ParenNode();
                ((ParenNode)rhs).accept(this);
            }
            while (getPrecedence(look.tag) > op)
            {
                rhs = parseBinExprNode(rhs, getPrecedence(look.tag));
            }
            lhs = new BinExprNode(token_op, lhs, rhs);
            lhs.type = rhs.type;
        }
        return lhs;
    }

    public void visit(BreakNode n)
    {
        match(Tag.BREAK);
        match(';');
    }

    public void visit(ConditionalNode n)
    {
        match(Tag.IF);
        match('(');
        ExprNode rhs_assign = null;

        if (look.tag == Tag.ID)
        {
            rhs_assign = new IdentifierNode();
            ((IdentifierNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.NUM)
        {
            rhs_assign = new NumNode();
            ((NumNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.REAL)
        {
            rhs_assign = new RealNode();
            ((RealNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.TRUE || look.tag == Tag.FALSE)
        {
            rhs_assign = new BooleanNode();
            ((BooleanNode)rhs_assign).accept(this);
        }
        else if (look.tag == '(')
        {
            rhs_assign = new ParenNode();
            ((ParenNode)rhs_assign).accept(this);
        }
        if (look.tag == ')')
        {
            n.condition = rhs_assign;
        }
        else
            n.condition = (BinExprNode) parseBinExprNode(rhs_assign, 0);

        match(')');
        switch (look.tag)
        {
            case Tag.ID:
                n.stmt = new AssignmentNode();
                (n.stmt).accept(this);
                break;
            case Tag.IF:
                n.stmt = new ConditionalNode();
                (n.stmt).accept(this);
                break;
            case Tag.WHILE:
                n.stmt = new WhileNode();
                (n.stmt).accept(this);
                break;
            case Tag.DO:
                n.stmt = new DoWhileNode();
                (n.stmt).accept(this);
                break;
            case Tag.BREAK:
                n.stmt = new BreakNode();
                (n.stmt).accept(this);
                break;
            case '{':
                n.stmt = new BlockStatementNode();
                (n.stmt).accept(this);
                break;
        }

        if (look.tag == Tag.ELSE)
        {
            switch (look.tag)
            {
                case Tag.ID:
                    n.elseStmt = new AssignmentNode();
                    (n.elseStmt).accept(this);
                    break;
                case Tag.IF:
                    n.elseStmt = new ConditionalNode();
                    (n.elseStmt).accept(this);
                    break;
                case Tag.WHILE:
                    n.elseStmt = new WhileNode();
                    (n.elseStmt).accept(this);
                    break;
                case Tag.DO:
                    n.elseStmt = new DoWhileNode();
                    (n.elseStmt).accept(this);
                    break;
                case Tag.BREAK:
                    n.elseStmt = new BreakNode();
                    (n.elseStmt).accept(this);
                    break;
                case '{':
                    n.elseStmt = new BlockStatementNode();
                    (n.elseStmt).accept(this);
                    break;
            }
        }
        else n.elseStmt= null;
    }

    public void visit(WhileNode n)
    {
        match(Tag.WHILE);
        match('(');
        ExprNode rhs_assign = null;
        if (look.tag == Tag.ID)
        {
            rhs_assign = new IdentifierNode();
            ((IdentifierNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.NUM)
        {
            rhs_assign = new NumNode();
            ((NumNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.REAL)
        {
            rhs_assign = new RealNode();
            ((RealNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.TRUE || look.tag == Tag.FALSE)
        {
            rhs_assign = new BooleanNode();
            ((BooleanNode)rhs_assign).accept(this);
        }
        else if (look.tag == '(')
        {
            rhs_assign = new ParenNode();
            ((ParenNode)rhs_assign).accept(this);
        }
        if (look.tag == ')')
        {
            n.condition = rhs_assign;
        }
        else
            n.condition = (BinExprNode) parseBinExprNode(rhs_assign, 0);

        match(')');
        n.stmt = new StatementNode();
        n.stmt.accept(this);
    }

    public void visit(BooleanNode n)
    {
        if (look.tag == Tag.TRUE)
        {
            n.bool =Word.True;
            match(Tag.TRUE);
        }
        else if (look.tag == Tag.FALSE)
        {
            n.bool =Word.False;
            match(Tag.FALSE);
        }
        n.type = Type.Bool;
    }

    public void visit(DoWhileNode n)
    {
        match(Tag.DO);
	while(look.tag != Tag.WHILE){
        switch (look.tag)
        {
            case Tag.ID:
                n.stmt = new AssignmentNode();
                (n.stmt).accept(this);
                break;
            case Tag.IF:
                n.stmt = new ConditionalNode();
                (n.stmt).accept(this);
                break;
            case Tag.WHILE:
                n.stmt = new WhileNode();
                ((WhileNode)n.stmt).accept(this);
                break;
            case Tag.DO:
                n.stmt = new DoWhileNode();
                (n.stmt).accept(this);
                break;
            case Tag.BREAK:
                n.stmt = new BreakNode();
                (n.stmt).accept(this);
                break;
            case '{':
                n.stmt = new BlockStatementNode();
                (n.stmt).accept(this);
                break;
        }
	}
        match(Tag.WHILE);
        match('(');
        ExprNode rhs_assign = null;
        if (look.tag == Tag.ID)
        {
            rhs_assign = new IdentifierNode();
            ((IdentifierNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.NUM)
        {
            rhs_assign = new NumNode();
            ((NumNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.REAL)
        {
            rhs_assign = new RealNode();
            ((RealNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.TRUE || look.tag == Tag.FALSE)
        {
            rhs_assign = new BooleanNode();
            ((BooleanNode)rhs_assign).accept(this);
        }else if (look.tag == '(')
        {
            rhs_assign = new ParenNode();
            ((ParenNode)rhs_assign).accept(this);
        }
        if (look.tag == ')')
        {
            n.condition = rhs_assign;
        }
        else
            n.condition = (ExprNode) parseBinExprNode(rhs_assign, 0);

        match(')');
        match(';');
    }


    public void visit(NumNode n)
    {
        n.value = ((Num)look).value;

        if (look.tag != Tag.NUM) // new code
            error("Syntax error: Integer number needed, instead of " + n.value); // new code

        match(Tag.NUM);
        n.type = Type.Int;
        n.printNode(); // new code
    }


    public void visit(RealNode n)
    {
        n.value = ((Real)look).value;

        if (look.tag != Tag.REAL) // new code
            error("Syntax error: Real number needed, instead of " + n.value); // new code

        match(Tag.REAL);
        n.type = Type.Float;
        n.printNode(); // new code
    }


    public void visit(IdentifierNode n)
    {
        n.id = look.toString();
        n.w = (Word)look; // new code
	if((IdentifierNode)top.get(n.w) != null){
		n = (IdentifierNode)top.get((Word)look);
	}
	

      	println("***** n.type: "+ n.type); // new code

        if (look.tag != Tag.ID) // new code
            error("Syntax error: Identifier or variable needed, instead of " + n.id); // new code

        match(Tag.ID);
        if(look.tag == '['){
            n.array = new ArrayIDNode();
            n.array.accept(this);
        }
        n.printNode(); // new code
    }

    public void visit (ArrayIDNode n)
    {
        match('[');
        ExprNode rhs_assign = null;
        if (look.tag == Tag.ID)
        {
            rhs_assign = new IdentifierNode();
            ((IdentifierNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.NUM)
        {
            rhs_assign = new NumNode();
            rhs_assign.type = Type.Int;
            ((NumNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.REAL)
        {
            rhs_assign = new RealNode();
            rhs_assign.type = Type.Float;
            ((RealNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.TRUE || look.tag == Tag.FALSE)
        {
            rhs_assign = new BooleanNode();
            rhs_assign.type = Type.Bool;
            ((BooleanNode)rhs_assign).accept(this);
        } else if (look.tag == '(') {
            rhs_assign = new ParenNode();
            ((ParenNode)rhs_assign).accept(this);
        }
        if (look.tag == ']')
        {

            n.node = rhs_assign;
        }
        else
            n.node = (BinExprNode) parseBinExprNode(rhs_assign, 0);

        match(']');
        if (look.toString().equals("["))
        {
            n.array = new ArrayIDNode();
            n.array.accept(this);
        }
    }

    public void visit(ParenNode n)
    {
        match('(');

        ExprNode rhs_assign = null;
        if (look.tag == Tag.ID)
        {
            rhs_assign = new IdentifierNode();
	    System.out.println("Inside Parenthesis node");
            ((IdentifierNode)rhs_assign).accept(this);
	    System.out.println("Type: " + rhs_assign.type);
        }
        else if (look.tag == Tag.NUM)
        {
            rhs_assign = new NumNode();
            ((NumNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.REAL)
        {
            rhs_assign = new RealNode();
            ((RealNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.TRUE || look.tag == Tag.FALSE)
        {
            rhs_assign = new BooleanNode();
            ((BooleanNode)rhs_assign).accept(this);
        } else if (look.tag == '(')
        {
            rhs_assign = new ParenNode();
            ((ParenNode)rhs_assign).accept(this);
        }
        if (look.tag == ')')
        {
            n.node = rhs_assign;
        }
        else
            n.node = (ExprNode) parseBinExprNode(rhs_assign, 0);

        match(')');
	
	  n.type = rhs_assign.type;
	  System.out.println("Mytype: "+ n.type);
    }

    Node parseArrayAccessNode(IdentifierNode id){
        for (int i = 0; i < level; i++) System.out.print(indent);
        System.out.println("parseArrayAccessNode");

        ArrayIDNode index = new ArrayIDNode();
        level++;
        index.accept(this);
        level--;

        return new ArrayIDNode(id, index);
    }

}
