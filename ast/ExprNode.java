package assign5.ast;

import assign5.visitor.*;


public class ExprNode extends Node
{
    public ExprNode()
    {

    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
