package assign5.ast;

import assign5.visitor.ASTVisitor;

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