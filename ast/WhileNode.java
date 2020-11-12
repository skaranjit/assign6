package assign6.ast;

import assign6.visitor.*;


public class WhileNode extends StatementNode
{
    public ParenNode condition;
    public StatementNode stmt;

    public WhileNode()
    {

    }

    public WhileNode(ParenNode node, StatementNode stmt)
    {
        this.condition = node;
        this.stmt = stmt;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
