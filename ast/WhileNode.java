package assign6.ast;

import assign6.visitor.*;


public class WhileNode extends StatementNode
{
    public ExprNode condition;
    public StatementNode stmt;

    public WhileNode()
    {

    }

    public WhileNode(ExprNode node, StatementNode stmt)
    {
        this.condition = node;
        this.stmt = stmt;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
