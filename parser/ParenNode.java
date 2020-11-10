package assign6.parser;

import assign6.visitor.ASTVisitor;

public class ParenNode extends Node
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
