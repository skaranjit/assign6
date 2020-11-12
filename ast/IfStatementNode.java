package assign6.ast;

import assign6.visitor.*;


public class IfStatementNode extends StatementNode
{
    public ParenthesesNode condition;
    public StatementNode stmt;
    public StatementNode elseStmt;

    public ConditionalNode()
    {

    }

    public ConditionalNode(Node node, StatementNode stmt, StatementNode elseStmt)
    {
        this.condition = node;
        this.stmt = stmt;
        this.elseStmt = elseStmt;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
