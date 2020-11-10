package assign5.ast;

import assign5.visitor.*;

public class DoWhileNode extends StatementNode
{
    public StatementNode stmt;
    public Node condition;

    public DoWhileNode()
    {

    }

    public DoWhileNode(Node node, StatementNode stmt)
    {
        this.condition = node;
        this.stmt = stmt;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
