package assign6.ast;

import assign6.visitor.*;


public class ConditionalNode extends StatementNode
{
    public ExprNode condition;
    public StatementNode stmt;
    public StatementNode elseStmt;

    public ConditionalNode()
    {

    }

    public ConditionalNode(ExprNode condition, StatementNode stmt, StatementNode elseStmt)
    {
        this.condition = condition;
        this.stmt = stmt;
        this.elseStmt = elseStmt;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
