package assign6.ast;

import assign6.visitor.*;

public class BlockStatementNode extends StatementNode
{
    public Declarations decls;
    public Statements stmts;

    public BlockStatementNode()
    {

    }

    public BlockStatementNode(Declarations decls, Statements stmts)
    {
        this.decls = decls;
        this.stmts = stmts;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }
}
