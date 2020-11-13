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
      
    void error (String s) throws Error{
	throw new Error ("near line " + this.parser.lexer.line + ": " + s);
    }

    public Type getType(IdentifierNode a){
        String x = a.id;
	if(!(top.table.containsKey(x)))
	{
		 error("Variable " + x +" has not been declared.");
	}
	return top.table.get(x);
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
        left = getType(n.left);
	lhsExp = left;
	n.left.accept(this);
	
	if (n.right instanceof  IdentifierNode){
		((IdentifierNode)n.right).accept(this);
		
	       
	}
    	else if (n.right instanceof NumNode){
    		((NumNode)n.right).accept(this);
		
	}
	else if (n.right instanceof RealNode){
	  	((RealNode)n.right).accept(this);
		       
        }
	else {
            ((BinExprNode)n.right).accept(this);
	    top.table.replace(n.left.id,rhsExp);
        }
      
    }

    public void visit(BinExprNode n)
    {
    	 if (n.left instanceof IdentifierNode)
        {
		((IdentifierNode)n.left).accept(this);
	}
        else if (n.left instanceof NumNode)
        {
	    ((NumNode)n.left).accept(this);		
        }
        else if (n.left instanceof RealNode)
        {
		((RealNode)n.left).accept(this);
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
       n.condition.accept(this);
        n.stmt.accept(this);
        if (n.elseStmt != null)
        {
            n.elseStmt.accept(this);
        }
    }

    public void visit(WhileNode n)
    {
    	n.condition.accept(this);
        n.stmt.accept(this);
    }

    public void visit(BooleanNode n)
    {
//     	    if(lhsExp == Type.Bool) {}
// 	    else error("TypeMismatch");
    }

    public void visit(DoWhileNode n)
    {
    	n.stmt.accept(this);
        n.condition.accept(this);
    }

    public void visit(NumNode n)
    {
    	    rhsExp = lhsExp;
	    if(lhsExp == Type.Int){}
	    else if(lhsExp == Type.Float){
			rhsExp = Type.Float;
	    }
	    else  error("Type mismatch: " +lhsExp + " type is not compatible with " +n.value + " of type int");
    }

    public void visit(RealNode n)
    {
    	rhsExp = lhsExp;
     	if(lhsExp == Type.Int || lhsExp == Type.Float){
		rhsExp = Type.Float;
	}
	else error("Type mismatch: " +lhsExp + " type is not compatible with " + n.value + " of type int");
    }

    public void visit(IdentifierNode n)
    {
    	rhsExp = lhsExp;
    	Type right;
	right = getType(n);
	System.out.println("Debug: Print type: " + right);
	if(lhsExp == right){ rhsExp = lhsExp; }
	else if((right == Type.Int && lhsExp == Type.Float) || (right == Type.Float && lhsExp == Type.Int)){
		rhsExp = Type.Float;
	}
	else error("Type mismatch: "+ lhsExp +" type is not compatible with " +n.id + " of type " + right);
    }

    public void visit (ArrayIDNode n)
    {
    }

    public void visit(ParenNode n)
    {
    }
}
