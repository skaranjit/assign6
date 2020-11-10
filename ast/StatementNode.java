package assign6.ast;

import assign6.visitor.ASTVisitor;

public class StatementNode
{
    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
