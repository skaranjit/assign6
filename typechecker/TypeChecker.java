package assign6.typechecker;

import assign6.ast.*;
import assign6.visitor.*;
import assign6.parser.*;
import assign6.lexer.*;
import java.io.*;

public class TypeChecker extends ASTVisitor
{
    public Parser parser = null;
    public CompilationUnit cu = null;
    public boolean whileLoop = false;
    public boolean doLoop = false;

    int level = 0;
    String indent = "...";

    public Env top = null;
//    public Type lhsExp = null;
//    public Type rhsExp = null;
//    public boolean hasbeenInitialized = false;
//    public String[] initilizedTokens = new String[1000];


    public TypeChecker(Parser parser)
    {
            this.parser = parser;
            cu = parser.cu;
            //visit(this.parser.cu);
            visit(cu);
    }
    public TypeChecker()
    {
            //this.parser = new Parser();
            visit(this.parser.cu);
    }

    void error(String s)
    {
        println(s);
        exit(1);
    }

    void exit(int n)
    {
        System.exit(n);
    }

    void print(String s)
    {
        System.out.print(s);
    }

    void println(String s)
    {
        System.out.println(s);
    }

    void printSpace()
    {
        System.out.print(" ");
    }
//    void error (String s) throws Error{
//	throw new Error ("near line " + this.parser.lexer.line + ": " + s);
//    }

//    public Type getType(IdentifierNode a){
//        String x = a.id;
//	if(!(top.table.containsKey(x)))
//	{
//		 error("Variable " + x +" has not been declared.");
//	}
//	return top.table.get(x);
//    }
    public void visit (CompilationUnit n)
    {
        System.out.println("\nTypechecker starts");
//        top = new Env();
//        top = n.symbolTable;
//        visit(n.block);
        n.block.accept(this);
    }

    public void visit (BlockStatementNode n)
    {
        System.out.println("visiting Block");
	top = new Env(top);
	top = n.sTable;
        n.decls.accept(this);
        n.stmts.accept(this);
    }

    public void visit(Declarations n)
    {
        System.out.println("visiting Declarations");
//         if(n.decls != null)
//         {
//             n.decl.accept(this);
//             n.decls.accept(this);
//         }
    }

    public void visit(DeclarationNode n)
    {
        System.out.println("visiting DeclarationNode");
        n.type.accept(this);
        n.id.accept(this);
    }

    public void visit(TypeNode n)
    {
        System.out.println("visiting TypeNode"+n.basic);
        if(n.array != null)
        {
            n.array.accept(this);
        }
    }

    public void visit (Statements n)
    {
        System.out.println("Visiting Statements");
//        if (n.stmts != null)
//        {
//             n.stmt.accept(this);
//             n.stmts.accept(this);
//         }
    }

    public void visit(ParenNode n) {
        System.out.println("visiting ParenNode");
        n.node.accept(this);
    }

    public void visit(ConditionalNode n)
    {
        System.out.println("IfStatement/ConditionalNode");
//          if (n.condition.type != Type.Bool){
//             error("Conditional Must be Boolean");
//         }
        n.condition.accept(this);
        n.stmt.accept(this);
        if (n.elseStmt != null)
        {
            System.out.println("Else Clause");
            n.elseStmt.accept(this);
        }
    }

    public void visit(WhileNode n)
    {
        whileLoop = true;
        System.out.println("visiting WhileNode");
       
        n.condition.accept(this);
        n.stmt.accept(this);
        whileLoop = false;
    }

    public void visit(DoWhileNode n)
    {
        doLoop = true;
        System.out.println("visiting DoWhileNode");
       
        n.stmt.accept(this);
         
        n.condition.accept(this);
        doLoop = false;
    }

    public void visit (ArrayIDNode n)
    {
        System.out.println("visiting ArrayIDNode");
//         n.id.accept(this);
//         n.node.accept(this);
//         if (n.node.type != Type.Int){
//             error("Index of Array must be an Integer");
//         }
//         if(n.id != null)
//         {
//             n.id.accept(this);
//         }
    }

    public void visit(ArrayTypeNode n)
    {
        System.out.println("ArrayDimsNode");
        //n.size.accept(this);
//         if (n.size.type != Type.Int){
//             error("Index of Array must be an Integer");
//         }
//         if(n.type != null)
//         {
//             n.type.accept(this);
//         }
    }

