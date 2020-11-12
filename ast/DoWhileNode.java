package assign6.ast;

import assign6.visitor.*;

public class DoWhileNode extends StatementNode
{
    public StatementNode stmt;
    public ParenNode condition;

    public DoWhileNode()
    {

    }

    public DoWhileNode(ParenNode node, StatementNode stmt)
    {
        this.condition = node;
        this.stmt = stmt;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
