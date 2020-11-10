package assign6.visitor;

import assign6.parser.*;

public class ASTVisitor
{
    public void visit(CompilationUnit n)
    {
        n.block.accept(this);
    }

    public void visit(BlockStatementNode n)
    {
        n.decls.accept(this);
        n.stmts.accept(this);
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
        n.id.accept(this);
    }

    public void visit(TypeNode n)
    {
        n.array.accept(this);
    }

    public void visit(ArrayTypeNode n)
    {
        n.type.accept(this);
    }

    public void visit(Statements n)
    {
        if (n.stmts != null)
        {
            n.stmt.accept(this);
            n.stmts.accept(this);
        }
    }

    public void visit(StatementNode n)
    {

    }

    public void visit(AssignmentNode n)
    {
        n.left.accept(this);

        if (n.right instanceof IdentifierNode)
        {
            ((IdentifierNode)n.right).accept(this);
        }
        else if (n.right instanceof LiteralNode)
        {
            ((LiteralNode)n.right).accept(this);
        }
        else if (n.right instanceof RealNode)
        {
            ((RealNode)n.right).accept(this);
        }
        else
            ((BinExprNode)n.right).accept(this);

    }

    public void visit(ExprNode n)
    {

    }

    public void visit(BinExprNode n)
    {
        //n.left.accept(this);
        //n.right.accept(this);
    }

    public void visit(LiteralNode n)
    {

    }

    public void visit(IdentifierNode n)
    {

    }

    public void visit(RealNode n)
    {

    }

    public void visit(BooleanNode n)
    {

    }

    public void visit(DoWhileNode n)
    {

    }

    public void visit(ConditionalNode n)
    {

    }

    public void visit(WhileNode n)
    {

    }

    public void visit (ArrayIDNode n)
    {

    }

    public void visit(BreakNode n)
    {

    }

    public void visit(ParenNode n)
    {

    }
}
