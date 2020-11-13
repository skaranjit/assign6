package assign6.typechecker;

import assign6.ast.*;
import assign6.visitor.*;
import assign6.parser.*;
import assign6.lexer.*;
import java.io.*;

public class TypeChecker extends ASTVisitor
{
    public Parser parser = null;
    public Env top = null;
    public Type lhsExp = null;
    public Type rhsExp = null;
    public boolean hasbeenInitialized = false;
    public String[] initilizedTokens = new String[1000];
    public TypeChecker(Parser parser)
    {
            this.parser = parser;
            
            visit(this.parser.cu);
    }
    public TypeChecker()
    {
            this.parser = new Parser();
            
            visit(this.parser.cu);
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
            rhsExp = Type.Bool;
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
   
	void error (String s){
		throw new Error ("near line " + this.parser.lexer.line + ": " + s);
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
        top = n.symbolTable;
        visit(n.block);
    }

    public void visit (BlockStatementNode n)
    {
        n.decls.accept(this);
        n.stmts.accept(this);

    }

    public void visit(Declarations n)
    {

    }

    public void visit(DeclarationNode n)
    {

    }

    public void visit(TypeNode n)
    {
    }

    public void visit (ArrayTypeNode n)
    {
    }

    public void visit (Statements n)
    {
       if (n.stmts != null){
            n.stmt.accept(this);
            n.stmts.accept(this);
        }
    }
    public void visit(StatementNode n)
    {

    }
    public void visit(AssignmentNode n)
    {
    	System.out.println("Debug: TypeChecker in Assignment Node");
    	Type left;
        if(!(top.table.containsKey(n.left.id)))
        {
	    System.out.println(n.left.id);
            error("Variable " + n.left.id +" has not been declared.");
        }
        left = top.table.get(n.left.id);
	lhsExp = left;
	System.out.println("Debug:" + left );
	n.left.accept(this);
	
	if (n.right instanceof  IdentifierNode){
		Type right;
		if(!(top.table.containsKey(((IdentifierNode)n.right).id)))
		{
			 error("Variable " + ((IdentifierNode)n.right).id +" has not been declared.");
		}
		right = top.table.get(((IdentifierNode)n.right).id);
		if(left == right) ((IdentifierNode)n.right).accept(this);
		else error("Type mismatch: "+ n.left.id+" of type " +left + " but " +((IdentifierNode)n.right).id + " of type " + right);
		System.out.println( "Debug: Right: "+ ((IdentifierNode)n.right).id);
 	       ((IdentifierNode)n.right).accept(this);
	       
	}
    	else if (n.right instanceof NumNode){
    		if(left == Type.Int) ((NumNode)n.right).accept(this);
		else error("Type mismatch: " + n.left.id+" of type " +left + " but " +((NumNode)n.right).value + " of type int");
	}
	else if (n.right instanceof RealNode){
	  	if(left== Type.Float) ((RealNode)n.right).accept(this);
		else	error("Type mismatch: "+n.left.id+" of type " +left + " but " +((RealNode)n.right).value + " of type float");        
        }
	else {
            ((BinExprNode)n.right).accept(this);
        }
      
    }

    public void visit(BinExprNode n)
    {
    	 if (n.left instanceof IdentifierNode)
        {
		Type right;
		if(!(top.table.containsKey(((IdentifierNode)n.right).id)))
		{
			 error("Variable " + ((IdentifierNode)n.right).id +" has not been declared.");
		}
		right = top.table.get(((IdentifierNode)n.right).id);
		if(lhsExp == right) ((IdentifierNode)n.right).accept(this);
		else error("Type mismatch: "+ n.left.id+" of type " +left + " but " +((IdentifierNode)n.right).id + " of type " + right);
         
        }
        else if (n.left instanceof NumNode)
        {
	    	if(lhsExp == Type.Int) ((NumNode)n.left).accept(this);
		else error("Type mismatch: " + n.left.id+" of type " +left + " but " +((NumNode)n.right).value + " of type int");
        }
        else if (n.left instanceof RealNode)
        {
		if(lhsExp == Type.Int) ((RealNode)n.left).accept(this);
		else error("Type mismatch: " + n.left.id+" of type " +left + " but " +((NumNode)n.right).value + " of type int");

            
        }
        else if (n.left instanceof BooleanNode)
        {
            ((BooleanNode)n.left).accept(this);
        }
        else if (n.left instanceof ParenNode)
        {
            ((ParenNode)n.left).accept(this);
        }
        else
            ((BinExprNode)n.left).accept(this);

        

        if (n.right != null)
        {
            if (n.right instanceof IdentifierNode)
            {
                ((IdentifierNode) n.right).accept(this);
            }
            else if (n.right instanceof NumNode)
            {
                ((NumNode) n.right).accept(this);
            }
            else if (n.right instanceof RealNode)
            {
                ((RealNode) n.right).accept(this);
            }
            else if (n.right instanceof BooleanNode)
            {
                ((BooleanNode) n.right).accept(this);
            }
            else if (n.right instanceof ParenNode)
            {
                ((ParenNode) n.right).accept(this);
            }
            else
                ((BinExprNode) n.right).accept(this);

        }
    }

    

   

    public void visit(BreakNode n)
    {
    }

    public void visit(ConditionalNode n)
    {
       
    }

    public void visit(WhileNode n)
    {
    }

    public void visit(BooleanNode n)
    {
    }

    public void visit(DoWhileNode n)
    {
    }

    public void visit(NumNode n)
    {
    }

    public void visit(RealNode n)
    {
    }

    public void visit(IdentifierNode n)
    {
    }

    public void visit (ArrayIDNode n)
    {
    }

    public void visit(ParenNode n)
    {
    }
}
