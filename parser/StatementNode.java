package assign5.parser;

import assign5.visitor.ASTVisitor;

public class StatementNode
{
    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