    public void visit(BreakNode n)
    {
        System.out.println("visiting BreakNode");
        if (whileLoop || doLoop){
            error("Break called outside of loop");
        }
    }

    public void visit(AssignmentNode n)
    {
        System.out.println("AssignmentNode");
        n.left.accept(this);

        IdentifierNode leftId = (IdentifierNode)n.left;
        Type leftType = leftId.type;

        println("In TypeChecker, AssignmentNode's left type: "+leftType);

        Type rightType = null;
        if(n.right instanceof IdentifierNode){
            ((IdentifierNode)n.right).accept(this);
	    println("Debug: " + n.right.type);}
        else if (n.right instanceof NumNode)
        {
            ((NumNode)n.right).accept(this);
            rightType = Type.Int;
        }
        else if(n.right instanceof RealNode)
            ((RealNode)n.right).accept(this);
        else if(n.right instanceof ArrayIDNode)              //Dr. Lee uses ArrayNodes differently
            ((ArrayIDNode)n.right).accept(this);
        else if(n.right instanceof ParenNode)
            ((ParenNode)n.right).accept(this);
        else{
            ((BinExprNode)n.right).accept(this);
            //rightType = ((BinExprNode)n.right).type;
        }

	rightType = n.right.type;
        if(leftType == Type.Int)
            println("************* leftType is Type.Int");
        if(leftType != rightType)
        {
            error("The right-hand side of an assignment of type: " + rightType + " is incompatible to the left-hand side "+leftId.id);
        }
    }

    public void visit(BinExprNode n) {
        System.out.println("visiting BinExprNode" + n.op);

        Type leftType = null;
        IdentifierNode leftId = null;

        if (n.left instanceof IdentifierNode) {
            ((IdentifierNode) n.left).accept(this);

            leftId = (IdentifierNode) n.left;
            leftType = leftId.type;
        } else if (n.left instanceof NumNode) {
            ((NumNode) n.left).accept(this);
        } else if (n.left instanceof RealNode) {
            ((RealNode) n.left).accept(this);
        }
        else if (n.left instanceof BooleanNode)
        {
            ((BooleanNode)n.left).accept(this);
        }
        //else if (n.left instanceof ArrayIDNode)            //Dr,lee Array node conversion???
        //{
        //    ((ArrayIDNode) n.left).accept(this);
        //    }
         else if (n.left instanceof ParenNode) {
            ((ParenNode) n.left).accept(this);
        } else
            ((BinExprNode) n.left).accept(this);

        Type rightType = null;

        if (n.right != null) {
            if (n.right instanceof IdentifierNode) {
                ((IdentifierNode) n.right).accept(this);

                IdentifierNode rightId = (IdentifierNode) n.right;
                rightType = rightId.type;
            } else if (n.right instanceof NumNode) {
                ((NumNode) n.right).accept(this);
            } else if (n.right instanceof RealNode) {
                ((RealNode) n.right).accept(this);


            } //else if (n.right instanceof ArrayIDNode)              //Dr.Lee Array conversion???
            //{
            //    ((ArrayIDNode) n.right).accept(this);
           // }

            else if (n.right instanceof BooleanNode)
            {
                ((BooleanNode) n.right).accept(this);
            }
            else if (n.right instanceof ParenNode) {
                ((ParenNode) n.right).accept(this);
            } else {
                ((BinExprNode) n.right).accept(this);
            }
        } else {
            System.out.println("@@@ n.right == null in BinExprNode: " + n.right);
        }
        if (leftType == Type.Float || rightType == Type.Float) {
            n.type = Type.Float;
        } else {
            n.type = Type.Int;
        }
    }

    public void visit(StatementNode n)
    {
        System.out.println("Visiting Statement");
    }


    public void visit(IdentifierNode n)
    {
        System.out.println("visiting IdentifierNode");
// 	if(top.get(n.w) == null){
// 		println("Variable is already declared: " +n.w);
// 	}
// 	println("Type: " + n.type);
//         if(n.type == null){
// 		error("Syntax error: Variable " + n.id + " not declared. Cannot use undeclared variable.");
// 	}
	
        if (n.array != null)
        {
            n.array.accept(this);
        }

    }

    public void visit(BooleanNode n)
    {
        System.out.println("visiting BooleanNode");

    }

    public void visit(NumNode n)
    {
        System.out.println("visiting NumNode");

    }

    public void visit(RealNode n)
    {
        System.out.println("visiting RealNode");
    }


}
