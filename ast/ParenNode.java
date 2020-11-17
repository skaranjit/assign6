package assign6.ast;

import assign6.visitor.ASTVisitor;

public class ParenNode extends ExprNode
{
    public Node node;

    public ParenNode()
    {

    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
