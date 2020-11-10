package assign6.parser;

import assign6.visitor.ASTVisitor;

public class ArrayIDNode extends IdentifierNode
{
    public ArrayIDNode id;
    public Node node;

    public ArrayIDNode()
    {

    }

    public ArrayIDNode(Node n, ArrayIDNode id)
    {
        this.node = n;
        this.id = id;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
