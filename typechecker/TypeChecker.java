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
       System.out.println("Debug: TypeChecker");
    }

    public void visit(DeclarationNode n)
    {
	 System.out.println("Debug: TypeChecker");
    }

    public void visit(TypeNode n)
    {
         System.out.println("Debug: TypeChecker");
    }

    public void visit (ArrayTypeNode n)
    {
         System.out.println("Debug: TypeChecker");
    }

    public void visit (Statements n)
    {
       System.out.println("Debug: TypeChecker");
    }

    public void visit(AssignmentNode n)
    {
       System.out.println("Debug: TypeChecker");
    }

    public void visit(BinExprNode n)
    {
	 System.out.println("Debug: TypeChecker");
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
        System.out.println("Debug: TypeChecker");
    }

    public void visit(BreakNode n)
    {
         System.out.println("Debug: TypeChecker");
    }

    public void visit(ConditionalNode n)
    {
       
        System.out.println("Debug: TypeChecker");
    }

    public void visit(WhileNode n)
    {
      System.out.println("Debug: TypeChecker");
    }

    public void visit(BooleanNode n)
    {
         System.out.println("Debug: TypeChecker");
    }

    public void visit(DoWhileNode n)
    {
        System.out.println("Debug: TypeChecker");
    }

    public void visit(NumNode n)
    {
	 System.out.println("Debug: TypeChecker");
    }

    public void visit(RealNode n)
    {
       System.out.println("Debug: TypeChecker");
    }

    public void visit(IdentifierNode n)
    {
      System.out.println("Debug: TypeChecker");
    }

    public void visit (ArrayIDNode n)
    {
        System.out.println("Debug: TypeChecker");
    }

    public void visit(ParenNode n)
    {
     System.out.println("Debug: TypeChecker");
    }
}
