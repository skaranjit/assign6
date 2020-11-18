package assign6.unparser;

import assign6.ast.*;
import assign6.parser.*;
import assign6.typechecker.TypeChecker;
import assign6.visitor.*;
import java.io.*;



public class Unparser extends ASTVisitor
{

    public File tempFile;
    public FileWriter tempFileWriter;
    public TypeChecker typeCheck;

    public Unparser(TypeChecker typeChecker)
    {
        try
        {
            tempFile = new File("src\\assign6\\output.txt");
            tempFileWriter = new FileWriter(tempFile);
            System.out.println("\n** Output.txt created successfully! ** ");
        }
        catch (IOException e)
        {

        }

        this.typeCheck = typeChecker;
        visit(this.typeCheck.cu);

        try
        {
            tempFileWriter.close();
        }
        catch (IOException e)
        {

        }
    }

    void print(String s)
    {
        try
        {
            tempFileWriter.append(s);
        }
        catch (IOException e)
        {

        }
    }

    void println(String s)
    {
        try
        {
            tempFileWriter.append(s + "\n");
        }
        catch (IOException e)
        {

        }
    }

    void printSpace()
    {
        System.out.print(" ");
    }

    int indent = 0;

    void indentUp()
    {
        indent++;
    }

    void indentDown()
    {
        indent--;
    }

    void printIndent()
    {
        String s = "";
        for (int i = 0; i < indent; i++)
        {
            s += "   ";
        }
        print(s);
    }

    public void visit(CompilationUnit n)
    {
        n.block.accept(this);
    }

    public void visit(BlockStatementNode n)
    {
        println("{");

        indentUp();
        n.decls.accept(this);
        indentDown();

        indentUp();
        n.stmts.accept(this);
        indentDown();
        printIndent();
        println("}");
    }

    public void visit(Declarations n)
    {
        if (n.decls != null)
        {
            n.decl.accept(this);
            n.decls.accept(this);
        }
    }

    public void visit(DeclarationNode n)
    {
            n.type.accept(this);
            print(" ");
            n.id.accept(this);

            println(" ;");
    }

    public void visit(TypeNode n)
    {
        printIndent();
        print(n.basic.toString());

        if(n.array != null)
            n.array.accept(this);
    }

    public void visit(ArrayTypeNode n)
    {
        print("[");
        print("" +n.size);
        print("]");

        if(n.type != null)
            n.type.accept(this);
    }

    public void visit(Statements n)
    {
        if (n.stmts != null){
            //n.stmt.accept(this);
            n.stmts.accept(this);
        }
    }

    public void visit(StatementNode n)
    {

    }

    public void visit(AssignmentNode n)
    {
        printIndent();
        n.left.accept(this);
        print(" = ");

        if (n.right instanceof  IdentifierNode)
            ((IdentifierNode)n.right).accept(this);
        else if (n.right instanceof NumNode)
            ((NumNode)n.right).accept(this);
        else if (n.right instanceof RealNode)
            ((RealNode)n.right).accept(this);
        else {
            ((BinExprNode)n.right).accept(this);
        }
        println(";");
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

        if (n.op != null)
        {
            print(" " + n.op.toString() + " ");
        }

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

    public void visit(IdentifierNode n)
    {
        print(n.id);
        if (n.array != null)
        {
            n.array.accept(this);
        }
    }

    public void visit(ArrayIDNode n)
    {
        print("[");
        n.node.accept(this);
        print("]");
        if (n.array != null)
        {
            n.array.accept(this);
        }
    }

    public void visit(NumNode n)
    {
        print("" + n.value);
    }

    public void visit(RealNode n)
    {
        print("" + n.value);
    }

    public void visit(BreakNode n)
    {
        printIndent();
        println("break;");
    }

    public void visit(ConditionalNode n)
    {
        printIndent();
        print("if (");
        n.condition.accept(this);
        println(")");
        indentUp();
        n.stmt.accept(this);
        indentDown();
        if (n.elseStmt != null)
        {
            println("else");
            indentUp();
            n.elseStmt.accept(this);
            indentDown();
        }
    }

    public void visit(WhileNode n)
    {
        printIndent();
        print("while (");
        n.condition.accept(this);
        print(")");
        indentUp();
        n.stmt.accept(this);
        indentDown();
    }

    public void visit(DoWhileNode n)
    {
       printIndent();
       println("do");
       indentUp();
       n.stmt.accept(this);
       indentDown();
       printIndent();
       print("while (");
       n.condition.accept(this);
       println(");");
    }

    public void visit(BooleanNode n)
    {
        print(n.bool.toString());
    }

    public void visit(ParenNode n)
    {
        print("(");
        n.node.accept(this);
        print(")");
    }
}

