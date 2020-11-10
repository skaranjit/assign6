package assign6.ast;

import assign6.visitor.*;


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
