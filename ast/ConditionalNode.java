package assign5.ast;

import assign5.visitor.*;


public class ConditionalNode extends StatementNode
{
    public Node condition;
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
