package assign6.typechecker;

import assign6.ast.*;
import assign6.visitor.*;
import assign6.parser.*;
import assign6.lexer.*;
import java.io.*;

public class TypeChecker extends ASTVisitor
{
  public Parser parser = null;
    public Lexer lexer = null ;  
    public CompilationUnit cu = null;
    public Env top = null;
    public Type rhsExp = null;
    public boolean hasbeenInitialized = false;
    public String[] initilizedTokens = new String[1000];
    public TypeChecker(Parser parser)
    {
            this.parser = parser;
            cu = parser.cu;
            visit(cu);
    }
    public TypeChecker()
    {
            this.parser = new Parser();
            cu = this.parser.cu;
            visit(cu);
   }

   public void getType(IdentifierNode a){
        String x = a.id;
        if(isDigit(x))
        {
            rhsExp =  Type.Int;
            hasbeenInitialized = true; /////////////////////////////////////
        }
        else if(isFloat(x))
        {
            rhsExp =  Type.Float;
            hasbeenInitialized = true;/////////////////////////////////////
        }
        else if(x.equals("true") || x.equals("false"))
        {
            rhsExp = Type.Boolean;
        }
        else if(top.table.get(a.id) == null)
        {
            error(  a.id + " variable not declared!");
        } 
        else
        {
            rhsExp = top.table.get(x);
        }
    }

    boolean isDigit(final String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    boolean isFloat(final String str) {
        int numDec = 0;
        for (char c : str.toCharArray()) {
            if(c=='.')
                numDec+=1;
            else if (!Character.isDigit(c) && numDec==0 || numDec>1) {
                return false;
            }  
        }
        return true;
    }
   
    void error(String s)
    {
        throw new Error ("near line " + lexer.line + ": " + s);
    }

    

    public boolean checkOperator(String x)
    {
        if(x.equals("+") || x.equals("-") || x.equals("*") || x.equals("/") || x.equals("(") || x.equals(")"))
        {return true;}
        return false;
    }

    public void visit (CompilationUnit n)
    {
        top = new Env();
        top.table = n.symbolTable.table;
        visit(n.block);
    }

    public void visit (BlockStatementNode n)
    {
        match('{');
        n.decls = new Declarations();
        n.decls.accept(this);
        n.stmts = new Statements();
        n.stmts.accept(this);
        match('}');
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
        n.id.accept(this);
       
        match(';');
    }

    public void visit(TypeNode n)
    {
        if (look.toString().equals("int"))
            n.basic = Type.Int;
        else if (look.toString().equals("float"))
            n.basic = Type.Float;
        match(Tag.BASIC);
        if (look.toString().equals("["))
        {
            n.array = new ArrayTypeNode();
	    n.array.ofType = n.basic;
            n.array.accept(this);
	    n.basic = new Array(n.array.size,n.array.ofType);
// 		match('[');
// 		Token temp = lookahead;
// 		match(Tag.NUM);
// 		n.type = new Array(((Num)temp).value, p);
// 		match(']');
        }
    }

    public void visit (ArrayTypeNode n)
    {
        match('[');
        n.size = ((Num)look).value;
	
        match(Tag.NUM);
        match(']');
        if (look.toString().equals("["))
        {
            n.type = new ArrayTypeNode();
            n.type.accept(this);
        }
    }

    public void visit (Statements n)
    {
        if (!look.toString().equals("}"))
        {
            switch (look.tag)
            {
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
        match('=');
        Node rhs_assign = null;
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
        if (look.tag == ';')
        {
            n.right = rhs_assign;
        }
        else
            n.right = (BinExprNode) parseBinExprNode(rhs_assign, 0);

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

    Node parseBinExprNode(Node lhs, int precedence)
    {
        while (getPrecedence(look.tag) >= precedence)
        {
            Token token_op = look;
            int op = getPrecedence(look.tag);
            move();
            Node rhs = null;
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
        Node rhs_assign = null;

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
        Node rhs_assign = null;
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
    }

    public void visit(DoWhileNode n)
    {
        match(Tag.DO);
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
        match(Tag.WHILE);
        match('(');
        Node rhs_assign = null;
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
            n.condition = (BinExprNode) parseBinExprNode(rhs_assign, 0);

        match(')');
        match(';');
    }

    public void visit(NumNode n)
    {
        n.value = ((Num)look).value;
        match(Tag.NUM);
    }

    public void visit(RealNode n)
    {
        n.value = ((Real)look).value;
        match(Tag.REAL);
    }

    public void visit(IdentifierNode n)
    {
        n.id = look.toString();
        match(Tag.ID);
        if (look.toString().equals("["))
        {
            n.array = new ArrayIDNode();
            ((ArrayIDNode)(n.array)).accept(this);
        }
    }

    public void visit (ArrayIDNode n)
    {
        match('[');
        Node rhs_assign = null;
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

        Node rhs_assign = null;
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
            n.node = (BinExprNode) parseBinExprNode(rhs_assign, 0);

        match(')');
    }
}