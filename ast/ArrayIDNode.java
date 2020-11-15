package assign6.ast;

import assign6.visitor.*;

public class ArrayIDNode extends IdentifierNode
{
    public ArrayIDNode id;
    public ExprNode node;

    public ArrayIDNode()
    {

    }

    public ArrayIDNode(ExprNode n, ArrayIDNode id)
    {
        this.node = n;
        this.id = id;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
